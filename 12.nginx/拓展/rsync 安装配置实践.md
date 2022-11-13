

# rsync 安装配置实践

[](#前言 "前言")前言
--------------

Rsync 代表 “remote sync”，它是本地和远程主机文件同步工具。它只同步更改的文件，以此实现最小化传输数据。rsync 的使用场景非常丰富，相信大家会经常使用，这里做下简单的总结。

> rsync 安装配置实践

[](#更新历史 "更新历史")更新历史
--------------------

2019 年 12 月 01 日 - 更新 rsync 常见错误和 lsyncd+rsync 实时同步方案  
2019 年 05 月 23 日 - 更新 rsync 中文帮助和相关实践配置  
2019 年 03 月 01 日 - 初稿

阅读原文 - [https://wsgzao.github.io/post/rsync/](https://wsgzao.github.io/post/rsync/)

**扩展阅读**

rsync - [https://www.samba.org/rsync/](https://www.samba.org/rsync/)

[](#rsync-简介 "rsync 简介")rsync 简介
--------------------------------

rsync is a file transfer program capable of efficient remote update via a fast differencing algorithm.

rsync 是类 unix 系统下的数据镜像备份工具，从软件的命名上就可以看出来了 ——remote sync。它的特性如下：

1.  可以镜像保存整个目录树和文件系统
2.  可以很容易做到保持原来文件的权限、时间、软硬链接等等
3.  无须特殊权限即可安装
4.  优化的流程，文件传输效率高
5.  可以使用 rsh、ssh 等方式来传输文件，当然也可以通过直接的 socket 连接
6.  支持匿名传输

在使用 rsync 进行远程同步时，可以使用两种方式：远程 Shell 方式（用户验证由 ssh 负责）和 C/S 方式（即客户连接远程 rsync 服务器，用户验证由 rsync 服务器负责）。

无论本地同步目录还是远程同步数据，首次运行时将会把全部文件拷贝一次，以后再运行时将只拷贝有变化的文件（对于新文件）或文件的变化部分（对于原有文件）。

[](#rsync-源配置文件示例 "rsync 源配置文件示例")rsync 源配置文件示例
-----------------------------------------------

```
# 编辑 rsync 配置文件 
vim /etc/rsync.conf

# /etc/rsyncd: configuration file for rsync daemon mode

# See rsyncd.conf man page for more options.

# configuration example:

# uid = nobody
# gid = nobody
# use chroot = yes
# max connections = 4
# pid file = /var/run/rsyncd.pid
# exclude = lost+found/
# transfer logging = yes
# timeout = 900
# ignore nonreadable = yes
# dont compress   = *.gz *.tgz *.zip *.z *.Z *.rpm *.deb *.bz2

# [ftp]
#        path = /home/ftp
#        comment = ftp export area
```

rsyncd.conf 官方文档请参考 - [https://www.samba.org/ftp/rsync/rsyncd.conf.html](https://www.samba.org/ftp/rsync/rsyncd.conf.html)

[](#rsync-常用参数 "rsync 常用参数")rsync 常用参数
--------------------------------------

注: 在指定复制源时，路径是否有最后的 “/” 有不同的含义，例如：

/data 表示将整个 /data 目录复制到目标目录  
/data/ 表示将 /data/ 目录中的所有内容复制到目标目录

man rsync 翻译 (rsync 命令中文手册)  
[http://www.cnblogs.com/f-ck-need-u/p/7221713.html](http://www.cnblogs.com/f-ck-need-u/p/7221713.html)

```
rsync is a file transfer program capable of efficient remote update via a fast differencing algorithm.

Usage: rsync [OPTION]... SRC [SRC]... DEST
  or   rsync [OPTION]... SRC [SRC]... [USER@]HOST:DEST
  or   rsync [OPTION]... SRC [SRC]... [USER@]HOST::DEST
  or   rsync [OPTION]... SRC [SRC]... rsync://[USER@]HOST[:PORT]/DEST
  or   rsync [OPTION]... [USER@]HOST:SRC [DEST]
  or   rsync [OPTION]... [USER@]HOST::SRC [DEST]
  or   rsync [OPTION]... rsync://[USER@]HOST[:PORT]/SRC [DEST]
The ':' usages connect via remote shell, while '::' & 'rsync://' usages connect
to an rsync daemon, and require SRC or DEST to start with a module name.

Options
 -v, --verbose               increase verbosity
     --info=FLAGS            fine-grained informational verbosity
     --debug=FLAGS           fine-grained debug verbosity
     --msgs2stderr           special output handling for debugging
 -q, --quiet                 suppress non-error messages
     --no-motd               suppress daemon-mode MOTD (see manpage caveat)
 -c, --checksum              skip based on checksum, not mod-time & size
 -a, --archive               archive mode; equals -rlptgoD (no -H,-A,-X)
     --no-OPTION             turn off an implied OPTION (e.g. --no-D)
 -r, --recursive             recurse into directories
 -R, --relative              use relative path names
     --no-implied-dirs       don't send implied dirs with --relative
 -b, --backup                make backups (see --suffix & --backup-dir)
     --backup-dir=DIR        make backups into hierarchy based in DIR
     --suffix=SUFFIX         set backup suffix (default ~ w/o --backup-dir)
 -u, --update                skip files that are newer on the receiver
     --inplace               update destination files in-place (SEE MAN PAGE)
     --append                append data onto shorter files
     --append-verify         like --append, but with old data in file checksum
 -d, --dirs                  transfer directories without recursing
 -l, --links                 copy symlinks as symlinks
 -L, --copy-links            transform symlink into referent file/dir
     --copy-unsafe-links     only"unsafe"symlinks are transformed
     --safe-links            ignore symlinks that point outside the source tree
     --munge-links           munge symlinks to make them safer (but unusable)
 -k, --copy-dirlinks         transform symlink to a dir into referent dir
 -K, --keep-dirlinks         treat symlinked dir on receiver as dir
 -H, --hard-links            preserve hard links
 -p, --perms                 preserve permissions
 -E, --executability         preserve the file's executability
     --chmod=CHMOD           affect file and/or directory permissions
 -A, --acls                  preserve ACLs (implies --perms)
 -X, --xattrs                preserve extended attributes
 -o, --owner                 preserve owner (super-user only)
 -g, --group                 preserve group
     --devices               preserve device files (super-user only)
     --copy-devices          copy device contents as regular file
     --specials              preserve special files
 -D                          same as --devices --specials
 -t, --times                 preserve modification times
 -O, --omit-dir-times        omit directories from --times
 -J, --omit-link-times       omit symlinks from --times
     --super                 receiver attempts super-user activities
     --fake-super            store/recover privileged attrs using xattrs
 -S, --sparse                handle sparse files efficiently
     --preallocate           allocate dest files before writing them
 -n, --dry-run               perform a trial run with no changes made
 -W, --whole-file            copy files whole (without delta-xfer algorithm)
 -x, --one-file-system       don't cross filesystem boundaries
 -B, --block-size=SIZE       force a fixed checksum block-size
 -e, --rsh=COMMAND           specify the remote shell to use
     --rsync-path=PROGRAM    specify the rsync to run on the remote machine
     --existing              skip creating new files on receiver
     --ignore-existing       skip updating files that already exist on receiver
     --remove-source-files   sender removes synchronized files (non-dirs)
     --del                   an alias for --delete-during
     --delete                delete extraneous files from destination dirs
     --delete-before         receiver deletes before transfer, not during
     --delete-during         receiver deletes during the transfer
     --delete-delay          find deletions during, delete after
     --delete-after          receiver deletes after transfer, not during
     --delete-excluded       also delete excluded files from destination dirs
     --ignore-missing-args   ignore missing source args without error
     --delete-missing-args   delete missing source args from destination
     --ignore-errors         delete even if there are I/O errors
     --force                 force deletion of directories even if not empty
     --max-delete=NUM        don't delete more than NUM files
     --max-size=SIZE         don't transfer any file larger than SIZE
     --min-size=SIZE         don't transfer any file smaller than SIZE
     --partial               keep partially transferred files
     --partial-dir=DIR       put a partially transferred file into DIR
     --delay-updates         put all updated files into place at transfer's end
 -m, --prune-empty-dirs      prune empty directory chains from the file-list
     --numeric-ids           don't map uid/gid values by user/group name
     --usermap=STRING        custom username mapping
     --groupmap=STRING       custom groupname mapping
     --chown=USER:GROUP      simple username/groupname mapping
     --timeout=SECONDS       set I/O timeout in seconds
     --contimeout=SECONDS    set daemon connection timeout in seconds
 -I, --ignore-times          don't skip files that match in size and mod-time
 -M, --remote-option=OPTION  send OPTION to the remote side only
     --size-only             skip files that match in size
     --modify-window=NUM     compare mod-times with reduced accuracy
 -T, --temp-dir=DIR          create temporary files in directory DIR
 -y, --fuzzy                 find similar file for basis if no dest file
     --compare-dest=DIR      also compare destination files relative to DIR
     --copy-dest=DIR         ... and include copies of unchanged files
     --link-dest=DIR         hardlink to files in DIR when unchanged
 -z, --compress              compress file data during the transfer
     --compress-level=NUM    explicitly set compression level
     --skip-compress=LIST    skip compressing files with a suffix in LIST
 -C, --cvs-exclude           auto-ignore files the same way CVS does
 -f, --filter=RULE           add a file-filtering RULE
 -F                          same as --filter='dir-merge /.rsync-filter'
                             repeated: --filter='- .rsync-filter'
     --exclude=PATTERN       exclude files matching PATTERN
     --exclude-from=FILE     read exclude patterns from FILE
     --include=PATTERN       don't exclude files matching PATTERN
     --include-from=FILE     read include patterns from FILE
     --files-from=FILE       read list of source-file names from FILE
 -0, --from0                 all *-from/filter files are delimited by 0s
 -s, --protect-args          no space-splitting; only wildcard special-chars
     --address=ADDRESS       bind address for outgoing socket to daemon
     --port=PORT             specify double-colon alternate port number
     --sockopts=OPTIONS      specify custom TCP options
     --blocking-io           use blocking I/O for the remote shell
     --stats                 give some file-transfer stats
 -8, --8-bit-output          leave high-bit chars unescaped in output
 -h, --human-readable        output numbers in a human-readable format
     --progress              show progress during transfer
 -P                          same as --partial --progress
 -i, --itemize-changes       output a change-summary for all updates
     --out-format=FORMAT     output updates using the specified FORMAT
     --log-file=FILE         log what we're doing to the specified FILE
     --log-file-format=FMT   log updates using the specified FMT
     --password-file=FILE    read daemon-access password from FILE
     --list-only             list the files instead of copying them
     --bwlimit=RATE          limit socket I/O bandwidth
     --outbuf=N|L|B          set output buffering to None, Line, or Block
     --write-batch=FILE      write a batched update to FILE
     --only-write-batch=FILE like --write-batch but w/o updating destination
     --read-batch=FILE       read a batched update from FILE
     --protocol=NUM          force an older protocol version to be used
     --iconv=CONVERT_SPEC    request charset conversion of filenames
     --checksum-seed=NUM     set block/file checksum seed (advanced)
 -4, --ipv4                  prefer IPv4
 -6, --ipv6                  prefer IPv6
     --version               print version number
(-h) --help                  show this help (-h is --help only if used alone)

Use"rsync --daemon --help"to see the daemon-mode command-line options.
Please see the rsync(1) and rsyncd.conf(5) man pages for full documentation.
See http://rsync.samba.org/ for updates, bug reports, and answers
```

```
# rsync 常用参数 
-v : 展示详细的同步信息 
-a : 归档模式，相当于 -rlptgoD
    -r : 递归目录 
    -l : 同步软连接文件 
    -p : 保留权限 
    -t : 将源文件的 < span 同步到目标机器 
    -g : 保持文件属组 
    -o : 保持文件属主 
    -D : 和 --devices --specials 一样，保持设备文件和特殊文件 
-z : 发送数据前，先压缩再传输 
-H : 保持硬链接 
-n : 进行试运行，不作任何更改 
-P same as --partial --progress
    --partial : 支持断点续传 
    --progress : 展示传输的进度 
--delete : 如果源文件消失，目标文件也会被删除 
--delete-excluded : 指定要在目的端删除的文件 
--delete-after : 默认情况下，rsync 是先清理目的端的文件再开始数据同步；如果使用此选项，则 rsync 会先进行数据同步，都完成后再删除那些需要清理的文件。
--exclude=PATTERN : 排除匹配 PATTERN 的文件 
--exclude-from=FILE : 如果要排除的文件很多，可以统一写在某一文件中 
-e ssh : 使用 SSH 加密隧道传输 

# 远程 Shell 方式 
rsync [OPTION]... SRC [SRC]... [USER@]HOST:DEST # 执行 “推” 操作 
or   rsync [OPTION]... [USER@]HOST:SRC [DEST]   # 执行 “拉” 操作 

# 远程 C/S 方式 
rsync [OPTION]... SRC [SRC]... [USER@]HOST::DEST                    # 执行 “推” 操作 
or   rsync [OPTION]... SRC [SRC]... rsync://[USER@]HOST[:PORT]/DEST # 执行 “推” 操作 
or   rsync [OPTION]... [USER@]HOST::SRC [DEST]                      # 执行 “拉” 操作 
or   rsync [OPTION]... rsync://[USER@]HOST[:PORT]/SRC [DEST]        # 执行 “拉” 操作
```

[](#rsync-同步方式 "rsync 同步方式")rsync 同步方式
--------------------------------------

Rsync 远程同步主要有两种方式：使用远程 shell（ssh 或 rsh） 或使用 rsync 的 daemon 方式

rsync 命令和 ssh，scp 命令有点相似。

我们创建两个测试目录和一些文件：

```
mkdir dir1
mkdir dir2
touch dir1/somefile{1..100}
# dir1 中有 100 文件，dir2 中为空。使用 rsync 把 dir1 内容同步到 dir2，-r 选项代表递归，在同步目录时使用。
rsync -r dir1/ dir2
# 你也可以使用 -a 选项，代表同步所有，包括修改时间、群组、权限、特殊文件、也包括递归。
rsync -anv dir1/ dir2
# 注意上面的 dir1 / 中的 “/” 不能少，它代表同步目录下文件， 如果没有 “/” 代表同步这个目录。

# 和远程主机进行同步目录首先，你要确保有远程主机的 SSH 访问权限 

# 把本地目录同步到远程主机：
rsync -a dir1/ root@linux:~/dir2
# 把远程主机目录同步到本地：
rsync -a root@linux:~/dir2/ dir1
```

### [](#本地文件同步 "本地文件同步")本地文件同步

```
# 如果没有 desc 目录，会自动创建 
rsync -avH /opt/resource/ /tmp/desc/
```

### [](#远程文件同步-–shell-方式 "远程文件同步 –shell 方式")远程文件同步 –shell 方式

```
# 从本地传到远端，目标文件会被写成 ssh 登录用户的属组和属主（如下 www）
rsync -avH /opt/nginx-1.12.1/ www@172.18.50.125:/tmp/nginx/

# 使用 ssh 加密隧道方式传输，保障数据的安全性 
rsync -avHe ssh /opt/nginx-1.12.1/ www@172.18.50.125:/tmp/nginx/

# 从远端传到本地，只要对目标文件有读的权限，就可以同步到本地 
rsync -avH www@172.18.50.125:/tmp/nginx/ /tmp/nginx/

# 如果远程服务器 ssh 端口不是默认的 22
rsync -avHe "ssh -p 11222" /opt/nginx-1.12.1/ www@172.18.50.125:/tmp/nginx/
```

### [](#远程文件同步-–daemon-方式 "远程文件同步 –daemon 方式")远程文件同步 –daemon 方式

> rsync 服务端配置

```
# 创建 rsync 服务的目录和配置文件 (可选)
mkdir /etc/rsync 
cd /etc/rsync
touch rsyncd.conf
touch rsyncd.secrets
touch rsyncd.motd
chmod 600 rsyncd.secrets

### rsyncd.conf 文件的配置 
vim /etc/rsync/rsyncd.conf
# /etc/rsyncd: configuration file for rsync daemon mode
# See rsyncd.conf man page for more options.
# 传输文件使用的用户和用户组，如果是从服务器 => 客户端，要保证 www 用户对文件有读取的权限；如果是从客户端 => 服务端，要保证 www 对文件有写权限。
uid = www
gid = www
# 允许 chroot，提升安全性，客户端连接模块，首先 chroot 到模块 path 参数指定的目录下，chroot 为 yes 时必须使用 root 权限，且不能备份 path 路径外的链接文件 
use chroot = yes
# 只读 
read only = no
# 只写 
write only = no
# 设定白名单，可以指定 IP 段（172.18.50.1/255.255.255.0）, 各个 Ip 段用空格分开 
hosts allow = 172.18.50.110 172.18.50.111
hosts deny = *
# 允许的客户端最大连接数 
max connections = 4
# 欢迎文件的路径，非必须 
motd file = /etc/rsync/rsyncd.motd
# pid 文件路径 
pid file = /var/run/rsyncd.pid
# 记录传输文件日志 
transfer logging = yes
# 日志文件格式 
log format = %t %a %m %f %b
# 指定日志文件 
log file = /var/log/rsync.log
# 剔除某些文件或目录，不同步 
exclude = lost+found/
# 设置超时时间 
timeout = 900
ignore nonreadable = yes
# 设置不需要压缩的文件 
dont compress   = *.gz *.tgz *.zip *.z *.Z *.rpm *.deb *.bz2

# 模块，可以配置多个，使用如: sate@172.18.50.125::125to110
[125to110]
# 模块的根目录，同步目录，要注意权限 
path = /tmp/nginx
# 是否允许列出模块内容 
list = no
# 忽略错误 
ignore errors
# 添加注释 
comment = ftp export area
# 模块验证的用户名称，可使用空格或者逗号隔开多个用户名 
auth users = sate
# 模块验证密码文件 可放在全局配置里 
secrets file = /etc/rsync/rsyncd.secrets
# 剔除某些文件或目录，不同步 
exclude = lost+found/ conf/ man/

### rsyncd.secrets 文件的配置 
cat rsyncd.secrets 
# 用户名: 密码 
sate:111111

### rsync 启动 
rsync --daemon --config=/etc/rsync/rsyncd.conf
```

> rsync 客户端配置

```
# 从 服务端 => 客户端 同步数据，会提示输入密码 
rsync -avzP --delete sate@172.18.50.125::125to110 /tmp/sync/

# 从 客户端 => 服务端 同步数据，会提示输入密码 
rsync -avzP --delete /tmp/sync/ sate@172.18.50.125::125to110

# 注: 如果是 /tmp/sync，则同步 sync 目录；如果 /tmp/sync/，则同步 sync 目录下的文件 

# 免密码同步，将密码写到文件，再通过 --password-file 指定该文件，注：该文件的权限必须是 600
echo "111111" > /tmp/secrets.file
chmod 600 /tmp/secrets.file
rsync -avzP --delete --password-file=/tmp/secrets.file sate@172.18.50.125::125to110 /tmp/sync/

# --exclude 排除文件目录时，如果有多个同名目录的情况 
# 目录结构 
tree
.
├── dir1
│   └── test
│       ├── 3.file
│       ├── 4.file
│       └── 5.file
├── dir2
└── test
    ├── 1.file
    ├── 2.file
    └── 3.file

# 情况一 ： 排除 /test 目录，同步其他目录（同步的是 / tmp/sync/ 下边的文件）
rsync -avP --delete --password-file=/tmp/secrets.file --exclude=test  /tmp/sync/ sate@172.18.50.125::125to110 

# 会发现，该目录下所有 test 目录都被排除了，如果想只排除第一层目录的 test，可以如下（/ 代表所同步目录第一层）：
rsync -avP --delete --password-file=/tmp/secrets.file --exclude=/test/  /tmp/sync/ sate@172.18.50.125::125to110 

# 情况二 ： 和情况一不同的是 同步的 /tmp/sync 这个目录（同步的是 / tmp/sync 目录本身，导致 exclude 后边的参数也会变化）
rsync -avP --delete --password-file=/tmp/secrets.file --exclude=/sync/test/  /tmp/sync sate@172.18.50.125::125to110
```

[](#rsync-简化配置实践 "rsync 简化配置实践")rsync 简化配置实践
--------------------------------------------

```
# 配置服务端 rsyncd.conf
vim /etc/rsyncd.conf

read only = no
list = yes
uid = root
gid = root

[backup]
path= /data/
hosts allow = 10.71.12.0/23

# 设置服务 
systemctl start rsyncd
systemctl enable rsyncd

# 配置 rsync 客户端 

# 编辑 backup.sh 同步脚本 
vim backup.sh

#!/bin/sh
SOURCE=$1
DEST=$2

CMD="rsync -ravz --bwlimit=2000 $1 rsync://{{log_server_ip}}:873/backup/$2"

PROCS=$(pgrep -f "{{log_server_ip}}:873/backup/$2")

if [ "x" != "x$PROCS" ]; then
       echo "not finished"
       exit
fi

$CMD

# 修改 crontab
vim /etc/crontab
15 * * * * root cd /opt/scripts/ && ./backup.sh /var/log/tmp/ 10.71.12.89/$(date +\%Y-\%m)
```

[](#rsync-有用的选项和注意事项 "rsync 有用的选项和注意事项")rsync 有用的选项和注意事项
--------------------------------------------------------

-n 选项，方便你执行 rsync 命令前预览结果，不会真正执行

```
$ rsync -n --delete -r . machineB:/home/userB/
deleting superman/xxx
deleting main.c
deleting acclink
```

–delete 选项，如果源端没有此文件，那么目的端也别想拥有，删除之。

```
rsync -aP --delete source dest


```

-P 选项非常有用，它是 -progress 和 -partial 的组合。第一个选项是用来显示传输进度条，第二个选项允许断点续传和增量传输：

```
rsync -aP source dest


```

-z 选项，压缩传输的文件，对于已经压缩过的文件不建议添加该参数，针对大文件压缩包传输效率会降低 8 倍，且消耗 CPU

```
rsync -az source dest


```

–bwlimit 选项，限制传输带宽，参数值的默认单位是 KBPS，也就是每秒多少 KB

```
rsync -avzP --bwlimit=100 source dest


```

–remove-source-files 该选项告诉 rsync 移除 sender 端已经成功传输到 receiver 端的文件 (不包括任何目录文件)。

```
rsync -avP --remove-source-files source dest


```

rsync 过滤模块比较复杂，建议参考官网和 Google 实例测试  
–exclude 选项如果只有前者可以单独使用，多个规则需要写多条  
–include 选项通常需要配合 –exclude 同时使用  
过滤规则比较多可以写在文件里读取，由于 rsync 不支持正则表达式，复杂的逻辑建议先使用 bash shell 过滤

Basically, first include all directories, then exclude all files.

```
rsync -a --include='*/' --exclude='*' source/ destination/


```

[https://rsync.samba.org/documentation.html](https://rsync.samba.org/documentation.html)

```
mkdir -p /data/test_backup/gop-live-msdk_api
cd data/test_backup/
touch -t 201912020808.20 test1202.txt
touch -t 201912010808.20 test1201.txt
touch -t 201911300808.20 test1130.txt
touch -t 201911290808.20 test1129.txt
touch -t 201911280808.20 test1128.txt

cd data/test_backup/gop-live-msdk_api
touch gop-live-msdk_api-data-2019-12-02_00103  
touch gop-live-msdk_api-data_current
touch gop-live-msdk_api-error-2019-12-02_00103  
touch gop-live-msdk_api-error_current
touch gop-live-msdk_api-data-2019-11-30.tgz

vim /data/exclude.txt
*_0*
*_current

vim /data/rsync_backup_mirror_xxx.sh

#!/bin/bash
var_src="/data/test_backup/"
var_des="rsync://xxx:873/backup/"
#rsync -aP --delete --exclude-from '/data/exclude.txt' ${var_src} ${var_des}
rsync -aP --exclude-from '/data/exclude.txt' ${var_src} ${var_des}
```

[](#rsync-常见错误 "rsync 常见错误")rsync 常见错误
--------------------------------------

```
rsync: failed to connect to 192.168.205.135 (192.168.205.135): No route to host (113)
rsync error: error in socket IO (code 10) at clientserver.c(125) [Receiver=3.1.2]
```

解决：防火墙问题，放行端口或直接关闭

```
@ERROR: auth failed on module rsync_test
rsync error: error starting client-server protocol (code 5) at main.c(1648) [Receiver=3.1.2]
```

解决：用户名与密码问题或模块问题，检查用户名与密码是否匹配，服务器端模块是否存在

```
rsync: read error: Connection reset by peer (104)
rsync error: error in rsync protocol data stream (code 12) at io.c(759) [Receiver=3.1.2]
```

解决：服务器端配置文件 /etc/rsyncd.conf 问题，检查配置文件参数是否出错

[](#参考文章 "参考文章")参考文章
--------------------

> Rsync+Inotify 文件自动同步，推荐使用 lsyncd+rsync

[sersync 基于 rsync+inotify 实现数据实时同步](https://wsgzao.github.io/post/sersync/)

[CentOS7 部署 lsyncd+rsync 实现服务器文件实时同步](https://www.xiebruce.top/919.html)

[rsync (一)：基本命令和用法](https://www.cnblogs.com/f-ck-need-u/p/7220009.html)

[Rsync(Remote Sync)：10 个实用的例子](https://gist.github.com/ilife5/8c5ba280c0c4f84db78a)

[rsync 的使用方法](https://segmentfault.com/a/1190000015669114)