





# 探测局域网内设备

注意这是发的广播信息，同一网段中其它机器都会收到这个信息（只有特殊的监听这类消息的机器会做出回应）：

> 1. 探测网元192.168.184.107:8888向广播地址192.168.184.255:9999发送报文，广播地址将该报文在局域网内广播192.168.184.107、110，监听
> 2. 192.168.184.107、110 启动UDP监听 9999端口的UDP报文，收到后解析发送的packet.getSocketAddress()并响应

SendUDP.java

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
 
public class SendUDP {
    public static void main(String[] args) throws Exception {
        // Use this port to send broadcast packet
        @SuppressWarnings("resource")
        final DatagramSocket detectSocket = new DatagramSocket(8888);
        detectSocket.setBroadcast(true);
 
        // Send packet thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Send thread started.");
                while (true) {
                    try {
                        byte[] buf = new byte[1024];
                        int packetPort = 9999;//需要与接收端ReceiveUDP监听端口一致 
 
                        // Broadcast address  192.168.184.255:999
                        InetAddress hostAddress = InetAddress.getByName("192.168.184.255");
                        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                        String outMessage = stdin.readLine();
 
                        if (outMessage.equals("bye"))
                            break;
                        buf = outMessage.getBytes();
                        System.out.println("Send " + outMessage + " to " + hostAddress);
                        // Send packet to hostAddress:9999, server that listen
                        // 9999 would reply this packet
                        // fix： 搜不到局域网IP 时排查报文长度必须大于一定值
                        DatagramPacket out = new DatagramPacket(buf,buf.length, hostAddress, packetPort);
                        detectSocket.send(out);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        
        // Receive packet thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Receive thread started.");
                while(true) {
                    byte[] buf = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    try {
                        detectSocket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String rcvd = "Received from " + packet.getSocketAddress() + ", Data="
                            + new String(packet.getData(), 0, packet.getLength());
                    System.out.println(rcvd);
                }
            }
        }).start();
    }
}
```

ReceiveUDP.java

```java
import java.net.DatagramPacket;
import java.net.DatagramSocket;
 
