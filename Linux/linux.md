[TOC]



# Linux开山篇

## 本套Linux课程内容

![image-20210425213634817](linux.assets/image-20210425213634817.png)

![image-20210425230938835](linux.assets/image-20210425230938835.png)

## Linux学习方向

![image-20210425231100024](linux.assets/image-20210425231100024.png)



Linux入式开发工程师、Linux嵌入式开发工程师、在linux下做各种程序开发

## Linux 的应用领域

### 个人桌面应用领域

此领域是传统linux 应用最薄弱的环节，传统linux 由于界面简单、操作复杂、应用软件少的缺点，一直被windows 所压制，但近些年来随着 ubuntu、fedora [fɪˈdɔ:rə] 等优秀桌面环境的兴起，同时各大硬件厂商对其支持的加大，linux 在个人桌面领域的占有率在逐渐的提高。

![image-20210425232604131](linux.assets/image-20210425232604131.png)

### 服务器应用领域

linux 在服务器领域的应用是最强的。

linux 免费、稳定、高效等特点在这里得到了很好的体现，近些年来 linux 服务器市场得到了飞速的提升，尤其在一些高端领域尤为广泛。

### 嵌入式应用领域

近些年来linux 在嵌入式领域的应用得到了飞速的提高

linux 运行稳定、对网络的良好支持性、低成本，且可以根据需要进行软件裁剪，内核最小可以达到几百KB 等特点，使其近些年来在嵌入式领域的应用得到非常大的提高

主要应用：机顶盒、数字电视、网络电话、程控交换机、手机、PDA、智能家居、智能硬件等都是其应用领域。以后再物联网中应用会更加广泛



## 学习 Linux 的阶段（高手进阶过程）

**linux 是一个开源、免费的操作系统**，其稳定性、安全性、处理多并发已经得到业界的认可，目前很多中型，大型甚至是集群项目都在使用linux,很多软件公司考虑到开发成本都首选linux,在中国软件公司得到广泛的使用。

我个人认为学习linux 流程为:

第1 阶段：linux 环境下的基本操作命令，包括 文件操作命令(rm mkdir chmod, chown) 编辑工具使用（vi vim）linux 用户管理(useradd userdel usermod)等
第2 阶段：linux 的各种配置（环境变量配置，网络配置，服务配置）
第3 阶段：linux 下如何搭建对应语言的开发环境（大数据，JavaEE, Python 等）
第4 阶段：能编写shell 脚本，对Linux 服务器进行维护。
第5 阶段：能进行安全设置，防止攻击，保障服务器正常运行，能对系统调优。
第6 阶段：深入理解Linux 系统（对内核有研究），熟练掌握大型网站应用架构组成、并熟悉各个环节的部署和维护方法。

## Linux 的学习方法和建议

1)	高效而愉快的学习
2)	先建立一个整体框架，然后细节
3)	**不需要掌握所有的Linux 指令，要学会查询手册和百度**
4)	先know how ,再know why
5)	计算机是一门”做中学” 的学科 ,不是会了再做，而是做了才会.
6)	适当的囫囵吞枣
7)	Linux 不是编程，重点是实际操作，各种常用指令要玩的溜



# 基础篇  Linux 入门

## Linux介绍

1）Linux怎么读【里纽克斯，利尼克斯，里纳克斯】
2）Linux是一款操作系统，免费，开源，安全，高效，稳定，处理高并发非常强悍，现在很多的企业级的项目都部署到Linuxiunix服务器运行。
3）Linux创始入linux林纳斯

<img src="linux.assets/image-20210425235555894.png" alt="image-20210425235555894" style="zoom:50%;" />

4）Linux的吉祥物


<img src="linux.assets/image-20210425235611889.png" alt="image-20210425235611889" style="zoom:50%;" />



|      |      |
| ---- | ---- |
|      |      |
5）Linux 的主要发行版

