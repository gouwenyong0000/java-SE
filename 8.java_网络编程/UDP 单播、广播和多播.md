> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.cnblogs.com](https://www.cnblogs.com/jingliming/p/4477264.html)

     使用 UDP 协议进行信息的传输之前不需要建议连接。换句话说就是客户端向服务器发送信息，客户端只需要给出服务器的 ip 地址和端口号，然后将信息封装到一个待发送的报文中并且发送出去。至于服务器端是否存在，或者能否收到该报文，客户端根本不用管。     

     单播用于两个主机之间的端对端通信，广播用于一个主机对整个局域网上所有主机上的数据通信。单播和广播是两个极端，要么对一个主机进行通信，要么对整个局域网上的主机进行通信。实际情况下，经常需要对一组特定的主机进行通信，而不是整个局域网上的所有主机，这就是多播的用途。

　　通常我们讨论的 udp 的程序都是一对一的单播程序。本章将讨论一对多的服务：广播（broadcast）、多播（multicast）。对于广播，网络中的所有主机都会接收一份数据副本。对于多播，消息只是发送到一个多播地址，网络知识将数据分发给哪些表示想要接收发送到该多播地址的数据的主机。总得来说，只有 UDP 套接字允许广播或多播。

一、UDP 广播
============================

　　广播 UDP 与单播 UDP 的区别就是 IP 地址不同，广播使用广播地址 255.255.255.255，将消息发送到在同一广播网络上的每个主机。值得强调的是：**本地广播信息是不会被路由器转发**【只能在路由器内广播】。当然这是十分容易理解的，因为如果路由器转发了广播信息，那么势必会引起网络瘫痪。这也是为什么 IP 协议的设计者故意没有定义互联网范围的广播机制。

广播地址通常用于在网络游戏中处于同一本地网络的玩家之间交流状态信息等。

　　其实广播顾名思义，就是想局域网内所有的人说话，**但是广播还是要指明接收者的端口号的**，因为不可能接受者的所有端口都来收听广播。

**UDP 服务端代码：**

```c++
#include<iostream>
 #include<stdio.h>
 #include<sys/socket.h>
 #include<unistd.h>
 #include<sys/types.h>
 #include<netdb.h>
 #include<netinet/in.h>
 #include<arpa/inet.h>
 #include<string.h>
 using namespace std;
 int main()
 {
     setvbuf(stdout,NULL,_IONBF,0);
     fflush(stdout);
     int sock=-1;
     if((sock=socket(AF_INET,SOCK_DGRAM,0))==-1)
     {
         cout<<"sock error"<<endl;
         return -1;
     }
     const int opt=-1;
     int nb=0;
     nb=setsockopt(sock,SOL_SOCKET,SO_BROADCAST,(char*)&opt,sizeof(opt));//设置套接字类型
     if(nb==-1)
     {
         cout<<"set socket error...\n"<<endl;
         return -1;
     }
     struct sockaddr_in addrto;
     bzero(&addrto,sizeof(struct sockaddr_in));
     addrto.sin_family=AF_INET;
     addrto.sin_addr.s_addr=htonl(INADDR_BROADCAST);//套接字地址为广播地址
     addrto.sin_port=htons(6000);//套接字广播端口号为6000
     int nlen=sizeof(addrto);
     while(1)
     {
         sleep(1);
         char msg[]={"the message broadcast"};
         int ret=sendto(sock,msg,strlen(msg),0,(sockaddr*)&addrto,nlen);//向广播地址发布消息
         if(ret<0)
         {
             cout<<"send error...\n"<<endl;
             return -1;
         }
         else 
         {
             printf("ok\n");
         }
     }
     return 0;
 }
```

 UDP 广播客户端代码：

```c++
#include<iostream>
 #include<stdio.h>
 #include<sys/socket.h>
 #include<unistd.h>
 #include<sys/types.h>
 #include<netdb.h>
 #include<netinet/in.h>
 #include<arpa/inet.h>
 #include<string.h>
 using namespace std;
 int main()
 {
         setvbuf(stdout,NULL,_IONBF,0);
         fflush(stdout);
         struct sockaddr_in addrto;
         bzero(&addrto,sizeof(struct sockaddr_in));
         addrto.sin_family=AF_INET;
         addrto.sin_addr.s_addr=htonl(INADDR_ANY);
         addrto.sin_port=htons(6000);
         socklen_t len=sizeof(addrto);
         int sock=-1;
         if((sock=socket(AF_INET,SOCK_DGRAM,0))==-1)
         {
                 cout<<"socket error..."<<endl;
                 return -1;
         }
         const int opt=-1;
         int nb=0;
         nb=setsockopt(sock,SOL_SOCKET,SO_BROADCAST,(char*)&opt,sizeof(opt));
         if(nb==-1)
         {
                 cout<<"set socket errror..."<<endl;
                 return -1;
         }
         if(bind(sock,(struct sockaddr*)&(addrto),len)==-1)
         {
                 cout<<"bind error..."<<endl;
                 return -1;
         }
         char msg[100]={0};
         while(1)
         {
                 int ret=recvfrom(sock,msg,100,0,(struct sockaddr*)&addrto,&len);
                 if(ret<=0)
                 {
                         cout<<"read error..."<<endl;
                 }
                 else
                 {
                         printf("%s\t",msg);
                 }
                 sleep(1);
         }
         return 0;
 }
```

二、UDP 多播
============================

1、多播（组播）的概念
------------------------------

　　多播，也称为 “组播”，将网络中同一业务类型主机进行了逻辑上的分组，进行数据收发的时候其数据仅仅在同一分组中进行，其他的主机没有加入此分组不能收发对应的数据。

　　在广域网上广播的时候，其中的交换机和路由器只向需要获取数据的主机复制并转发数据。主机可以向路由器请求加入或退出某个组，网络中的路由器和交换机有选择地复制并传输数据，将数据仅仅传输给组内的主机。多播的这种功能，可以一次将数据发送到多个主机，又能保证不影响其他不需要（未加入组）的主机的其他通 信。

相对于传统的一对一的单播，多播具有如下的优点：

　　1、具有同种业务的主机加入同一数据流，共享同一通道，节省了带宽和服务器的优点，具有广播的优点而又没有广播所需要的带宽。

　　2、服务器的总带宽不受客户端带宽的限制。由于组播协议由接收者的需求来确定是否进行数据流的转发，所以服务器端的带宽是常量，与客户端的数量无关。

　　3、与单播一样，多播是允许在广域网即 Internet 上进行传输的，而广播仅仅在同一局域网上才能进行。

组播的缺点：

　　1、多播与单播相比没有纠错机制，当发生错误的时候难以弥补，但是可以在应用层来实现此种功能。

　　2、多播的网络支持存在缺陷，需要路由器及网络协议栈的支持。

　　3、多播的应用主要有网上视频、网上会议等。

2、广域网的多播
---------------------------

　　多播的地址是特定的，D 类地址用于多播。D 类 IP 地址就是多播 IP 地址，即 224.0.0.0 至 239.255.255.255 之间的 IP 地址，并被划分为局部连接多播地址、预留多播地址和管理权限多播地址 3 类：

　　1、局部多播地址：在 224.0.0.0～224.0.0.255 之间，这是为路由协议和其他用途保留的地址，路由器并不转发属于此范围的 IP 包。

　　2、预留多播地址：在 224.0.1.0～238.255.255.255 之间，可用于全球范围（如 Internet）或网络协议。

　　3、管理权限多播地址：在 239.0.0.0～239.255.255.255 之间，可供组织内部使用，类似于私有 IP 地址，不能用于 Internet，可限制多播范围。

　　多播的程序设计使用 setsockopt() 函数和 getsockopt() 函数来实现，组播的选项是 IP 层的，其选项值和含义参见 11.5 所示。

　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　表 11.5 多播相关的选项

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top"><p align="center"><strong>getsockopt()/setsockopt()</strong> 的选项</p></td><td valign="top"><p align="center">含 义</p></td></tr><tr><td valign="top"><p>IP_MULTICAST_TTL</p></td><td valign="top"><p>设置多播组数据的 TTL 值</p></td></tr><tr><td valign="top"><p>IP_ADD_MEMBERSHIP</p></td><td valign="top"><p>在指定接口上加入组播组</p></td></tr><tr><td valign="top"><p>IP_DROP_MEMBERSHIP</p></td><td valign="top"><p>退出组播组</p></td></tr><tr><td valign="top"><p>IP_MULTICAST_IF</p></td><td valign="top"><p>获取默认接口或设置接口</p></td></tr><tr><td valign="top"><p>IP_MULTICAST_LOOP</p></td><td valign="top"><p>禁止组播数据回送</p></td></tr></tbody></table>

3、多播程序设计的框架
------------------------------

  

要进行多播的编程，需要遵从一定的编程框架。多播程序框架主要包含套接字初始化、设置多播超时时间、加入多播组、发送数据、接收数据以及从多播组中离开几个方面。其步骤如下：

（1）建立一个 socket。

（2）然后设置多播的参数，例如超时时间 TTL、本地回环许可 LOOP 等。

（3）加入多播组。

（4）发送和接收数据。

（5）从多播组离开。

4、多播实现代码
---------------------------

  

**服务端代码：**

```c++
#include<iostream>
 #include<stdio.h>
 #include<sys/socket.h>
 #include<netdb.h>
 #include<sys/types.h>
 #include<arpa/inet.h>
 #include<netinet/in.h>
 #include<unistd.h>
 #include<stdlib.h>
 #include<string.h>
 #define MCAST_PORT 8888
 #define MCAST_ADDR "224.0.0.88"  // 多播地址
 #define MCAST_DATA "BROADCAST TEST DATA"  // 多播内容
 #define MCAST_INTERVAL 5  //多播时间间隔
 using namespace std;
 int main()
 {
         int sock;
         struct sockaddr_in mcast_addr;
         sock=socket(AF_INET,SOCK_DGRAM,0);
         if(sock==-1)
         {
                 cout<<"socket error"<<endl;
                 return -1;
         }
         memset(&mcast_addr,0,sizeof(mcast_addr));
         mcast_addr.sin_family=AF_INET;
         mcast_addr.sin_addr.s_addr=inet_addr(MCAST_ADDR);
         mcast_addr.sin_port=htons(MCAST_PORT);
         while(1)
         {       //向局部多播地址发送多播内容
                 int n=sendto(sock,MCAST_DATA,sizeof(MCAST_DATA),0,(struct sockaddr*)&mcast_addr,sizeof(mcast_addr));
                 if(n<0)
                 {
                         cout<<"send error"<<endl;
                         return -2;
                 }
                 else
                 {
                         cout<<"send message is going ...."<<endl;
                 }
                 sleep(MCAST_INTERVAL);
         }
         return 0;
 }
```

**客户端代码：**

```c++
#include<iostream>
 #include<stdio.h>
 #include<stdlib.h>
 #include<string.h>
 #include<sys/types.h>
 #include<unistd.h>
 #include<sys/socket.h>
 #include<netdb.h>
 #include<arpa/inet.h>
 #include<netinet/in.h>
 #define MCAST_PORT 8888
 #define MCAST_ADDR "224.0.0.88" /*一个局部连接多播地址，路由器不进行转发*/
 #define MCAST_INTERVAL 5  //发送时间间隔
 #define BUFF_SIZE 256   //接收缓冲区大小
 using namespace std;
 int main()
 {
         int sock;
         struct sockaddr_in local_addr;
         int err=-1;
         sock=socket(AF_INET,SOCK_DGRAM,0);
         if(sock==-1)
         {
                 cout<<"sock error"<<endl;
                 return -1;
         }
         /*初始化地址*/
         local_addr.sin_family=AF_INET;
         local_addr.sin_addr.s_addr=htonl(INADDR_ANY);
         local_addr.sin_port=htons(MCAST_PORT);
         /*绑定socket*/
         err=bind(sock,(struct sockaddr*)&local_addr,sizeof(local_addr));
         if(err<0)
         {
                 cout<<"bind error"<<endl;
                 return -2;
         }
         /*设置回环许可*/
         int loop=1;
         err=setsockopt(sock,IPPROTO_IP,IP_MULTICAST_LOOP,&loop,sizeof(loop));
         if(err<0)
         {
                 cout<<"set sock error"<<endl;
                 return -3;
         }
         struct ip_mreq mreq;/*加入广播组*/
         mreq.imr_multiaddr.s_addr=inet_addr(MCAST_ADDR);//广播地址
         mreq.imr_interface.s_addr=htonl(INADDR_ANY); //网络接口为默认
         /*将本机加入广播组*/
         err=setsockopt(sock,IPPROTO_IP,IP_ADD_MEMBERSHIP,&mreq,sizeof(mreq));
         if(err<0)
         {
                 cout<<"set sock error"<<endl;
                 return -4;
         }
         int times=0;
         socklen_t addr_len=0;
         char buff[BUFF_SIZE];
         int n=0;
         /*循环接受广播组的消息，5次后退出*/
         for(times=0;;times++)
         {
                 addr_len=sizeof(local_addr);
                 memset(buff,0,BUFF_SIZE);
                 n=recvfrom(sock,buff,BUFF_SIZE,0,(struct sockaddr*)&local_addr,&addr_len);
                 if(n==-1)
                 {
                         cout<<"recv error"<<endl;
                         return -5;
                 }
                 /*打印信息*/
                 printf("RECV %dst message from server : %s\n",times,buff);
                 sleep(MCAST_INTERVAL);
         }
         /*退出广播组*/
         err=setsockopt(sock,IPPROTO_IP,IP_DROP_MEMBERSHIP,&mreq,sizeof(mreq));
         close(sock);
         return 0;
 }
```

 **关于此处 bind 函数的解析**

　　bind 操作首先检查用户指定的端口是否可用，然后为 socket 的一些成员设置正确的值，并添加到哈希表 myudp_hash 中。然后，协议栈每次收到 UDP 数据，就会检查该数据报的源和目的地址，还有源和目的端口，在 myudp_hash 中找到匹配的 socket，把该数据报放入该 socket 的接收队列，以备用户读取。在这个程序中，bind 操作把 socket 绑定到地址 224.0.0.88:8888 上， 该操作产生的直接结果就是，对于 socket 本身，下列值受影响：  

```c++
    struct inet_sock{  
        .rcv_saddr = 224.0.0.88;  
        .saddr = 0.0.0.0;  
        .sport = 8888;  
        .daddr = 0.0.0.0;  
        .dport = 0;  
    }  

```



    这五个数据表示，该套接字在发送数据包时，本地使用端口 8888，本地可以使用任意一个网络设备接口，发往的目的地址不指定。在接收数据时，只接收发往 IP 地址 224.0.0.88 的端口为 8888 的数据。

**我的疑问？？？**

**为什么要广播方和接受方的端口号相同才能收到广播？我试了在一台 linux 机子上开两个客户端其中一个和广播方的端口号不同，这个客户端结果收不到广播，哪位网友知道恳请告之。**  
    程序中，紧接着 bind 有一个 setsockopt 操作，它的作用是将 socket 加入一个组播组，因为 socket 要接收组播地址 224.0.0.1 的数据，它就必须加入该组播组。

三、UDP 广播与单播
===============================

  

广播与单播的比较
----------------------------

  

　　广播和单播的处理过程是不同的，单播的数据只是收发数据的特定主机进行处理，而广播的数据整个局域网都进行处理。

　　例如在一个以太网上有 3 个主机，主机的配置如表 11.4 所示。

　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　表 11.4 某局域网中主机的配置情况

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top"><p align="center">主 机</p></td><td valign="top"><p align="center"><strong>A</strong></p></td><td valign="top"><p align="center"><strong>B</strong></p></td><td valign="top"><p align="center"><strong>C</strong></p></td></tr><tr><td valign="top"><p>IP 地址</p></td><td valign="top"><p>192.168.1.150</p></td><td valign="top"><p>192.168.1.151</p></td><td valign="top"><p>192.168.1.158</p></td></tr><tr><td valign="top"><p>MAC 地址</p></td><td valign="top"><p>00:00:00:00:00:01</p></td><td valign="top"><p>00:00:00:00:00:02</p></td><td valign="top"><p>00:00:00:00:00:03</p></td></tr></tbody></table>

　　单播流程：主机 A 向主机 B 发送 UDP 数据报，发送的目的 IP 为 192.168.1.151，端口为 80，目的 MAC 地址为 00:00:00:00:00:02。此数据经过 UDP 层、IP 层，到达数据链路层，数据在整个以太网上传播，在此层中其他主机会 判断目的 MAC 地址。主机 C 的 MAC 地址为 00:00:00:00:00:03，与目的 MAC 地址 00:00:00:00:00:02 不匹配，数据链路层 不会进行处理，直接丢弃此数据。

　　主机 B 的 MAC 地址为 00:00:00:00:00:02，与目的 MAC 地址 00:00:00:00:00:02 一致，此数据会经过 IP 层、UDP 层，到达接收数据的应用程序。

　　广播的流程：主机 A 向整个网络发送广播数据，发送的目的 IP 为 192.168.1.255，端口为 80，目的 MAC 地址为 FF:FF:FF:FF:FF:FF。此数据经过 UDP 层、IP 层，到达数据链路层，数据在整个以太网上传播，在此层中其他主机会 判断目的 MAC 地址。由于目的 MAC 地址为 FF:FF:FF:FF:FF:FF，主机 C 和主机 B 会忽略 MAC 地址的比较（当然，如果协议栈不支持广播，则 仍然比较 MAC 地址），处理接收到的数据。

　　主机 B 和主机 C 的处理过程一致，此数据会经过 IP 层、UDP 层，到达接收数据的应用程序。

