# 第02章_MySQL的数据目录

## 1. MySQL8的主要目录结构

```bash
[root@atguigu01 ~]$ find / -name mysql

/etc/logrotate.d/mysql
/etc/selinux/targeted/active/modules/100/mysql
/var/lib/mysql   # 数据目录
/var/lib/mysql/mysql
/usr/bin/mysql
/usr/lib/python2.7/site-packages/ansible/modules/database/mysql
/usr/lib64/mysql
```

### 1.1 数据库文件的存放路径

> MySQL数据库文件的存放路径：`/var/lib/mysql/`

```mysql
mysql> show variables like 'datadir';
+---------------+-----------------+
| Variable_name | Value           |
+---------------+-----------------+
| datadir       | /var/lib/mysql/ |
+---------------+-----------------+
1 row in set (0.00 sec)
```

从结果中可以看出，在我的计算机上MySQL的数据目录就是`/var/lib/mysql/`

### 1.2 相关命令目录

**相关命令目录：`/usr/bin`（mysqladmin、mysqlbinlog、mysqldump等命令）和/`usr/sbin`。**

```bash
[root@localhost bin]$ cd /usr/bin/
[root@localhost bin]$ find . -name mysqladmin
./mysqladmin
[root@localhost bin]$ find . -name mysqldump
./mysqldump
```

### 1.3 配置文件目录

配置文件目录：`/usr/share/mysql-8.0`（命令及配置文件），`/etc/mysql`（如my.cnf）

```bash
[root@localhost bin]$ cd /usr/share/mysql-8.0/
[root@localhost mysql-8.0]# ll
总用量 976
drwxr-xr-x. 2 root root     24 1月  16 21:34 bulgarian
drwxr-xr-x. 2 root root   4096 1月  16 21:34 charsets
drwxr-xr-x. 2 root root     24 1月  16 21:34 czech
drwxr-xr-x. 2 root root     24 1月  16 21:34 danish
-rw-r--r--. 1 root root  25575 4月  23 2021 dictionary.txt
drwxr-xr-x. 2 root root     24 1月  16 21:34 dutch
drwxr-xr-x. 2 root root     24 1月  16 21:34 english
drwxr-xr-x. 2 root root     24 1月  16 21:34 estonian
drwxr-xr-x. 2 root root     24 1月  16 21:34 french
drwxr-xr-x. 2 root root     24 1月  16 21:34 german
drwxr-xr-x. 2 root root     24 1月  16 21:34 greek
drwxr-xr-x. 2 root root     24 1月  16 21:34 hungarian
-rw-r--r--. 1 root root   3999 4月  23 2021 innodb_memcached_config.sql
-rw-r--r--. 1 root root   2216 4月  24 2021 install_rewriter.sql
drwxr-xr-x. 2 root root     24 1月  16 21:34 italian
drwxr-xr-x. 2 root root     24 1月  16 21:34 japanese
drwxr-xr-x. 2 root root     24 1月  16 21:34 korean
-rw-r--r--. 1 root root 608148 4月  23 2021 messages_to_clients.txt
-rw-r--r--. 1 root root 339567 4月  23 2021 messages_to_error_log.txt
-rw-r--r--. 1 root root   1977 4月  24 2021 mysql-log-rotate
drwxr-xr-x. 2 root root     24 1月  16 21:34 norwegian
drwxr-xr-x. 2 root root     24 1月  16 21:34 norwegian-ny
drwxr-xr-x. 2 root root     24 1月  16 21:34 polish
drwxr-xr-x. 2 root root     24 1月  16 21:34 portuguese
drwxr-xr-x. 2 root root     24 1月  16 21:34 romanian
drwxr-xr-x. 2 root root     24 1月  16 21:34 russian
drwxr-xr-x. 2 root root     24 1月  16 21:34 serbian
drwxr-xr-x. 2 root root     24 1月  16 21:34 slovak
drwxr-xr-x. 2 root root     24 1月  16 21:34 spanish
drwxr-xr-x. 2 root root     24 1月  16 21:34 swedish
drwxr-xr-x. 2 root root     24 1月  16 21:34 ukrainian
-rw-r--r--. 1 root root   1248 4月  24 2021 uninstall_rewriter.sql
```