![image-20210426000134745](linux.assets/image-20210426000134745.png)

![image-20210425235906192](linux.assets/image-20210425235906192.png)

6）目前主要的操作系统有
windows，android，车载系统1inux等。

## unix 是怎么来的

![image-20210426000205660](linux.assets/image-20210426000205660.png)



## Linux 是怎么来的

![image-20210426001619014](linux.assets/image-20210426001619014.png)

## linux 和 unix 关系一览图

![image-20210426001629912](linux.assets/image-20210426001629912.png)



## linux 和 Windows 比较

![image-20210426001727301](linux.assets/image-20210426001727301.png)

# 基础篇 VM 和 Linux 系统(CentOS)安装

## 安装vm 和Centos

学习Linux 需要一个环境，我们需要创建一个虚拟机，然后在虚拟机上安装一个 Centos 系统来学习。

1)	先安装virtual machine ,vm12
2)	再安装Linux (CentOS 6.8)
3)	原理示意图，这里我们画图说明一下 VM 和 CentOS 的关系。

![image-20210426002720722](linux.assets/image-20210426002720722.png)

## vm 软件和CentOS 的安装软件

![image-20210426002908298](linux.assets/image-20210426002908298.png)

## VM 安装的步骤

![image-20210426002955751](linux.assets/image-20210426002955751.png)

## 安装vm和Centos

1）创建虚拟机

这里在配置网络连接时，有三种形式，需要大家伙注意 ：



![image-20210426010015461](linux.assets/image-20210426010015461.png)



2）开始安装系统（CentOS6.8）

参考word文档

## CentOS 的终端使用和联网

1) 终端的使用，点击鼠标右键，即可选择打开终端

![image-20210426233353261](linux.assets/image-20210426233353261.png)

2) 配置网络，可以上网。

点击上面右侧的；两个计算机图片，选择启用eth0,即可成功连接到网络，就可以上网。

![image-20210426233406997](linux.assets/image-20210426233406997.png)

##  vmtools 安装

### 介绍

1)	可以直接粘贴命令在windows 和 centos 系统之间
2)	可以设置windows 和centos 的共享文件夹
3)	示意图

![image-20210426233539599](linux.assets/image-20210426233539599.png)

## vmtools 的安装和使用

安装vmtools的步骤:


1.	进入centos
2.	点击vm菜单的->install vmware tools
3.	centos会出现一个vm的安装包
4.	点击右键解压, 得到一个安装文件
5.	进入该vm解压的目录，该文件在 /root/桌面/vmware-tools-distrib/下
6.	安装./vmware-install.pl
7.	全部使用默认设置即可
8.	需要reboot重新启动即可生效



## 设置共享 文件夹

![image-20210426234713181](linux.assets/image-20210426234713181.png)

映射到`cd /mnt/hgfs` 目录下



# 基础篇  Linux目录结构

## 基本介绍:

linux的文件系统是采用级层式的树状目录结构，在此`结构中的最上层是根目录“/”`，然后在此目录下再创建其他的目录。
深刻理解linux树状文件目录是非常重要的，这里我给大家说明一下。



记住一句经典的话：`在Linux世界里，一切皆文件`

## 具体的目录结构

![image-20210426235818215](linux.assets/image-20210426235818215.png)



![image-20210426235943820](linux.assets/image-20210426235943820.png)

![image-20210426235956699](linux.assets/image-20210426235956699.png)

![image-20210427000008763](linux.assets/image-20210427000008763.png)

![image-20210427000016983](linux.assets/image-20210427000016983.png)

![image-20210427000207524](linux.assets/image-20210427000207524.png)

说明：

1)	linux 的目录中有且只要一个根目录 /
2)	linux 的各个目录存放的内容是规划好，不用乱放文件。
3)	linux 是以文件的形式管理我们的设备，因此linux 系统，一切皆为文件。
4)	linux 的各个文件目录下存放什么内容，大家必须有一个认识。
5)	学习后，你脑海中应该有一颗linux 目录树

