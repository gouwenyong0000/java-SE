> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [mp.weixin.qq.com](https://mp.weixin.qq.com/s/wVtp6LNYhSymI_SHauIOIQ)

ThreadLocal 是什么
===============

ThreadLocal 是一个本地线程副本变量工具类。主要用于将私有线程和该线程存放的副本对象做一个映射，各个线程之间的变量互不干扰，在高并发场景下，可以实现无状态的调用，特别适用于各个线程依赖不通的变量值完成操作的场景。

下图为 ThreadLocal 的内部结构图

![](https://mmbiz.qpic.cn/mmbiz_png/GpcH5Yqqj0mrSia3Lb2oibNMnhwJEyX3HbgY0gM2CMvZ8abU1Sr4tP0rHHHJVAhn6LfGQuk7Y77v2v5GEEZxibLdw/640?wx_fmt=png) 

**从上面的结构图，我们已经窥见 ThreadLocal 的核心机制：**

*   每个 Thread 线程内部都有一个 Map。
    
*   Map 里面存储线程本地对象（key）和线程的变量副本（value）
    
*   但是，Thread 内部的 Map 是由 ThreadLocal 维护的，由 ThreadLocal 负责向 map 获取和设置线程的变量值。
    

所以对于不同的线程，每次获取副本值时，别的线程并不能获取到当前线程的副本值，形成了副本的隔离，互不干扰。

ThreadLocalMap
==============

![](https://mmbiz.qpic.cn/mmbiz_png/GpcH5Yqqj0mrSia3Lb2oibNMnhwJEyX3HbGUs068lMIDmKDXVmqsDCiax1b67B2xdOoz0hSpibIBjUg229NRhdovEg/640?wx_fmt=png)  
ThreadLocalMap 是 ThreadLocal 的内部类，没有实现 Map 接口，用独立的方式实现了 Map 的功能，其内部的 Entry 也独立实现。

和 HashMap 的最大的不同在于，ThreadLocalMap 结构非常简单，没有 next 引用，也就是说 ThreadLocalMap 中解决 Hash 冲突的方式并非链表的方式，而是`采用线性探测的方式`。（**ThreadLocalMap 如何解决冲突？**）

在 ThreadLocalMap 中，也是用 Entry 来保存 K-V 结构数据的。但是 Entry 中 key 只能是 ThreadLocal 对象，这点被 Entry 的构造方法已经限定死了。

```java
static class Entry extends WeakReference<ThreadLocal> {            
    /** The value associated with this ThreadLocal. */    
    Object value;    
    Entry(ThreadLocal k, Object v) {                
        super(k);        
        value = v;    
    }
}
```

`注意了！！`  
**Entry 继承自 WeakReference（`弱引用，生命周期只能存活到下次GC前`），但只有 Key 是弱引用类型的，Value 并非弱引用。**（`问题马上就来了`）

由于 ThreadLocalMap 的 key 是弱引用，而 Value 是强引用。这就导致了一个问题，ThreadLocal 在没有外部对象强引用时，**发生 GC 时弱引用 Key 会被回收，而 Value 不会回收**。

当线程没有结束，但是 ThreadLocal 已经被回收，则可能导致线程中存在 ThreadLocalMap<null, Object> 的键值对，**造成内存泄露。**（`ThreadLocal被回收，ThreadLocal关联的线程共享变量还存在`）。

如何避免泄漏
======

为了防止此类情况的出现，我们有两种手段。

1、使用完线程共享变量后，显示调用 ThreadLocalMap.remove 方法清除线程共享变量；

既然 Key 是弱引用，那么我们要做的事，就是在调用 ThreadLocal 的 get()、set() 方法时完成后再**调用 remove 方法，将 Entry 节点和 Map 的引用关系移除**，这样整个 Entry 对象在 GC Roots 分析后就变成不可达了，下次 GC 的时候就可以被回收。

2、JDK 建议 ThreadLocal 定义为 private static，这样 ThreadLocal 的弱引用问题则不存在了。

