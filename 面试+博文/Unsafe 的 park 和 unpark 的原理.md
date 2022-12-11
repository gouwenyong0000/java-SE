# 简单理解下Unsafe的park和unpark的原理

我们知道各种并发框架如 CountDownLatch、CyclicBarrier 和 Semaphore 是基于 AQS (AbstractQueuedSynchronizer) 框架实现的，AQS 框架借助于两个类：

*   Unsafe（提供 CAS 操作） //JDK9 以后引入了 VarHandle 变量句柄，代替了 Unsafe
*   LockSupport（提供 park/unpark 操作）

而 LockSupport 的 park 和 unpark 的实现是依赖于 Unsafe 类的 prak 和 unpark 的。重载方法中可以传入一个 blocker 对象，在 dump 线程时能获得更多的信息，用于问题排查或系统监控。

```java
public static void park() {
    U.park(false, 0L); //U = Unsafe.getUnsafe();
}
```

```java
public static void park(Object blocker) {
    Thread t = Thread.currentThread();
    setBlocker(t, blocker);  
    U.park(false, 0L);
    setBlocker(t, null);
}
```

那么 Unsafe 类是个什么东西呢？Unsafe 的全限定名是 sun.misc.Unsafe。在源码的注释中我们可以看到：执行低级、不安全操作的方法集合。从名字就知道它是不安全的，它的功能有很多，比如：volatile 的读写一个变量的 field，有序的写一个变量的 field，直接内存操作：申请内存，释放内存，CAS 的修改变量等。

本文主要说说 park 和 unpark 方法。最简单的理解：park 阻塞一个线程，unpark 唤醒一个线程。

核心设计原理：“许可”。park 方法本质是消费许可，如果没有可消费的许可，那么就阻塞当前线程，一直等待，直到阻塞线程的 unpark 方法被其他线程调用，然后消费许可，当前线程被唤醒，继续执行。unpark 方法本质是生产许可，一个线程刚创建出来，然后运行，此时是没有许可的，所以 unpark 方法可以在 park 方法前调用。下次 park 方法调用时，直接消费许可，线程不用阻塞等待许可。许可最多只有一个，连续多次调用 unpark 只能生产一个许可。

park 方法有几种重载的形式，可以设置等待时间，等待时间可以设置为绝对的或者相对的，超过等待时间，线程会自动被唤醒。

底层实现原理：