# 实操篇 远程登录 Linux 系统

## 为什么需要远程登录Linux

### 示意图

<img src="linux.assets/image-20210427000814141.png" alt="image-20210427000814141" style="zoom:50%;" />

### 说明

说明: 公司开发时候， 具体的情况是这样的

1)	linux 服务器是开发小组共享的.
2)	正式上线的项目是运行在公网的.
3)	因此程序员需要远程登录到centos 进行项目管理或者开发.
4)	画出简单的网络拓扑示意图(帮助理解)
5)	远程登录客户端有 Xshell5， Xftp5 , 我们学习使用 Xshell5 和 Xftp , 其它的远程工具大同

## 远程登录Linux-Xshell5

说明: Xshell 是目前最好的远程登录到 Linux 操作的软件，流畅的速度并且完美解决了**中文乱码**的问题， 是目前程序员首选的软件。
Xshell [1] 是一个强大的安全终端模拟软件，它支持 SSH1, SSH2, 以及 Microsoft Windows 平台的TELNET 协议。
Xshell 可以在Windows 界面下用来访问远端不同系统下的服务器，从而比较好的达到远程控制终端的目的。

**特别说明**：如果希望安装好 XShell 5 就可以远程访问 Linux 系统的话，需要有一个**前提**，就是`Linux 启用了SSHD 服务`，该`服务会监听22 号端口`。



### 查看方式是否开启SSHD服务

终端输入     `setup`

选择System services   进行查看SSHD服务

<img src="linux.assets/image-20210427001906985.png" alt="image-20210427001906985" style="zoom:50%;" />

## 安装 XShell5 并使用

### XShell5 的关键配置

![image-20210427001233905](linux.assets/image-20210427001233905.png)



## 远程上传下载文件Xftp5

![image-20210427002844372](linux.assets/image-20210427002844372.png)

连接

![image-20210427003237075](linux.assets/image-20210427003237075.png)

# Linux实操篇 Vi和Vim编辑器



## vi和vim的基本介绍

所有的Linux系統都会**内建vi文本編辑器**。
**Vim**具有程序编辑的能力，可以看做是**Vi的增强版本**，可以主动的以字体颜色辨别语法的正确性，方便程序设计。代码补完、编译及错误跳转等方便编程的功能特别丰富，在程序员中被广泛使用。

## vi和vim常用的三种模式

+ 正常模式: 

  **在正常模式下，我们可以使用快捷键。**
  以vim 打开一个档案就直接进入一般模式了(这是默认的模式)。在这个模式中， 你可以使用『上下左右』按键来移动光标，你可以使用『删除字符』或『删除整行』来处理档案内容，也可以使用『复制、贴上』来处理你的文件数据。

+ 插入模式:
  按下i, I, o, O, a, A, r, R等任何一个字母之后才会进入编辑模式, 一般来说按i即可.

+ 命令行模式
  在这个模式当中，可以提供你相关指令，完成读取、存盘、替换、离开vim 、显示行号等的动作则是在此模式中达成的！



## vi和vim模式的相互切换

![image-20210427222127896](linux.assets/image-20210427222127896.png)

## 快捷键的使用案例

1)【复制】**拷贝当前行 `yy`** , **拷贝当前行至向下的5行 `5yy`**，并**粘贴 `p`**。

2)【删除】**删除当前行 `dd`**  , **删除当前行至向下的5行 `5dd`**

3)【查找】在文件中**查找某个单词**[**命令行下`/关键字，回车查找`,  `输入n 就是查找下一个`**] ,查询 `/word `   `n下一个`

4)【行号】设置文件的**行号**，取消文件的行号.[命令行下 **`: set nu` 和 `:set nonu`**]

5)【首尾】编辑/etc/profile 文件，使用快捷键到底文档的**最末行[G]**和**最首行[gg]**