## 2. 数据库和文件系统的关系

### 2.1 查看默认数据库

查看一下在我的计算机上当前有哪些数据库：

```mysql
mysql> SHOW DATABASES;
```

可以看到有4个数据库是属于MySQL自带的系统数据库。

+ `mysql`
  MySQL 系统自带的核心数据库，它存储了MySQL的用户账户和权限信息，一些存储过程、事件的定义信息，一些运行过程中产生的日志信息，一些帮助信息以及时区信息等。
  
+ `information_schema`
  MySQL 系统自带的数据库，这个数据库保存着MySQL服务器`维护的所有其他数据库的信息`，比如有哪些表、哪些视图、哪些触发器、哪些列、哪些索引。这些信息并不是真实的用户数据，而是一些描述性信息，有时候也称之为`元数据`。在系统数据库`information_schema` 中提供了一些以`innodb_sys` 开头的表，用于表示内部系统表。

  ```mysql
  mysql> USE information_schema;
  Database changed
  mysql> SHOW TABLES LIKE 'innodb_sys%';
  +--------------------------------------------+
  | Tables_in_information_schema (innodb_sys%) |
  +--------------------------------------------+
  | INNODB_SYS_DATAFILES |
  | INNODB_SYS_VIRTUAL |
  | INNODB_SYS_INDEXES |
  | INNODB_SYS_TABLES |
  | INNODB_SYS_FIELDS |
  | INNODB_SYS_TABLESPACES |
  | INNODB_SYS_FOREIGN_COLS |
  | INNODB_SYS_COLUMNS |
  | INNODB_SYS_FOREIGN |
  | INNODB_SYS_TABLESTATS |
  +--------------------------------------------+
  10 rows in set (0.00 sec)
  ```

+ `performance_schema`
  MySQL 系统自带的数据库，这个数据库里主要保存MySQL服务器运行过程中的一些状态信息，可以用来`监控 MySQL 服务的各类性能指标`。包括统计最近执行了哪些语句，在执行过程的每个阶段都花费了多长时间，内存的使用情况等信息。

+ `sys`
  MySQL 系统自带的数据库，这个数据库主要是通过`视图`的形式把i`nformation_schema` 和`performance_schema` 结合起来，帮助系统管理员和开发人员监控 MySQL 的技术性能。

### 2.2 数据库在文件系统中的表示

