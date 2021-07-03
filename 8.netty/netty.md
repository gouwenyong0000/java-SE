# Netty介绍和应用场景

要求已经掌握了Java编程，主要技术构成：Java OOP编程、Java多线程编程、JavaIO编程、Java网络编程、常用的Java设计模式（比如观察者模式，命令模式，职责链模式）、常用的数据结构（比如链表）



## 1.2 Netty的介绍

1）Netty是由JBOSS提供的一个Java开源框架，现为Github上的独立项目。
2）Netty是一个异步的、基于事件驱动的网络应用框架，用以快速开发高性能、高可靠性的网络IO程序。



![image-20210703164010783](netty.assets/image-20210703164010783.png)

3）Netty主要针对在TCP协议下，面向Clients端的高并发应用，或者Peer-to-Peer场景下的大量数据持续传输的应用。

4）Netty本质是一个NIO框架，适用于服务器通讯相关的多种应用场景

![image-20210703164046708](netty.assets/image-20210703164046708.png)

5）要透彻理解Netty，需要先学习NIO，这样我们才能阅读Netty的源码。

## 1.3 Netty的应用场景

### 1.3.1互联网行业

1）互联网行业：在分布式系统中，各个节点之间需要远程服务调用，高性能的RPC框架必不可少，Netty作为异步高性能的通信框架，往往作为基础通信组件被这些RPC框架使用。
2）典型的应用有：阿里分布式服务框架Dubbo的RPC框架使用Dubbo协议进行节点间通信，Dubbo协议默认使用Netty作为基础通信组件，用于实现各进程节点之间的内部通信

### 1.3.2游戏行业

1）无论是手游服务端还是大型的网络游戏，Java语言得到了越来越广泛的应用

2）Netty作为高性能的基础通信组件，提供 TCPUDP和HTTP协议栈，方便定制和开发私有协议栈，账号登录服务器
3）地图服务器之间可以方便的通过Netty进行高性能的通信

### 1.3.3大数据领域

1）经典的Hadoop的高性能通信和序列化组件Avro的RPC框架，默认采用Netty进行跨界点通信

2）它的Netty Service基于Netty框架二次封装实现。

![image-20210703164324217](netty.assets/image-20210703164324217.png)

## 1.3.4其它开源项目使用到Netty

https://netty.io/wiki/related-projects.html

## 1.4Netty的学习参考资料

![image-20210703164434121](netty.assets/image-20210703164434121.png)

# BIO编程

## I/O模型

### I/O 模型基本说明

1) I/O 模型简单的理解：就是用什么样的通道进行数据的发送和接收，很大程度上决定了程
序通信的性能
2) Java共支持3种网络编程模型/IO模式：BIO、NIO、AIO
3) Java `BIO` ： 同步并阻塞(**传统阻塞型**)，服务器实现模式为一个连接一个线程，即客户端
有连接请求时服务器端就需要启动一个线程进行处理，如果这个连接不做任何事情会造成
不必要的线程开销 【简单示意图】

![image-20210703164839412](netty.assets/image-20210703164839412.png)

4) Java `NIO` ： **同步非阻塞**，服务器实现模式为一个线程处理多个请求(连接)，即客户端发
送的连接请求都会注册到多路复用器上，多路复用器轮询到连接有I/O请求就进行处理
【简单示意图】

![image-20210703170633989](netty.assets/image-20210703170633989.png)

5) Java `AIO`(NIO.2) ： **异步非阻塞**，AIO 引入异步通道的概念，采用了 Proactor 模式，简
化了程序编写，对于有效的请求才启动线程，它的特点是先由操作系统完成后才通知服务端程序启动线程去处理，一般适用于连接数较多且连接时间较长的应用

## BIO、NIO、AIO适用场景分析

1) BIO方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，
并发局限于应用中，JDK1.4以前的唯一选择，但程序简单易理解。

2) NIO方式适用于**连接数目多且连接比较短**（轻操作）的架构，比如聊天服务器，弹幕
系统，服务器间通讯等。编程比较复杂，JDK1.4开始支持。

3) AIO方式使用于**连接数目多且连接比较长**（重操作）的架构，比如相册服务器，充分
调用OS参与并发操作，编程比较复杂，JDK7开始支持。【目前没有得到广泛应用】

## Java BIO 基本介绍

1) Java BIO 就是**传统的java io 编程**，其相关的类和接口在 `java.io`

2) `BIO(blocking I/O)` ： **同步阻塞**，服务器实现模式为一个连接一个线程，即客户端有连
接请求时服务器端就需要启动一个线程进行处理，如果这个连接不做任何事情会造
成不必要的线程开销，可以通过**线程池机制改善**(实现多个客户连接服务器)。 