6)【撤销】在一个文件中输入"hello" ,然后又**撤销**这个动作 **u**

7)【跳转】编辑 /etc/profile 文件，并将光标移动到 第20行**【shift+g】**

​		第一步：显示行号 ：set nu      第二步：输入20         第三步：输入`shift+g`

## vim和vi的快捷键键盘一览图

![image-20210427223657018](linux.assets/image-20210427223657018.png)

# 实操篇 开机、重启和用户登录注销

## 关机&重启命令

```sh
sync 				#把内存的数据同步到磁盘

shutdown –h now 	#立该进行关机
shutdown -h  1  	#"hello, 1 分钟后会关机了" 
shutdown –r now 	#现在重新启动计算机
halt				#关机，作用和上面一样.
reboot        		#现在重新启动计算机
```

> 注意世界：不管是重启系统还是关闭系统，首先要运行sync命令，把内存中的数据写到磁盘中，防止数据丢失

## 用户登录和注销

基本介绍

1)	登录时尽量少用root帐号登录，因为它是系统管理员，最大的权限，避免操作失误。可以利用普通用户登录，登录后再用**”su - 用户名’**命令`【su root】`来切换成系统管理员身份.
2)	在提示符下输入**logout 即可注销用户**

```shell
[root@hadoop1 linux-share]# logout
```

> 注意：
>
> 1)	logout 注销指令在图形运行级别无效，在 运行级别3下有效.
> 2)	运行级别这个概念，后面给大家介绍



# 实操篇 用户管理



## 基本介绍

Linux系统是一个多用户多任务的操作系统，任何一个要使用系统资源的用户，都必须首先向系统管理员申请一个账号，然后以这个账号的身份进入系统。

![image-20210427235716199](linux.assets/image-20210427235716199.png)

说明
1）Linux系统是一个多用户多任务的操作系统，任何一个要使用系统资源的用户，都必须首先向系统管理员申请一个账号，然后以这个账号的身份进入系统。
2）Linux的用户需要至少要属于一个组。

## 添加用户

基本语法:

`useradd [选项]  用户名`
应用案例
1)案例1:添加一个用户xiaoming

```sh
[root@hadoop1 xm]$ useradd xm  # 添加xm用户，添加到名称为xm的组，并且添加到xm组内

# 添加用户后会自动创建家目录
[root@hadoop1 xm]$ cd /home/
[root@hadoop1 home]$ ls
xm
```

> 细节说明
>
> 1)	当创建用户成功后，会自动的创建和用户同名的家目录
> 2)	也可以通过useradd -d  指定目录 新的用户名，给新创建的用户指定家目录，（PS：这个目录不能手动创建，自动创建）



特别说明：
cd 表示change directory, 切换目录.

## 指定/修改密码

`passwd    用户名`



应用案例

```sh
# 给xm用户指定密码，输入密码时界面不显示
[root@hadoop1 home]$ passwd xm
更改用户 xm 的密码 。
新的 密码：
无效的密码： WAY 过短
无效的密码： 过于简单
重新输入新的 密码：
passwd： 所有的身份验证令牌已经成功更新。
```

## 删除用户

基本语法

`userdel   用户名`

应用案例

1)	删除用户xiaoming，但是要保留家目录

```sh
[root@hadoop1 home]$ userdel xm
[root@hadoop1 home]$ ls /home/
xm
```

2)	删除用户以及用户主目录

```sh
[root@hadoop1 home]$ useradd xh 
[root@hadoop1 home]$ ls /home/
xh  xm
[root@hadoop1 home]$ userdel -r xh   # -r 表示删除删除用户及家目录
[root@hadoop1 home]$ ls /home/
xm
```

## 查询用户信息指令

基本语法
`id  用户名`
应用实例
案例1：请查询root 信息

