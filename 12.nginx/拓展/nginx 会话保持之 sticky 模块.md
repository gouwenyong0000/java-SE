> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.cnblogs.com](https://www.cnblogs.com/tssc/p/7481885.html)

**阅读目录**

*   [1.Sticky 工作原理](#_label0)
*   [2. 重新编译 nginx 增加 nginx-sticky-module 模块](#_label1)
*   [3.sticky 模块的使用](#_label2)
*   [4. 其他的参数：语法](#_label3)
*   [5. 其他需要注意的](#_label4)

nginx 会话保持之 nginx-sticky-module 模块

在使用负载均衡的时候会遇到会话保持的问题，常用的方法有：  
1.ip hash，根据客户端的 IP，将请求分配到不同的服务器上；  
2.cookie，服务器给客户端下发一个 cookie，具有特定 cookie 的请求会分配给它的发布者，  
注意：cookie 需要浏览器支持，且有时候会泄露数据

[回到顶部](#_labelTop)

**1.Sticky 工作原理**
-----------------

Sticky 是 nginx 的一个模块，它是基于 cookie 的一种 nginx 的负载均衡解决方案，通过分发和识别 cookie，来使同一个客户端的请求落在同一台服务器上，默认标识名为 route  
1. 客户端首次发起访问请求，nginx 接收后，发现请求头没有 cookie，则以轮询方式将请求分发给后端服务器。  
2. 后端服务器处理完请求，将响应数据返回给 nginx。  
3. 此时 nginx 生成带 route 的 cookie，返回给客户端。route 的值与后端服务器对应，可能是明文，也可能是 md5、sha1 等 Hash 值  
4. 客户端接收请求，并保存带 route 的 cookie。  
5. 当客户端下一次发送请求时，会带上 route，nginx 根据接收到的 cookie 中的 route 值，转发给对应的后端服务器。

[回到顶部](#_labelTop)

**2. 重新编译 nginx 增加 nginx-sticky-module 模块**
-------------------------------------------

查询 bitbucket.org 上的该模块相关文档

```
https://bitbucket.org/nginx-goodies/nginx-sticky-module-ng/overview
```

 # 查询当前的 nginx 编译参数可以使用以下命令：

[![](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

```
[root@nginx ~]# mkdir -p /server/tools
[root@nginx ~]# cd /server/tools
[root@nginx tools]# /usr/local/nginx/sbin/nginx -V
nginx version: nginx/1.8.0
built by gcc 4.4.7 20120313 (Red Hat 4.4.7-4) (GCC) 
built with OpenSSL 1.0.1e-fips 11 Feb 2013
TLS SNI support enabled
configure arguments: --prefix=/usr/local/nginx --user=nginx --group=nginx --with-http_stub_status_module --with-http_ssl_module
```

[![](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

# 下载该模块的软件包（）  
# 其他平台上的 nginx-sticky-module 模块包多是国外的资源的不方便下载，找到 GITHUB 上面的该软件包

```
[root@nginx tools]# wget https://bitbucket.org/nginx-goodies/nginx-sticky-module-ng/get/08a395c66e42.zip
```

# 解压

```
[root@nginx tools]# unzip -D nginx-goodies-nginx-sticky-module-ng-08a395c66e42.zip 
[root@nginx tools]# mv nginx-goodies-nginx-sticky-module-ng-08a395c66e42 nginx-sticky-module-ng
```

# 备份之前的 nginx 目录（注意：nginx 日志可能很大）

```
[root@nginx tools]# cp -rf /usr/local/nginx/ /server/backup/
```

# 进入到之前编译好的 nginx 目录下，重新进行编译安装

# 注意：是覆盖安装

```
[root@nginx tools]# cd nginx-1.8.0
[root@nginx nginx-1.8.0]# ./configure --prefix=/usr/local/nginx  --user=nginx --group=nginx --with-http_stub_status_module --with-http_ssl_module --add-module=/server/tools/nginx-sticky-module-ng
[root@nginx nginx-1.8.0]# make
[root@nginx nginx-1.8.0]# make install
```

# 安装完再次查看 nginx 编译参数

[![](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

```
[root@nginx tools]# /usr/local/nginx/sbin/nginx -V
nginx version: nginx/1.8.0
built by gcc 4.4.7 20120313 (Red Hat 4.4.7-4) (GCC) 
built with OpenSSL 1.0.1e-fips 11 Feb 2013
TLS SNI support enabled
configure arguments: --prefix=/usr/local/nginx --user=nginx --group=nginx --with-http_stub_status_module --with-http_ssl_module --add-module=/server/tools/nginx-sticky-module-ng
```

[![](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

# 需要重新启动 nginx 才可以使用 sticky 模块

```
[root@nginx tools]# service nginx restart
```

# 编辑配置文件，实例：

```
upstream www_web_com {
#   ip_hash;
   sticky expires=1h domain=web.com path=/;
   server 10.0.0.16:8080;
   server 10.0.0.17:8080;
}
```

# 具体的配置根据公司的相关业务配置即可

# 之后打开网站进行测试，使用 sticky 的情况下，不管怎么刷新都是如下结果

# 不使用 Nginx sticky 模块时，刷几次就变了（有时候刷一次，有时候多刷几次）

![](https://images2017.cnblogs.com/blog/1211667/201709/1211667-20170905212453788-1785119408.png)

备注：每台后端真实服务器都会有一个唯一的 route 值, 所以不管你真实服务器前端有几个装了 sticky 的 nginx 代理, 他都是不会变化的.

[回到顶部](#_labelTop)

**3.sticky 模块的使用**
------------------

# 位置：upstream 标签内

```
upstream {
  sticky;
  server 127.0.0.1:9000;
  server 127.0.0.1:9001;
  server 127.0.0.1:9002;
}
```

# 参数，解析

```
sticky [name=route] [domain=.foo.bar] [path=/] [expires=1h] 
       [hash=index|md5|sha1] [no_fallback] [secure] [httponly];
```

[![](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

```
[name=route]　　　　　　　设置用来记录会话的cookie名称
[domain=.foo.bar]　　　　设置cookie作用的域名
[path=/]　　　　　　　　  设置cookie作用的URL路径，默认根目录
[expires=1h] 　　　　　　 设置cookie的生存期，默认不设置，浏览器关闭即失效，需要是大于1秒的值
[hash=index|md5|sha1]   设置cookie中服务器的标识是用明文还是使用md5值，默认使用md5
[no_fallback]　　　　　　 设置该项，当sticky的后端机器挂了以后，nginx返回502 (Bad Gateway or Proxy Error) ，而不转发到其他服务器，不建议设置
[secure]　　　　　　　　  设置启用安全的cookie，需要HTTPS支持
[httponly]　　　　　　　  允许cookie不通过JS泄漏，没用过
```

[![](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

官方说明：

[![](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

```
name: the name of the cookies used to track the persistant upstream srv; default: route
domain: the domain in which the cookie will be valid default: nothing. Let the browser handle this.
path: the path in which the cookie will be valid default: /
expires: the validity duration of the cookie default: nothing. It's a session cookie. restriction: must be a duration greater than one second
hash: the hash mechanism to encode upstream server. It cant' be used with hmac. default: md5
md5|sha1: well known hash
index: it's not hashed, an in-memory index is used instead, it's quicker and the overhead is shorter Warning: the matching against upstream servers list is inconsistent. So, at reload, if upstreams servers has changed, index values are not guaranted to correspond to the same server as before! USE IT WITH CAUTION and only if you need to!
hmac: the HMAC hash mechanism to encode upstream server It's like the hash mechanism but it uses hmac_key to secure the hashing. It can't be used with hash. md5|sha1: well known hash default: none. see hash.
hmac_key: the key to use with hmac. It's mandatory when hmac is set default: nothing.
no_fallback: when this flag is set, nginx will return a 502 (Bad Gateway or Proxy Error) if a request comes with a cookie and the corresponding backend is unavailable.
secure enable secure cookies; transferred only via https
httponly enable cookies not to be leaked via js
```

[![](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

[回到顶部](#_labelTop)

**4. 其他的参数：语法**
---------------

以下内容摘录于：http://blog.csdn.net/yu870646595/article/details/52056340

```
session_sticky [cookie=name] [domain=your_domain] [path=your_path] [maxage=time][mode=insert|rewrite|prefix] 
　　　　　　　　 [option=indirect] [maxidle=time] [maxlife=time] [fallback=on|off] [hash=plain|md5]
```

[![](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

```
mode设置cookie的模式:
　　insert: 在回复中本模块通过Set-Cookie头直接插入相应名称的cookie。
　　prefix: 不会生成新的cookie，但会在响应的cookie值前面加上特定的前缀，当浏览器带着这个有特定标识的cookie再次请求时，模块在传给后端服务前先删除加入的前缀，后端服务拿到的还是原来的cookie值，这些动作对后端透明。如：”Cookie: NAME=SRV~VALUE”。
　　rewrite: 使用服务端标识覆盖后端设置的用于session sticky的cookie。如果后端服务在响应头中没有设置该cookie，则认为该请求不需要进行session sticky，使用这种模式，后端服务可以控制哪些请求需要sesstion sticky，哪些请求不需要。
option设置用于session sticky的cookie的选项，可设置成indirect或direct。indirect不会将session sticky的cookie传送给后端服务，该cookie对后端应用完全透明。direct则与indirect相反。
maxidle设置session cookie的最长空闲的超时时间
maxlife设置session cookie的最长生存期
maxage是cookie的生存期。不设置时，浏览器或App关闭后就失效。下次启动时，又会随机分配后端服务器。所以如果希望该客户端的请求长期落在同一台后端服务器上，可以设置maxage。
hash不论是明文还是hash值，都有固定的数目。因为hash是server的标识，所以有多少个server，就有等同数量的hash值。
```

[![](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

[回到顶部](#_labelTop)

5. 其他需要注意的
----------

1. 同一客户端的请求，有可能落在不同的后端服务器上  
如果客户端启动时同时发起多个请求。由于这些请求都没带 cookie，所以服务器会随机选择后端服务器，返回不同的 cookie。当这些请求中的最后一个请求返回时，客户端的 cookie 才会稳定下来，值以最后返回的 cookie 为准。  
2.cookie 不一定生效  
由于 cookie 最初由服务器端下发，如果客户端禁用 cookie，则 cookie 不会生效。  
3. 客户端可能不带 cookie  
Android 客户端发送请求时，一般不会带上所有的 cookie，需要明确指定哪些 cookie 会带上。如果希望用 sticky 做负载均衡，请对 Android 开发说加上 cookie。  
4.cookie 名称不要和业务使用的 cookie 重名。Sticky 默认的 cookie 名称是 route，可以改成任何值。  
5. 客户端发的第一个请求是不带 cookie 的。服务器下发的 cookie，在客户端下一次请求时才能生效。

6.Nginx sticky 模块不能与 ip_hash 同时使用

其他使用方法可以参考以下：

http://tengine.taobao.org/document_cn/http_upstream_session_sticky_cn.html

# 完毕，呵呵呵呵