> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.cnblogs.com](https://www.cnblogs.com/whseay/p/3456038.html)

系统环境
=======

　　FTP 客户端：Windows7 旗舰版，管理员权限命令行；

　　FTP 服务端：CentOS 6.5，VSFTP，端口 21（默认）

登陆 FTP
=========

1. 在命令行下输入 ftp，出现 ftp > 即进入 FTP 命令行

2. open _FTP 的 IP 地址 / 域名_ 例如 `open 169.254.234.241` （当然有最简单的方式 直接在命令行下 ftp _FTP 的 IP 地址 / 域名_）
3. 按照提示输入用户名和密码，完成登陆

```bash
Microsoft Windows [版本 6.1.7601]
版权所有 (c) 2009 Microsoft Corporation。保留所有权利。

C:\Users\Administrator>ftp
ftp> open 169.254.234.241
连接到 169.254.234.241。
220 Welcome to blah FTP service.
用户(169.254.234.241:(none)): wy
331 Please specify the password.
密码:
230 Login successful.
ftp>
```

上传一个文件
=========

1. put（或者 send，上传多个使用 mput） _文件名（包含路径）_ 例如 put C:\Users\Administrator\Documents\Myself.pub
2. ls（或者 dir） 查看当前目录文件

```sh
ftp> put C:\Users\Administrator\Documents\Myself.pub
200 PORT command successful. Consider using PASV.
150 Ok to send data.
226 Transfer complete.
ftp: 发送 233 字节，用时 0.02秒 14.56千字节/秒。
ftp> send C:\Users\Administrator\Documents\Myself
200 PORT command successful. Consider using PASV.
150 Ok to send data.
226 Transfer complete.
ftp: 发送 1004 字节，用时 0.00秒 1004000.00千字节/秒
ftp> ls
200 PORT command successful. Consider using PASV.
150 Here comes the directory listing.
Myself
Myself.pub
226 Directory send OK.
ftp: 收到 20 字节，用时 0.00秒 20.00千字节/秒。
ftp>
```

　　很显然，send 的速度比 put 快不少。但是他们两个的区别我没有详细的查。

下载一个文件
=========

1. lcd _本地目录路径_ 设置当前工作路径，也就是你要把文件下载到哪。默认是在当前使用命令行的用户的主目录。我的是 C:\Users\Administrator。也可以使用 !chdir 查看当前目录。
2. cd _服务器目录_ 进入到你要下载的文件在服务器端的目录位置
3. get（下载多个用 mget） 文件名 下载该文件。例如 get Myself
4. !dir 查看当前目录文件，就能看到你刚才下载的文件啦

```sh
ftp> dir
200 PORT command successful. Consider using PASV.
150 Here comes the directory listing.
-rw-r--r--    1 500      500           986 Dec 03 10:08 Myself
-rw-r--r--    1 500      500           233 Dec 03 10:07 Myself.pub
226 Directory send OK.
ftp: 收到 132 字节，用时 0.00秒 132000.00千字节/秒。
ftp> !cd C:\Users\Administrator\Desktop
ftp> !chdir
C:\Users\Administrator
ftp> lcd C:\Users\Administrator\Desktop
目前的本地目录 C:\Users\Administrator\Desktop。
ftp> !chdir
C:\Users\Administrator\Desktop
ftp> get Myself
200 PORT command successful. Consider using PASV.
150 Opening ASCII mode data connection for Myself (986 bytes).
226 Transfer complete.
ftp: 收到 1004 字节，用时 0.00秒 1004.00千字节/秒。
ftp> dir
200 PORT command successful. Consider using PASV.
150 Here comes the directory listing.
-rw-r--r--    1 500      500           986 Dec 03 10:08 Myself
-rw-r--r--    1 500      500           233 Dec 03 10:07 Myself.pub
226 Directory send OK.
ftp: 收到 132 字节，用时 0.00秒 132.00千字节/秒。
ftp> !dir
 驱动器 C 中的卷没有标签。
 卷的序列号是 941F-307E

 C:\Users\Administrator\Desktop 的目录

2013/12/03  18:27    <DIR>          .
2013/12/03  18:27    <DIR>          ..
2013/11/17  13:23               932 Evernote.lnk
2013/11/24  15:43             1,023 FlashFXP.lnk
2013/11/21  08:53             2,176 Git Shell.lnk
2013/11/21  08:53               308 GitHub.appref-ms
2013/12/03  18:27             1,004 Myself
2013/10/26  21:26             1,627 SecureCRT.lnk
               6 个文件          7,070 字节
               2 个目录 50,591,227,904 可用字节
ftp>
```

断开连接
=======

　　bye 就是这样。

```
ftp> status
连接到 169.254.234.241。
类型: ascii；详细: 开 ；铃声: 关 ；提示: 开 ；通配: 开
调试: 关 ；哈希标记打印: 关 。
ftp> bye
221 Goodbye.

C:\Users\Administrator>
```

总结
=====

　　这里就是拿一个例子来说明了一下简单的上传和下载命令的使用。使用 help 和 help _[Command]_ 来查看对应命令的解释。最后给出常用命令的说明和格式。

<table border="1"><tbody><tr><td><strong>命令</strong></td><td><strong>说明</strong></td><td><strong>格式</strong></td><td><strong>参数说明</strong></td></tr><tr><td>bye</td><td>结束与远程计算机的 FTP 会话并退出 ftp</td><td><strong>bye</strong></td><td>&nbsp;</td></tr><tr><td>cd</td><td>更改远程计算机上的工作目录</td><td><strong>cd</strong>&nbsp;<em>RemoteDirectory</em></td><td><dl><dt><strong><em>RemoteDirectory</em></strong></dt><dd>指定要更改的远程计算机上的目录。</dd></dl></td></tr><tr><td>dir</td><td>显示远程计算机上的目录文件和子目录列表</td><td><strong>dir</strong>&nbsp;[<em>RemoteDirectory</em>] [<em>LocalFile</em>]</td><td><dl><dt><strong><em>RemoteDirectory</em></strong></dt><dd>指定要查看其列表的目录。如果没有指定目录，将使用远程计算机中的当前工作目录。</dd></dl><dl><dt><strong><em>LocalFile</em></strong></dt><dd>指定要存储列表的本地文件。如果没有指定本地文件，则屏幕上将显示结果。</dd></dl>&nbsp;</td></tr><tr><td>get</td><td><p>使用当前文件传输类型将远程文件复制到本地计算机。如果没有指定&nbsp;<em>LocalFile</em>，文件就会赋以&nbsp;<em>RemoteFile</em>&nbsp;名。get&nbsp;命令与&nbsp;recv&nbsp;相同。</p></td><td><strong>get</strong>&nbsp;<em>RemoteFile</em>&nbsp;[<em>LocalFile</em>]</td><td><dl><dt><strong><em>RemoteFile</em></strong></dt><dd>指定要复制的远程文件。</dd></dl><dl><dt><strong><em>LocalFile</em></strong></dt><dd>指定要在本地计算机上使用的文件名。</dd></dl></td></tr><tr><td>lcd</td><td>更改本地计算机上的工作目录。默认情况下，工作目录是启动&nbsp;<strong>ftp</strong>&nbsp;的目录</td><td><strong>lcd</strong>&nbsp;[<em>Directory</em>]</td><td><dl><dt><strong><em>Directory</em></strong></dt><dd>指定要更改的本地计算机上的目录。如果没有指定&nbsp;<em>Directory</em>，将显示本地计算机中的当前工作目录。</dd></dl></td></tr><tr><td>ls</td><td>显示远程目录上的文件和子目录的简短列表</td><td><strong>ls</strong>&nbsp;[<em>RemoteDirectory</em>] [<em>LocalFile</em>]</td><td>&nbsp;<dl><dt><strong><em>RemoteDirectory</em></strong></dt><dd>指定要查看其列表的目录。如果没有指定目录，将使用远程计算机中的当前工作目录。</dd></dl><dl><dt><strong><em>LocalFile</em></strong></dt><dd>指定要存储列表的本地文件。如果没有指定本地文件，将在屏幕上显示结果。</dd></dl></td></tr><tr><td>open</td><td>与指定的 FTP 服务器连接。可以使用 IP 地址或计算机名（两种情况下都必须使用 DNS 服务器或主机文件）指定&nbsp;<em>Computer。</em></td><td><strong>open</strong>&nbsp;<em>Computer</em>&nbsp;[<em>Port</em>]</td><td><dl><dt><strong><em>Computer</em></strong></dt><dd>必需。指定试图要连接的远程计算机。</dd></dl><dl><dt><strong><em>Port</em></strong></dt><dd>指定用于联系 FTP 服务器的 TCP 端口号。默认情况下，使用 TCP 端口号 21。</dd></dl></td></tr><tr><td>put(send)</td><td><p>使用当前文件传输类型将本地文件复制到远程计算机上。<strong>put</strong>&nbsp;命令与&nbsp;<strong>send</strong>&nbsp;命令相同。如果没有指定&nbsp;<em>RemoteFile</em>，文件就会赋以&nbsp;<em>LocalFile</em>&nbsp;名。&nbsp;</p></td><td><strong>put</strong>&nbsp;<em>LocalFile</em>&nbsp;[<em>RemoteFile</em>]</td><td><dl><dt><strong><em>LocalFile</em></strong></dt><dd>指定要复制的本地文件。</dd></dl><dl><dt><strong><em>RemoteFile</em></strong></dt><dd>指定要在远程计算机上使用的名称。</dd></dl></td></tr></tbody></table>

更多详细的说明请看 [http://technet.microsoft.com/zh-cn/library/cc756013(v=ws.10).aspx](http://technet.microsoft.com/zh-cn/library/cc756013(v=ws.10).aspx) 和 [http://blog.csdn.net/chaoqunz/article/details/5973317](http://blog.csdn.net/chaoqunz/article/details/5973317)