```bash
[root@localhost mysql-8.0]$ cd /var/lib/mysql
[root@localhost mysql]$ ll
总用量 190288
drwxr-x---. 2 mysql mysql        6 1月  16 23:59 atguigudb
-rw-r-----. 1 mysql mysql       56 1月  16 21:38 auto.cnf
-rw-r-----. 1 mysql mysql   397577 1月  17 00:00 binlog.000001
-rw-r-----. 1 mysql mysql       16 1月  16 21:38 binlog.index
drwxr-x---. 2 mysql mysql       85 1月  16 23:59 book
-rw-------. 1 mysql mysql     1680 1月  16 21:38 ca-key.pem
-rw-r--r--. 1 mysql mysql     1112 1月  16 21:38 ca.pem
-rw-r--r--. 1 mysql mysql     1112 1月  16 21:38 client-cert.pem
-rw-------. 1 mysql mysql     1680 1月  16 21:38 client-key.pem
drwxr-x---. 2 mysql mysql     4096 1月  17 00:00 guli
-rw-r-----. 1 mysql mysql   196608 1月  17 00:03 #ib_16384_0.dblwr
-rw-r-----. 1 mysql mysql  8585216 1月  16 21:38 #ib_16384_1.dblwr
-rw-r-----. 1 mysql mysql     5929 1月  16 21:38 ib_buffer_pool
-rw-r-----. 1 mysql mysql 12582912 1月  17 00:01 ibdata1
-rw-r-----. 1 mysql mysql 50331648 1月  17 00:03 ib_logfile0
-rw-r-----. 1 mysql mysql 50331648 1月  16 21:38 ib_logfile1
-rw-r-----. 1 mysql mysql 12582912 1月  16 21:38 ibtmp1
drwxr-x---. 2 mysql mysql      187 1月  16 21:38 #innodb_temp
drwxr-x---. 2 mysql mysql     4096 1月  16 23:25 mysql
-rw-r-----. 1 mysql mysql 26214400 1月  17 00:01 mysql.ibd
srwxrwxrwx. 1 mysql mysql        0 1月  16 21:38 mysql.sock
-rw-------. 1 mysql mysql        5 1月  16 21:38 mysql.sock.lock
drwxr-x---. 2 mysql mysql     8192 1月  16 21:38 performance_schema
-rw-------. 1 mysql mysql     1680 1月  16 21:38 private_key.pem
-rw-r--r--. 1 mysql mysql      452 1月  16 21:38 public_key.pem
-rw-r--r--. 1 mysql mysql     1112 1月  16 21:38 server-cert.pem
-rw-------. 1 mysql mysql     1676 1月  16 21:38 server-key.pem
drwxr-x---. 2 mysql mysql       28 1月  16 21:38 sys
-rw-r-----. 1 mysql mysql 16777216 1月  17 00:03 undo_001
-rw-r-----. 1 mysql mysql 16777216 1月  17 00:03 undo_002

```

这个数据目录下的文件和子目录比较多，除了`information_schema` 这个系统数据库外，其他的数据库在数据目录下都有对应的子目录。
以我的`temp 数据库`为例，在MySQL5.7 中打开：

```bash
[root@atguigu02 mysql]# cd ./temp
[root@atguigu02 temp]# ll
总用量 1144
-rw-r-----. 1 mysql mysql 8658 8月 18 11:32 countries.frm
-rw-r-----. 1 mysql mysql 114688 8月 18 11:32 countries.ibd
-rw-r-----. 1 mysql mysql 61 8月 18 11:32 db.opt   # 数据库相关信息：字符集 比较规则等
-rw-r-----. 1 mysql mysql 8716 8月 18 11:32 departments.frm
-rw-r-----. 1 mysql mysql 147456 8月 18 11:32 departments.ibd
-rw-r-----. 1 mysql mysql 3017 8月 18 11:32 emp_details_view.frm
-rw-r-----. 1 mysql mysql 8982 8月 18 11:32 employees.frm
-rw-r-----. 1 mysql mysql 180224 8月 18 11:32 employees.ibd
-rw-r-----. 1 mysql mysql 8660 8月 18 11:32 job_grades.frm
-rw-r-----. 1 mysql mysql 98304 8月 18 11:32 job_grades.ibd
-rw-r-----. 1 mysql mysql 8736 8月 18 11:32 job_history.frm
-rw-r-----. 1 mysql mysql 147456 8月 18 11:32 job_history.ibd
-rw-r-----. 1 mysql mysql 8688 8月 18 11:32 jobs.frm
-rw-r-----. 1 mysql mysql 114688 8月 18 11:32 jobs.ibd
-rw-r-----. 1 mysql mysql 8790 8月 18 11:32 locations.frm
-rw-r-----. 1 mysql mysql 131072 8月 18 11:32 locations.ibd
-rw-r-----. 1 mysql mysql 8614 8月 18 11:32 regions.frm
-rw-r-----. 1 mysql mysql 114688 8月 18 11:32 regions.ibd
```

在MySQL8.0中打开：

