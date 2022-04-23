# Nginx的安装

## 版本区别

常用版本分为四大阵营

+ Nginx开源版
  http://nginx.org/
+ Nginx plus 商业版
  https://www.nginx.com
+ openresty  [开源]
  http://openresty.org/cn/
+ Tengine [淘宝开源]
  http://tengine.taobao.org/

## 编译安装

### 上传文件安装

```shell
#进入安装包解压目录 
#/usr/local/nginx 表示nginx安装的路径，类似于windows中Program Files

./configure --prefix=/usr/local/nginx

make

make install
```

### 如果出现警告或报错

提示安装gcc

```
checking for OS
+ Linux 3.10.0-693.el7.x86_64 x86_64
checking for C compiler ... not found
./configure: error: C compiler cc is not found
```

安装gcc
```shell
yum install -y gcc
```

提示：安装perl库

```
./configure: error: the HTTP rewrite module requires the PCRE library.
You can either disable the module by using --without-http_rewrite_module
option, or install the PCRE library into the system, or build the PCRE library
statically from the source with nginx by using --with-pcre=<path> option.
```

```sh
yum install -y pcre pcre-devel
```

提示：安装zlib库

```提示：
./configure: error: the HTTP gzip module requires the zlib library.
You can either disable the module by using --without-http_gzip_module
option, or install the zlib library into the system, or build the zlib library
statically from the source with nginx by using --with-zlib=<path> option.
```

```sh
yum install -y zlib zlib-devel
```

接下来执行
```sh
make
make install
```

## 启动Nginx

进入安装好的目录`/usr/local/nginx/sbin`

```sh
./nginx #启动
./nginx -s stop #快速停止
./nginx -s quit #优雅关闭，在退出前完成已经接受的连接请求
./nginx -s reload #重新加载配置
```



>  nginx: [error] open() "/usr/local/nginx/logs/nginx.pid" failed (2: No such file or directory)
>
> 解决办法，执行以下语法即可修复：

```sh
/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf
```



## 关于防火墙

```sh
#关闭防火墙
systemctl stop firewalld.service
#禁止防火墙开机启动
systemctl disable firewalld.service
#放行端口
firewall-cmd --zone=public --add-port=80/tcp --permanent

#重启防火墙
firewall-cmd --reload
```

## nginx安装成系统服务

创建服务脚本
`vi /usr/lib/systemd/system/nginx.service`

服务脚本内容[注意安装位置，是否在`/usr/local/nginx`]

```sh
[Unit]
Description=nginx - web server
After=network.target remote-fs.target nss-lookup.target
[Service]
Type=forking
PIDFile=/usr/local/nginx/logs/nginx.pid
ExecStartPre=/usr/local/nginx/sbin/nginx -t -c /usr/local/nginx/conf/nginx.conf
ExecStart=/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf
ExecReload=/usr/local/nginx/sbin/nginx -s reload
ExecStop=/usr/local/nginx/sbin/nginx -s stop
ExecQuit=/usr/local/nginx/sbin/nginx -s quit
PrivateTmp=true
[Install]
WantedBy=multi-user.target
```

重新加载系统服务
```sh
systemctl daemon-reload
```

启动服务
```sh
systemctl start nginx.service
```

开机启动
```sh
systemctl enable nginx.service
```

