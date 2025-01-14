> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.cnblogs.com](https://www.cnblogs.com/librarookie/p/16364384.html)

Keytool 工具的介绍与使用
================

keytool 简介
----------

> Keytool 是一个 Java 数据证书的管理工具, Keytool 将密钥（key）和证书（certificates）存在一个称为 keystore 的文件中。
>
> keytool 是个密钥和证书管理工具。它使用户能够管理自己的公钥 / 私钥对及相关证书，用于（通过数字签名）自我认证（用户向别的用户 / 服务认证自己）或数据完整性以及认证服务。在 JDK 1.4 以后的版本中都包含了这一工具，它的位置为 `“%JAVA_HOME%\bin\keytool.exe”。`

### keystore 文件介绍

在 keystore 里，包含两种数据：

*   密钥实体（Key entity）——密钥（secret key）又或者是私钥和配对公钥（采用非对称加密）
*   可信任的证书实体（trusted certificate entries）——只包含公钥

我们常说的证书就是就是上面的公钥，公钥是公开给其它人使用的。

### 证书后缀解释

*   `jks` 是 Java 的 keytool 证书工具支持的证书私钥格式；
*   `pfx` 是微软支持的私钥格式（p12 是 pfx 的新格式）；
*   `cer` / `crt` 是证书的公钥格式（cer 是 crt 证书的微软形式）
*   `csr` 数字证书签名请求文件（Cerificate Signing Request）

Tips:

*   `.der` `.cer` ： 此证书文件是二进制格式，只含有证书信息，不包含私钥。
*   `.crt` ： 此证书文件是二进制格式或文本格式，一般为文本格式，功能与 `.der` 及 `.cer` 证书文件相同。
*   `.pem` ： 此证书文件一般是文本格式，可以存放证书或私钥，或者两者都包含。 `.pem` 文件如果只包含私钥，一般用 `.key` 文件代替。
*   `.pfx` `.p12` ： 此证书文件是二进制格式，同时包含证书和私钥，且一般有密码保护。
*   `.keystore` `.truststore` : 两者本质都是 keystore，都是储存密钥的容器：
    *   两者存放的密钥所有者不同，keystore 是存储自己的公钥和私钥而，truststore 是存储自己信任对象的公钥。约定通过文件名称区分类型以及用途；
    *   `truststore` 是必须的，如果我们没有显式的指定，那么 java 会默认指定为 `$JAVA_HOME/lib/security/cacerts` 这个文件；
    *   java 在 jdk 中已经默认在 $JAVA_HOME/lib/security/cacerts 这个文件中预置了常用的证书；
*   不同语言需要的证书格式并不一致，比如说 Java 采用 jks，.Net 采用 pfx 和 cer，Php 则采用 pem 和 cer；
*   区别证书的不是后缀名，而是文件的格式和内容。

keytool 命令详解
------------

### 密钥和证书管理工具

```bash
C:\Users\Administrator>keytool
密钥和证书管理工具
 
命令:
 
 -certreq            生成证书请求
 -changealias        更改条目的别名
 -delete             删除条目
 -exportcert         导出证书
 -genkeypair         生成密钥对
 -genseckey          生成密钥
 -gencert            根据证书请求生成证书
 -importcert         导入证书或证书链
 -importpass         导入口令
 -importkeystore     从其他密钥库导入一个或所有条目
 -keypasswd          更改条目的密钥口令
 -list               列出密钥库中的条目
 -printcert          打印证书内容
 -printcertreq       打印证书请求的内容
 -printcrl           打印 CRL 文件的内容
 -storepasswd        更改密钥库的存储口令
 
使用 "keytool -command_name -help" 获取 command_name 的用法
```

### 常用参数

```
-genkey         产生密钥对（genkeypair 简写）；表示要创建一个新的密钥；alias和keystore缺省时，在用户主目录中创建一个”.keystore”文件，且别名为mykey，包含用户的公钥、私钥证书
-alias          产生证书别名，和keystore关联的唯一别名，不区分大小写（默认 `mykey`）
-keystore       指定密钥库文件的名称（默认在用户主目录创建证书库）
-keyalg         指定密钥的算法（可选择密钥算法：`RSA`、`DSA`、`EC`，默认`DSA`）
-keysize        指定密钥长度（与keyalg默认对应关系：`RSA=2048`、`DSA=2048`、`EC=256`）
-sigalg         指定签名算法（MD5和 SHA1的签名算法已经不安全）
-validity       指定证书有效期天数（默认 `90`天）
-storepass      指定密钥库口令，推荐与keypass一致（获取keystore信息所需的密码）
-storetype      指定密钥库的类型，可用类型为：JKS、PKCS12等。（jdk9以前，默认为JKS。自jdk9开始，默认为PKCS12）
-keypass        指定别名条目口令（私钥的密码）
-dname          指定证书发行者信息（其中 CN 要和服务器的 `域名` 或 `IP` 相同，本地测试则使用localhost，其他的可以不填）
-list           显示密钥库中的证书信息
-v              详细输出，显示密钥库中的证书详细信息
-file           指定导出或导出的文件名
-export         将别名指定的证书导出到文件（exportcert 简写）
-import         将已签名数字证书导入密钥库（importcert 简写）
-printcert      查看导出的证书信息
-delete         删除密钥库中某条目
-keypasswd      修改密钥库中指定条目口令
-storepasswd    修改keystore口令
-ext            X.509 扩展
```

Tips:

*   所有密码长度必须大于或等于 6 位
*   keyalg 指定加密算法；可以选择的密钥算法有：RSA、DSA（默认）、EC。
*   sigalg 指定签名算法（MD5 和 SHA1 的签名算法已经不安全）:
    *   keyalg = RSA 时，签名算法有：MD5withRSA、SHA1withRSA、SHA256withRSA（默认）、SHA384withRSA、SHA512withRSA
    *   keyalg = DSA 时，签名算法有：SHA1withDSA、SHA256withDSA（默认）
*   dname 表明了密钥的发行者身份（Distinguished Names）
    *   CN = 域名或 IP（Common Name） 注：生成服务器证书时，CN 要和服务器的 `域名` 或 `IP` 相同，本地测试则使用 localhost，其他的可以不填（客户端证书无要求）
    *   OU = 组织单位名称（Organization Unit）
    *   O = 组织名称（Organization Name）
    *   L = 城市或区域名称（Locality Name）
    *   ST = 州或省份名称（State Name）
    *   C = 国家的简写（Country，CN 代表中国）

证书操作
----

### 创建证书

> 创建秘钥库 (keystore), 秘钥库是存储一个或多个密钥条目的文件，每个密钥条目应该以一个别名标识，它包含密钥和证书相关信息。

*   Usage:
    
    ```sh
    keytool -genkey 
            -alias <alias> 
            -keyalg RSA 
            [-sigalg SHA256withRSA] 
            [-keysize 2048] 
            -keypass <keypasswd> 
            -keystore <keystore_file> 
            -storetype JKS|PKCS12 
            -storepass <keystore_passwd> 
            -validity 3650 
            -dname "CN=github.com,OU=github.com,Inc.,O=Github, Inc.,L=San Francisco,ST=California,C=US" 
            -ext SAN=dns:github.com,dns:www.github.com,ip:127.0.0.1 
    ```
    
*   Options:
    
    ```sh
    -genkey     产生密钥对（genkeypair 简写）
    -alias      证书别名；和keystore关联的唯一别名，这个alias通常不区分大小写（默认`mykey`）
    -keyalg     指定加密算法，RSA：非对称加密（默认`DSA`）
    -sigalg     指定签名算法，可选;
    -keysize    指定密钥长度，可选;
    -keypass    指定别名条目口令（私钥的密码）
    -storetype  生成证书类型，可用的证书库类型为：JKS、PKCS12等。（jdk9以前，默认为JKS。自jdk9开始，默认为PKCS12）
    -keystore   指定产生的密钥库的位置；
    -storepass  指定密钥库的存取口令，推荐与keypass一致
    -validity   证书有效期天数；（默认为 90天）
    -dname      表明了密钥的发行者身份（Distinguished Names）生成证书时，其中 CN 要和服务器的 `域名` 或 `IP` 相同，本地测试则使用localhost，其他的可以不填
    -ext        X.509 扩展
    ```
    

Tips：

*   此处需要注意：MD5 和 SHA1 的签名算法已经不安全；
*   如果 Tomcat 所在服务器的域名不是 “localhost” 时，浏览器会弹出警告窗口，提示用户证书与所在域不匹配。
    *   服务器证书 dname 的 CN 应改为对应的域名，如 “www.github.com”；在本地做开发测试时，CN 应填入 “localhost”；
    *   客户端证书 dname 的 CN 可以是任意值，且不用使用 -ext 扩展。

### 导出证书信息

> 此证书文件不包含私钥；分为自签名证书和认证证书，下面分别介绍了两中证书的生成方式

*   认证证书与导出的服务器自签名证书作用一致，使用时取其中一种证书即可。两者主要区别为是否经证书机构认证；
*   使用自签名证书则无需生成证书签名请求 (CSR)，使用认证证书则无需导出服务器自签名证书；
*   大部分认证证书都是收费的；

#### 导出自签名证书

> 自签名证书没有经过证书认证机构进行认证，但并不影响使用，我们可以使用相应的命令对证书进行导出;

*   Usage:
    
    ```sh
    keytool -export 
            -alias <alias> 
            -keystore <keystore_file> 
            -storepass <keystore_passwd> 
            -file <file_cer>    
            [-rfc] 
    ```
    
*   Options:
    
    ```sh
    -export     执行证书导出操作（exportcert 简写）
    -alias      密钥库中的证书条目别名（jks里可以存储多对公私钥文件，通过别名指定导出的公钥证书）
    -keystore   指定密钥库文件
    -storepass  密钥库口令
    -file       导出文件的输出路径
    -rfc        使用Base64格式输出（输出pem编码格式的证书，文本格式），不适用则导出的证书为DER编码格式
    ```
    

#### 获取认证证书（生成证书签名请求）

> 如果想得到证书认证机构的认证，则不使用上述的自签名证书，需要使用步骤导出数字证书并签发申请（Cerificate Signing Request），经证书认证机构认证并颁发后，再将认证后的证书导入本地密钥库与信任库。

*   Usage:
    
    ```sh
    keytool -certreq 
            -alias <alias> 
            -keystore <keystore_file> 
            -storepass <keystore_passwd> 
            -file <file_csr> 
    ```
    
*   Options:
    
    ```sh
    -certreq    执行证书签发申请导出操作
    -alias      密钥库中的证书条目别名
    -keystore   密钥库文件名称
    -storepass  密钥库口令
    -file       输出的csr文件路径
    ```
    

### 导入证书库

> 双向认证： 将各自的公钥证书分别导入对方的信任库，使客户端和服务端相互信任。

*   Usage:
    
    ```sh
    keytool -import 
            [-trustcacerts] 
            -alias <alias_cer> 
            -keystore <keystore_file>
            -storepass <keystore_passwd> 
            -file <file_cer> 
    ```
    
*   Options:
    
    ```sh
    -import     执行证书导入操作（importcert 简写）
    -alias      指定导入密钥库中的证书别名（指定的条目别名不能与密钥库中已存在的条目别名重复（导入签发证书除外））
    -trustcacerts    将证书导入信任库（信任来自 cacerts 的证书）
    -keystore   密钥库名称
    -storepass  密钥库口令
    -file       输入文件名
    ```
    

Tips： 此步骤会生成信任证书 truststore.jks 文件， 文件存放需要信任的公钥证书，如客户端证书（也可以将 keystore 值改为服务器密钥库，如 tomcat.jks。此时的 tomcat.jks 就同时是服务的密钥库和信任库）

### 安装服务器证书（将服务器公钥证书导入客户端）

> 双向认证： 客户端信任服务端： 在客户机器上双击证书文件完成导入操作（window 中导入）

1.  安装公钥证书
    
    *   将服务器公钥证书（cer | crt 等格式文件）发往客户端机器
        *   --> 双击该证书进入 “证书信息” 页
            *   --> 点击【安装证书】进入 “证书导入向导” 首页
                *   --> 点击【下一步】
                    *   --> 选中【将所有的证书都放入下列存储】，然后单击【浏览】
                        *   --> 选择【受信任的根证书颁发机构】并点击【确定】
                            *   --> 点击【下一步】
                                *   --> 点击【完成】。然后弹出提示【导入完成】。
2.  安装客户端证书
    
    *   将客户端证书（jsk | p12 等格式文件）发往客户端机器
        *   --> 双击该证书进入 “证书导入向导” 首页
            *   --> 点击【下一步】
                *   --> 点击【下一步】
                    *   --> 输入证书密码（keystore 密码）并点击【下一步】
                        *   --> 点击【下一步】（这里也可以自己指定证书储存）
                            *   --> 点击【完成】。然后弹出提示【导入完成】。

### 查看证书

*   Usage:
    
    ```sh
    # 查看单个证书（cer | crt）
    keytool -printcert -file <cert_file> [-v|-rfc]
     
    # 查看密钥库中的证书条目
    keytool -list [-alias <alias_name>] -keystore <keystore_file> -storepass <keystore_passwd> [-v|-rfc]
     
    # 查看生成的CSR证书请求
    keytool -printcertreq -file <certreq_file>     
    ```
    
*   Options:
    
    ```sh
    -alias      密钥库中的证书条目别名；
    -keystore   指定密钥库文件；
    -storepass  密钥库口令；
    -printcert  执行证书打印命令；
    -list       缺省情况下，命令打印证书的 MD5 指纹。
        而如果指定了 -v 选项，将以可读格式打印证书，
        如果指定了 -rfc 选项，将以可打印的编码格式输出证书。
     
    ```
    

### 其他命令

```sh
# 删除keystore里面指定证书条目
keytool -delete -alias <alias> -keystore <keystore_file> -storepass <keystore_passwd>
 
# 修改条目别名
keytool -changealias -keystore <keystore_file> -alias <old_alias> -destalias <new_alias>
 
# 修改条目密码
keytool -keypasswd -alias <alias> -keypass <old_keypasswd> -new <new_keypasswd> -keystore <keystore_file> -storepass <keystore_passwd>
 
# 修改keysore密码
keytool -storepasswd -new <new_storepasswd> -keystore <keystore_file> -storepass <old_storepasswd>
 
# 列出信任的CA证书（查看 JVM的信任库中的证书，storepass 默认为changeit）
## 该证书文件存在于JAVA_HOME\jre\lib\security目录下，是Java系统的CA证书仓库，可以用 'alias' 来查看证书是否真的导入到JVM中
keytool -list -v [-alias clientCer] -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit
 
# 导入新的CA到信任证书，导入到 JRE的信任证书库
## 常出现的异常：“未找到可信任的证书”  -- 主要原因为在客户端未将服务器下发的证书导入到JVM中。
keytool -import -trustcacerts -alias clientCer -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -file ~/ssl/client.cer
```

栗子
--

### 生成自签名SSL证书

1. **打开命令行工具**

首先，打开你的命令行工具（Windows下是CMD或PowerShell，MacOS和Linux下是Terminal）。

2. **生成密钥库和密钥对**

执行以下命令生成一个新的密钥库（keystore）和密钥对（key pair）。密钥库是存放密钥和证书的地方，需要设置一个密码保护。

```sh
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -keystore keystore.jks -validity 365
```

- `-alias server`：为你的密钥对设置一个别名，这里使用`server`。
- `-keyalg RSA`：指定密钥算法为RSA。
- `-keysize 2048`：设置密钥大小为2048位。
- `-keystore keystore.jks`：指定密钥库文件名，这里使用`keystore.jks`。
- `-validity 365`：证书有效期为365天。

执行命令后，你需要填写一些信息，包括你的姓名、组织单位、组织名称、城市或地区、省份或州以及国家/地区代码。

3. **导出证书**

生成密钥库和密钥对后，你可能需要将证书导出为`.crt`或`.pem`格式，以便在客户端或其他服务器上进行配置。

```sh
keytool -export -alias server -keystore keystore.jks -rfc -file server.crt
```

- `-alias server`：指定要导出的密钥对的别名。
- `-keystore keystore.jks`：指定密钥库文件名。
- `-rfc`：指定以RFC格式导出证书。
- `-file server.crt`：指定导出的证书文件名。

4. **查看密钥库内容**

如果你需要查看密钥库中的内容，可以使用以下命令：

```sh
keytool -list -keystore keystore.jks
```

输入你设置的密钥库密码后，你将看到密钥库中的证书列表。

#### Apache配置SSL证书

在Apache的配置文件中（通常是`httpd.conf`或`ssl.conf`），添加如下配置：

```nginx
<VirtualHost *:443>
    ServerName yourdomain.com
    SSLEngine on
    SSLCertificateFile "/path/to/server.crt"
    SSLCertificateKeyFile "/path/to/keystore.jks"
    # 注意：Apache直接不支持JKS格式，需转换为PEM
    # 这里仅为示例，实际应转换为PEM格式
    # SSLCertificateKeyFile "/path/to/server.key"
    # SSLCertificateChainFile "/path/to/chain.crt" # 如果需要链证书
</VirtualHost>
```

注意：Apache不支持直接加载JKS格式的密钥库，你需要将密钥和证书转换为PEM格式。

#### Nginx配置SSL证书

在Nginx的配置文件中（通常是`nginx.conf`），添加如下配置：

```nginx
server {
listen 443 ssl;
server_name yourdomain.com;
ssl_certificate /path/to/server.crt;
ssl_certificate_key /path/to/keystore.jks; # 同样，Nginx不直接支持JKS

# 转换为PEM格式后使用#
```



#### 在Spring Boot中配置HTTPS

接下来，我们需要在Spring Boot应用程序中配置HTTPS。请按照以下步骤操作：

1. 在你的Spring Boot项目的`src/main/resources`目录下创建一个名为`application.properties`的文件（如果该文件已经存在，请直接编辑）。

2. 在`application.properties`文件中添加以下配置：

   ```properties
   server.port=8443
   server.ssl.key-store=classpath:keystore.jks
   server.ssl.key-store-password=你的密钥库密码
   server.ssl.key-alias=tomcat
   ```

   请确保将`你的密钥库密码`替换为你生成证书时设置的密码。

3. 将生成的`keystore.jks`文件复制到Spring Boot项目的`src/main/resources`目录下。

4. 启动你的Spring Boot应用程序。现在，应用程序将使用HTTPS协议在8443端口上运行



### Netty实现SSL双向验证完整实例

####  一、证书准备

   要使用ssl双向验证，就必须先要生成服务端和客户端的证书，并相互添加信任，具体流程如下(本人调试这个用例的时候，花了很多时间来验证证书是否正确，以及握手失败的原因，这里证书生成过程只要按流程走，本人能保证绝对没有问题)

现在打开cmd，在哪个目录下打开，证书就会放在哪个目录下：

**第一步:  生成Netty服务端私钥和证书仓库命令**

```sh
keytool -genkey -alias securechat -keysize 2048 -validity 365 -keyalg RSA -dname "CN=localhost" -keypass sNetty -storepass sNetty -keystore sChat.jks
```

- -keysize 2048 密钥长度2048位（这个长度的密钥目前可认为无法被暴力破解）
- -validity 365 证书有效期365天
- -keyalg RSA 使用RSA非对称加密算法
- -dname "CN=localhost" 设置Common Name为localhost
- -keypass sNetty密钥的访问密码为sNetty
- -storepass sNetty密钥库的访问密码为sNetty（其实这两个密码也可以设置一样，通常都设置一样，方便记）
- -keystore sChat.jks 指定生成的密钥库文件为sChata.jks

**第二步：生成Netty服务端自签名证书**

       ```sh
       keytool -export -alias securechat -keystore sChat.jks -storepass sNetty -file sChat.cer
       ```

 **第三步：生成客户端的密钥对和证书仓库，用于将服务端的证书保存到客户端的授信证书仓库中**

```sh
keytool -genkey -alias smcc -keysize 2048 -validity 365  -keyalg RSA -dname "CN=localhost" -keypass sNetty  -storepass sNetty -keystore cChat.jks
```

**第四步：将Netty服务端证书导入到客户端的证书仓库中**

```sh
keytool -import -trustcacerts -alias securechat -file sChat.cer -storepass sNetty -keystore cChat.jks
```

如果你**只做单向认证，则到此就可以结束了**，如果是双响认证，则还需继续往下走

**第五步:生成客户端自签名证书**

```sh
keytool -export -alias smcc -keystore cChat.jks -storepass sNetty -file cChat.cer
```

**最后一步:将客户端的自签名证书导入到服务端的信任证书仓库中：**

```sh
keytool -import -trustcacerts -alias smcc -file cChat.cer -storepass sNetty -keystore sChat.jks
```

到这里，证书就生成完毕了，我们就可以得到两个jks文件，一个是服务端的sChat.jks  ，一个是客户端的cChat.jks  ,这两个文件后面初始化sslCOntext的时候会用到

#### 二、netty服务端

首先是实例化SSLContext的类：

```java
package main.java.com.nionetty;  
  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.security.KeyStore;  
import java.security.NoSuchAlgorithmException;  
  
import javax.net.ssl.KeyManager;  
import javax.net.ssl.KeyManagerFactory;  
import javax.net.ssl.SSLContext;  
import javax.net.ssl.TrustManager;  
import javax.net.ssl.TrustManagerFactory;  
  
import org.springframework.core.io.ClassPathResource;  
/** 
 * 初始化sslcontext类 
 * 
 */  
public class ContextSSLFactory {  
      
    private static final SSLContext SSL_CONTEXT_S ;  
      
    private static final SSLContext SSL_CONTEXT_C ;  
      
    static{  
        SSLContext sslContext = null ;  
        SSLContext sslContext2 = null ;  
        try {  
            sslContext = SSLContext.getInstance("SSLv3") ;  
            sslContext2 = SSLContext.getInstance("SSLv3") ;  
        } catch (NoSuchAlgorithmException e1) {  
            e1.printStackTrace();  
        }  
        try{  
            if(getKeyManagersServer() != null && getTrustManagersServer() != null ){  
                sslContext.init(getKeyManagersServer(), getTrustManagersServer(), null);  
            }  
            if(getKeyManagersClient() != null && getTrustManagersClient() != null){  
                sslContext2.init(getKeyManagersClient(), getTrustManagersClient(), null);  
            }  
              
        }catch(Exception e){  
            e.printStackTrace() ;  
        }  
        sslContext.createSSLEngine().getSupportedCipherSuites() ;  
        sslContext2.createSSLEngine().getSupportedCipherSuites() ;  
        SSL_CONTEXT_S = sslContext ;   
        SSL_CONTEXT_C = sslContext2 ;  
    }  
    public ContextSSLFactory(){  
          
    }  
    public static SSLContext getSslContext(){  
        return SSL_CONTEXT_S ;  
    }  
    public static SSLContext getSslContext2(){  
        return SSL_CONTEXT_C ;  
    }  
    private static TrustManager[] getTrustManagersServer(){  
        FileInputStream is = null ;  
        KeyStore ks = null ;  
        TrustManagerFactory keyFac = null ;  
          
        TrustManager[] kms = null ;  
        try {  
             // 获得KeyManagerFactory对象. 初始化位默认算法  
            keyFac = TrustManagerFactory.getInstance("SunX509") ;  
            is =new FileInputStream( (new ClassPathResource("main/java/conf/sChat.jks")).getFile() );  
            ks = KeyStore.getInstance("JKS") ;  
            String keyStorePass = "sNetty" ;  
            ks.load(is , keyStorePass.toCharArray()) ;  
            keyFac.init(ks) ;  
            kms = keyFac.getTrustManagers() ;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            if(is != null ){  
                try {  
                    is.close() ;  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return kms ;  
    }  
    private static TrustManager[] getTrustManagersClient(){  
        FileInputStream is = null ;  
        KeyStore ks = null ;  
        TrustManagerFactory keyFac = null ;  
          
        TrustManager[] kms = null ;  
        try {  
             // 获得KeyManagerFactory对象. 初始化位默认算法  
            keyFac = TrustManagerFactory.getInstance("SunX509") ;  
            is =new FileInputStream( (new ClassPathResource("main/java/conf/cChat.jks")).getFile() );  
            ks = KeyStore.getInstance("JKS") ;  
            String keyStorePass = "sNetty" ;  
            ks.load(is , keyStorePass.toCharArray()) ;  
            keyFac.init(ks) ;  
            kms = keyFac.getTrustManagers() ;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            if(is != null ){  
                try {  
                    is.close() ;  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return kms ;  
    }  
    private static KeyManager[] getKeyManagersServer(){  
        FileInputStream is = null ;  
        KeyStore ks = null ;  
        KeyManagerFactory keyFac = null ;  
          
        KeyManager[] kms = null ;  
        try {  
             // 获得KeyManagerFactory对象. 初始化位默认算法  
            keyFac = KeyManagerFactory.getInstance("SunX509") ;  
            is =new FileInputStream( (new ClassPathResource("main/java/conf/sChat.jks")).getFile() );  
            ks = KeyStore.getInstance("JKS") ;  
            String keyStorePass = "sNetty" ;  
            ks.load(is , keyStorePass.toCharArray()) ;  
            keyFac.init(ks, keyStorePass.toCharArray()) ;  
            kms = keyFac.getKeyManagers() ;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            if(is != null ){  
                try {  
                    is.close() ;  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return kms ;  
    }  
    private static KeyManager[] getKeyManagersClient(){  
        FileInputStream is = null ;  
        KeyStore ks = null ;  
        KeyManagerFactory keyFac = null ;  
          
        KeyManager[] kms = null ;  
        try {  
             // 获得KeyManagerFactory对象. 初始化位默认算法  
            keyFac = KeyManagerFactory.getInstance("SunX509") ;  
            is =new FileInputStream( (new ClassPathResource("main/java/conf/cChat.jks")).getFile() );  
            ks = KeyStore.getInstance("JKS") ;  
            String keyStorePass = "sNetty" ;  
            ks.load(is , keyStorePass.toCharArray()) ;  
            keyFac.init(ks, keyStorePass.toCharArray()) ;  
            kms = keyFac.getKeyManagers() ;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            if(is != null ){  
                try {  
                    is.close() ;  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return kms ;  
    }  
}  
```

服务端启动类：

```java
package main.java.com.nionetty;  
  
import javax.net.ssl.SSLEngine;  
import javax.print.attribute.standard.MediaSize.Engineering;  
  
import main.java.com.nettyTest.SecureChatServerHandler;  
  
import io.netty.bootstrap.ServerBootstrap;  
import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.ChannelInitializer;  
import io.netty.channel.ChannelOption;  
import io.netty.channel.ChannelPipeline;  
import io.netty.channel.EventLoopGroup;  
import io.netty.channel.nio.NioEventLoopGroup;  
import io.netty.channel.socket.SocketChannel;  
import io.netty.channel.socket.nio.NioServerSocketChannel;  
import io.netty.handler.logging.LogLevel;  
import io.netty.handler.logging.LoggingHandler;  
import io.netty.handler.ssl.SslHandler;  
import io.netty.handler.timeout.IdleState;  
import io.netty.handler.timeout.IdleStateEvent;  
import io.netty.handler.timeout.IdleStateHandler;  
  
public class NettySocketServer {  
    private static SslHandler sslHandler = null ;  
      
    private EventLoopGroup bossGroup = null ;  
      
    private EventLoopGroup workerGroup = null ;  
      
    public void start(){  
        bossGroup = new NioEventLoopGroup() ;  
        workerGroup = new NioEventLoopGroup() ;  
        try{  
            ServerBootstrap serverStrap = new ServerBootstrap() ;  
            serverStrap.group(bossGroup , workerGroup)  
            .channel(NioServerSocketChannel.class)  
            .option(ChannelOption.SO_BACKLOG, 128)  
            .option(ChannelOption.SO_KEEPALIVE, true)  
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000 * 5 * 60)  
            .handler(new LoggingHandler(LogLevel.DEBUG))  
            .childHandler(new ChannelInitializer<SocketChannel>() {  
  
                @Override  
                protected void initChannel(SocketChannel socketChannel) throws Exception {  
                    ChannelPipeline pie = socketChannel.pipeline() ;  
                    pie.addLast("decoder" , new MyDecoder()) ;  
                    pie.addLast("encoder" , new MyEncoder()) ;  
                    pie.addLast("handler" , new NettySocketSSLHandler()) ;  
                    SSLEngine engine = ContextSSLFactory.getSslContext().createSSLEngine();  
                    engine.setUseClientMode(false);  
                    engine.setNeedClientAuth(true);  
                    pie.addFirst("ssl", new SslHandler(engine));  
                }  
                  
            });  
            serverStrap.bind(161616).sync() ;  
            System.out.println("服务已开启");  
        }catch(Exception e){  
            e.printStackTrace() ;  
            bossGroup.shutdownGracefully() ;  
            workerGroup.shutdownGracefully() ;  
        }  
          
    }  
        private SslHandler getSslHandler(){  
            if(sslHandler == null ){  
                SSLEngine sslEngine = ContextSSLFactory.getSslContext().createSSLEngine() ;  
                sslEngine.setUseClientMode(false) ;  
                //false为单向认证，true为双向认证  
                sslEngine.setNeedClientAuth(true) ;  
                sslHandler = new SslHandler(sslEngine);  
            }  
            return sslHandler ;  
        }  
        public static void main(String[] args) {  
            new NettySocketServer().start() ;  
        }  
          
}  
```



 编码器：

```java
package main.java.com.nionetty;  
  
import io.netty.buffer.ByteBuf;  
import io.netty.channel.ChannelHandlerContext;  
import io.netty.handler.codec.MessageToByteEncoder;  
  
import java.nio.ByteBuffer;  
  
public class MyEncoder extends MessageToByteEncoder<ByteBuffer>{  
  
    @Override  
    protected void encode(ChannelHandlerContext ctx, ByteBuffer message,  
            ByteBuf out) throws Exception {  
  
        if(message==null){  
            return;  
        }     
        if(message.hasArray()){  
            byte[] msg =message.array();  
            if(msg == null || msg.length <= 0){  
                return;  
            }  
            out.writeBytes(msg) ;  
        }  
    }     
}  
```

 解码器：

```java
/* 
 * Copyright (C) TD Tech<br> 
 * All Rights Reserved.<br> 
 *  
 */  
package main.java.com.nionetty;  
  
import io.netty.buffer.ByteBuf;  
import io.netty.channel.ChannelHandlerContext;  
import io.netty.handler.codec.ByteToMessageDecoder;  
  
import java.nio.ByteBuffer;  
import java.util.List;  
  
/** 
 * Create Date: 2014-11-4 下午02:42:21<br> 
 * Create Author: lWX232692<br> 
 * Description : 
 */  
public class MyDecoder extends ByteToMessageDecoder {  
  
  
    @Override  
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,  
            List<Object> out) throws Exception {  
        //UnpooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 1024)  
        if (buffer != null) {  
            ByteBuffer msg = null;  
            try {  
                if(buffer.readableBytes() > 0 ){  
                    msg = ByteBuffer.allocate(buffer.readableBytes()) ;  
                    byte[] bb = new byte[buffer.readableBytes()] ;  
                    buffer.readBytes(bb) ;  
                    msg.put(bb);  
                    msg.flip();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
                msg = null ;  
            }  
            if (msg != null) {  
                out.add(msg);  
            }  
        }  
    }  
  
  
}  
```

业务实现类：

```java
package main.java.com.nionetty;  
  
import io.netty.channel.Channel;  
import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.SimpleChannelInboundHandler;  
import io.netty.handler.ssl.SslHandler;  
import io.netty.util.concurrent.Future;  
import io.netty.util.concurrent.GenericFutureListener;  
  
import java.net.InetAddress;  
import java.nio.ByteBuffer;  
import java.util.Arrays;  
  
public class NettySocketSSLHandler extends SimpleChannelInboundHandler<ByteBuffer>{  
     @Override  
        public void channelActive(final ChannelHandlerContext ctx) throws Exception {  
            // Once session is secured, send a greeting and register the channel to the global channel  
            // list so the channel received the messages from others.  
            ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(  
                    new GenericFutureListener<Future<Channel>>() {  
                        @Override  
                        public void operationComplete(Future<Channel> future) throws Exception {  
                            if(future.isSuccess()){  
                                System.out.println("握手成功");  
                                byte[] array = new byte[]{ (byte)7d,  04} ;  
                                ByteBuffer bu = ByteBuffer.wrap(array) ;  
                                ctx.channel().writeAndFlush(bu) ;  
                            }else{  
                                System.out.println("握手失败");  
                            }  
                            ctx.writeAndFlush(  
                                    "Welcome to " + InetAddress.getLocalHost().getHostName() +  
                                            " secure chat service!\n");  
                            ctx.writeAndFlush(  
                                    "Your session is protected by " +  
                                            ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +  
                                            " cipher suite.\n");  
  
                        }  
                    });  
        }  
    @Override  
    public void handlerAdded(ChannelHandlerContext ctx)  
        throws Exception {  
         System.out.println("服务端增加");  
    }  
      
    @Override  
    public void handlerRemoved(ChannelHandlerContext ctx){  
        System.out.println("移除:"+ctx.channel().remoteAddress());  
    }  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
       System.out.println("Unexpected exception from downstream.");  
        ctx.close();  
    }  
    @Override  
    public void messageReceived(ChannelHandlerContext ctx, ByteBuffer msg) throws Exception {  
        System.out.println("服务端receive msg ");  
        byte[] array = new byte[]{00, 01, 00, 00, 00, 06, 05, 03, (byte)7d, 00, 00, 07} ;  
        ByteBuffer bu = ByteBuffer.wrap(array) ;  
        ctx.channel().writeAndFlush(bu) ;  
    }  
      
}  
```



#### 三、客户端

 客户端实现类

```java
package main.java.com.nionetty.client;  
  
import java.net.InetSocketAddress;  
import java.net.SocketAddress;  
  
import javax.net.ssl.SSLEngine;  
  
import io.netty.bootstrap.Bootstrap;  
import io.netty.channel.Channel;  
import io.netty.channel.ChannelFuture;  
import io.netty.channel.ChannelInitializer;  
import io.netty.channel.ChannelOption;  
import io.netty.channel.ChannelPipeline;  
import io.netty.channel.EventLoopGroup;  
import io.netty.channel.nio.NioEventLoopGroup;  
import io.netty.channel.socket.SocketChannel;  
import io.netty.channel.socket.nio.NioSocketChannel;  
import io.netty.handler.ssl.SslHandler;  
import main.java.com.nionetty.ContextSSLFactory;  
import main.java.com.nionetty.MyDecoder;  
import main.java.com.nionetty.MyEncoder;  
  
public class NettySocketClient {  
      
    private EventLoopGroup group ;  
      
    private Channel channel = null ;   
    public void connect(String ip , int port){  
        group = new NioEventLoopGroup();  
        try{  
            Bootstrap strap = new Bootstrap();  
            strap.group(group)  
            .channel(NioSocketChannel.class)  
            .option(ChannelOption.TCP_NODELAY, true)  
            .option(ChannelOption.SO_KEEPALIVE , true)  
            .handler(new ChannelInitializer<SocketChannel>() {  
                @Override  
                protected void initChannel(SocketChannel socketChannel) throws Exception {  
                    ChannelPipeline pieple = socketChannel.pipeline() ;  
                    pieple.addLast("decoder" , new MyClientDecoder()) ;  
                    pieple.addLast("encoder" , new MyClientEncoder()) ;  
                    pieple.addLast("handler" , new NettySocketSSLClientHandler()) ;  
                     SSLEngine engine = ContextSSLFactory.getSslContext2().createSSLEngine();  
                     engine.setUseClientMode(true);  
                     pieple.addFirst("ssl", new SslHandler(engine));  
                }  
            });  
        SocketAddress address = new InetSocketAddress(ip, port);  
        final ChannelFuture future = strap.connect(address).sync();  
        channel = future.awaitUninterruptibly().channel();  
        System.out.println("连接成功， channel =" + channel.remoteAddress());  
        }catch(Exception e ){  
            e.printStackTrace();  
            group.shutdownGracefully() ;  
        }finally{  
              
        }  
    }  
    private static SslHandler sslHandlerClient = null ;  
    public static SslHandler getSslHandler(){  
        if(sslHandlerClient == null){  
            SSLEngine sslEngine = ContextSSLFactory.getSslContext2().createSSLEngine() ;  
            sslEngine.setUseClientMode(true) ;  
            sslHandlerClient = new SslHandler(sslEngine);  
        }  
        return sslHandlerClient ;  
    }  
    public static void main(String[] args) {  
        new NettySocketClient().connect("192.168.10.256", 161616) ;  
    }  
}  
```

编码器：

```java
package main.java.com.nionetty.client;  
  
import io.netty.buffer.ByteBuf;  
import io.netty.channel.ChannelHandlerContext;  
import io.netty.handler.codec.MessageToByteEncoder;  
  
import java.nio.ByteBuffer;  
  
public class MyClientEncoder extends MessageToByteEncoder<ByteBuffer>{  
  
    @Override  
    protected void encode(ChannelHandlerContext ctx, ByteBuffer message,  
            ByteBuf out) throws Exception {  
  
        if(message==null){  
            return;  
        }     
        if(message .hasArray()){  
            byte[] msg =message.array();  
            if(msg == null || msg.length <= 0){  
                return;  
            }  
            out.writeBytes(msg);  
        }  
    }  
      
  
     
}  
```

 解码器：

```java
/* 
 * Copyright (C) TD Tech<br> 
 * All Rights Reserved.<br> 
 *  
 */  
package main.java.com.nionetty.client;  
  
import io.netty.buffer.ByteBuf;  
import io.netty.channel.ChannelHandlerContext;  
import io.netty.handler.codec.ByteToMessageDecoder;  
  
import java.nio.ByteBuffer;  
import java.util.List;  
  
/** 
 * Create Date: 2014-11-4 下午02:42:21<br> 
 * Create Author: lWX232692<br> 
 * Description : 
 */  
public class MyClientDecoder extends ByteToMessageDecoder {  
  
  
    @Override  
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,  
            List<Object> out) throws Exception {  
        //UnpooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 1024)  
        if (buffer != null) {  
            ByteBuffer msg = null;  
            try {  
                if(buffer.readableBytes() > 0 ){  
                    msg = ByteBuffer.allocate(buffer.readableBytes()) ;  
                    byte[] bb = new byte[buffer.readableBytes()] ;  
                    buffer.readBytes(bb) ;  
                    msg.put(bb);  
                    msg.flip();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
                msg = null ;  
            }  
            if (msg != null) {  
                out.add(msg);  
            }  
        }  
    }  
  
  
}  
```



业务handler:

```java
/* 
 * Copyright (C) TD Tech<br> 
 * All Rights Reserved.<br> 
 *  
 */  
package main.java.com.nionetty.client;  
  
import io.netty.buffer.ByteBuf;  
import io.netty.channel.ChannelHandlerContext;  
import io.netty.handler.codec.ByteToMessageDecoder;  
  
import java.nio.ByteBuffer;  
import java.util.List;  
  
/** 
 * Create Date: 2014-11-4 下午02:42:21<br> 
 * Create Author: lWX232692<br> 
 * Description : 
 */  
public class MyClientDecoder extends ByteToMessageDecoder {  
  
  
    @Override  
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,  
            List<Object> out) throws Exception {  
        //UnpooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 1024)  
        if (buffer != null) {  
            ByteBuffer msg = null;  
            try {  
                if(buffer.readableBytes() > 0 ){  
                    msg = ByteBuffer.allocate(buffer.readableBytes()) ;  
                    byte[] bb = new byte[buffer.readableBytes()] ;  
                    buffer.readBytes(bb) ;  
                    msg.put(bb);  
                    msg.flip();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
                msg = null ;  
            }  
            if (msg != null) {  
                out.add(msg);  
            }  
        }  
    }  
  
  
}  
```





常见问题
----

> 浏览器访问时，出现的提示与可能原因：

*   此服务器无法证实它是 “192.168..” - 您计算机的操作系统不信任其安全证书 。。。
    
    -- 客户端未导入服务器证书
    
*   此服务器无法证实它就是 “192.168..” - 它的安全证书没有指定主题备用名称 。。。
    
    -- 生成服务器证书库未使用 -ext 参数
    
*   “192.168..” 不接受您的登录证书，或者您可能没有提供登录证书。。。
    
    -- Tomcat 配置开了双向认证，且未指定信任证书库（truststore）
    

Via

*   [https://www.cnblogs.com/molao-doing/articles/9687445.html](https://www.cnblogs.com/molao-doing/articles/9687445.html)
*   [https://yoloz.github.io/2020/04/17/security/keytool 命令详解 /](https://yoloz.github.io/2020/04/17/security/keytool%E5%91%BD%E4%BB%A4%E8%AF%A6%E8%A7%A3/)
*   [https://blog.csdn.net/qq_26708427/article/details/68491201](https://blog.csdn.net/qq_26708427/article/details/68491201)