3) BIO方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，
并发局限于应用中，JDK1.4以前的唯一选择，程序简单易理解

### Java BIO 工作机制

![image-20210703171002549](netty.assets/image-20210703171002549.png)



###  Java BIO应用实例

实例说明：
1）使用BIO模型编写一个服务器端，监听6666端口，当有客户端连接时，就启动一个线程与之通讯。

2）要求使用线程池机制改善，可以连接多个客户端.

3）服务器端可以接收客户端发送的数据（telnet方式即可）

4）代码演示

```java
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {

//线程池机制
//思路
//1.创建一个线程池
//2．如果有客户端连接，就创建一个线程，与之通讯（单独写一个方法）

    public static void main(String[] args) throws IOException {
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(6666);
        System.out.println("服务器启动...");
        while (true) {
            System.out.println("线程信息" + Thread.currentThread().getName() + "\t" + Thread.currentThread().getId());
            //监听等待客户端连接
            System.out.println("等待连接...");//检测accept是阻塞的
            Socket socket = server.accept();
            System.out.println("连接到一个客户端...");
            //创建一个线程，与之通讯
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    handle(socket);
                }
            });

        }
    }

    //编写一个handler方法，和客户端通讯
    public static void handle(Socket socket) {
        System.out.println("线程信息" + Thread.currentThread().getName() + "\t" + Thread.currentThread().getId());
        byte[] bytes = new byte[1024];
        //通过socket获取输入流，
        try {
            InputStream inputStream = socket.getInputStream();
            int len = 0;
            //循环的读取客户端发送的数据
            while (true) {
                System.out.println("read...");//检测read是阻塞的
                len = inputStream.read(bytes);
                if (len!= -1) {
                    String s = new String(bytes, 0, len);
                    System.out.println(s);
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("关闭client连接");
        }

    }
}

```

使用`telnet 127.0.0.1 6666`模拟客户端进行测试



### Java BIO 问题分析

1) 每个请求都需要创建独立的线程，与对应的客户端进行数据Read，业务处理，数据 Write 。
2) 当并发数较大时，需要**创建大量线程来处理连接**，系统资源占用较大。
3) 连接建立后，如果当前线程暂时没有数据可读，则线程就阻塞在 Read 操作上，造成线程资源浪费

# Java NIO编程

## Java NIO 基本介绍

1) Java NIO 全称 **java non-blocking IO**，是指 JDK 提供的新API。从 JDK1.4 开始，Java 提供了一系列改进的输入/输出的新特性，被统称为 NIO(即 New IO)，是**同步非阻塞**的

2) NIO 相关类都被放在 `java.nio` 包及子包下，并且对原 java.io包中的很多类进行改写。

3) NIO 有三大核心部分：`Channel(通道)，Buffer(缓冲区),Selector(选择器)`

4) NIO是 面向**缓冲区** **，或者面向 块** 编程的。数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动，这就增加了处理过程中的灵活性，使用它可以提供非阻塞式的高伸缩性网络

5) Java NIO的非阻塞模式，使一个线程从某通道发送请求或者读取数据，但是它仅能得到目前可用的数据，如果目前没有数据可用时，就什么都不会获取，**而不是保持线程阻塞**，所以直至数据变的可以读取之前，该线程可以继续做其他的事情。 非阻塞写也是如此，一个线程请求写入一些数据到某通道，但不需要等待它完全写入，这个线程同时可以去做别的事情。

6) 通俗理解：NIO是可以做到用一个线程来处理多个操作的。假设有10000个请求过来,根据实际情况，可以分配50或者100个线程来处理。不像之前的阻塞IO那样，非得分配10000个。

7) HTTP2.0使用了多路复用的技术，做到同一个连接并发处理多个请求，而且并发请求的数量比HTTP1.1大了好几个数量级。



8) 案例说明NIO的Buffer

```java
import java.nio.IntBuffer;

public class BasicBuffer {

    public static void main(String[] args) {
        //举例说明Buffer的使用（简单说明）
        //创建一个Buffer，大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //循环向buffer存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(10+i);
        }

        //如何从buffer读取数据
        intBuffer.flip(); //将buffer转换，读写切换

        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }

    }
}
```

## NIO 和 BIO 的比较

1) BIO 以流的方式处理数据,而 NIO 以块的方式处理数据,块 I/O 的效率比流 I/O 高很
多

2) BIO 是阻塞的，NIO 则是非阻塞的

