# WinRAR的命令行模式用法介绍

1. 最简单的压缩命令：`winrar a asdf.txt.rar asdf.txt` 

​	a的意思是进行压缩动作，后面第一个参数是被压缩后的文件名，后缀当然是rar了，最后面 的参数就是要被压缩的文件名

2. 最简单的解压缩命令：`winrar e asdf.txt.rar` 

​	e的意思是执行解压缩，解压缩的文件是后面这唯一的参数，但是这个e解压缩是把解出来的 文件释放到当前目录下面，与asdf.txt.rar文件并列了，因此，更加实用的是下面的带路径 解压缩。

3. 带路径的解压缩命令`：winrar x asdf.rar` 

​	x的意思是执行带绝对路径解压动作，这会在当前文件夹下创建一个文件夹asdf，把压缩包 里的文件、文件夹不改动结构释放到文件asdf里面，就像我们在winrar的图形界面下看到的 一样。

因工作中要对数据打包，顺便研究了下WinRAR的命令行模式，自己写了些例子，基本用法如下：

测试压缩文件准备：文件夹test_data，内部包含子文件夹，分别存放了一些*.log和*.txt文件。

测试代码如下：

```sh
rem 压缩全部文件，按类型压缩,zip打包
 WinRAR.exe a num_all.zip .\test_data\
 WinRAR.exe a num_txt.zip .\test_data\num*.txt
 WinRAR.exe a num_log.zip .\test_data\num*.log
 
 rem 压缩全部文件，按类型压缩,rar打包
 WinRAR.exe a num_all.rar .\test_data\
 WinRAR.exe a num_txt.rar .\test_data\num*.txt
 WinRAR.exe a num_log.rar .\test_data\num*.log

rem 默认压缩根目录，递归处理子文件夹使用 -r
 WinRAR.exe a -r num_all_tg.zip .\test_data\*.*
 WinRAR.exe a -r num_all_txt.zip .\test_data\*.txt
 WinRAR.exe a -r num_all_log.rar .\test_data\*.log
 
 
rem 添加注释，注释从一个txt文件读取，txt文件名info.txt
 WinRAR.exe c -zinfo.txt num_all.zip
 WinRAR.exe c -zinfo.txt num_all.rar
 
 rem 从压缩包中读取注释，写入到read.txt文件,默认ASCII，参数-scuc表示unicode
  WinRAR.exe cw num_all.zip read1_ASCII.txt
  WinRAR.exe cw num_all.rar read2_ASCII.txt
  WinRAR.exe cw -scuc num_all.rar read_unicode.txt
  
  rem 从压缩包中删除制定文件
  WinRAR.exe d  num_all_tg.zip *.log
 
 rem 解压到当前目录下,不包含压缩包内的路径
 WinRAR.exe e  num_all_tg.zip
 rem 解压到制定目录下,不包含压缩包内的路径
 WinRAR.exe e  num_all_tg.zip .\test_d2
 rem 解压到当前目录下,只解压制定类型的文件
 WinRAR.exe e num_all_tg.zip *.log
 
 rem 给压缩包内的文件重命名
  WinRAR.exe rn num_all_tg.zip num(1).txt  num(1).bak num(2).txt num(2).bak
  WinRAR.exe rn num_all.rar *.txt *.bak
  
 rem 使用压缩包捏的绝对路径解压
  WinRAR.exe x  num_all_tg.zip
 rem 使用压缩包捏的绝对路径解压，解压指定类型文件
  WinRAR.exe x  num_all_tg.zip *.log
 rem 使用压缩包捏的绝对路径解压，解压指定类型文件,并解压到指定文件夹
  WinRAR.exe x  num_all_tg.zip *log  .\new_data\
```

以上为winrar的基本压缩，解压命令。

关于winrar 的开关选项，以后有空再研究整理。

 

备注：WinRAR.exe为安装完后，从安装目录将主程序winRAR.exe拷贝出来即可，该程序是独立可运行的。