public class ReceiveUDP {
    public static void main(String[] args) throws Exception {
        int listenPort = 9999;
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        @SuppressWarnings("resource")
        DatagramSocket responseSocket = new DatagramSocket(listenPort);
        System.out.println("Server started, Listen port: " + listenPort);
        while (true) {
            responseSocket.receive(packet);
            String rcvd = "Received "
                    + new String(packet.getData(), 0, packet.getLength())
                    + " from address: " + packet.getSocketAddress();
            System.out.println(rcvd);
 
            // Send a response packet to sender
            String backData = "DCBA";
            byte[] data = backData.getBytes();
            System.out.println("Send " + backData + " to " + packet.getSocketAddress());
            DatagramPacket backPacket = new DatagramPacket(data, 0,
                    data.length, packet.getSocketAddress());
            responseSocket.send(backPacket);
        }
    }
}
```

下图是 SendUDP 端的执行截图，发送内容为 Message：

![](http://static.oschina.net/uploads/space/2015/0409/125104_bllV_1434710.png)

在 SendUDP 端发送了消息后，UDP 端会立即显示收到消息，如下图：

![](http://static.oschina.net/uploads/space/2015/0409/125123_IWYr_1434710.png)

正如第一幅图看到的，我在**同一子网下的两台机器上运行着 ReceiveUDP**，于是两台机器都做出了回应。

如果将这种方式移植到 Android 手机上，可以用来探测同一 WiFi 下的其它设备（前提是这些设备上运行着类似 ReceiveUDP 的），以获取它们的 IP 地址。此后可以建立 TCP 连接，做其他的事情。有人说可以用 Ping 网段的方式来发现其它设备，但对于 Android 来说，这个方式并不可靠。因为判定消息不可达的时间难以确定。





# 理论介绍

## IP 地址分类 _ 广播 _ 广播地址运算

#### I UDP 单播 广播 多播

* * *

**1. 单播 : 两个设备之间相互通信 , 不涉及第三方的网络设备 ; 两者间通信 , 不被第三方感知 ;**

**2. 多播 (组播) : 一个设备给一组设备发送信息 , 并不是给所有的设备发送信息 ;**

**3. 广播 : 给所有设备发送信息 , 这个所有设备指的是局域网的所有设备 , 或者一定范围内的所有设备 ;**

**4. 广播局限性 : 如果某些设备一直发送广播 , 会导致网络带宽被占满 , 影响网络使用 , 因此路由器都会拒绝发送广播 ; 广播发送之后 , 一般只能在路由器内部进行广播 , 不能发送到路由器之外 , 路由器防火墙会拦截向外发送的广播 ;**

**5. 多播就是为了解决广播的局限性产生的 , 多播可以尽量少的给某一组设备精准发送信息 , 比广播要更能节省带宽 ; 多播比广播更好 ;**

#### II IP 地址类别

* * *

**IP 地址由两部分组成 : ① 网络地址 , ② 主机地址 ;**

*   **① 网络地址 : 表示当前设备处于哪个网络 ;**
*   **② 主机地址 : 表示当前设备是网络中的哪一台主机 ;**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190830220038687.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9oYW5zaHVsaWFuZy5ibG9nLmNzZG4ubmV0,size_16,color_FFFFFF,t_70)

**IP 地址分类 :**

*   **① A 类 IP 地址 : 第一个字节是网络地址 , 后面三个字节是主机地址 ;**
*   **② B 类 IP 地址 : 前两个字节是网络地址 , 后两个字节是主机地址 ;**
*   **③ C 类 IP 地址 : 前三个字节是网络地址 , 后一个字节是主机地址 ;**
*   **④ D 类 IP 地址 : 该类地址 , 专门为多播预留 , 多播比广播优越 , 也是因为有这些预留的多播地址 , 可以被所有的路由器所感知的 ;**
*   **E 类 IP 地址 : 该类地址是用于研究的实验型地址 ;**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190830220225957.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9oYW5zaHVsaWFuZy5ibG9nLmNzZG4ubmV0,size_16,color_FFFFFF,t_70)

#### III 广播地址

* * *

**1. 受限广播地址 : 255.255.255.255 是受限广播地址 , 向该地址的某端口号发送 UDP 数据包 , 只有局域网内部的设备能收到该信息 , 如果局域网内的设备对该端口进行了监听 , 那么就会收到该数据 ;**

**2. C 类网络的广播地址 : C 网广播地址格式 xxx.yyy.zzz.255 , 第一位 xxx 取值范围 192 ~ 223 , 剩下的 yyy 和 zzz 取值范围不限制 , 即 0 ~ 255 ; 在普通家庭的路由器局域网中没有设置的情况下可能是 192.168.1.255 ;**

#### IV 网络配置信息

* * *

**在 Windows 上的命令行中 , 执行 ipconfig 命令 , 会打印出相关网络的配置信息 ;**  
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019083111595014.png)

*   **① IPv4 地址是 : 192.168.1.6 ;**
*   **② 路由器地址 (网关地址) : 192.168.1.1 ;**
*   **③ 子网掩码 : 255.255.255.0 , 前三位都是 255 ; 最后一位取决于路由器的分配策略 , 一般情况下最后一位是 0 ,**

#### V 广播地址计算

* * *

**1. IP 地址构成 : IP 地址由 32 位构成 , 拆分成 4 个 byte 值 , 每个 8 位 , 就是三个点之间的数字 , 每个 byte 数字取值范围 0 ~ 255 ;**

**2. 广播地址运算 1 :**

*   **① IP 地址 : 192.168.1.6 , 转为二进制形式是 11000000 10101000 00000001 00000110 ;**
*   **② 子网掩码 255.255.255.0 ; 转为二进制形式是 11111111 11111111 11111111 00000000 ;**
*   **③ 计算网络地址 : IP 地址 和 子网掩码都是 32 位的二进制数组成 , 使用 IP 地址与子网掩码进行按位与操作 , 得到的就是网络地址 , 11000000 10101000 00000001 00000000 , 即 192.168.1.0 ;**
*   **④ 广播地址 : 网络地址的最后一位 , 就是广播地址 , 192.168.1.255 ;**

**3. 广播地址运算示例 2 :**

*   **① IP 地址 : 192.168.73.88 ;**
*   **② 子网掩码 : 255.255.255.192 , 这里着重说明下 , 子网掩码转为二进制后为 11111111 11111111 11111111 11000000 , 最后一位为 11000000 , 该子网掩码说明该局域网其可划分为 4 个网段 , 分别是 0 ~ 63 , 64 ~ 127 , 128 ~ 191 , 192 ~ 255 ;**
*   **③ 网络地址 : 每个网段第一位是网络地址 , 分别是 192.168.73.0 , 192.168.73.64 , 192.168.73.128 , 192.168.73.192 ; 此处 192.168.73.88 处于第二网段 64 ~ 127 网段 , 其网络地址是 192.168.73.64 ;**
*   **④ 广播地址 : 每个网段最后一位就是该网段的广播地址 , 分别是 192.168.73.63 , 192.168.73.127 , 192.168.73.191 , 192.168.73.255 ; 此处 192.168.73.88 处于第二网段 64 ~ 127 网段 , 其广播地址是 192.168.73.127 ;**

#### VI 广播通信

* * *

**广播通信 : 广播只能在本网段进行发送和接收 , 不能给其它网段发送广播 , 如上面的示例 , 子网掩码为 255.255.255.192 , 其网络有四个网段 , 分别是 0 ~ 63 , 64 ~ 127 , 128 ~ 191 , 192 ~ 255 , 网段之间是不能发送广播的 , 只能在网段内部发送广播 ; 如 192.168.73.88 是不能给 192.168.73.44 发送广播的 , 可以给 192.168.73.89 发送广播 ;**





## java UDP协议实现

### 什么是 UDP 协议。

**UDP（即用户数据报协议）**它是除了 TCP 协议以外的另一种网络信息传输的形式，我们知道 TCP 和 UDP 协议的不同点在于：

**TCP 协议是可靠而非安全的网络协议，它可以保证数据在从一端传输至另一端的时候可以准确的送达，并且送达的数据的排列顺序和送出时的顺序是相同的。**

**UDP 协议的安全而非可靠的网络协议，基于 UDP 的信息传输快，但是不提供可靠的保证，**

使用 UDP 协议进行数据传输时，用户无法知道数据能否到达主机，也不能确保到达目的地的顺序是否和发送的顺序相同，它就像是像一个广播站一样，将消息通过喇叭广播出去，然后人们可以听到这条消息，但是谁收了消息，谁没有收到消息，广播员是不知道的。即使如此，它也可以在较短时间内通知到听到消息的大部分人，所以说 UDP 协议是一种不可靠的协议，但是对于需要快速传输信息，并且能够容忍小的错误的通信，可以考虑使用 UDP 协议。

**基于 UDP 通信的基本模式类似于 “收发快递” 的过程。**

*   将数据打包（称为数据包），然后将数据包发往目的地。
*   接收别人发来的数据包，然后查看数据包。

**发送数据包的过程如下：**

1.  使用 DatagramSocket() 创建一个数据包套接字，

2.  使用 DatagramPacket(byte[] buf,int offset,int length,InetAddress address,int port) 创建要发送的数据包。

3.  使用 DatagramSocket 类的 send() 方法发送数据包。

**接收数据包的步骤如下：**

1.  使用 DatagramSocket(int port) 创建数据包套接字，并绑定到指定的端口

2.  使用 DatagramPocket(byte[] buf,int length) 创建字节数组来接收数据包。

3.  使用 DatagramPacket 类的 receive() 方法来接收 UDP 包，

**在这里需要注意的一点是：DatagramPacket 类的 receive() 方法开始接收数据时，如果还没有可以接收的数据，在正常情况下 DatagramPacket 类的 receive() 方法将会阻塞，一直等到网络上有数据传来，receive() 方法接收该数据并返回，**

**如果网络上没有一个数据传来，receive() 方法也没有阻塞，肯定是程序有问题，一般是使用了一个已经被占用的端口。**



### UDP 协议传输常用的两个类

#### DatagramPacket 

DatagramPacket 类位于 Java.net 包下，用来表示数据包。

DatagramPacket 类的构造函数有：

*   DatagramPocket(byte[] buf,int length)
*   DatagramPacket(byte[] buf,int offset,int length,InetAddress address,int port)

第一种构造函数用于接收数据包，它指定了数据包的内存空间和大小，可以形象的表示为接收快递的收件人，只需要获取到包裹就可以了。

第二种构造函数用于发送数据包，它不仅指定了数据包的内存空间和大小，还指定了数据包的目标地址和端口，在发送数据时必须指定接收方的 Socket 地址和端口号，使用第二种构造函数可以创建发送数据的 DatagramPacket 对象，因此第二种构造函数也可以理解为快递员，他不仅需要获取到要发送的快递包裹，还需要知道发送的地址（ip 地址）和门牌号（端口号）。

#### DatagramSocket 

DatagramSocket 类位于 java.net 包中，它用于表示接收和发送数据包的套接字，该类有以下的构造函数：

*   DatagramSocket()
*   DatagramSocket(int port)
*   DatagramSocket(int port,InetAddress addr)

第一种构造函数创建 DatagramSocket 对象，构造数据报套接字，并将其绑定到本地主机任何可用的端口上，

第二种构造函数创建 DatagramSocket 对象，创建数据报套接字，并将其绑定到本地主机的指定端口上，

第三种构造函数创建 DatagramSocket 对象，创建数据报套接字，并将其绑定到指定的本地地址上，这一种构造函数适用于有多块网卡和多个 ip 地址的情况。

在进行程序的接收时，必须指定一个端口号，不允许系统随机生成，此时可以使用第二种构造函数，就像你去发快递收货地址必须指定是一样的，在发送程序时通常使用第一种构造函数，不需要指定端口号，这就像发快递不管去哪一个快递公司都可以。
