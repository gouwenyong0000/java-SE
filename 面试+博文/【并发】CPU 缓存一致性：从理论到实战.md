> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [zhuanlan.zhihu.com](https://zhuanlan.zhihu.com/p/570953356)

> 本文转自：

[CPU 缓存一致性：从理论到实战](https://mp.weixin.qq.com/s?__biz=MzkzMzM5MTUwNQ==&mid=2247483899&idx=1&sn=3bd15de41b4c3d38f5b7e7bbd5bdac01&chksm=c24c7c05f53bf51383fdf07844a4fdd53d6ff559e0e5450dbabfbf275c997c68d15674019b9b&token=1184165571&lang=zh_CN#rd)

**一、存储体系结构**
------------

速度快的存储硬件成本高、容量小，速度慢的成本低、容量大。为了权衡成本和速度，计算机存储分了很多层次，扬长避短，有**寄存器**、**L1 cache**、**L2 cache**、**L3 cache**、**主存**（内存）和**硬盘**等。图 1 展示了现代存储体系结构。

![](https://pic2.zhimg.com/v2-88ab60b2bc5a0573578b11eac1109b69_r.jpg)

根据程序的**空间局部性**和**时间局部性**原理，缓存命中率可以达到 **70～90%** 。因此，增加缓存可以让整个存储系统的性能接近寄存器，并且每字节的成本都接近内存，甚至是磁盘。

所以缓存是存储体系结构的灵魂。

**二、缓存原理**
----------

### **2.1 缓存的工作原理**

**cache line（缓存行）**是缓存进行管理的最小存储单元，也叫缓存块，每个 cache line 包含 **Flag**、**Tag** 和 **Data** ，通常 Data 大小是 **64 字节**，但不同型号 CPU 的 Flag 和 Tag 可能不相同。从内存向缓存加载数据是按整个缓存行加载的，一个缓存行和一个相同大小的**内存块**对应。

![](https://pic2.zhimg.com/v2-03f1223efe5148a014c3c912238d9411_r.jpg)

图 2 中，缓存是按照矩阵方式排列 (M × N)，横向是**组 (Set)**，纵向是**路 (Way)**。每一个元素是缓存行 (cache line)。

那么给定一个虚拟地址 **addr** 如何在缓存中定位它呢？首先把它所在的**组号**找到，即：

```
//左移6位是因为 Block Offset 占 addr 的低 6 位，Data 为 64 字节
Set Index = (addr >> 6) % M;
```

然后遍历该组所有的路，找到 **cache line** 中的 **Tag** 与 **addr** 中 **Tag** 相等为止，所有路都没有匹配成功，那么缓存未命中。

```
整个缓存容量 = 组数 × 路数 × 缓存行大小
```

我电脑的 CPU 信息：

![](https://pic4.zhimg.com/v2-6203ea45224ae31d45ecce1793d3b81b_r.jpg)

我电脑的缓存信息：

![](https://pic1.zhimg.com/v2-980d8335021edf1adadbf34cec0100d8_r.jpg)

通过缓存行大小和路数可以倒推出缓存的组数，即：

```
缓存组数 = 整个缓存容量 ÷ 路数 ÷ 缓存行大小
```

### **2.2 缓存行替换策略**

目前最常用的缓存替换策略是**最近最少使用算法**（Least Recently Used ，**LRU**）或者是类似 LRU 的算法。

**LRU** 算法比较简单，如图 3，缓存有 4 路，并且访问的地址都哈希到了同一组，访问顺序是 D1、D2、D3、D4 和 D5，那么 D1 会被 D5 替换掉。算法的实现方式有很多种，最简单的实现方式是**位矩阵**。

首先，定义一个行、列都与缓存路数相同的矩阵。当访问某个路对应的缓存行时，先将该路对应的所有行置为 1，然后再将该路对应的所有列置为 0。

最近最少使用的缓存行所对应的矩阵行中 1 的个数最少，最先被替换出去。

![](https://pic1.zhimg.com/v2-fc47f11d0808227cb2a19c641997a600_r.jpg)

### **2.3 缓存缺失**

缓存缺失就是缓存未命中，需要把内存中数据加载到缓存，所以运行速度会变慢。

就拿我的电脑来测试，L1d 的缓存大小是 32KB（32768B），8 路，缓存行大小 64B，那么

```
缓存组数 = 32 × 1024 ÷ 8 ÷ 64 = 64
```

运行下面的代码

```
char *a = new char(64 * 64 * 8); //32768B
for(int i = 0; i < 20000000; i++) 
    for(int j = 0; j < 32768; j += 4096) 
        a[j]++;
```

结果：循环 160000000 次，耗时 301 ms。除了第一次未命中缓存，后面每次读写数据都能命中缓存。

调整上面的代码，并运行

```
char *a = new char(64 * 64 * 8 * 2); //65536B
for(int i = 0; i < 10000000; i++)
    for(int j = 0; j < 65536; j += 4096)
        a[j]++;
```

结果：循环 160000000 次，耗时 959 ms。每一次读写数据都没有命中缓存，所以耗时增加了 2 倍。

### **2.4 程序局部性**

程序局部性就是读写内存数据时读写连续的内存空间，目的是让缓存可以命中，减少缓存缺失导致替换的开销。

我电脑上运行下面代码

```
int M = 10000, N = 10000;
char (*a)[N] = (char(*)[N])calloc(M * N, sizeof(char));
for(int i = 0; i < M; i++)
    for(int j = 0; j < N; j++)
        a[i][j]++;
```

结果：循环 100000000 次，耗时 314 ms。利用了程序局部性原理，缓存命中率高。

修改上面的代码如下，并运行

```
int M = 10000, N = 10000;
char (*a)[N] = (char(*)[N])calloc(M * N, sizeof(char));
for(int j = 0; j < N; j++)
    for(int i = 0; i < M; i++)
        a[i][j]++;
```

结果：循环 100000000 次，耗时 1187 ms。没有利用程序局部性原理，缓存命中率低，所以耗时增加了 2 倍。

### **2.5 伪共享（false-sharing）**

当两个线程同时各自修改两个相邻的变量，由于缓存是**按缓存行来整体组织**的，当一个线程对缓存行中数据执行写操作时，必须通知其他线程该缓存行失效，导致另一个线程从缓存中读取其想修改的数据失败，必须从内存重新加载，导致性能下降。

我电脑运行下面代码

```
struct S {
    long long a;
    long long b;
} s;
std::thread t1([&]() {
    for(int i = 0; i < 100000000; i++)
        s.a++;
});
std::thread t2([&]() {
    for(int i = 0; i < 100000000; i++)
        s.b++;
});
```

结果：耗时 512 ms，原因上面提到了，就是两个线程互相影响，使对方的缓存行失效，导致直接从内存读取数据。

解决办法是对上面代码做如下修改：

```
struct S {
    long long a;
    long long noop[8];
    long long b;
} s;
```

结果：耗时 181 ms，原因是通过 long long noop[8] 把两个数据（a 和 b）划分到两个不同的缓存行中，不再互相使对方的缓存失效，所以速度变快了。

本小节的测试代码都没有开启编译器优化，即编译选项为 **-O0** 。

**三、缓存一致性协议**
-------------

在单核时代，增加缓存可以大大提高读写速度，但是到了多核时代，却引入了缓存一致性问题，如果有一个核心修改了缓存行中的某个值，那么必须有一种机制保证其他核心能够观察到这个修改。

### **3.1 缓存写策略**

从缓存和内存的更新关系来看，分为：

*   **写回（write-back）**对缓存的修改不会立刻传播到内存，只有当缓存行被替换时，这些被修改的缓存行才会写回并覆盖内存中过时的数据。
*   **写直达（write through）**缓存中任何一个字节的修改，都会立刻穿透缓存直接传播到内存，这种比较耗时。

从写缓存时 CPU 之间的更新策略来看，分为：

*   **写更新（Write Update）**每次缓存写入新的值，该核心必须发起一次总线请求，通知其他核心更新他们缓存中对应的值。

*   坏处：写更新会占用很多总线带宽；
*   好处：其他核心能立刻获得最新的值。

*   **写无效（Write Invalidate）**每次缓存写入新的值，都将其他核心缓存中对应的缓存行置为无效。

*   坏处：当其他核心再次访问该缓存时，发现缓存行已经失效，必须从内存中重新载入最新的数据；
*   好处：多次写操作只需发一次总线事件，第一次写已经将其他核心缓存行置为无效，之后的写不必再更新状态，这样可以有效地节省核心间总线带宽。

从写缓存时数据是否被加载来看，分为：

*   **写分配（Write Allocate）**在写入数据前将数据读入缓存。当缓存块中的数据在未来读写概率较高，也就是程序空间局部性较好时，写分配的效率较好。
*   **写不分配（Not Write Allocate）**在写入数据时，直接将数据写入内存，并不先将数据块读入缓存。当数据块中的数据在未来使用的概率较低时，写不分配性能较好。

### **3.2 MESI 协议**

MESI 协议是⼀个基于**失效**的缓存⼀致性协议，是⽀持**写回（write-back）**缓存的最常⽤协议。也称作伊利诺伊协议 (Illinois protocol，因为是在伊利诺伊⼤学厄巴纳 - ⾹槟分校被发明的)。

为了解决多个核心之间的数据传播问题，提出了**总线嗅探（Bus Snooping）**策略。本质上就是把所有的读写请求都通过总线（Bus）广播给所有的核心，然后让各个核心去**嗅探**这些请求，再根据本地的状态进行响应。

**3.2.1 状态**

*   **已修改 Modified (M)**：缓存⾏是脏的，与主存的值不同。如果别的 CPU 内核要读主存这块数据，该缓存⾏必须回写到主存，状态变为共享 (S).
*   **独占 Exclusive (E)**：缓存⾏只在当前缓存中，但是⼲净的，缓存数据等于主存数据。当别的缓存读取它时，状态变为共享；当前写数据时，变为已修改状态。
*   **共享 Shared (S)**：缓存⾏也存在于其它缓存中且是⼲净的。缓存⾏可以在任意时刻抛弃。
*   **⽆效 Invalid (I)**：缓存⾏是⽆效的。

这些状态信息实际上存储在**缓存行**（**cache line**）的 **Flag** 里。

**3.2.2 事件**

*   处理器对缓存的请求:

*   **PrRd**：核心请求从缓存块中读出数据；
*   **PrWr**：核心请求向缓存块写入数据。

*   总线对缓存的请求:

*   **BusRd**：总线嗅探器收到来自其他核心的读出缓存请求；
*   **BusRdX**：总线嗅探器收到另一核心写⼀个其不拥有的缓存块的请求；
*   **BusUpgr**：总线嗅探器收到另一核心写⼀个其拥有的缓存块的请求；
*   **Flush**：总线嗅探器收到另一核心把一个缓存块写回到主存的请求；
*   **FlushOpt**：总线嗅探器收到一个缓存块被放置在总线以提供给另一核心的请求，和 Flush 类似，但只不过是从缓存到缓存的传输请求。

**3.2.3 状态机**

![](https://pic2.zhimg.com/v2-eebf98aea4b35471d62caeb5aff2d6d9_r.jpg)

表 1 是对状态机图 4 的详解讲解（选读）

![](https://pic2.zhimg.com/v2-e6804c6443a7f4246f9d262b3d6a00f9_r.jpg)

**3.2.4 动画演示**

![](https://pic4.zhimg.com/v2-ea0ec3b55246553250edb8d4ea9e28d7_r.jpg)

各家 CPU 厂商没有都完全按照 MESI 实现缓存一致性协议，导致 MESI 有很多变种，例如：Intel 采用的 MESIF 和 AMD 采用的 MOESI，ARM 大部分采用的是 MESI，少部分使用的是 MOESI 。

### **3.3 MOESI 协议（选读）**

MOESI 是一个**完整的缓存一致性协议**，它包含了其他协议中常用的所有可能状态。除了四种常见的 MESI 协议状态之外，还有第五种 **Owned** 状态，表示修改和共享的数据。

这就避免了在共享数据之前将修改过的数据写回主存的需要。虽然数据最终仍然必须写回，但写回可能是延迟的。

*   **已修改 Modified (M)**：缓存⾏是脏的（dirty），与主存的值不同，并且缓存具有系统中唯一有效数据。处于修改状态的缓存可以将数据提供给另一个读取器，而无需将其传输到内存，然后状态变为 O，读取者变为 S。
*   **拥有 Owned(O)**：缓存⾏是脏的（dirty），与主存的值不同，但不是系统中唯一有效副本，一定存在其他的 S。为其他核心提供读请求，较少核心间总线带宽。  
    
*   **独占 Exclusive (E)**：缓存⾏只在当前缓存中，但是⼲净的（clean），缓存数据同于主存数据。当别的缓存读取它时，状态变为共享；当前写数据时，变为已修改状态。
*   **共享 Shared (S)**：缓存⾏也存在于其它缓存中且**不一定**是⼲净的。如果 O 存在，就是脏的，反之亦然。
*   **⽆效 Invalid (I)**：缓存⾏是⽆效的。

### **3.4 MESIF 协议（选读）**

MESIF 是一个**缓存一致性**和**记忆连贯**协议，该协议由五个状态组成：**已修改（M）**，**互斥（E）**，**共享（S）**，**无效（I）**和**转发（F）**。

**M，E，S 和 I 状态与 MESI 协议一致**。**F 状态是 S 状态的一种特殊形式**，当系统中有多个 S 时，必须选取一个转换为 F，只有 F 状态的负责应答。通常是最后持有该副本的转换为 F，注意 **F 是干净的数据**。

该协议与 MOESI 协议有较大的不同，也远比 MOESI 协议复杂。该协议由 Intel 的 快速通道互联 **QPI**（QuickPath Interconnect）技术引入，其主要目的是解决 “**基于点到点互联的非一致性内存访问（**Non-uniform memory access，**NUMA）处理器系统**” 的缓存一致性问题，而不是 “**基于共享总线的一致性内存访问（**Uniform Memory Access， **UMA）处理器系统**” 的缓存一致性问题。

**四、内存屏障（Memory Barriers）**
---------------------------

编译器和处理器都必须遵守重排序规则。在单处理器的情况下，不需要任何额外的操作便能保持正确的顺序。但是对于多处理器来说，保证一致性通常需要增加内存屏障指令。即使编译器可以优化掉字段的访问（例如因为未使用加载到的值），编译器仍然需要生成内存屏障，就好像字段访问仍然存在一样（可以单独将内存屏障优化掉）。

内存屏障只与内存模型中的高级概念（例如 **acquire** 和 **release**）间接相关。内存屏障指令只直接控制 CPU 与其缓存的交互，以及它的写缓冲区（持有等待刷新到内存的数据的存储）和它的用于等待加载或推测执行指令的缓冲。这些影响可能导致缓存、主内存和其他处理器之间的进一步交互。

几乎所有的处理器都至少支持一个粗粒度的屏障指令（通常称为 **Fence**，也叫**全屏障**），它保证了严格的有序性：在 Fence 之前的所有读操作（load）和写操作（store）先于在 Fence 之后的所有读操作（load）和写操作（store）执行完。对于任何的处理器来说，这通常都是最耗时的指令之一（它的开销通常接近甚至超过原子操作指令）。大多数处理器还支持更细粒度的屏障指令。

*   **LoadLoad Barrier（读读屏障）**  
    指令 **Load1; LoadLoad; Load2** 保证了 Load1 先于 Load2 和后续所有的 load 指令加载数据。通常情况下，在执行预测读（speculative loads）或乱序处理（out-of-order processing）的处理器上需要显式的 LoadLoad Barrier。在始终保证读顺序（load ordering）的处理器上，这些屏障相当于无操作（no-ops）。
*   **StoreStore Barrier（写写屏障）**  
    指令 **Store1; StoreStore; Store2** 保证了 Store1 的数据先于 Store2 及后续 store 指令的数据对其他处理器可见（刷新到内存）。通常情况下，在不保证严格按照顺序从写缓冲区（store buffers）或者 缓存（caches）刷新到其他处理器或内存的处理器上，需要使用 StoreStore Barrier。
*   **LoadStore Barrier（读写屏障）**  
    指令 **Load1; LoadStore; Store2** 保证了 Load1 的加载数据先于 Store2 及后续 store 指令刷新数据到主内存。只有在乱序（out-of-order）处理器上，等待写指令（waiting store instructions）可以绕过读指令（loads）的情况下，才会需要使用 LoadStore 屏障。
*   **StoreLoad Barrier（写读屏障）刷新写缓冲区，最耗时**  
    指令 **Store1; StoreLoad; Load2** 保证了 Store1 的数据对其他处理器可见（刷新数据到内存）先于 Load2 及后续的 load 指令加载数据。StoreLoad 屏障可以防止后续的读操作错误地使用了 Store1 写的数据，而不是使用来自另一个处理器的更近的对同一位置的写。因此只有需要将对同一个位置的写操作（stores）和随后的读操作（loads）分开时，才严格需要 StoreLoad 屏障。StoreLoad 屏障通常是开销最大的屏障，几乎所有的现代处理器都需要该屏障。之所以开销大，部分原因是它需要禁用绕过缓存（cache）从写缓冲区（Store Buffer）读取数据的机制。这可以通过让缓冲区完全刷新，外加暂停其他操作来实现，这就是 **Fence** 的效果。一般用 **Fence** 代替 StoreLoad Barrier ，所以事实上，执行 StoreLoad 指令同时也获得了其他三个屏障的效果，但是通过组合其他屏障通常不能获得与 StoreLoad Barrier 相同的效果。

表 2 是各处理器支持的内存屏障和原子操作

![](https://pic3.zhimg.com/v2-21edfc07bc2de3360776f007e22c532a_r.jpg)

### **4.1 写缓冲与写屏障**

严格按照 MESI 协议，核心 0 在修改本地缓存之前，需要向其他核心发送 Invalid 消息，其他核心收到消息后，使他们本地对应的缓存行失效，并返回 Invalid acknowledgement 消息，核心 0 收到后修改缓存行。这里核心 0 等待其他核心返回确认消息的时间对核心来说是漫长的。

![](https://pic4.zhimg.com/v2-73faeaf26192e6efde26b02ba9199d9f_r.jpg)

为了解决这个问题，引入了 **Store Buffer** ，当核心想修改缓存时，直接写入 **Store uffer** ，无需等待，继续处理其他事情，由 **Store Buffer** 完成后续工作。

![](https://pic3.zhimg.com/v2-97adf2ddd569626761c37c4f0f4f4712_r.jpg)

这样一来写的速度加快了，但是引来了新问题，下面代码的 bar 函数中的断言可能会失败。

```
int a = 0, b = 0;
// CPU0
void foo() {
    a = 1;
    b = 1;
}
// CPU1
void bar() {
    while (b == 0) continue;
    assert(a == 1);
}
```

第一种情况：CPU 为了提升运行效率和提高缓存命中率，采用了**乱序执行**；

第二种情况：**Store Buffer** 在写入时，b 所对应的缓存行是 **E** 状态，a 所对应的缓存行是 **S** 状态，因为对 b 的修改不需要核心间同步，但是修改 a 则需要，也就是 b 会先写入缓存。与之对应 CPU1 中 a 是 **S** 状态，b 是 **I** 状态，由于 b 所对应的缓存区域是 **I** 状态，它就会向总线发出 BusRd 请求，那么 CPU1 就会先把 b 的最新值读到本地，完成变量 b 值的更新，但是从缓存直接读取 a 值是 0 。

举一个更极端的例子

```
// CPU0
void foo() {
    a = 1;
    b = a;
}
```

第一种情况不会发生了，原因是代码有依赖，不会乱序执行。但由于 Store Buffer 的存在，第二种情况仍然可能发生，原因同上。这会让人感到更加匪夷所思。

为了解决上面问题，引入了**内存屏障**，**屏障的作用是前边的读写操作未完成的情况下，后面的读写操作不能发生**。这就是 **Arm** 上 **dmb** 指令的由来，它是数据内存屏障（**Data Memory Barrier**）的缩写。

```
int a = 0, b = 0; 
// CPU0
void foo() {
    a = 1;
    smp_mb(); //内存屏障，各CPU平台实现不一样
    b = 1;
}
// CPU1
void bar() {
    while (b == 0) continue;
    assert(a == 1);
}
```

加上内存屏障后，保证了 a 和 b 的写入缓存顺序。

**总的来说，Store Buffer 提升了写性能，但放弃了缓存的顺序一致性，这种现象称为弱缓存一致性**。通常情况下，多个 CPU 一起操作同一个变量的情况是比较少的，所以 Store Buffer 可以大幅提升程序的性能。但在需要核间同步的情况下，还是需要通过手动添加内存屏障来保证缓存一致性。

上面解决了核间同步的写问题，但是核间同步还有一个瓶颈，那就是读。

### **4.2 失效队列与读屏障**

前面引入 Store Buffer 提升了写入速度，那么 invalid 消息确认速度相比起来就慢了，带来了**速度不匹配**，很容易导致 Store Buffer 的内容还没及时写到缓存里，自己就满了，从而失去了加速的作用。

为了解决这个问题，又引入了 **Invalid Queue**。收到 Invalid 消息的核心立刻返回 Invalid acknowledgement 消息，然后把 Invalid 消息加入 Invalid Queue ，等到空闲的时候再去处理 Invalid 消息。

![](https://pic1.zhimg.com/v2-fabea200611b5c9b266be256c4ba0128_r.jpg)

运行上面增加内存屏障的代码，第 11 行的断言又可能失败了。

核心 0 中 a 所对应的缓存行是 **S** 状态，b 所对应的缓存行是 **E** 状态；核心 1 中 a 所对应的缓存行是 **S** 状态，b 所对应的缓存行是 **I** 状态；

*   因为有内存屏障在，a 和 b 的写入缓存的顺序不会乱。
*   a 先向其他核心发送 Invalid 消息，并且等待 Invalid 确认消息；
*   Invalid 消息先入 核心 1 对应的 Invalid Queue 并立刻返回确认消息，等待 核心 1 处理；
*   核心 0 收到确认消息后把 a 写入缓存，继续处理 b 的写入，由于 b 是 **E** 状态，直接写入缓存；
*   核心 1 发送 BusRd 消息，读取到新的 b 值，然后获取 a（**S** 状态）值是 0，因为使其无效的消息还在 Invalid Queue 中，第 11 行断言失败。

引入 Invalid Queue 后，对核心 1 来说看到的 a 和 b 的写入又出现乱序了。

解决办法是继续加内存屏障，核心 1 想越过屏障必须清空 Invalid Queue，及时处理了对 a 的无效，然后读取到新的 a 值，如下代码：

```
int a = 0, b = 0;
// CPU0
void foo() {
    a = 1;
    smp_mb();
    b = 1;
}
// CPU1
void bar() {
    while (b == 0) continue;
    smp_mb(); //继续加内存屏障
    assert(a == 1);
}
```

这里使用的内存屏障是**全屏障**，包括读写屏障，过于严格了，会导致性能下降，所以有了细粒度的**读屏障**和**写屏障**。

### **4.3 读写屏障分离**

**分离的写屏障和读屏障的出现，是为了更加精细地控制 Store Buffer** **和** **Invalid Queue** **的顺序。**

*   **读屏障**不允许其前后的读操作越过屏障；
*   **写屏障**不允许其前后的写操作越过屏障；

优化前面的代码如下

```
int a = 0, b = 0;
// CPU0
void foo() {
  a = 1;
  smp_wmb(); //写屏障
  b = 1;
}
// CPU1
void bar() {
  while (b == 0) continue;
  smp_rmb(); //读屏障
  assert(a == 1);
}
```

这种修改只有在区分读写屏障的体系结构里才会有作用，比如 **alpha** 结构。在 **x86** 和 **Arm** 中是没有作用的，因为 x86 采用了 TSO 模型，后面会详细介绍，而 Arm 采用了单向屏障。

### **4.4 单向屏障**

单向屏障 (**half-way barrier**) 也是一种内存屏障，但它不是以读写来区分的，而是像单行道一样，只允许单向通行，例如 ARM 中的 stlr 和 ldar 指令就是这样。

*   **stlr** 的全称是 store release register，包括 StoreStore barrier 和 LoadStore barrier（场景少），通常使用 release 语义将寄存器的值写入内存；
*   **ldar** 的全称是 load acquire register，包括 LoadLoad barrier 和 LoadStore barrier（对，你没看错，我没写错），通常使用 acquire 语义从内存中将值加载入寄存器；
*   **release** 语义的内存屏障只不允许其前面的读写向后越过屏障，**挡前不挡后**；
*   **acquire** 语义的内存屏障只不允许其后面的读写向前越过屏障，**挡后不挡前;**
*   StoreLoad barrier 就只能使用 **dmb**（全屏障） 代替了。

![](https://pic3.zhimg.com/v2-43e165683e6a19dafbce79fc229e996e_r.jpg)

理论普及的差不多了，接下单独来说说服务端同学工作中最常用的 x86 内存模型，填一下 4.3 中留下的坑。

**五、x86-TSO**
-------------

x86-TSO（ **Total Store Order**）采用的是图 10 模型。

![](https://pic3.zhimg.com/v2-ae07465c572dacf1f7564b748eecc2ce_r.jpg)

x86-TSO 有下面几个特点：

*   **Store Buffer** 被实现为 **FIFO** 队列，CPU 务必优先读取本地 **Store Buffer** 中的值（如果有的话），否则去缓存或内存里读取；
*   因为 **Store Buffer** 是 **FIFO**，所以写写不会重排，也就不需要 **StoreStore barrier**；
*   **MFENCE** 指令用于清空本地 Store Buffer，并将数据刷到缓存和内存；
*   某 CPU 执行 **lock 前缀**的指令时，会去争抢全局锁，拿到锁后其他线程的读取操作会被阻塞，在释放锁之前，会清空该线程的本地的 **Store Buffer**，这里和 **MFENCE** 执行逻辑类似；
*   **Store Buffer** 被写入变量后，除了被其他线程持有锁以外的情况，在任何时刻均有可能写回内存。
*   因为没有引入 **Invalid Queue**，所以不需要 **LoadLoad barrier**；
*   **LoadStore barrier** 仅在乱序 (**out-of-order**) 处理器上有效，因为等待写指令可以绕过读指令；而 x86-TSO 相对其他平台缓存一致性是最严格的，读操作不会延后，不会使读写重排；
*   那么最后只有 **StoreLoad barrier** 是有效的，其他屏障都是 **no-op**。

下面的代码是 Linux 在 x86 下的内存屏障定义

![](https://pic3.zhimg.com/v2-6989c7d0b3b1136e51a0560940fc6eea_r.jpg)

**六、基准测试**
----------

### **6.1 关于 Store Buffer 的测试**

**6.1.1 测试核心内是否存在 Store Buffer**

![](https://pic4.zhimg.com/v2-62edf73a5735aa06d1230a660a65d78f_r.jpg)

*   **解析**

*   如果 核心 0 和 核心 1 各有自己的 Store Buffer，会造成上述情况；
*   核心 0 将 x = 1 缓存在自己的 Store Buffer 里，同样 核心 1 也将 y = 1 缓存在自己的 Store Buffer 里，核心 0 从共享存储中获取 y = 0；
*   同理，核心 1 从共享存储中获取 x = 0，无法见到 x = 1；
*   现代 Intel CPU 和 AMD x86 中都有 Store Buffer 结构。

*   **解决**

*   这个测试中从其他核心角度看当前核心的读操作提前了，就是因为有 Store Buffer 的存在，导致了从其他核心角度看写操作被延后了；
*   所以需要引入 **StoreLoad barrier** 来防止读操作提前写操作延后；
*   在 x86 中，带 **lock 前缀**的指令 / **XCHG** 指令 / **MFENCE**，会清空 Store Buffer，使得当前核心之前的写操作立马可以被其他核心看见。
*   下面有两种解决办法示意图：

![](https://pic3.zhimg.com/v2-4c85fb68824f24f62b37f8d3385a18c6_r.jpg)

*   **在我的电脑上使用 smp_mb、mb 或 rmb 可以使上述情况不再出现，而使用 barrier 或 wmb 问题还在；**
*   **除此之外，还可**以使用高级语言的**原子变量**来解决。

**6.1.2 测试核心间是否共享 Store Buffer**

![](https://pic2.zhimg.com/v2-90e92e5beed0e70cc30a95c636e45c2d_r.jpg)

*   **解析**

*   如果 核心 0 和 核心 2 共享一个 Store Buffer，核心 1 和 核心 3 共享一个 Store Buffer 会出现上述情况；
*   因为读取时会先去 Store Buffer 读取修改，所以 核心 0 执行的 x = 1 会被 核心 2 读取到，故 EAX = 1 ；
*   因为 核心 1 和 核心 2 不共享 StoreBuffer，核心 1 的 y = 1 操作缓存在自己和 核心 3 的共享 Store Buffer 中，所以 EBX = 0 ；
*   核心 3 的 ECX = 1 和 EDX = 0 与上述同理。

*   **总结**

*   实际上，上述现象不允许在任何 CPU 上观察到，在我的电脑上没有出现；
*   本例子违反了**共享存储一致性**，刷到共享存储的数据一定被所有核心可见，并且是一致的。

**6.1.3 测试** **Store Forwarding （转发）是否生效**

![](https://pic3.zhimg.com/v2-de5da73e71fd1c644942957eaf28bcea_r.jpg)

*   **解析**

*   如果 核心 0 和 核心 1 有各自的 Store Buffer；
*   核心 0 将 x = 1 缓存在自己的 Store Buffer 中，并且根据 Store Forwarding 原则，核心 0 读取 x 到 EAX 的时候会读取自己的 Store Buffer (中 x = 1)，故 EAX = 1；
*   同理，核心 1 也会缓存自己的写操作， 即缓存 y = 2 和 x = 2 到自己的 Store Buffer，因此 y = 2 这个操作不会被核心 0 观察到，核心 0 从共享存储中读到 y = 0 ，故 EBX = 0；

*   **总结**

*   出现上述情况就说明核心存在 Store Buffer，并且有转发功能；
*   在我的电脑（i7）上可以出现上述现象；
*   其实还有一个更直接的测试用例，如下：

![](https://pic4.zhimg.com/v2-d490608387002180ca7a0fffc3f9302f_r.jpg)

### **6.2 测试 CPU 是否乱序执行**

**6.2.1 测试：StoreStore 乱序**

![](https://pic1.zhimg.com/v2-bb34d39c29eca767c1b2c9c781d9b2e0_r.jpg)

*   **解析**

*   在 x86-TSO 上，从 核心 1 的角度看 核心 0，x 和 y 的写入顺序不能颠倒；
*   因为写操作会按照 FIFO 的规则进入 Store Buffer，并且按照 FIFO 的顺序刷入共享存储，所以写操作无法重排序；
*   所以 x = 1 先入 Store Buffer 队列，接着 y = 1 入；
*   接着 x = 1 先刷入缓存和内存，y = 1 后刷入；
*   所以，如果 EAX 读到 1 的话，那么 EBX 一定不是 0。

*   **总结**

*   在 x86 上 Store Buffer 是 **FIFO** 队列，**写操作不允许重排序**，无论是从自己还是其他核心角度看都不会发生重排序；
*   在乱序（**out-of-order**）CPU 上，比如 Arm 上可能发生 StoreStore 重排序，所以需要 **StoreStore barrier** ；

**6.2.2 测试：LoadStore 乱序**

![](https://pic2.zhimg.com/v2-9e312fb4f7eb4e86e0bfcdbea6123c09_r.jpg)

*   **解析**

*   在 x86-TSO 上，如果 EAX = 1，那么说明 x = 1 操作已经从 Store Buffer 中刷入到共享存储，并且优先 EAX = x 执行；
*   由于 x86-TSO 的**读操纵不能延后**，所以 EBX = y 的操作在 x = 1 之前执行；
*   同理，EAX = x 这个读操作也不能延后到 y = 1 之后执行;
*   所以 EBX = y 先于 x = 1 ，x = 1 先于 EAX = x, EAX = x 先于 y = 1 , 所以 EBX 不可能等于 1；

*   **总结**

*   在 x86 上**读操作不能延后，但是可以提前**（9.1.1 中就是读提前了）；
*   在乱序（**out-of-order**）CPU 上，因为**等待写指令**可以绕过**读指令**，比如 Arm 上可能发生 LoadStore 重排序，所以需要 **LoadStore barrier**；

### **6.3 测试 n5 / n4b：两个核心同时修改同一个变量**

**6.3.1 测试：n5**

*   **解析**

*   假如 核心 0 和 核心 1 都有自己的 Store Buffer；
*   如果 EAX = 2，那么说明 核心 1 的 Store Buffer 中 x = 2 已经刷到了共享存储， 那么 x = 2 必然在 x = 1 和 EAX = x 之间执行，因为 EAX 会优先读取 Store Buffer 中的 x ，既然 EAX = 2，说明 核心 0 的 Store Buffer 中的 x = 1 已经刷到了共享存储，并且在 x = 2 之前执行的；
*   EBX 会优先读取 核心 1 中的 Store Buffer ，所以 EBX 不可能等于 1 ；

*   **总结**

*   n5 实际上不应该在任何 CPU 上观察到。

**6.3.2 测试：n4b**

*   **解析**

*   假如 核心 0 和 核心 1 都有自己的 Store Buffer；
*   如果 EAX = 2 ，说明 核心 1 的 x = 2 操作已经刷到共享存储，并被 核心 0 观察到，所以 x = 2 先于 EAX = x 执行；
*   在 x86 上读操作不会延后，即 EX = x 和 x = 2 不会重排，故 EBX = x 先于 EAX = x 执行，更先于 x = 1 执行，所以 EBX 不可能等于 1；

*   **总结**

*   n4b 实际上不应该在任何 CPU 上观察到。

### **6.4 测试：写操作的可见性是否传递（如果 A 能看到 B 的动作，B 能看到 C 的动作，那么 A 是否能看到 C 的动作）**

![](https://pic1.zhimg.com/v2-5cad5c724c236a03580fd5a40bf20d70_r.jpg)

*   **解析**

*   在 x86-TSO 上，对于 核心 1，如果 EAX = 1 ，那么说明 核心 1 已经见到了 核心 0 的动作；
*   对于 核心 2，EBX = 1，说明 核心 2 已经见到了 核心 1 的动作，又根据之前的 x86-TSO 上**读操作不能延后**，EAX = x 不能延迟到 y = 1 之后，所以 核心 2 必能见到 核心 0 的动作，所以 ECX = x 不能为 0。

*   **总结**

*   在 x86-TSO 上**写操作的可见性是传递的**；
*   在乱序（**out-of-order**）CPU 上，写写和读写都是乱序，就不可能保证写的传递性了；

**七、CAS 原理**
------------

**比较并交换 (compare and swap, CAS)**，是原子操作的一种，可用于在多线程编程中实现不被打断的数据交换操作，从而避免多线程同时改写某一数据时由于执行顺序不确定性以及中断的不可预知性产生的数据不一致问题。该操作通过将内存中的值与指定数据进行比较，当数值一样时将内存中的数据替换为新的值。

下面代码是使用 CAS 的一个例子（无锁队列 Pop 函数）

```
template <typename T>
bool AtomQueue<T>::Pop(T& v)
{
    uint64_t tail = tail_;
    if (tail == head_ || !valid_[tail])
        return false;
    if (!__sync_bool_compare_and_swap(&tail_, tail, (tail + 1) & mod_)) 
        return false;
    v = std::move(data_[tail]);
    valid_[tail] = 0;
    return true;
}
```

在使用上，通常会记录下某块内存中的**_旧值_**，通过对**_旧值_** 进行一系列的操作后得到**_新值_**，然后通过 **CAS** 操作将**_新值_** 与**_旧值_** 进行交换。

如果这块内存的值在这期间内没被修改过，则**_旧值_** 会与内存中的数据相同，这时 **CAS** 操作将会成功执行，使内存中的数据变为**_新值_**。

如果内存中的值在这期间内被修改过，则一般来说**_旧值_** 会与内存中的数据不同，这时 **CAS** 操作将会失败，**_新值_** 将不会被写入内存。

### **7.1 应用**

在应用中 **CAS** 可以用于实现**无锁数据结构**，常见的有**无锁队列**（先入先出）以及**无锁栈**（先入后出）。对于可在任意位置插入数据的**链表以及双向链表**，实现无锁操作的**难度较大**。

### **7.2 ABA 问题**

ABA 问题是无锁结构实现中常见的一种问题，可基本表述为：

1.  线程 P1 读取了一个数值 A；
2.  P1 被挂起 (时间片耗尽、中断等)，线程 P2 开始执行；
3.  P2 修改数值 A 为数值 B，然后又修改回 A；
4.  P1 被唤醒，比较后发现数值 A 没有变化，程序继续执行。

对于 P1 来说，数值 A 未发生过改变，但实际上 A 已经被变化过了，继续使用可能会出现问题。在 CAS 操作中，由于比较的多是指针，这个问题将会变得更加严重。试想如下情况：

![](https://pic4.zhimg.com/v2-b53d8668af9a8aca18d191e07cc501af_r.jpg)

有一个栈 (先入后出) 中有 top 和 NodeA，NodeA 目前位于栈顶，top 指针指向 A。现在有一个线程 P1 想要 pop 一个节点，因此按照如下无锁操作进行

```
pop()
{
    do{
        ptr = top;            // ptr = top = NodeA
        next_ptr = top->next; // next_ptr = NodeX
    } while(CAS(top, ptr, next_ptr) != true);

    return ptr;   
}
```

而线程 P2 在 P1 执行 CAS 操作之前把它打断了，并对栈进行了一系列的 pop 和 push 操作，使栈变为如下结构：

![](https://pic2.zhimg.com/v2-8fbd49f7d73ddfe31d8c53acc592844d_r.jpg)

线程 P2 首先 pop 出 NodeA，之后又 push 了两个 NodeB 和 C，由于内存管理机制中广泛使用的**内存重用机制**，导致 NodeC 的地址与之前的 NodeA 一致。

这时 P1 又开始继续运行，在执行 CAS 操作时，由于 top 依旧指向的是 NodeA 的地址 (实际上已经变为 NodeC)，因此将 top 的值修改为了 NodeX，这时栈结构如下：

![](https://pic3.zhimg.com/v2-7b1bd831f7a3f18f407fa6e84a1a2272_r.jpg)

经过 CAS 操作后，top 指针错误地指向了 NodeX 而不是 NodeB。

简单的解决办法是采用 **DCAS**（双长度 CAS），一个 CAS 长度 保存原始有效数据，另一个 CAS 长度 保存累计变化的次数，第一个 CAS 可能出现 ABA 问题，但是第二个 CAS 极难出现 ABA 问题。

### **7.3 实现**

CAS 操作基于 CPU 提供的原子操作指令实现。对于 Intel X86 处理器，可通过在汇编指令前增加 lock 前缀来锁定系统总线，使系统总线在汇编指令执行时无法访问相应的内存地址。而各个编译器根据这个特点实现了各自的原子操作函数。

*   C 语言，C11 的头文件 <stdatomic.h>。由 GNU 提供了对应的__sync 系列函数完成原子操作。
*   C++11，STL 提供了 atomic 系列函数。
*   JAVA，sun.misc.Unsafe 提供了 compareAndSwap 系列函数。
*   C#，通过 Interlocked 方法实现。
*   Go，通过 import "sync/atomic" 包实现。
*   Windows，通过 Windows API 实现了 InterlockedCompareExchangeXYZ 系列函数。

**八、原子操作**
----------

程序代码最终都会被翻译为 CPU 指令，一条最简单的加减法语句都会被翻译成几条指令执行；为了避免语句在 CPU 这一层级上的指令交叉带来的不可预知行为，在多线程程序设计时必须通过一些方式来进行规范，最常见的做法就是引入**互斥锁**，但互斥锁是操作系统这一层级的，最终映射到 CPU 上也是一堆指令，是指令就必然会带来额外的开销。

既然 CPU 指令是多线程不可再分的最小单元，那我们如果有办法将代码语句和指令对应起来，不就不需要引入互斥锁从而提高性能了吗? 而这个对应关系就是所谓的原子操作；在 C++11 的 atomic 中有两种做法:

*   **常用类型**，长度等于 1、2、4 和 8 字节的整形数据，有相应的 CPU 层级的对应，这就是一个标准的 lock-free 类型；
*   **大数据类型**，结构体等非常用类型数据，采用**互斥锁模拟**，比如说对于一个 atomic<T> 类型，我们可以给他附带一个 mutex，操作时 lock / unlock 一下，这种在多线程下进行访问，必然会导致线程阻塞；

可以通过 is_lock_free 函数，判断一个 atomic 是否是 lock-free 类型。

原子操作有三类：

*   **读**：在读取的过程中，读取位置的内容不会发生任何变动。
*   **写**：在写入的过程中，其他执行线程不会看到部分写入的结果。
*   **读‐修改‐写**：读取内存、修改数值、然后写回内存，整个操作的过程中间不会有其他写入操作插入，其他执行线程不会看到部分写入的结果。

### **8.1 自旋锁**

使用**原子操作模拟互斥锁**的行为就是自旋锁，互斥锁状态是由操作系统控制的，自旋锁的状态是程序员自己控制的，常用的自旋锁模型有：

*   **TAS**，Test-and-set，有且只有 atomic_flag 类型与之对应；
*   **CAS**，Compare-and-swap，对应 atomic 的 compare_exchange_strong 和 compare_exchange_weak，这两个版本的区别是：

*   weak 版本如果数据符合条件被修改，其也可能返回 false，就好像不符合修改状态一致；
*   strong 版本不会有这个问题，但在某些平台上 strong 版本比 Weak 版本慢（在 x86 平台他们之间没有任何性能差距）；绝大多数情况下，优先选择使用 strong 版本；

LOCK 时自旋锁是自己轮询状态，如果不引入中断机制，会有大量计算资源浪费到轮询本身上；常用的做法是使用 yield 切换到其他线程执行，或直接使用 sleep 暂停当前线程.

### **8.2 C++ 内存模型**

C++11 原子操作的很多函数都有个 std::memory_order 参数，这个参数就是这里所说的内存模型，对应缓存一致性模型，其作用是对同一时间的读写操作进行排序，一共定义了 6 种类型如下：

*   **memory_order_relaxed**：松散内存序，只用来保证对原子对象的操作是原子的，在不需要保证顺序时使用；
*   **memory_order_release**：**释放操作**，在写入某原子对象时，当前线程的任何前面的读写操作都不允许重排到这个操作的后面去，并且当前线程的所有内存写入都在对**同一个原子**对象进行获取的其他线程可见；通常与 memory_order_acquire 或 memory_order_consume 配对使用；
*   **memory_order_acquire**：**获得操作**，在读取某原子对象时，当前线程的任何后面的读写操作都不允许重排到这个操作的前面去，并且其他线程在对**同一个原子**对象释放之前的所有内存写入都在当前线程可见；
*   **memory_order_consume**：同 memory_order_acquire 类似，区别是它仅对依赖于该原子变量操作涉及的对象，比如这个操作发生在原子变量 a 上，而 s = a + b；那 s 依赖于 a，但 b 不依赖于 a；当然这里也有循环依赖的问题，例如：t = s + 1，因为 s 依赖于 a，那 t 其实也是依赖于 a 的；在大多数平台上，这只会影响编译器的优化；**不建议使用**；
*   **memory_order_acq_rel**：**获得释放操作**，一个**读‐修改‐写**操作同时具有_获得_语义和_释放_语义，即它前后的任何读写操作都不允许重排，并且其他线程在对同一个原子对象释放之前的所有内存写入都在当前线程可见，当前线程的所有内存写入都在对同一个原子对象进行获取的其他线程可见；  
    
*   **memory_order_seq_cst**：**顺序一致性语义**，对于读操作相当于_获得_，对于写操作相当于_释放_，对于读‐修改‐写操作相当于_获得释放_，是所有原子操作的**默认内存序**，并且会对所有使用此模型的原子操作建立一个**全局顺序**，保证了**多个原子变量**的操作在所有线程里观察到的操作顺序相同，当然它是最慢的同步模型。

在不同的 CPU 架构上，这些模型的具体实现方式可能不同，但是 C++11 帮你屏蔽了内部细节，不用考虑内存屏障，只要符合上面的使用规则，就能得到想要的效果。可能有时使用的模型粒度比较大，会损耗性能，当然还是使用各平台底层的内存屏障粒度更准确，效率也会更高，对程序员的功底要求也高。

### **8.3 C++ volatile**

这个关键字仅仅保证**数据只在内存中读写**，直接操作它既**不能保证操作是原子的**，也不能通用地达到内存同步的效果；

由于 volatile 不能在多处理器的环境下确保多个线程能看到同样顺序的数据变化，在今天的通用应用程序中，**不应该再看到 volatile 的出现**。

**九、无锁队列**
----------

本节是 CPU 缓存一致性的实战部分，通过运用前面的理论知识实现一个无锁队列，达到学以致用的目的。

下面是我采用 **CAS** 实现了一个**多生产者多消费者无锁队列**，设计参考 **Disruptor** ，最高可达 **660 万** QPS（单生产者单消费者）和 **160 万** QPS（10 个生产者 10 个消费者）。

### **9.1 设计思路**

1、如图 15，使用 2 个**环形数组**，数组元素均非原子变量，一个存储 **T** 范型数据（一般为指针），另一个是可用性检查数组（**uint8_t**）。**Head** 是所有**生产者**的竞争标记，**Tail** 是所有**消费者**的竞争标记。**红色区**表示**待生产位置**，**绿色区**表示**待消费位置**。

![](https://pic1.zhimg.com/v2-59759b7fe64e2828700e3e0543775d18_r.jpg)

2、生产者们通过 **CAS** 来竞争和移动 **Head**，抢到 **Head** 的生产者，先将 **Head** 加 1，再生产原 **Head** 位置的数据；同样的消费者们通过 **CAS** 来竞争和移动 **Tail，**抢到 **Tail** 的消费者，先将 **Tail** 加 1，再消费原 **Tail** 位置的数据 。

### **9.2 实现细节**

下面多生产者多消费者无锁队列的代码是在 x86-64（x86-TSO） 平台上编写和测试的。

> Talk is cheap. Show me the code.

**9.2.1 AtomQueue 类模板定义**

```
template <typename T>
class AtomQueue
{
public:
    AtomQueue(uint64_t size);
    ~AtomQueue();
    bool Push(const T& v);
    bool Pop(T& v);
    
private:
    uint64_t    P0[8];  //频繁变化数据, 避免伪共享, 采用Padding
    uint64_t    head_;  //生产者标记, 表示生产到这个位置，但还没有生产该位置
    uint64_t    P1[8];
    uint64_t    tail_;  //消费者标记, 表示消费到这个位置，但还没有消费该位置
    uint64_t    P2[8];
    uint64_t    size_;  //数组最大容量, 必须满足2^N
    int         mod_;   //取模 % -> & 减少2ns
    T*          data_;  //环形数据数组
    uint8_t*    valid_; //环形可用数组，与数据数组大小一致
};
```

细心的你会看到 **head_** 和 **tail_** 还有后面的变量中加添加了无意义的字段 **P0**、**P1** 和 **P2** ，因为 **head_** 和 **tail_** 频繁变化，目的是防止出现前面讲过的**伪共享**导致性能下降问题。

**9.2.2 构造函数与析构函数**

```
template <typename T> 
AtomQueue<T>::AtomQueue(uint64_t size) : size_(size << 1), head_(0), tail_(0) 
{
    if ((size_ & (size_ - 1))) 
    {
        printf("AtomQueue::size_ must be 2^N !!!\n");
        exit(0);
    }
    mod_    = size_ - 1;
    data_   = new T[size_];
    valid_  = new uint8_t[size_];
    std::memset(valid_, 0, sizeof(valid_));
    for (int i = 0; i < size_; i++) 
        data_available_[i] = 0;
}

template <typename T>
AtomQueue<T>::~AtomQueue()
{
    delete[] data_;
    delete[] valid_; 
}
```

构造函数中强制传入的队列大小（size）必须为 2 的幂数，目的是想用 & 而不是 % 取模，因为 & 比 % 快 2ns，最求极致性能。

**9.2.3 生产者调用的 Push 函数 和 消费者调用的 Pop 函数**

```
template <typename T>
bool AtomQueue<T>::Push(const T& v)
{
    uint64_t head = head_, tail = tail_;
    if (tail <= head ? tail + size_ <= head + 1 : tail <= head + 1)
        return false;
    if (valid_[head])
        return false;
    if (!__sync_bool_compare_and_swap(&head_, head, (head + 1) & mod_))
        return false;
    data_[head] = v;
    valid_[head] = 1;
    return true;
}

template <typename T>
bool AtomQueue<T>::Pop(T& v)
{
    uint64_t tail = tail_;
    if (tail == head_ || !valid_[tail])
        return false;
    if (!__sync_bool_compare_and_swap(&tail_, tail, (tail + 1) & mod_)) 
        return false;
    v = std::move(data_[tail]);
    valid_[tail] = 0;
    return true;
}
```

分析一下上述 Push 和 Pop 函数中读写操作是否需要增加内存屏障，读写操作可以抽象描述如下表格：

![](https://pic4.zhimg.com/v2-3346207a3ad365717115891736c97ec3_r.jpg)

在读写操作乱序的 CPU 上可以出现上述情况，会导出线 Bug，解释一下：

*   当刚初始化的队列，队列还是空的，这时核心 0 执行 Push 函数，同时核心 1 执行 Pop 函数；
*   Push 里的条件**（tail <= head ? tail + size_ <= head + 1 : tail <= head + 1）**为 **true**，表示队列已经满了，所以生产失败，其实队列还是空的；
*   Pop 里的条件**（tail == head_ || !valid_[tail]）**为 **false**，表示队列有数据，并且消费 **tail** 位置数据，实际上 **tail** 位置还没数据；
*   导致生产和消费都发生了错误。

解决办法是添加**读写屏障**（**LoadStore barrier**），如下表格：

![](https://pic3.zhimg.com/v2-5b4e8d96dfb6b7670e7d2cfe7fe40542_r.jpg)

在 **Arm** 等乱序执行的平台上可以解决问题；幸好 x86-TSO 平台上读操作不能延后，也就不需要读写屏障，手动加了也是空操作（**no-op**）。

通过执行反汇编命令（**objdump -S a.out**）得到 Push 中下面代码的汇编代码。

```
if (!__sync_bool_compare_and_swap(&tail_, tail, (tail + 1) & mod_)) 
400a61:  48 8b 45 f8            mov    -0x8(%rbp),%rax
400a65:  48 8d 50 01            lea    0x1(%rax),%rdx
400a69:  48 8b 45 e8            mov    -0x18(%rbp),%rax
400a6d:  8b 80 d8 00 00 00      mov    0xd8(%rax),%eax
400a73:  48 98                  cltq   
400a75:  48 89 d1               mov    %rdx,%rcx
400a78:  48 21 c1               and    %rax,%rcx
400a7b:  48 8b 45 e8            mov    -0x18(%rbp),%rax
400a7f:  48 8d 90 88 00 00 00   lea    0x88(%rax),%rdx
400a86:  48 8b 45 f8            mov    -0x8(%rbp),%rax
400a8a:  f0 48 0f b1 0a         lock cmpxchg %rcx,(%rdx)
400a8f:  0f 94 c0               sete   %al
400a92:  83 f0 01               xor    $0x1,%eax
400a95:  84 c0                  test   %al,%al
400a97:  74 07                  je     400aa0 <_ZN9AtomQueueIiE3PopERi+0x8c>

return false;
400a99:  b8 00 00 00 00         mov    $0x0,%eax
400a9e:  eb 40                  jmp    400ae0 <_ZN9AtomQueueIiE3PopERi+0xcc>
```

发现 __sync_bool_compare_and_swap 函数对应的汇编代码为：

```
400a8a:  f0 48 0f b1 0a         lock cmpxchg %rcx,(%rdx)
```

是带 **lock** 前缀的命令，前面讲过，在 x86-TSO 上，带有 **lock** 前缀的命令具有刷新 Store Buffer 的功能，也就是 **head_** 和 **tail_** 的修改都能及时被其他核心观察到，可以做到及时生产和消费。

**十、参考资料**
----------

*   Alder Lake - 维基百科，自由的百科全书
*   CPU Cache：访存速度是如何大幅提升的？
*   MESI 协议 - 维基百科，自由的百科全书
*   MESI 协议：多核 CPU 是如何同步高速缓存的？
*   内存模型：有了 MESI 为什么还需要内存屏障？
*   [https://www.scss.tcd.ie/Jeremy.Jones/VivioJS/caches/MESIHelp.htm](https://www.scss.tcd.ie/Jeremy.Jones/VivioJS/caches/MESIHelp.htm)
*   MESIF 协议 - 维基百科，自由的百科全书
*   MOESI 协议 - 维基百科，自由的百科全书
*   为什么在 x86 架构下只有 StoreLoad 屏障是有效指令？
*   The JSR-133 Cookbook for Compiler Writers
*   The JSR-133 Cookbook for Compiler Writers[译]
*   x86-TSO: A Rigorous and Usable Programmer’s Model for x86 Multiprocessors
*   从 Java 内存模型看内部细节
*   比较并交换 - 维基百科，自由的百科全书
*   [https://en.wikipedia.org/wiki/Compare-and-swap](https://en.wikipedia.org/wiki/Compare-and-swap)
*   C++11 原子操作与无锁编程
*   内存模型和 atomic：理解并发的复杂性
*   x86-TSO : 适用于 x86 体系架构并发编程的内存模型

**十一、结束语**
----------

OMG，竟然写了这么多，头一次！终于把 CPU 缓存、内存屏障、原子操作以及无锁队列一口气梳理完了。期间查阅大量资料，这里特地感谢一下参考资料中的作者，让我学到了很多知识；期间也写了很多测试代码来验证理论，避免误人子弟，尽量做到有理有据。由于作者水平有限，本文错漏缺点在所难免，希望读者批评指正。

最后，欢迎点赞和关注微信公众号：科英