```sh
[root@hadoop1 home]$ id root
uid=0(root) gid=0(root) 组=0(root)   # uid：用户id	gid：所在组的id号   组：组名称
[root@hadoop1 home]$ id xm
uid=500(xm) gid=500(xm) 组=500(xm)
```

细节说明
1) 当用户不存在时，返回无此用户

## 切换用户

介绍
在操作Linux中，如果当前用户的权限不够，可以通过 `su - 指令`，切换到高权限用户，比如root

基本语法
`su  – 切换用户名`
应用实例
1) 创建一个用户zf, ，指定密码，然后切换到zf.

```sh
[root@hadoop1 home]$ useradd zf   	#创建用户
[root@hadoop1 home]$ passwd zf		# 设置密码
更改用户 zf 的密码 。
新的 密码：
无效的密码： WAY 过短
无效的密码： 过于简单
重新输入新的 密码：
passwd： 所有的身份验证令牌已经成功更新。
[root@hadoop1 home]$ su zf  	# 切换到zf用户   root -> zf
[zf@hadoop1 home]$ cd /root
bash: cd: /root: 权限不够  		# 当用户权限不足时会给提示
[zf@hadoop1 home]$ exit			# 返回到原来的用户  zf->root
exit
[root@hadoop1 home]$
```



细节说明

1)	**从权限高的用户切换到权限低的用户，不需要输入密码**，反之需要。
2)	当需要返回到原来用户时，使用exit指令

## 查看当前用户/登录用户

基本语法：`whoami/ who am I`

```sh
[root@hadoop1 home]$ whoami
root
[root@hadoop1 home]$ who am i
root     pts/0        2021-04-28 07:48 (192.168.64.1)
```

## 用户组

### 新增组

指令：`groupadd 组名`

```sh
[root@hadoop1 home]$ groupadd def_group
```

### 删除组

指令(基本语法)

`groupdel 组名`

```sh
[root@hadoop1 home]$ groupdel def_group
```

### 增加用户时直接加上组

指令(基本语法)

`useradd  –g 用户组用户名`

案例：增加一个用户zwj, 直接将他指定到wudang

```sh
[root@hadoop1 home]$ groupadd wudang		#创建一个组
[root@hadoop1 home]$ useradd -g wudang zwj	#创建zwj用户，并指定到wudang组
[root@hadoop1 home]$ id zwj					#查询zwj信息
uid=502(zwj) gid=502(wudang) 组=502(wudang)
[root@hadoop1 home]$ ls /home/				#自动创建home目录下用户目录
xm  zf  zwj
```

### 修改用户的组

指令（基本语法）
`usermod -g 用户组 用户名`

案例演示：创建一个shaolin 组，让将zwj 用户修改到shaolin

```sh
[root@hadoop1 home]$ groupadd shaolin
[root@hadoop1 home]$ usermod -g shaolun zwj
usermod：“shaolun”组不存在
[root@hadoop1 home]$ usermod -g shaolin zwj
[root@hadoop1 home]$ id zwj
uid=502(zwj) gid=503(shaolin) 组=503(shaolin)
```

## 用户和组的相关文件

![image-20210428005255560](linux.assets/image-20210428005255560.png)

+ `/etc/passwd`文件
  用户**（user）的配置文件**，记录用户的各种信息
  每行的含义：`用户名：口令：用户标识号：组标识号：注释性描述：主目录：登录Shell`

  ![image-20210428010248553](linux.assets/image-20210428010248553.png)

+ `/etc/shadow`文件
  口令的配置文件
  每行的含义：`登录名：加密口令：最后一次修改时间：最小时间间隔：最大时间间隔：警
  告时间：不活动时间：失效时间：标志`

+ `/etc/group`文件
  **组（group）的配置文件**，记录Linux包含的组的信息
  每行含义：`组名：口令组标识号：组内用户列表`

  ![image-20210428010304036](linux.assets/image-20210428010304036.png)

# 实操篇 实用指令

## 指定运行级别