3) BIO基于字节流和字符流进行操作，而 NIO 基于 Channel(通道)和 Buffer(缓冲区)进
行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。
Selector(选择器)用于监听多个通道的事件（比如：连接请求，数据到达等），因
此使用**单个线程就可以监听多个客户端通**道

## NIO 三大核心原理示意图

![image-20210703194038009](netty.assets/image-20210703194038009.png)



Selector 、 Channel 和 Buffer 的关系图(简单版)关系图的说明:
1) 每个channel 都会对应一个Buffer

2) Selector 对应一个线程， 一个线程对应多个channel(连接)

3) 该图反应了有三个channel 注册到 该selector //程序

4) 程序切换到哪个channel ，是由事件决定的, Event 就是一个重要的概念

5) Selector 会根据不同的事件，在各个通道上切换

6) Buffer 就是一个内存块 ， 底层是有一个数组

7) 数据的读取写入是通过Buffer, 这个和BIO , BIO 中要么是输入流，或者是输出流, 不能双向，但是NIO的Buffer 是可以读也可以写, 需要 flip() 方法切换

8) channel 是双向的, 可以返回底层操作系统的情况, 比如Linux ， 底层的操作系统通道就是双向的.

## 缓冲区(Buffer)

### 基本介绍

缓冲区（Buffer）：缓冲区本质上是一个可以读写数据的内存块，可以理解成是一个
**容器对象(含数组)**，该对象提供了**一组方法**，可以更轻松地使用内存块，，缓冲区对
象内置了一些机制，能够跟踪和记录缓冲区的状态变化情况。Channel 提供从文件、
网络读取数据的渠道，但是读取或写入的数据都必须经由 Buffer，如图: 

![image-20210703202142050](netty.assets/image-20210703202142050.png)

### Buffer 类及其子类

1) 在 NIO 中，Buffer 是一个顶层父类，它是一个抽象类, 类的层级关系图:

![image-20210703202322176](netty.assets/image-20210703202322176.png)

2) Buffer类定义了所有的缓冲区都具有的四个属性来提供关于其所包含的数据元素的信息:

![image-20210703202750149](netty.assets/image-20210703202750149.png)



3) Buffer类相关方法一览

```java
public abstract class Buffer {
//JDK1.4时，引入的api
public final int capacity( )//返回此缓冲区的容量**
public final int position( )//返回此缓冲区的位置**
public final Buffer position (int newPositio)//设置此缓冲区的位置**
public final int limit( )//返回此缓冲区的限制**
public final Buffer limit (int newLimit)//设置此缓冲区的限制**
public final Buffer mark( )//在此缓冲区的位置设置标记
public final Buffer reset( )//将此缓冲区的位置重置为以前标记的位置
public final Buffer clear( )//清除此缓冲区, 即将各个标记恢复到初始状态，但是数据并没真正擦除**
public final Buffer flip( )//反转此缓冲区**
public final Buffer rewind( )//重绕此缓冲区
public final int remaining( )//返回当前位置与限制之间的元素数
public final boolean hasRemaining( )//告知在当前位置和限制之间是否有元素**
public abstract boolean isReadOnly( );//告知此缓冲区是否为只读缓冲区**
    
//JDK1.6时引入的api
public abstract boolean hasArray();//告知此缓冲区是否具有可访问的底层实现数组**
public abstract Object array();//返回此缓冲区的底层实现数组**
public abstract int arrayOffset();//返回此缓冲区的底层实现数组中第一个缓冲区元素的偏移量
public abstract boolean isDirect();//告知此缓冲区是否为直接缓冲区
}
```

### ByteBuffer

从前面可以看出对于 Java 中的基本数据类型(boolean除外)，都有一个 Buffer 类型与之
相对应，**最常用**的自然是ByteBuffer 类（二进制数据），该类的主要方法如下：

```java
public abstract class ByteBuffer {
//缓冲区创建相关api
public static ByteBuffer allocateDirect(int capacity)//创建直接缓冲区**
public static ByteBuffer allocate(int capacity)//设置缓冲区的初始容量**
public static ByteBuffer wrap(byte[] array)//把一个数组放到缓冲区中使用
//构造初始化位置offset和上界length的缓冲区
public static ByteBuffer wrap(byte[] array,int offset, int length)
//缓存区存取相关API
public abstract byte get( );//从当前位置position上get，get之后，position会自动+1 **
public abstract byte get (int index);//从绝对位置get **
public abstract ByteBuffer put (byte b);//从当前位置上添加，put之后，position会自动+1 **
public abstract ByteBuffer put (int index, byte b);//从绝对位置上put **
}
```

