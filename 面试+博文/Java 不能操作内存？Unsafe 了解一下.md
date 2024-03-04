> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.cnblogs.com](https://www.cnblogs.com/star95/p/17619438.html)

前言
--

C++ 可以动态的分类内存（但是得主动释放内存，避免内存泄漏），而 java 并不能这样，java 的内存分配和垃圾回收统一由 JVM 管理，是不是 java 就不能操作内存呢？当然有其他办法可以操作内存，接下来有请`Unsafe`出场，我们一起看看`Unsafe`是如何花式操作内存的。

Unsafe 介绍
---------

`Unsafe`见名知意，不安全的意思，因为通过这个类可以绕过 JVM 的管理直接去操作内存，如果操作不当或者使用完成后没有及时释放的话，这部分的内存不会被回收，久而久之，这种没有被释放的内存会越来越多造成内存泄漏。所以这是一个比较不安全的操作，一般不建议直接使用，毕竟这种问题导致的线上问题很难查出，另外通常的解决办法就是重启系统了。  
虽然`Unsafe`是不安全的操作，但可以根据实际情况合理使用可以达到更好的效果，比如像 java NIO 里就有通过这种方式创建堆外存。`Unsafe`常用的操作包括：分配内存释放内存、实例化及对象操作、数组操作、CAS、线程同步等。

Unsafe 实例对象的获取
--------------

Unsafe 不能直接获取到，像下面这样使用会直接抛出安全检查异常。

```
public static void main(String[] args) throws Exception {    Unsafe unsafe = Unsafe.getUnsafe();}
```

运行结果就这样：

```
Exception in thread "main" java.lang.SecurityException: Unsafe	at sun.misc.Unsafe.getUnsafe(Unsafe.java:90)	at com.star95.study.UnsafeTest.main(UnsafeTest.java:21)
```

我们来看看`sun.misc.Unsafe.getUnsafe`这个方法的源码：

```
public static Unsafe getUnsafe() {    Class var0 = Reflection.getCallerClass();    if (!VM.isSystemDomainLoader(var0.getClassLoader())) {        throw new SecurityException("Unsafe");    } else {        return theUnsafe;    }}
```

这里会判断这个类的加载器是否是启用类加载器`Bootstrap ClassLoader`，如果不是启动类加载器加载的则抛异常。  
`Bootstrap ClassLoader`这个类加载器主要加载 java 核心类库，比如 rt.jar 这类包的，java 采用的是双亲委托方式加载类，如果父加载器已加载了某个类，则子加载器不再加载，采用这样的方式进一步加强安全性。关于启动类加载器`Bootstrap ClassLoader`、扩展类加载器`Extension Classloader`、应用程序类加载器`Application Classloader`这里不再介绍，感兴趣的可自行搜索。  
以下介绍 3 种方法来获取`Unsafe`。

### 1、修改启动类加载器的搜索路径

```
public class UnsafeUtil {    private UnsafeUtil() {}    public static Unsafe getUnsafe() {        System.out.println("get getUnsafe...");        return Unsafe.getUnsafe();    }}
```

就这一个类，打成一个 jar 包。  
![](https://img2023.cnblogs.com/blog/3230688/202308/3230688-20230810090828959-1714185210.png)

把这个 jar 包的路径放到 jvm 启动参数里`-Xbootclasspath/a:D:\work\projects\test\unsafe\target\unsafe-1.0-SNAPSHOT.jar`，然后调用即可获取到`Unsafe`。  
![](https://img2023.cnblogs.com/blog/3230688/202308/3230688-20230810090846281-1908705805.png)

关于 jvm 参数`-Xbootclasspath`说明:

```
-Xbootclasspath:新jar路径（Windows用;分隔,linux用:分隔），这种相当于覆盖了java默认的核心类搜索路径，包括核心类库例如rt.jar，这种基本不用。 -Xbootclasspath/a:新jar路径（Windows用;分隔,linux用:分隔）,这种是把新jar路径追加到已有的classpath后，相当于扩大了核心类的范围，这个常用。 -Xbootclasspath/p:新jar路径（Windows用;分隔,linux用:分隔）,这种是把新jar路径追加到已有的classpath前，也相当于扩大了核心类的范围，但是放到核心类前可能会引起冲突，这个不常用。
```

### 2、利用反射使用构造方法创建实例

`Unsafe`有一个私有的无参构造方法，利用反射使用构造方法也可以创建`Unsafe`实例。

```
Constructor constructor = Unsafe.class.getDeclaredConstructors()[0];constructor.setAccessible(true);Unsafe unsafe = (Unsafe)constructor.newInstance();System.out.println(unsafe);
```

### 3、利用反射获取 Unsafe 属性创建实例

`Unsafe`类里有一个属性`private static final Unsafe theUnsafe;`利用反射获取到这个属性然后对`null`这个对象获取属性值就会触发类静态块儿的执行，从而达到实例化的目的。  
![](https://img2023.cnblogs.com/blog/3230688/202308/3230688-20230810090902510-236761779.png)

```
private static Unsafe getUnsafe() {    try {        Field field = Unsafe.class.getDeclaredField("theUnsafe");        field.setAccessible(true);        return (Unsafe)field.get(null);    } catch (Exception e) {        e.printStackTrace();    }    return null;}
```

以上 3 种方法虽然都可以获取到`Unsafe`的实例，但第三种更常用一些。

Unsafe 常用操作
-----------

Unsafe 类里大概有 100 多个方法，按用途主要分为以下几大类，分别介绍。

### Unsafe 操作内存

内存操作主要包括内存分配、扩展内存、设置内存值、释放内存等，常用的方法介绍如下。

```
//分配指定大小的内存public long allocateMemory(long bytes)//根据给定的内存地址address调整内存大小public long reallocateMemory(long address, long bytes)//设置内存值public void setMemory(Object o, long offset, long bytes, byte value)public void setMemory(long address, long bytes, byte value)//内存复制，支持两种地址模式public void copyMemory(Object srcBase, long srcOffset,  Object destBase, long destOffset, long bytes)//释放allocateMemory和reallocateMemory申请的内存public native void freeMemory(long address)
```

举个栗子：

```
public void test() throws Exception {        Unsafe unsafe = getUnsafe();        long address = unsafe.allocateMemory(8);        System.out.println("allocate memory with 8 bytes, address=" + address);        long data = 13579L;        unsafe.putLong(address, data);        System.out.println("direct put data to address, data=" + data);        System.out.println("get address data=" + unsafe.getLong(address));        long address1 = unsafe.allocateMemory(8);        System.out.println("allocate memory with 8 bytes, address1=" + address);        unsafe.copyMemory(address, address1, 8);        System.out.println("copy memory with 8 bytes to address1=" + address1);        System.out.println("get address1 data=" + unsafe.getLong(address1));        unsafe.reallocateMemory(address1, 16);        unsafe.setMemory(address1, 16, (byte)20);        System.out.println("after setMemory address1=" + unsafe.getByte(address1));        unsafe.freeMemory(address1);        unsafe.freeMemory(address);        System.out.println("free memory over");        long[] l1 = new long[] {11, 22, 33, 44};        long[] l2 = new long[4];        long offset = unsafe.arrayBaseOffset(long[].class);        unsafe.copyMemory(l1, offset, l2, offset, 32);        System.out.println("l2=" + Arrays.toString(l2));    }
```

输出结果：

```
allocate memory with 8 bytes, address=510790256direct put data to address, data=13579get address data=13579allocate memory with 8 bytes, address1=510790256copy memory with 8 bytes to address1=510788736get address1 data=13579after setMemory address1=20free memory overl2=[11, 22, 33, 44]
```

### Unsafe 操作类、对象及变量

下面介绍关于类、对象及变量相关的一些操作，还有一些其他的方法没有一一列出，大家可以自行研究。

```
//实例化对象，不调构造方法Object allocateInstance(Class<?> cls)//字段在内存中的地址相对于实例对象内存地址的偏移量public long objectFieldOffset(Field f)//字段在内存中的地址相对于class内存地址的偏移量public long objectFieldOffset(Class<?> c, String name)//静态字段在class对象中的偏移public long staticFieldOffset(Field f)//获得静态字段所对应类对象public Object staticFieldBase(Field f)//获取对象中指定偏移量的int值，这里还有基本类型的其他其中，比如char,boolean,long等public native int getInt(Object o, long offset);//将int值放入指定对象指定偏移量的位置，这里还有基本类型的其他其中，比如char,boolean,long等public native void putInt(Object o, long offset, int x);//获取obj对象指定offset的属性对象public native Object getObject(Object obj, long offset);//将newObj对象放入指定obj对象指定offset偏移量的位置public native void putObject(Object obj, long offset, Object newObj);
```

实战一下吧

```
class Cat {    private String name;    private long speed;     public Cat(String name, long speed) {        this.name = name;        this.speed = speed;    }     public Cat() {        System.out.println("constructor...");    }     static {        System.out.println("static...");    }     @Override    public String toString() {        return "Cat{" + " + name + '\'' + ", speed=" + speed + '}';    }} class Foo {    private int age;    private Cat cat;    private static String defaultString = "default........";     public int getAge() {        return age;    }     public void setAge(int age) {        this.age = age;    }     public Cat getCat() {        return cat;    }     public void setCat(Cat cat) {        this.cat = cat;    }} //测试方法public void test1() throws Exception {    // 使用allocateInstance方法创建一个Cat实例，这里不会调用构造方法，但是静态块会执行，所以会输出"static..."    Cat cat = (Cat)unsafe.allocateInstance(Cat.class);    System.out.println("allocateInstance cat--->" + cat);    Foo f = new Foo();    f.setAge(13);    f.setCat(new Cat("ketty", 120));    long ageOffset = unsafe.objectFieldOffset(Foo.class.getDeclaredField("age"));    // 这个offset的属性是一个Cat对象    long catOffset = unsafe.objectFieldOffset(Foo.class.getDeclaredField("cat"));    // 获取静态属性的时候直接用Class对象，用实例对象的话会发生NPE异常    long defaultStringOffset = unsafe.staticFieldOffset(Foo.class.getDeclaredField("defaultString"));    System.out.println("get age=" + unsafe.getInt(f, ageOffset));    System.out.println("get cat=" + unsafe.getObject(f, catOffset));    System.out.println("get defaultString=" + unsafe.getObject(Foo.class, defaultStringOffset));    System.out.println("---------------------");    // 操作内存放入新值    unsafe.putInt(f, ageOffset, 100);    unsafe.putObject(f, catOffset, new Cat("hello", 333));    unsafe.putObject(f, defaultStringOffset, "new default string");    System.out.println("after put then get age=" + unsafe.getInt(f, ageOffset));    System.out.println("after put then get cat=" + unsafe.getObject(f, catOffset));    System.out.println("after put then get defaultString=" + unsafe.getObject(f, defaultStringOffset));    System.out.println("---------------------");}
```

程序输出如下：

```
static...allocateInstance cat--->Cat{name='null', speed=0}get age=13get cat=Cat{name='ketty', speed=120}get defaultString=default........---------------------after put then get age=100after put then get cat=Cat{name='hello', speed=333}after put then get defaultString=new default string---------------------
```

### Unsafe 操作数组

数组除了 8 种基本类型外，还包括 Object 数组。在`Unsafe`类里是通过静态块来获取这些数据。  
![](https://img2023.cnblogs.com/blog/3230688/202308/3230688-20230810090917788-1962458667.png)

```
public void test2() {    // 获取8种基本类型和Object类型数组的基础偏移量，scale相关的可以理解每个类型对应的值所占的大小    // 通过输出信息我们可以看到基础偏移量都是16，scale除Object的是4外，基础数据类型的scale就是相应的字节大小    System.out.println("Unsafe.ARRAY_BOOLEAN_BASE_OFFSET=" + Unsafe.ARRAY_BOOLEAN_BASE_OFFSET);    System.out.println("Unsafe.ARRAY_BOOLEAN_INDEX_SCALE=" + Unsafe.ARRAY_BOOLEAN_INDEX_SCALE);    System.out.println("Unsafe.ARRAY_BYTE_BASE_OFFSET=" + Unsafe.ARRAY_BYTE_BASE_OFFSET);    System.out.println("Unsafe.ARRAY_BYTE_INDEX_SCALE=" + Unsafe.ARRAY_BYTE_INDEX_SCALE);    System.out.println("Unsafe.ARRAY_SHORT_BASE_OFFSET=" + Unsafe.ARRAY_SHORT_BASE_OFFSET);    System.out.println("Unsafe.ARRAY_SHORT_INDEX_SCALE=" + Unsafe.ARRAY_SHORT_INDEX_SCALE);    System.out.println("Unsafe.ARRAY_CHAR_BASE_OFFSET=" + Unsafe.ARRAY_CHAR_BASE_OFFSET);    System.out.println("Unsafe.ARRAY_CHAR_INDEX_SCALE=" + Unsafe.ARRAY_CHAR_INDEX_SCALE);    System.out.println("Unsafe.ARRAY_INT_BASE_OFFSET=" + Unsafe.ARRAY_INT_BASE_OFFSET);    System.out.println("Unsafe.ARRAY_INT_INDEX_SCALE=" + Unsafe.ARRAY_INT_INDEX_SCALE);    System.out.println("Unsafe.ARRAY_LONG_BASE_OFFSET=" + Unsafe.ARRAY_LONG_BASE_OFFSET);    System.out.println("Unsafe.ARRAY_LONG_INDEX_SCALE=" + Unsafe.ARRAY_LONG_INDEX_SCALE);    System.out.println("Unsafe.ARRAY_FLOAT_BASE_OFFSET=" + Unsafe.ARRAY_FLOAT_BASE_OFFSET);    System.out.println("Unsafe.ARRAY_FLOAT_INDEX_SCALE=" + Unsafe.ARRAY_FLOAT_INDEX_SCALE);    System.out.println("Unsafe.ARRAY_DOUBLE_BASE_OFFSET=" + Unsafe.ARRAY_DOUBLE_BASE_OFFSET);    System.out.println("Unsafe.ARRAY_DOUBLE_INDEX_SCALE=" + Unsafe.ARRAY_DOUBLE_INDEX_SCALE);    System.out.println("Unsafe.ARRAY_OBJECT_BASE_OFFSET=" + Unsafe.ARRAY_OBJECT_BASE_OFFSET);    System.out.println("Unsafe.ARRAY_OBJECT_INDEX_SCALE=" + Unsafe.ARRAY_OBJECT_INDEX_SCALE);    System.out.println("------------------------------");    // 基本数据数组类型操作    int[] array = new int[] {11, 22, 33};    /*        改变最后一个元素的值，地址的算法就是：基础地址+偏移量，这个偏移量就是类型占用的大小*位置，Unsafe.ARRAY_INT_BASE_OFFSET + (array.length - 1) * Unsafe.ARRAY_INT_INDEX_SCALE     */    System.out.println("before put array[2]=" + array[2]);    unsafe.putInt(array, (long)Unsafe.ARRAY_INT_BASE_OFFSET + (array.length - 1) * Unsafe.ARRAY_INT_INDEX_SCALE,        100);    // 获取最后一个元素的值    System.out.println("after put array[2]=" + array[2]);    // 也可以这么获取，使用基础地址+偏移量的方式    System.out.println("after put array[2]=" + unsafe.getInt(array,        (long)Unsafe.ARRAY_INT_BASE_OFFSET + (array.length - 1) * Unsafe.ARRAY_INT_INDEX_SCALE));    System.out.println("-------------------");    // Object类型数组操作    Cat[] cats = {new Cat("cat1", 1), new Cat("cat2", 2), new Cat("cat3", 3)};    System.out.println("before put cats[2]=" + cats[2]);    unsafe.putObject(cats,        (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET + (cats.length - 1) * Unsafe.ARRAY_OBJECT_INDEX_SCALE,        new Cat("newcat", 10000));    // 获取最后一个元素的值    System.out.println("after put cats[2]=" + cats[2]);    // 也可以这么获取，使用基础地址+偏移量的方式    System.out.println("after put cats[2]=" + unsafe.getObject(cats,        (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET + (cats.length - 1) * Unsafe.ARRAY_OBJECT_INDEX_SCALE));    System.out.println("-------------------");}
```

输出：

```
Unsafe.ARRAY_BOOLEAN_BASE_OFFSET=16Unsafe.ARRAY_BOOLEAN_INDEX_SCALE=1Unsafe.ARRAY_BYTE_BASE_OFFSET=16Unsafe.ARRAY_BYTE_INDEX_SCALE=1Unsafe.ARRAY_SHORT_BASE_OFFSET=16Unsafe.ARRAY_SHORT_INDEX_SCALE=2Unsafe.ARRAY_CHAR_BASE_OFFSET=16Unsafe.ARRAY_CHAR_INDEX_SCALE=2Unsafe.ARRAY_INT_BASE_OFFSET=16Unsafe.ARRAY_INT_INDEX_SCALE=4Unsafe.ARRAY_LONG_BASE_OFFSET=16Unsafe.ARRAY_LONG_INDEX_SCALE=8Unsafe.ARRAY_FLOAT_BASE_OFFSET=16Unsafe.ARRAY_FLOAT_INDEX_SCALE=4Unsafe.ARRAY_DOUBLE_BASE_OFFSET=16Unsafe.ARRAY_DOUBLE_INDEX_SCALE=8Unsafe.ARRAY_OBJECT_BASE_OFFSET=16Unsafe.ARRAY_OBJECT_INDEX_SCALE=4------------------------------before put array[2]=33after put array[2]=100after put array[2]=100-------------------static...before put cats[2]=Cat{name='cat3', speed=3}after put cats[2]=Cat{name='newcat', speed=10000}after put cats[2]=Cat{name='newcat', speed=10000}-------------------
```

Tips:

> 如果操作的元素位置没有在数组范围内的话，put 和 get 操作不会异常，都会成功，因为这是内存操作，使用的是基础地址 + 偏移量，但是并没有改变原始数组的大小，put 后可以获取相应位置的内存数据，在没有 put 前调用 get 则获取的是数据类型的默认值。

### CAS

比较并交换（Compare And Swap），在 jvm 里是一个原子操作，先获取内存的值，然后判断内存值和预期值是否相同，相同则更新为新值表示操作成功，不同则直接返回 false，表明操作失败。java 里的 JUC 包下很多队列或者锁都采用了这种实现方式。

```
//每次都从主内存获取var1对象var2偏移量的long值public native long getLongVolatile(Object var1, long var2);//将var4值放入指定var1对象的var2偏移量位置，直接刷新到主内存public native void putLongVolatile(Object var1, long var2, long var4);// 比较并替换原值为新值，操作成功返回true否则false，var1是指定对象，var2是偏移量地址，var4是预期的原值，var5是要更新的新值public final native boolean compareAndSwapLong(Object var1, long var2, long var4, long var6);// 自旋获取原值并增加数值，var1是指定对象，var2是偏移量地址，var4是要增加的值public final int getAndAddLong(Object var1, long var2, long var4) {    int var5;    do {        var5 = this.getLongVolatile(var1, var2);    } while(!this.compareAndSwapLong(var1, var2, var5, var5 + var4));     return var5;}// 自旋获取原值并设置新值，var1是指定对象，var2是偏移量地址，var4是要设置的新值public final int getAndSetLong(Object var1, long var2, long var4) {    int var5;    do {        var5 = this.getIntVolatile(var1, var2);    } while(!this.compareAndSwapLong(var1, var2, var5, var4));     return var5;}// 还有Object相关的cas操作这里没有列出
```

举几个栗子

```
public void test3() throws Exception {    Cat cat = new Cat("Kitty", 1000);    long speedOffset = unsafe.objectFieldOffset(Cat.class.getDeclaredField("speed"));    System.out.println("before putLongVolatile,getLongVolatile=" + unsafe.getLongVolatile(cat, speedOffset));    // 设置speed的值为2000    unsafe.putLongVolatile(cat, speedOffset, 2000);    System.out.println("after putLongVolatile,getLongVolatile=" + unsafe.getLongVolatile(cat, speedOffset));    // 到这里speed的值是2000，但是compareAndSwapLong里预期的值是3000，所以cas失败，返回false    System.out.println("compareAndSwapLong result:" + unsafe.compareAndSwapLong(cat, speedOffset, 3000, 4000));    // 到这里speed的值是2000，但是compareAndSwapLong里预期的值是2000，cas更新成功，返回true    System.out.println("compareAndSwapLong result:" + unsafe.compareAndSwapLong(cat, speedOffset, 2000, 4000));    // cas后speed的值就是4000了    System.out.println("after compareAndSwapLong,getLongVolatile=" + unsafe.getLongVolatile(cat, speedOffset));    // getAndAddLong会返回原值4000，新值=原值+10    System.out.println("getAndAddLong:" + unsafe.getAndAddLong(cat, speedOffset, 10));    // getAndAddLong后speed新值是4010    System.out.println("after getAndAddLong,getLongVolatile=" + unsafe.getLongVolatile(cat, speedOffset));    // getAndSetLong会返回原值4010，新值=要设置的新值1000    System.out.println("getAndSetLong:" + unsafe.getAndSetLong(cat, speedOffset, 1000));    // getAndSetLong后speed新值是1000    System.out.println("after getAndSetLong,getLongVolatile=" + unsafe.getLongVolatile(cat, speedOffset));}
```

输出结果：

```
static...before putLongVolatile,getLongVolatile=1000after putLongVolatile,getLongVolatile=2000compareAndSwapLong result:falsecompareAndSwapLong result:trueafter compareAndSwapLong,getLongVolatile=4000getAndAddLong:4000after getAndAddLong,getLongVolatile=4010getAndSetLong:4010after getAndSetLong,getLongVolatile=1000
```

### 线程调度及同步

```
// 释放线程让其继续执行，多次调用只会生效一次，可以在park前调用public native void unpark(Object thread);// 阻塞线程，isAbsolute为true：表示绝对时间，time的单位是毫秒ms，false：表示相对时间，time的单位是纳秒级的时间public native void park(boolean isAbsolute, long time);//以下3个方法均标注过期了，建议使用其他同步方法// 获取var1的对象锁，没获取到则阻塞等待public native void monitorEnter(Object var1);// 尝试获取var1的对象锁，不阻塞，获取到则返回true，没获取到返回falsepublic native boolean tryMonitorEnter(Object var1);// 释放var1对象锁public native void monitorExit(Object var1);
```

我们先看看 monitor 同步相关的测试：

```
public void test4() {    Object obj = new Object();    Thread t1 = new Thread(new Runnable() {        @Override        public void run() {            try {                System.out.println(Thread.currentThread().getName() + " isrunning...");                unsafe.monitorEnter(obj);                System.out.println(Thread.currentThread().getName() + " got monitorEnter...");                Thread.sleep(3000);                System.out.println(Thread.currentThread().getName() + " business over...");            } catch (Exception e) {                e.printStackTrace();            } finally {                unsafe.monitorExit(obj);                System.out.println(Thread.currentThread().getName() + " monitorExit...");            }        }    });    Thread t2 = new Thread(new Runnable() {        @Override        public void run() {            try {                System.out.println(Thread.currentThread().getName() + " isrunning...");                unsafe.monitorEnter(obj);                System.out.println(Thread.currentThread().getName() + " got monitorEnter...");                Thread.sleep(2000);                System.out.println(Thread.currentThread().getName() + " business over...");            } catch (Exception e) {                e.printStackTrace();            } finally {                unsafe.monitorExit(obj);                System.out.println(Thread.currentThread().getName() + " monitorExit...");            }        }    });    Thread t3 = new Thread(new Runnable() {        @Override        public void run() {            boolean flag = false;            try {                System.out.println(Thread.currentThread().getName() + " isrunning...");                flag = unsafe.tryMonitorEnter(obj);                System.out.println(Thread.currentThread().getName() + " tryMonitorEnter:" + flag);            } catch (Exception e) {                e.printStackTrace();            } finally {                if (flag) {                    unsafe.monitorExit(obj);                    System.out.println(Thread.currentThread().getName() + " monitorExit...");                }            }            System.out.println(Thread.currentThread().getName() + " over...");        }    });    t1.start();    t2.start();    t3.start();}
```

可能的一种输出如下（线程是根据系统分配资源调度的，输出先后顺序会有多种），下面的这个输出我们可以看到先输出 3 个线程都启动了，Thread-2 尝试获取锁失败就结束了，然后 Thread-0 竞争到了对象锁，等 Thread-0 线程运行完毕释放了锁，Thread-1 才会获取到锁继续执行直到结束释放锁。  
Tips：

> monitor 相关的方法已经加了 @deprecated 注解，官方已经不再建议使用，可以换成其他锁或者同步方式

```
Thread-0 isrunning...Thread-2 isrunning...Thread-2 tryMonitorEnter:falseThread-2 over...Thread-1 isrunning...Thread-0 got monitorEnter...Thread-0 business over...Thread-0 monitorExit...Thread-1 got monitorEnter...Thread-1 business over...Thread-1 monitorExit...
```

我们在来看看 park、unpark 相关的使用

```
public void test5() throws Exception {    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");    System.out        .println(LocalDateTime.now().format(formatter) + " " + Thread.currentThread().getName() + " is running...");    // 这里让当前线程阻塞6s，注意：如果park第一个参数是true的话，表示绝对时间,这个时间是毫秒级的，也就是系统时间，系统到这个绝对时间后才唤醒执行    unsafe.park(true, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(6));    System.out        .println(LocalDateTime.now().format(formatter) + " " + Thread.currentThread().getName() + " continue...");    // 这里让当前线程阻塞3s，注意：如果park第一个参数是false的话，这个是纳秒级别的时间，表示相对当前时间3s后继续唤醒执行    unsafe.park(false, TimeUnit.SECONDS.toNanos(3));    System.out        .println(LocalDateTime.now().format(formatter) + " " + Thread.currentThread().getName() + " continue...");    // 如果park的第一个参数是false，第二个值是0，则会一直等待，直到其他线程调用了unpark这个线程才会结束阻塞    // 一般像这种无限期等待的调了多少次park(false, 0)就要对于调同样次数的unpark才会完全解除阻塞    unsafe.park(false, 0);}
```

输出：

```
2020-05-11 08:01:04 main is running...2020-05-11 08:01:10 main continue...2020-05-11 08:01:13 main continue...
```

再看一个案例：

```
public void test6() throws Exception {    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");    Thread t1 = new Thread(new Runnable() {        @Override        public void run() {            try {                System.out.println(LocalDateTime.now().format(formatter) + " " + Thread.currentThread().getName()                    + " is running...");                // 这里让当前线程阻塞600s也就是10分钟，注意：如果park第一个参数是true的话，表示绝对时间,这个时间是毫秒级的，也就是系统时间，系统到这个绝对时间后才唤醒执行                unsafe.park(true, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(600));                System.out.println(LocalDateTime.now().format(formatter) + " " + Thread.currentThread().getName()                    + " continue...");            } catch (Exception e) {                e.printStackTrace();            }        }    });    Thread t2 = new Thread(new Runnable() {        @Override        public void run() {            try {                System.out.println(LocalDateTime.now().format(formatter) + " " + Thread.currentThread().getName()                    + " is running...");                // 这里让当前线程阻塞600s也就是10分钟，注意：如果park第一个参数是true的话，表示绝对时间,这个时间是毫秒级的，也就是系统时间，系统到这个绝对时间后才唤醒执行                unsafe.park(true, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(600));                // unsafe.park(true, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(600));                System.out.println(LocalDateTime.now().format(formatter) + " " + Thread.currentThread().getName()                    + " continue...");            } catch (Exception e) {                e.printStackTrace();            }        }    });    t1.start();    t2.start();    // 主线程休眠2秒    Thread.sleep(2000);    System.out        .println(LocalDateTime.now().format(formatter) + " " + Thread.currentThread().getName() + " is running...");    // 这里调了unpark方法，参数就是t1线程，unsafe.unpark唤醒了t1线程，使得t1线程不用等到10分钟立马就可以执行    unsafe.unpark(t1);    // 下面连续调用了两次unpark t2线程，但是结果只释放了一次令牌，如果把t2线程的unsafe.park注释去掉，那么t2线程会一直等到park的时间到后被唤醒执行，    unsafe.unpark(t2);    unsafe.unpark(t2);    System.out.println(LocalDateTime.now().format(formatter) + " " + Thread.currentThread().getName() + " over...");}
```

输出如下：

```
2020-05-11 08:05:26 Thread-1 is running...2020-05-11 08:05:26 Thread-0 is running...2020-05-11 08:05:28 main is running...2020-05-11 08:05:28 main over...2020-05-11 08:05:28 Thread-1 continue...2020-05-11 08:05:28 Thread-0 continue...
```

> unsafe 的 park 和 unpark 在 JUC 并发包下使用的特别多，后续再介绍吧

### 内存屏障

这个我没有深入的了解，网上找了些资料看了看，没有具体的实践过，大家可以了解下。

```
loadFence：保证在这个屏障之前的所有读操作都已经完成。storeFence：保证在这个屏障之前的所有写操作都已经完成。fullFence：保证在这个屏障之前的所有读写操作都已经完成。
```

### 其他

类加载，类实例化相关的一些方法，还有其他的方法，这里不再一一说明，虽然不常用，但是了解其运行原理，或者去研究一下 jvm 的源码对自己都是一种提升。

再见
--

好了，就胡扯到这里吧，文章里的都是个人理解和实践，难免会有理解错误和实践错误的，请各位看官多多指正。