```bash
[root@atguigu01 mysql]# cd ./temp
[root@atguigu01 temp]# ll
总用量 1080
-rw-r-----. 1 mysql mysql 131072 7月 29 23:10 countries.ibd
-rw-r-----. 1 mysql mysql 163840 7月 29 23:10 departments.ibd
-rw-r-----. 1 mysql mysql 196608 7月 29 23:10 employees.ibd
-rw-r-----. 1 mysql mysql 114688 7月 29 23:10 job_grades.ibd
-rw-r-----. 1 mysql mysql 163840 7月 29 23:10 job_history.ibd
-rw-r-----. 1 mysql mysql 131072 7月 29 23:10 jobs.ibd
-rw-r-----. 1 mysql mysql 147456 7月 29 23:10 locations.ibd
-rw-r-----. 1 mysql mysql 131072 7月 29 23:10 regions.ibd
```

### 2.3 表在文件系统中的表示

#### 2.3.1 InnoDB存储引擎模式

**1 、表结构**
为了保存表结构， `InnoDB` 在`数据目录`下对应的数据库子目录下创建了一个专门用于`描述表结构的文件`，文件名是这样：

```bash
表名.frm   #描述表结构的文件
```

比方说我们在atguigu 数据库下创建一个名为test 的表：

```mysql
mysql> USE atguigu;
Database changed
mysql> CREATE TABLE test (
-> 		c1 INT
-> 		);
Query OK, 0 rows affected (0.03 sec)
```

那在数据库`atguigu` 对应的子目录下就会创建一个名为`test.frm` 的用于描述表结构的文件。.frm文件的格式在不同的平台上都是相同的。这个后缀名为.frm是以`二进制格式`存储的，我们直接打开是乱码的。

**2、表中数据和索引**
**① 系统表空间（system tablespace）**
默认情况下，InnoDB会在数据目录下创建一个名为`ibdata1`【`/var/lib/mysql/ibdata1`】 、大小为`12M` 的文件，这个文件就是对应的系统表空间在文件系统上的表示。

> 怎么才12M？注意这个文件是`自扩展文件`，当不够用的时候它会自己增加文件大小。

当然，如果你想让系统表空间对应文件系统上多个实际文件，或者仅仅觉得原来的`ibdata1` 这个文件名难听，那可以在MySQL启动时配置对应的文件路径以及它们的大小，比如我们这样修改一下my.cnf 配置文件：

```properties
[server]
innodb_data_file_path=data1:512M;data2:512M:autoextend
```

**② 独立表空间(file-per-table tablespace)**

在MySQL5.6.6以及之后的版本中，*InnoDB并不会默认的把各个表的数据存储到系统表空间中*，而是为`每一个表建立一个独立表空间`，也就是说我们创建了多少个表，就有多少个独立表空间。使用独立表空间来存储表数据的话，会在该表所属数据库对应的子目录下创建一个表示该独立表空间的文件，文件名和表名相同，只不过添加了一个`.ibd` 的扩展名而已，所以完整的文件名称长这样：

```
表名.ibd
```

>  比如：我们使用了独立表空间去存储`atguigu 数据库下`的`test表`的话，那么在该表所在数据库对应的atguigu 目录下会为test 表创建这两个文件：
>
> ```bash
> test.frm   # 表结构
> test.ibd   # 数据和索引
> ```
>
> 其中`test.ibd` 文件就用来存储test 表中的数据和索引。

**③ 系统表空间与独立表空间的设置**

我们可以自己指定使用`系统表空间`还`是独立表空间`来存储数据，这个功能由启动参数`innodb_file_per_table` 控制，比如说我们想刻意将表数据都存储到`系统表空间`时，可以在启动MySQL服务器的时候这样配置：

```properties
[server]
innodb_file_per_table=0 # 0：代表使用系统表空间； 1：代表使用独立表空间
```

默认情况：

```mysql
mysql> show variables like 'innodb_file_per_table';
+-----------------------+-------+
| Variable_name 		| Value |
+-----------------------+-------+
| innodb_file_per_table | ON 	|
+-----------------------+-------+
1 row in set (0.01 sec)
```