在 Linux 系统下，是用的 Posix 线程库 pthread 中的 mutex（互斥量），condition（条件变量）来实现的。  
mutex 和 condition 保护了一个_counter 的变量，简单点说：当 park 时，这个变量被设置为 0，当 unpark 时，这个变量被设置为 1。 关于源码，可以看这篇博客：[https://blog.csdn.net/weixin_39687783/article/details/85058686](https://blog.csdn.net/weixin_39687783/article/details/85058686) 总结： LockSupport 的 park 和 unpark 方法相比于 Synchronize 的 wait 和 notify，notifyAll 方法： 1. 更简单，不需要获取锁，能直接阻塞线程。 2. 更直观，以 thread 为操作对象更符合阻塞线程的直观定义； 3. 更精确，可以准确地唤醒某一个线程（notify 随机唤醒一个线程，notifyAll 唤醒所有等待的线程）； 4. 更灵活 ，unpark 方法可以在 park 方法前调用。





LockSupport 是用来创建 locks 的基本线程阻塞基元，比如 AQS 中实现线程[挂起](https://so.csdn.net/so/search?q=%E6%8C%82%E8%B5%B7&spm=1001.2101.3001.7020)的方法，就是 park, 对应唤醒就是 unpark。JDK 中有使用的如下

![](https://img-blog.csdnimg.cn/20181217235341549.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTY4Nzc4Mw==,size_16,color_FFFFFF,t_70)  
LockSupport 提供的是一个许可，如果存在许可，线程在调用`park`的时候，会立马返回，此时许可也会被消费掉，如果没有许可，则会阻塞。调用 unpark 的时候，如果许可本身不可用，则会使得许可可用

> 许可只有一个，不可累加

park [源码](https://so.csdn.net/so/search?q=%E6%BA%90%E7%A0%81&spm=1001.2101.3001.7020)跟踪
=======================================================================================

park 的声明形式有一下两大块  
![](https://img-blog.csdnimg.cn/20181217235400453.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTY4Nzc4Mw==,size_16,color_FFFFFF,t_70)  
一部分多了一个 Object 参数，作为 blocker, 另外的则没有。blocker 的好处在于，在诊断问题的时候能够知道 park 的原因

> 推荐使用带有 Object 的 park 操作

park 函数作用
---------

park 用于挂起当前线程，如果许可可用，会立马返回，并消费掉许可。

*   park(Object): 恢复的条件为 1：线程调用了 unpark; 2: 其它线程中断了线程；3：发生了不可预料的事情
*   parkNanos(Object blocker, long nanos): 恢复的条件为 1：线程调用了 unpark; 2: 其它线程中断了线程；3：发生了不可预料的事情; 4: 过期时间到了
*   parkUntil(Object blocker, long deadline): 恢复的条件为 1：线程调用了 unpark; 2: 其它线程中断了线程；3：发生了不可预料的事情; 4: 指定的 deadLine 已经到了  
    以 park 的源码为例
    
    ```java
    public static void park(Object blocker) {
       //获取当前线程
        Thread t = Thread.currentThread();
       //记录当前线程阻塞的原因,底层就是unsafe.putObject,就是把对象存储起来
        setBlocker(t, blocker);
        //执行park
        unsafe.park(false, 0L);
       //线程恢复后，去掉阻塞原因
        setBlocker(t, null);
    }
    ```
    

从源码可以看到真实的实现均在 unsafe

unsafe.park
-----------

核心实现如下

```c++
JavaThread* thread=JavaThread::thread_from_jni_environment(env);
...
thread->parker()->park(isAbsolute != 0, time);
```

就是获取 java 线程的 parker 对象, 然后执行它的 park 方法。Parker 的定义如下

```c++
class Parker : public os::PlatformParker {
private:
   //表示许可
  volatile int _counter ; 
  Parker * FreeNext ;
  JavaThread * AssociatedWith ; // Current association
public:
  Parker() : PlatformParker() {
    //初始化_counter
    _counter       = 0 ; 
    FreeNext       = NULL ;
    AssociatedWith = NULL ;
  }
protected:
  ~Parker() { ShouldNotReachHere(); }
public:
  void park(bool isAbsolute, jlong time);
  void unpark();

  // Lifecycle operators  
  static Parker * Allocate (JavaThread * t) ;
  static void Release (Parker * e) ;
private:
  static Parker * volatile FreeList ;
  static volatile int ListLock ;

};
```

它继承了 os::PlatformParker，内置了一个 volatitle 的 _counter。PlatformParker 则是在不同的操作系统中有不同的实现，以 linux 为例

```c++
class PlatformParker : public CHeapObj {
  protected:
    //互斥变量类型
    pthread_mutex_t _mutex [1] ; 
   //条件变量类型
    pthread_cond_t  _cond  [1] ;

  public:        
     ~PlatformParker() { guarantee (0, "invariant") ; }

  public:
    PlatformParker() {
      int status;
     //初始化条件变量，使用    pthread_cond_t之前必须先执行初始化
      status = pthread_cond_init (_cond, NULL);
      assert_status(status == 0, status, "cond_init”);
      // 初始化互斥变量，使用    pthread_mutex_t之前必须先执行初始化
      status = pthread_mutex_init (_mutex, NULL);
      assert_status(status == 0, status, "mutex_init");
    }
}
```

> 上述代码均为 POSIX 线程接口使用，所以 pthread 指的也就是 posixThread

parker 实现如下

```c++
void Parker::park(bool isAbsolute, jlong time) {
  if (_counter > 0) {
       //已经有许可了，用掉当前许可
      _counter = 0 ;
     //使用内存屏障，确保 _counter赋值为0(写入操作)能够被内存屏障之后的读操作获取内存屏障事前的结果，也就是能够正确的读到0
      OrderAccess::fence();
     //立即返回
      return ;
  }

  Thread* thread = Thread::current();
  assert(thread->is_Java_thread(), "Must be JavaThread");
  JavaThread *jt = (JavaThread *)thread;

 if (Thread::is_interrupted(thread, false)) {
 // 线程执行了中断，返回
    return;
  }

  if (time < 0 || (isAbsolute && time == 0) ) { 
    //时间到了，或者是代表绝对时间，同时绝对时间是0（此时也是时间到了），直接返回，java中的parkUtil传的就是绝对时间，其它都不是
   return;
  }
  if (time > 0) {
  //传入了时间参数，将其存入absTime，并解析成absTime->tv_sec(秒)和absTime->tv_nsec(纳秒)存储起来，存的是绝对时间
    unpackTime(&absTime, isAbsolute, time);
  }

 //进入safepoint region，更改线程为阻塞状态
  ThreadBlockInVM tbivm(jt);

 if (Thread::is_interrupted(thread, false) || pthread_mutex_trylock(_mutex) != 0) {
  //如果线程被中断，或者是在尝试给互斥变量加锁的过程中，加锁失败，比如被其它线程锁住了，直接返回
    return;
  }
//这里表示线程互斥变量锁成功了
  int status ;
  if (_counter > 0)  {
    // 有许可了，返回
    _counter = 0;
    //对互斥变量解锁
    status = pthread_mutex_unlock(_mutex);
    assert (status == 0, "invariant") ;
    OrderAccess::fence();
    return;
  }

#ifdef ASSERT
  // Don't catch signals while blocked; let the running threads have the signals.  
// (This allows a debugger to break into the running thread.)  
 //debug用
sigset_t oldsigs;
  sigset_t* allowdebug_blocked = os::Linux::allowdebug_blocked_signals();
  pthread_sigmask(SIG_BLOCK, allowdebug_blocked, &oldsigs);
#endif
//将java线程所拥有的操作系统线程设置成 CONDVAR_WAIT状态 ，表示在等待某个条件的发生
OSThreadWaitState osts(thread->osthread(), false /* not Object.wait() */);
//将java的_suspend_equivalent参数设置为true
  jt->set_suspend_equivalent();
  // cleared by handle_special_suspend_equivalent_condition() or java_suspend_self()
  if (time == 0) {
    //把调用线程放到等待条件的线程列表上，然后对互斥变量解锁，（这两是原子操作），这个时候线程进入等待，当它返回时，互斥变量再次被锁住。
  //成功返回0，否则返回错误编号
    status = pthread_cond_wait (_cond, _mutex) ;
  } else {
  //同pthread_cond_wait，只是多了一个超时，如果超时还没有条件出现，那么重新获取胡吃两然后返回错误码 ETIMEDOUT
    status = os::Linux::safe_cond_timedwait (_cond, _mutex, &absTime) ;
    if (status != 0 && WorkAroundNPTLTimedWaitHang) {
   //WorkAroundNPTLTimedWaitHang 是JVM的运行参数，默认为1
  //去除初始化
      pthread_cond_destroy (_cond) ;
//重新初始化
      pthread_cond_init    (_cond, NULL);
    }
  }
  assert_status(status == 0 || status == EINTR ||
                status == ETIME || status == ETIMEDOUT,
                status, "cond_timedwait");

#ifdef ASSERT
  pthread_sigmask(SIG_SETMASK, &oldsigs, NULL);
#endif
 //等待结束后，许可被消耗，改为0  _counter = 0 ;
//释放互斥量的锁
  status = pthread_mutex_unlock(_mutex) ;
  assert_status(status == 0, status, "invariant") ;
  // If externally suspended while waiting, re-suspend 
    if (jt->handle_special_suspend_equivalent_condition()) {
    jt->java_suspend_self();
  }
//加入内存屏障指令
  OrderAccess::fence();
}
```

从 park 的实现可以看到

1.  无论是什么情况返回，park 方法本身都不会告知调用方返回的原因，所以调用的时候一般都会去判断返回的场景，根据场景做不同的处理
2.  线程的等待与挂起、唤醒等等就是使用的 POSIX 的线程 API
3.  park 的许可通过原子变量_count 实现，当被消耗时，_count 为 0，只要拥有许可，就会立即返回

OrderAccess::fence();
---------------------

在 linux 中实现原理如下

```c++
inline void OrderAccess::fence() {
  if (os::is_MP()) {
#ifdef AMD64
  // 没有使用mfence,因为mfence有时候性能差于使用 locked addl
    __asm__ volatile ("lock; addl $0,0(%%rsp)" : : : "cc", "memory");
#else    __asm__ volatile ("lock; addl $0,0(%%esp)" : : : "cc", "memory");
#endif  }
}
```

> [内存重排序网上的验证](https://preshing.com/20120515/memory-reordering-caught-in-the-act/)

ThreadBlockInVM tbivm(jt)
-------------------------

这属于 C++ 新建变量的语法，也就是调用构造函数新建了一个变量，变量名为 tbivm, 参数为 jt。类的实现为

```c++
class ThreadBlockInVM : public ThreadStateTransition {
 public:
  ThreadBlockInVM(JavaThread *thread)
  : ThreadStateTransition(thread) {
    // Once we are blocked vm expects stack to be walkable    
    thread->frame_anchor()->make_walkable(thread);
   //把线程由运行状态转成阻塞状态
    trans_and_fence(_thread_in_vm, _thread_blocked);
  }
  ...
};
```

_thread_in_vm 表示线程当前在 VM 中执行，_thread_blocked 表示线程当前阻塞了，他们是`globalDefinitions.hpp`中定义的枚举

```c++
//这个枚举是用来追踪线程在代码的那一块执行，用来给 safepoint code使用，有4种重要的类型，_thread_new/_thread_in_native/_thread_in_vm/_thread_in_Java。形如xxx_trans的状态都是中间状态，表示线程正在由一种状态变成另一种状态，这种方式使得 safepoint code在处理线程状态时，不需要对线程进行挂起，使得safe point code运行更快，而给定一个状态，通过+1就可以得到他的转换状态
enum JavaThreadState {
  _thread_uninitialized     =  0, // should never happen (missing initialization) 
_thread_new               =  2, // just starting up, i.e., in process of being initialized 
_thread_new_trans         =  3, // corresponding transition state (not used, included for completeness)  
_thread_in_native         =  4, // running in native code  . This is a safepoint region, since all oops will be in jobject handles
_thread_in_native_trans   =  5, // corresponding transition state  
_thread_in_vm             =  6, // running in VM 
_thread_in_vm_trans       =  7, // corresponding transition state 
_thread_in_Java           =  8, //  Executing either interpreted or compiled Java code running in Java or in stub code  
_thread_in_Java_trans     =  9, // corresponding transition state (not used, included for completeness) 
_thread_blocked           = 10, // blocked in vm 
_thread_blocked_trans     = 11, // corresponding transition state 
_thread_max_state         = 12  // maximum thread state+1 - used for statistics allocation
};
```

父类 ThreadStateTransition 中定义 trans_and_fence 如下

```c++
void trans_and_fence(JavaThreadState from, JavaThreadState to) { transition_and_fence(_thread, from, to);} //_thread即构造函数传进来de thread
// transition_and_fence must be used on any thread state transition
// where there might not be a Java call stub on the stack, in
// particular on Windows where the Structured Exception Handler is
// set up in the call stub. os::write_memory_serialize_page() can
// fault and we can't recover from it on Windows without a SEH in
// place.
//transition_and_fence方法必须在任何线程状态转换的时候使用
static inline void transition_and_fence(JavaThread *thread, JavaThreadState from, JavaThreadState to) {
  assert(thread->thread_state() == from, "coming from wrong thread state");
  assert((from & 1) == 0 && (to & 1) == 0, "odd numbers are transitions states");
//标识线程转换中
    thread->set_thread_state((JavaThreadState)(from + 1));

  // 设置内存屏障，确保新的状态能够被VM 线程看到
if (os::is_MP()) {
    if (UseMembar) {
      // Force a fence between the write above and read below     
        OrderAccess::fence();
    } else {
      // Must use this rather than serialization page in particular on Windows      
        InterfaceSupport::serialize_memory(thread);
    }
  }

  if (SafepointSynchronize::do_call_back()) {
    SafepointSynchronize::block(thread);
  }
//线程状态转换成最终的状态,对待这里的场景就是阻塞
  thread->set_thread_state(to);

  CHECK_UNHANDLED_OOPS_ONLY(thread->clear_unhandled_oops();)
}
```

操作系统线程状态的一般取值
-------------

在 osThread 中给定了操作系统线程状态的大致取值，它本身是依据平台而定

```c++
enum ThreadState {
 ALLOCATED,                    // Memory has been allocated but not initialized  
INITIALIZED,                  // The thread has been initialized but yet started 
RUNNABLE,                     // Has been started and is runnable, but not necessarily running  
MONITOR_WAIT,                 // Waiting on a contended monitor lock  
CONDVAR_WAIT,                 // Waiting on a condition variable  
OBJECT_WAIT,                  // Waiting on an Object.wait() call  
BREAKPOINTED,                 // Suspended at breakpoint  
SLEEPING,                     // Thread.sleep()  
ZOMBIE                        // All done, but not reclaimed yet
};
```

unpark 源码追踪
===========

实现如下

```c++
void Parker::unpark() {
  int s, status ;
 //给互斥量加锁，如果互斥量已经上锁，则阻塞到互斥量被解锁
//park进入wait时，_mutex会被释放
  status = pthread_mutex_lock(_mutex);
  assert (status == 0, "invariant") ; 
  //存储旧的_counter
  s = _counter; 
//许可改为1，每次调用都设置成发放许可
  _counter = 1;
  if (s < 1) {
     //之前没有许可
     if (WorkAroundNPTLTimedWaitHang) {
      //默认执行 ,释放信号，表明条件已经满足，将唤醒等待的线程
        status = pthread_cond_signal (_cond) ;
        assert (status == 0, "invariant") ;
        //释放锁
        status = pthread_mutex_unlock(_mutex);
        assert (status == 0, "invariant") ;
     } else {
        status = pthread_mutex_unlock(_mutex);
        assert (status == 0, "invariant") ;
        status = pthread_cond_signal (_cond) ;
        assert (status == 0, "invariant") ;
     }
  } else {
   //一直有许可，释放掉自己加的锁,有许可park本身就返回了
    pthread_mutex_unlock(_mutex);
    assert (status == 0, "invariant") ;
  }
}
```

从源码可知 unpark 本身就是发放许可，并通知等待的线程，已经可以结束等待了

总结
==

*   park/unpark 能够精准的对线程进行唤醒和等待。
*   linux 上的实现是通过 POSIX 的线程 API 的等待、唤醒、互斥、条件来进行实现的
*   park 在执行过程中首选看是否有许可，有许可就立马返回，而每次 unpark 都会给许可设置成有，这意味着，可以先执行 unpark，给予许可，再执行 park 立马自行，适用于 producer 快，而 consumer 还未完成的场景[参考地址](http://www.cnblogs.com/zhizhizhiyuan/p/4966827.html)