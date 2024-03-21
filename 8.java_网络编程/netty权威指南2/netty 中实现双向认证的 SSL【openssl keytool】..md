#### 1. 前期准备工作

​    双向证书认证的双方称为client和server，首先为client和server生成证书。由于仅仅是自己学习使用，因此可以在本地自建一个CA，然后用CA的证书分别签发client和server的证书。CA的创建和签发使用[OpenSSL](https://www.cnblogs.com/dingshaohua/p/12271280.html)。 
在windows环境上安装OpenSSL，然后依据OpenSSL目录下的openssl.cnf中[ CA_default ]的配置创建相应的文件夹和文件 

```html
demoCA/ —- CA的根目录 
|– newcerts/—- CA签发出去的证书 
|– private/ —- CA自己的私钥，默认名称是cakey.pem 
|– serial —- 存放证书序列号的文件 
|– index.txt —- 签发过的证书的记录，文本文件
```

​    serial这个文件中可以初始写入一行记录，包含两个字符01，表示下一个签发的证书采用的序列号是01 
接下来生成CA自己的公私钥（public/private key），生成证书签名请求（CSR, Certificate Signing Request）文件并对该请求进行自签名 
在openssl的根目录下运行 

```html
openssl genrsa -out ./demoCA/private/cakey.pem 2048 
```

genrsa —- 同时生成public key和private key 
很多人将genrsa解释为只生成private key，这是不对的。可以用下面的命令从文件中解出公钥 

```html
openssl rsa -in cakey.pem -pubout > capublickey.pub
```

注意最后的数字2048表示生成的RSA公私钥的长度

*JDK7中对证书检查要求公钥的长度最少为1024位，否则会抛出异常* 

*java.security.cert.CertPathValidatorException: Algorithm constraints check failed 
该长度限制是可以配置的，配置文件路径是JAVA_HOME/jre/lib/security/java.security 
jdk.certpath.disabledAlgorithms=MD2, RSA keySize < 1024* 

然后用上面生成的公私钥文件创建一个证书签名请求文件 

``` 
openssl req -new -key ./demoCA/private/cakey.pem -out careq.pem
```

req —- 创建CSR或者证书 
-key —- openssl从这个文件中读取private key 
careq.pem的内容格式是 

```html
—–BEGIN CERTIFICATE REQUEST—– 
MIICnzCCAYcCAQAwWjELMAkGA1UEBhMCQ04xCzAJBgNVBAgTAlpKMQswCQYDVQQH 
… … 
ZYu4AZp0VzqnQzCTeYTbC+AsA0RrPVjr95Il46AHvhq2JQpFw8DhrS8Ja1VburI4 
ngFK 
—–END CERTIFICATE REQUEST—–
```

最后将该请求文件给CA机构做签名，但我们现在是想在本地建CA，因此自己对该文件进行自签名即可。 

```html
openssl ca -selfsign -in careq.pem -out cacert.pem
```

其实，上面生成CSR然后做自签名的两个步骤可合并到一步完成 

```html
openssl req -new -x509 -key ./demoCA/private/cakey.pem -out cacert.pem
```

至此，我们已经建立了自己的CA，接下去来分别签发client和server的证书。

#### 创建client和server的证书、key store和trust store

​    以创建client的证书为例。由于jdk自带的keytool工具可以方便的创建key store和公私钥，因此公私钥和csr的创建直接使用keytool 
key store和trust store分别对应于ssl握手证书认证中自己的证书和自己所信任的证书列表，二者的文件格式相同，不同之处是key store里面包含ssl握手一方的公私钥和证书，trust store里面包含ssl握手一方所信任的证书，一般没有这些证书所对应的私钥

1. 生成client的keystore 和key pair 

   ```html
   keytool -genkey -alias client -keyalg RSA -keystore client.keystore -keysize 2048
   ```

2. 生成csr 

   ```html
   keytool -certreq -alias client -keystore client.keystore -file client.csr
   ```

3. 用本地CA对该csr签名 

   client证书中我们想添加证书的一项扩展，比如client id，用来区分client的身份，因此需要额外的一份扩展文件client.cnf，内容如下

   ```html
   [v3_req] 
   1.2.3.412=ASN1:UTF8String:0000001444 
   ```

   可以将该csr和client.cnf文件拷贝到openssl根目录下，运行 

   ```html
   openssl ca -in client.csr -out client.pem -config ./openssl.cnf -extensions v3_req -extfile client.cnf
   ```

4. 将签过名的client.pem导入到keystore文件中 

   在导入之前，需要先将CA的证书导入keystore文件 

   ```html
   keytool -keystore client.keystore -importcert -alias CA -file cacert.pem 
   ```

   然后导入client自己的证书。注意alias是client，与生产keystore和key pair的必须匹配 

   ```html
   keytool -keystore client.keystore -importcert -alias client -file client.pem
   ```

keystore文件内容的查看可以使用 
keytool -list -v -keystore client.keystore 
 或者使用可视化工具KeyStore Explorer查看

5. 创建client的trust store 

由于server的证书也是本地CA签发的，因此client只要信任CA的证书那么自然会信任CA签发出的证书，所以我们只需将CA的证书导入trust store即可 

```html
keytool -import -alias cacert -file cacert.pem -keystore clienttruststore.keystore 
```

由于clienttruststore.keystore文件尚不存在，此命令首先创建该文件并将CA的证书导入该trust store

server的证书和key store和trust store可类似创建 

```html
keytool -genkey -alias server -keyalg RSA -keystore server.keystore -keysize 2048 
keytool -certreq -alias server -keystore server.keystore -file server.csr
 
openssl ca -in server.csr -out server.pem -config ./openssl.cnf
 
keytool -keystore server.keystore -importcert -alias CA -file cacert.pem 
keytool -keystore server.keystore -importcert -alias server -file server.pem 
keytool -import -alias ca -file cacert.pem -keystore servertruststore.keystore
```



# 2. 创建SSL通讯的client和server

由于netty 5现在只有alpha版本，因此保险起见使用4.0.24.final版本的netty。 
netty的SSLContext提供了newClientContext来为client创建ssl context，但查看其源码未发现能支持双向认证，即client端的ssl context只接收一个trust store，而不能指定自己的证书以供server端校验。仿照netty example下的securechat的ssl实现但做了修改 
首先创建一个能提供client和server的ssl context的工具类，分别加载server和client的key store和trust store里面的证书



```java
public class SslContextFactory
{
        private static final String     PROTOCOL    = "TLS";    // TODO: which protocols will be adopted?
    private static final SSLContext SERVER_CONTEXT;
    private static final SSLContext CLIENT_CONTEXT;
 
    static
    {
        SSLContext serverContext = null;
        SSLContext clientContext = null;
 
        String keyStorePassword = "aerohive";
        try
        {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(SslContextFactory.class.getClassLoader().getResourceAsStream("cert\\server.keystore"), keyStorePassword.toCharArray());
 
            // Set up key manager factory to use our key store
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, keyStorePassword.toCharArray());
 
            // truststore
            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(SslContextFactory.class.getClassLoader().getResourceAsStream("cert\\servertruststore.keystore"), keyStorePassword.toCharArray());
 
            // set up trust manager factory to use our trust store
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);
 
            // Initialize the SSLContext to work with our key managers.
            serverContext = SSLContext.getInstance(PROTOCOL);
            serverContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
 
        } catch (Exception e)
        {
            throw new Error("Failed to initialize the server-side SSLContext", e);
        }
 
        try
        {
            // keystore
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(SslContextFactory.class.getClassLoader().getResourceAsStream("cert\\client.keystore"), keyStorePassword.toCharArray());
 
            // Set up key manager factory to use our key store
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, keyStorePassword.toCharArray());
 
            // truststore
            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(SslContextFactory.class.getClassLoader().getResourceAsStream("cert\\clienttruststore.keystore"), keyStorePassword.toCharArray());
 
            // set up trust manager factory to use our trust store
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);
            clientContext = SSLContext.getInstance(PROTOCOL);
            clientContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (Exception e)
        {
            throw new Error("Failed to initialize the client-side SSLContext", e);
        }
 
        SERVER_CONTEXT = serverContext;
        CLIENT_CONTEXT = clientContext;
    }
 
    public static SSLContext getServerContext()
    {
        return SERVER_CONTEXT;
    }
 
    public static SSLContext getClientContext()
    {
        return CLIENT_CONTEXT;
    }
    ... ...
}
```



​     io.netty.example.securechat.SecureChatClientInitializer类的构造器接收一个io.netty.handler.ssl.SslContext类型的对象，这个SslContext的对象最终被用来创建SslHandler，而上面factory产生的是javax.net.ssl.SSLContext的对象，因此可以做改动如下 

```java
public class ClientInitializer extends ChannelInitializer 
{
 
private final javax.net.ssl.SSLContext  sslCtx;
 
public ClientInitializer(javax.net.ssl.SSLContext sslCtx)
{
    this.sslCtx = sslCtx;
}
 
@Override
public void initChannel(SocketChannel ch) throws Exception
{
    ChannelPipeline pipeline = ch.pipeline();
 
    SSLEngine sslEngine = sslCtx.createSSLEngine(Client.HOST, Client.PORT);
    sslEngine.setUseClientMode(true);
    pipeline.addLast(new SslHandler(sslEngine));
 
    // On top of the SSL handler, add the text line codec.
    pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    pipeline.addLast(new StringDecoder());
    pipeline.addLast(new StringEncoder());
 
    // and then business logic.
    pipeline.addLast(new ClientHandler());
}
}
```



最后添加jvm参数 

```html
-Djavax.net.debug=ssl,handshake 
```

来查看ssl握手过程控制台的log



具体实现请参考附件源码。

http://download.csdn.net/detail/virgilli/8373319

附录： 
openssl的配置 
对证书签名时，遇到openssl异常failed to update database TXT_DB error 
有可能是因为签名的csr文件的subject中的一项或几项在该CA之前签发过的证书中已经出现过或者是csr中提供的国家/省份等等的名称与CA自己的不相同，这些限制都可以在openssl.cnf文件中修改 
unique_subject=no

[ policy_match ] 
countryName = match 
\#stateOrProvinceName = match 
organizationName = match 
organizationalUnitName = optional 
commonName = supplied 
emailAddress = optional