## 通道(Channel)

### 基本介绍

1) NIO的通道类似于流，但有些区别如下：
	• 通道可以同时进行读写，而流只能读或者只能写
	• 通道可以实现异步读写数据
	• 通道可以从缓冲读数据，也可以写数据到缓冲:

![image-20210703204354045](netty.assets/image-20210703204354045.png)

2) BIO 中的 stream 是单向的，例如 FileInputStream 对象只能进行读取数据的操作，而 NIO 中的通道(Channel)是双向的，可以读操作，也可以写操作。

3) Channel在NIO中是一个接口`public interface Channel extends Closeable{}`

4) 常用的 Channel 类有：FileChannel、DatagramChannel、ServerSocketChannel 和SocketChannel。【ServerSocketChanne 类似ServerSocket , SocketChannel 类似 Socket】

5) FileChannel 用于文件的数据读写，DatagramChannel 用于 UDP 的数据读写，ServerSocketChannel 和 SocketChannel 用于 TCP的数据读写。



### FileChannel 类

FileChannel主要用来对本地文件进行 IO 操作，常见的方法有
1) public int read(ByteBuffer dst) ，从通道读取数据并放到缓冲区中
2) public int write(ByteBuffer src) ，把缓冲区的数据写到通道中
3) public long transferFrom(ReadableByteChannel src, long position, long count)，从目标通道中复制数据到当前通道
4) public long transferTo(long position, long count, WritableByteChannel target)，把数据从当
前通道复制给目标通道



### 应用实例1-本地文件写数据

实例要求:
1) 使用前面学习后的ByteBuffer(缓冲) 和 FileChannel(通道)， 将 "hello,尚硅谷" 写入
到file01.txt 中
2) 文件不存在就创建

```java
public class NIOFileChannel01 {

    public static void main(String[] args) throws IOException {
        String str ="hello,尚硅谷";

        //创建一个输出流 -> channel
        FileOutputStream outputStream = new FileOutputStream("d:\\file01.txt");

        //通过fileoutputstream获取对应的Filechannel
        //这个filechannel真实类型是FilechannelImpl
        FileChannel fileChannel = outputStream.getChannel();

        //创建一个缓冲区ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将str放入byteBuffer
        byteBuffer.put(str.getBytes(StandardCharsets.UTF_8));

        //对byteBuffer进行flip,设置position=0开始读
        byteBuffer.flip();

        //将byteBuffer数据写入到filechannel
        fileChannel.write(byteBuffer);

        outputStream.close();


    }
}
```

### 应用实例2-本地文件读数据

实例要求:
1) 使用前面学习后的ByteBuffer(缓冲) 和 FileChannel(通道)， 将 file01.txt 中的数据读
入到程序，并显示在控制台屏幕
2) 假定文件已经存在

```java
public class NIOFileChannel01 {

    public static void main(String[] args) throws IOException {

        //创建一个输入流 -> channel
        File file = new File("d:\\file01.txt");
        FileInputStream inputStream = new FileInputStream(file);

        //通过fileinputstream获取对应的Filechannel->实际类型FilechannelImpl
        FileChannel channel = inputStream.getChannel();

        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());

        //将通道的数据读入到Buffer   返回-1表示读取完毕
        channel.read(buffer);

        //将byteBuffer的字节数据转成string    array()返回内部的数组
        System.out.println(new String(buffer.array()));

        inputStream.close();
    }
}
```

### 应用实例3-使用一个Buffer完成文件读取写入

实例要求:
1) 使用 FileChannel(通道) 和 方法 read , write，完成文件的拷贝
2) 拷贝一个文本文件 1.txt , 放在项目下即可

![image-20210704014024950](netty.assets/image-20210704014024950.png)

```java
public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {

        File file = new File("1.txt");

        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();


        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        while (true){

            //这里有一个重要的操作，一定不要忘了
            /* 重置标志位，清空buffer，重置position位置，之前写出是position等于limit
                public final Buffer clear() {
                    position = 0;
                    limit = capacity;
                    mark = -1;
                    return this;
                }
             */
            byteBuffer.clear();

            //从通道读取数据到缓冲区
            int len = inputStreamChannel.read(byteBuffer);
            if (len == -1){//表示读取完毕
                break;
            }
            //反转 position
            byteBuffer.flip();

            //将缓冲区数据写入到通道
            fileOutputStreamChannel.write(byteBuffer);
        }

        //关闭相应的流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
```

### 应用实例4-拷贝文件`transferFrom` 方法

实例要求:
1) 使用 FileChannel(通道) 和 方法 transferFrom ，完成文件的拷贝
2) 拷贝一张图片