**④ 其他类型的表空间**

随着MySQL的发展，除了上述两种老牌表空间之外，现在还新提出了一些不同类型的表空间，比如通用表空间（general tablespace）、临时表空间（temporary tablespace）等。

#### 2.3.2 MyISAM存储引擎模式

**1、表结构**
在存储表结构方面， `MyISAM` 和`InnoDB` 一样，也是在数据目录下对应的数据库子目录下创建了一个专门用于描述表结构的文件：

```
表名.frm
```

**2、表中数据和索引**
在MyISAM中的索引全部都是`二级索引`，该存储引擎的`数据和索引是分开存放`的。所以在文件系统中也是使用不同的文件来存储数据文件和索引文件，同时表数据都存放在对应的数据库子目录下。假如`test`表使用MyISAM存储引擎的话，那么在它所在数据库对应的`atguigu` 目录下会为`test` 表创建这三个文件：

```bash
test.frm  # 存储表结构
test.MYD  # 存储数据 (MYData)
test.MYI  # 存储索引 (MYIndex)
```

举例：创建一个`MyISAM` 表，使用`ENGINE` 选项显式指定引擎。因为`InnoDB` 是默认引擎。

```mysql
CREATE TABLE `student_myisam` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR ( 64 ) DEFAULT NULL,
	`age` INT DEFAULT NULL,
	`sex` VARCHAR ( 2 ) DEFAULT NULL,
PRIMARY KEY ( `id` ) 
) ENGINE = MYISAM AUTO_INCREMENT = 0 DEFAULT CHARSET = utf8mb3;
```

```bash
# ---------------------8.0
[root@localhost testdb]$ pwd
/var/lib/mysql/testdb
[root@localhost testdb]$ ll
总用量 12
-rw-r-----. 1 mysql mysql 4329 1月  18 01:07 student_myisam_417.sdi  #描述表结构文件，字段长度
-rw-r-----. 1 mysql mysql    0 1月  18 01:07 student_myisam.MYD
-rw-r-----. 1 mysql mysql 1024 1月  18 01:07 student_myisam.MYI

# ---------------------5.7
[root@mysql5 testdb]# pwd
/var/lib/mysql/testdb
[root@mysql5 testdb]# ll
总用量 20
-rw-r-----. 1 mysql mysql   61 1月  18 01:25 db.opt
-rw-r-----. 1 mysql mysql 8642 1月  18 01:25 student_myisam.frm
-rw-r-----. 1 mysql mysql    0 1月  18 01:25 student_myisam.MYD
-rw-r-----. 1 mysql mysql 1024 1月  18 01:25 student_myisam.MYI

```



### 2.4 小结

举例： `数据库a` ， `表b` 。

1、如果表b采用`InnoDB` ，`data\a`中会产生1个或者2个文件： 

+ `b.frm` ：描述表结构文件，字段长度等
+ 如果采用`系统表空间`模式的，数据信息和索引信息都存储在`ibdata1` 中
+ 如果采用`独立表空间`存储模式，`data\a`中还会产生`b.ibd` 文件（存储数据信息和索引信息）
  此外：
  ① MySQL5.7 中会在data/a的目录下生成`db.opt` **文件用于保存数据库的相关配置**。比如：字符集、比较规则。而MySQL8.0不再提供db.opt文件。
  ② MySQL8.0中不再单独提供b.frm，而是合并在b.ibd文件中。

2、如果表b采用`MyISAM` ，data\a中会产生3个文件：

+ MySQL5.7 中： `b.frm` ：描述表结构文件，字段长度等。
  MySQL8.0 中 `b.xxx.sdi` ：描述表结构文件，字段长度等
+ `b.MYD` (MYData)：数据信息文件，存储数据信息(如果采用独立表存储模式)
+ `b.MYI` (MYIndex)：存放索引信息文件