以下为winRAR的命令帮助原文：

Alphabetic commands list

------

| [**a**](http://www.cnblogs.com/HELPCmdA.htm)        | add files to an archive                       |
| --------------------------------------------------- | --------------------------------------------- |
| [**c**](http://www.cnblogs.com/HELPCmdC.htm)        | add an archive comment                        |
| [**ch**](http://www.cnblogs.com/HELPCmdCH.htm)      | change archive parameters                     |
| [**cv**](http://www.cnblogs.com/HELPCmdCV.htm)      | convert archives                              |
| [**cw**](http://www.cnblogs.com/HELPCmdCW.htm)      | write an archive comment to file              |
| [**d**](http://www.cnblogs.com/HELPCmdD.htm)        | delete files from an archive                  |
| [**e**](http://www.cnblogs.com/HELPCmdE.htm)        | extract files from an archive, ignoring paths |
| [**f**](http://www.cnblogs.com/HELPCmdF.htm)        | freshen files within an archive               |
| [**i**](http://www.cnblogs.com/HELPCmdI.htm)        | find string in archives                       |
| [**k**](http://www.cnblogs.com/HELPCmdK.htm)        | lock an archive                               |
| [**m**](http://www.cnblogs.com/HELPCmdM.htm)        | move files and folders to an archive          |
| [**r**](http://www.cnblogs.com/HELPCmdR.htm)        | repair a damaged archive                      |
| [**rc**](http://www.cnblogs.com/HELPCmdRC.htm)      | reconstruct missing volumes                   |
| [**rn**](http://www.cnblogs.com/HELPCmdRN.htm)      | rename archived files                         |
| [**rr[N\]**](http://www.cnblogs.com/HELPCmdRR.htm)  | add data recovery record                      |
| [**rv[N\]**](http://www.cnblogs.com/HELPCmdRV.htm)  | create recovery volumes                       |
| [**s[name\]**](http://www.cnblogs.com/HELPCmdS.htm) | convert an archive to a self-extracting type  |
| [**s-**](http://www.cnblogs.com/HELPCmdSm.htm)      | remove SFX module                             |
| [**t**](http://www.cnblogs.com/HELPCmdT.htm)        | test archive files                            |
| [**u**](http://www.cnblogs.com/HELPCmdU.htm)        | update files within an archive                |
| [**x**](http://www.cnblogs.com/HELPCmdX.htm)        | extract files from an archive with full paths |

 

 

**Winrar的命令行模式程序在安装目录下的 rar.exe (打包压缩程序)，unrar.exe(解压缩程序)，以我安装的winrar5.3为例，帮助文档如下：**

 **..\WinRAR\Rar.exe：**

```
 RAR 5.30 beta 2    版权所有 (C) 1993-2015 Alexander Roshal
 试用版本            输入 RAR -? 以获得帮助
 
 用法:rar <命令> -<参数 1> -<参数 N> <压缩文件> <文件...>
                <@列表文件...> <解压路径\>
 
 <命令>
   a             添加文件到压缩文件
   c             添加压缩文件注释
   ch            更改压缩文件参数
   cw            将压缩文件注释写入文件
   d             从压缩文件中删除文件
   e             提取文件无需压缩文件的路径
   f             更新压缩文件里的文件
   i[par]=<str>  查找压缩文件中的字符串
   k             锁定压缩文件
   l[t[a],b]     列出压缩文件内容 [technical[all], bare]
   m[f]          移动到压缩文件 [仅文件]
   p             打印文件到 stdout
   r             修复压缩文件
   rc            重建丢失的分卷
   rn            重命名已压缩文件
   rr[N]         添加数据恢复记录
   rv[N]         创建恢复分卷
   s[name|-]     转换压缩文件为自解压或自解压转换为压缩文件
   t             测试压缩文件
   u             更新压缩文件中的文件
   v[t[a],b]     详细列出压缩文件内容 [technical[all],bare]
   x             使用完整路径提取文件
 
 <参数>
   -             停止参数扫描
   @[+]          禁用 [启用] 文件列表
   ac            压缩或解压后清除存档属性
   ad            添加压缩文件名到目标路径
   ag[格式]      使用当前日期生成压缩文件名
   ai            忽略文件属性
   ao            添加具有压缩属性的文件
   ap<格式>      添加路径到压缩文件中
   as            同步压缩文件内容
   c-            禁用注释显示
   cfg-          禁用读取配置
   cl            转换名称到小写
   cu            转换名称到大写
   df            压缩文件后删除原来的文件
   dh            打开已共享文件
   dr            删除文件到回收站
   ds            对固实压缩文件禁用名称排序
   dw            压缩文件后清除文件
   e[+]<attr>    设置文件排除和包含属性
   ed            不要添加空目录
   en            不要放置 '压缩文件结束' 区块
   ep            从名称里排除路径
   ep1           从名称里排除基目录
   ep2           扩展路径到完整路径
   ep3           扩展路径为完整路径包括驱动器盘符
   f             更新文件
   hp[password]  加密文件数据和文件头
   ht[b|c]       为文件校验和选择哈希类型 [BLAKE2,CRC32]
   id[c,d,p,q]   禁用信息
   ieml[addr]    通过电邮发送压缩文件
   ierr          发送所有消息到 stderr
   ilog[name]    记录错误到文件（仅注册版本）
   inul          禁用所有消息
   ioff          完成操作后关闭电脑
   isnd          启用声音
   k             锁定压缩文件
   kb            保留损坏的已解压缩文件
   log[f][=name] 将名称写入日志文件
   m<0..5>       设置压缩级别(0-存储...3-默认...5-最大)
   ma[4|5]       指定压缩格式的一个版本
   mc<par>       设置高级压缩参数
   md<n>[k,m,g]  字典大小显示为 KB, MB 或 GB
   ms[ext;ext]   指定要存储的文件类型
   mt<threads>   设置线程数
   n<file>       额外的包含过滤器的文件
   n@            从 stdin 读取额外的过滤器掩码
   n@<list>      从列表文件读取额外的过滤器掩码
   o[+|-]        设置覆盖模式
   oc            设置 NTFS 压缩属性
   oh            将硬链接保存为链接而非文件
   oi[0-4][:min] 将完全相同的文件保存为引用
   ol[a]         将符号链接作为链接处理 [绝对路径]
   or            自动重命名文件
   os            保存 NTFS 流
   ow            保存或恢复文件所有者和组
   p[password]   设置密码
   p-            不查询密码
   qo[-|+]       添加快速打开信息 [无|强制]
   r             递归子目录
   r-            禁用递归
   r0            仅为通配符名称递归子目录
   ri<P>[:<S>]   设置优先级 (0-默认,1-最小..15-最大) 和睡眠时间为 ms
   rr[N]         添加数据恢复记录
   rv[N]         创建恢复分卷
   s[<N>,v[-],e] 创建固实压缩文件
   s-            禁用固实压缩文件
   sc<chr>[obj]  指定字符集
   sfx[name]     创建自解压文档
   si[name]      从标准输入 (stdin) 读取数据
   sl<size>      处理小于指定大小的文件
   sm<size>      处理大于指定大小的文件
   t             压缩文件后测试文件
   ta<date>      处理在 <日期> 之后修改过的文件，以 YYYYMMDDHHMMSS 格式
   tb<date>      处理在 <日期> 之前修改过的文件，以 YYYYMMDDHHMMSS 格式
   tk            保存原来的压缩文件时间
   tl            设置压缩文件时间为最新的文件
   tn<time>      处理比 <时间> 较新的文件
   to<time>      处理比 <时间> 较旧的文件
   ts<m,c,a>[N]  保存或恢复文件时间（修改，创建，访问）
   u             更新文件
   v<size>[k,b]  创建分卷大小为=<size>*1000 [*1024, *1]
   vd            创建分卷之前清除磁盘内容
   ver[n]        文件版本控制
   vn            使用旧式的分卷命名方案
   vp            创建每个分卷之前暂停
   w<path>       指定工作目录
   x<file>       排除指定的文件
   x@            读取文件名以从 stdin 排除
   x@<list>      排除指定列表文件里列出的文件
   y             对所有询问假定选择“是”
   z[file]       从文件读取压缩文件注释
```



 

 **..\WinRAR\unRar.exe：**

```
UNRAR 5.30 beta 2 免费软件      版权所有 (C) 1993-2015 Alexander Roshal
用法:unrar <命令> -<参数 1> -<参数 N> <压缩文件> <文件...>
               <@列表文件...> <解压路径\>

<命令>
  e             提取文件无需压缩文件的路径
  l[t[a],b]     列出压缩文件内容 [technical[all], bare]
  p             打印文件到 stdout
  t             测试压缩文件
  v[t[a],b]     详细列出压缩文件内容 [technical[all],bare]
  x             使用完整路径提取文件

<参数>
  -             停止参数扫描
  @[+]          禁用 [启用] 文件列表
  ac            压缩或解压后清除存档属性
  ad            添加压缩文件名到目标路径
  ag[格式]      使用当前日期生成压缩文件名
  ai            忽略文件属性
  ap<格式>      添加路径到压缩文件中
  c-            禁用注释显示
  cfg-          禁用读取配置
  cl            转换名称到小写
  cu            转换名称到大写
  dh            打开已共享文件
  ep            从名称里排除路径
  ep3           扩展路径为完整路径包括驱动器盘符
  f             更新文件
  id[c,d,p,q]   禁用信息
  ierr          发送所有消息到 stderr
  inul          禁用所有消息
  ioff          完成操作后关闭电脑
  kb            保留损坏的已解压缩文件
  n<file>       额外的包含过滤器的文件
  n@            从 stdin 读取额外的过滤器掩码
  n@<list>      从列表文件读取额外的过滤器掩码
  o[+|-]        设置覆盖模式
  oc            设置 NTFS 压缩属性
  ol[a]         将符号链接作为链接处理 [绝对路径]
  or            自动重命名文件
  ow            保存或恢复文件所有者和组
  p[password]   设置密码
  p-            不查询密码
  r             递归子目录
  ri<P>[:<S>]   设置优先级 (0-默认,1-最小..15-最大) 和睡眠时间为 ms
  sc<chr>[obj]  指定字符集
  sl<size>      处理小于指定大小的文件
  sm<size>      处理大于指定大小的文件
  ta<date>      处理在 <日期> 之后修改过的文件，以 YYYYMMDDHHMMSS 格式
  tb<date>      处理在 <日期> 之前修改过的文件，以 YYYYMMDDHHMMSS 格式
  tn<time>      处理比 <时间> 较新的文件
  to<time>      处理比 <时间> 较旧的文件
  ts<m,c,a>[N]  保存或恢复文件时间（修改，创建，访问）
  u             更新文件
  v             列出所有分卷
  ver[n]        文件版本控制
  vp            创建每个分卷之前暂停
  x<file>       排除指定的文件
  x@            读取文件名以从 stdin 排除
  x@<list>      排除指定列表文件里列出的文件
  y             对所有询问假定选择“是”
```

 

Fetty：姓名、性别、年龄、具体职业不详，更无联系方式。 本人在博客园发布的文章（包括但不限于：简体中文、英文、标点符号、图像，以及以上任意组合等）均为敲打键盘、鼠标、屏幕等工具所造成结果，用于检验本人电脑、显示器的各项机械性能、光电性能，并不代表本人观点，如有雷同，不胜荣幸！