```java
public class NIOFileChannel04 {
    public static void main(String[] args) throws IOException {
        File file = new File("test.png");

        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("test_bak.png");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        long position = 0;
        while (true) {

            long l = fileOutputStreamChannel.transferFrom(inputStreamChannel, position, 512);
            position += l;
            if (l==0){
                break;
            }
        }

        //关闭相应的流
        fileInputStream.close();
        fileOutputStream.close();
    }
}

```

### 关于Buffer 和 Channel的注意事项和细节

1) ByteBuffer 支持类型化的put 和 get, put 放入的是什么数据类型，get就应该使用
相应的数据类型来取出，否则可能有 BufferUnderflowException 异常。[举例说明]

```java
public class ByteBUfferPutGet {
    public static void main(String[] args) {
        //创建一个Buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);
        //类型化方式放入数据
        buffer.putInt(100);
        buffer.putLong(9);
        buffer.putChar('尚');
        buffer.putShort((short) 4);

        //反转
        buffer.flip();
        //取出
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());

    }
}
```

2) 可以将一个普通Buffer 转成只读Buffer [举例说明]

```java
public class ReadOnlyBuffer {

    public static void main(String[] args)  {
        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            buffer.put((byte) i);
        }
        //读取
        buffer.flip();
        //得到一个只读的Buffer
        ByteBuffer asReadOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(asReadOnlyBuffer.getClass());

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
```

3) NIO 还提供了 MappedByteBuffer， 可以让文件直接在内存（堆外的内存）中进
行修改， 而如何同步到文件由NIO 来完成. [举例说明]

```java
/**
 * 说明
 * 1.MappedByteBuffer可让文件直接在内存(堆外内存)修改,操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {

    public static void main(String[] args)throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt","rw");

        //获取对应通道
        FileChannel channel = randomAccessFile.getChannel();

        /*
         * 参数：Filechannel.MapMode.READWRITE使用的读写模式
         * 参数2：0：可以直接修改的起始位置
         * 参数3：5：是映射到内存的大小(不是索引位置),即将文件1.txt的多少个字节映射到内存
         * 可以直接修改的范围就是0-5
         * 实际类型DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');

        randomAccessFile.close();
    }
}

```

4) 前面我们讲的读写操作，都是通过一个Buffer 完成的，**NIO 还支持 通过多个Buffer (即 Buffer 数组) 完成读写操作**，即 Scattering 【分散】和 Gathering【聚合】

```java
/**
 * Scattering：将数据写入到buffer时，可以采用buffer数组，依次写入[分散]
 * Gathering：从buffer读取数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {
        //使用 ServerSocketChannel和SocketChannel网络

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] =  ByteBuffer.allocate(5);
        byteBuffers[1] =  ByteBuffer.allocate(3);

        //等待客户端连接（telnet测试）
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLen = 8;//假定从客户端接收8个字习
        //循环读取
        while (true){
            int byteRead = 0;

            while (byteRead <messageLen){
                long read = socketChannel.read(byteBuffers);
                byteRead += read;//累计读取的字节数
                System.out.println("byteRead = " + byteRead);

                //使用流打印，看看当前的buffer的position和limit   map 映射成新的字符串流，循环打印
                Arrays.asList(byteBuffers).stream().map(it->{
                    return "position = "+ it.position() +"，limit" +it.limit();
                }).forEach(System.out::println);
            }

            //将所有的buffer进行反转
            Arrays.asList(byteBuffers).forEach(it->{ it.flip();  });

            //将数据显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLen){
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;
            }
            //将所有的buffer进行clear
            Arrays.asList(byteBuffers).forEach(it->{ it.clear();  });

            System.out.print ("byteRead = " + byteRead+" byteWrite = " + byteWrite+" messageLen = " + messageLen);

        }

    }
}
```

# Selector(选择器)

## 基本介绍

1) Java 的 NIO，用非阻塞的 IO 方式。可以用一个线程，处理多个的客户端连
接，就会使用到`Selector(选择器)`
2) **Selector 能够检测多个注册的通道上是否有事件发生(注意:多个Channel以**
**事件的方式可以注册到同一个Selector)**，如果有事件发生，便获取事件然
后针对每个事件进行相应的处理。这样就可以只用一个单线程去管理多个
通道，也就是管理多个连接和请求。【示意图】
3) 只有在 连接/通道 真正有读写事件发生时，才会进行读写，就大大地减少
了系统开销，并且不必为每个连接都创建一个线程，不用去维护多个线程
4) 避免了多线程之间的上下文切换导致的开销