> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.cnblogs.com](https://www.cnblogs.com/librarookie/p/16364384.html)

# 深入浅出 SSL/CA 证书及其相关证书文件（pem、crt、cer、key、csr）

1. 一种是 Base64 (ASCII) 编码的文本格式。这种证书文件是可以通过文本编辑器打开，甚至进行编辑，常见有 PEM 证书格式，扩展名包括 PEM、CRT 和 KEY。
2. 另外一种是 Binary 二进制文件。常见有 DER 证书格式，扩展名包括 DER 和 CER。

Linux 系统使用 CRT，Windows 系统使用 CER。

| 名词          | 含义                                                         |
| ------------- | ------------------------------------------------------------ |
| X.509         | 一种通用的证书格式，包含证书持有人的公钥，加加密算法等信息   |
| pkcs1 ~pkcs12 | 公钥加密（非对称加密）的一种标准(Public Key Cryptography Standards)，一般存储为 *.pN，,*.p12 是包含证书和密的封装格式 |
| *.der         | 证书的二进制存储格式（不常用）                               |
| *.pem         | 证书或密钥的 Base64 文本存储格式，可以单独存放证书或密钥，也可以同时存放证书或密钥 |
| *.key         | 单独存放的 pem 格式的密钥，一般保存为 *.key                  |
| *.cer *.crt   | 两个指的都是证书，Linux 下叫 crt，Windows 下叫 cer；存储格式可以是 pem，也可以是 der |
| *.csr         | 证书签名请求（Certificate signing request），包含证书持有人的信息，如：国家，邮件，域名等信息 |
| *.pfx         | 微软 IIS 的实现                                              |
| *.jks         | Java 的 keytool 实现的证书格式                               |



# openssl工具使用模拟CA机构和



生成 CA 的私钥（后缀名可以用 .pem 也可以用 .key）

```bash
openssl genrsa -out ca.key 2048
```

生成 CA 证书请求文件，会车后会询问一系列基本信息

```bash
openssl req -new -key ca.key -out ca.csr
```

生成证书（公钥包含在证书中），正常情况下，要拿私钥和请求文件去被认可的 CA 机构进行证书申请和签发。这里选择使用 openssl 模拟 CA 机构来签发一个证书。

```bash
openssl x509 -req -days 365 -in ca.csr -signkey ca.key -out ca.crt
```

上面三步完成后，在文件夹下会得到三个文件：

```vbnet
ca.key
ca.csr
ca.crt
```

下面是证书操作的常见操作：

```bash
# 查看证书序列号
openssl x509 -in ca.crt -noout -serial
# 打印证书名称以 RFC2253 规定的格式打印出证书的拥有者名字
openssl x509 -in ca.crt -noout -subject
# 打印出证书的 MD5 特征参数
openssl x509 -in ca.crt -noout -fingerprint
# 打印出证书的 SHA 特征参数
openssl x509 -sha1 -in ca.crt -noout -fingerprint
```

**格式转换**

证书格式的转换其实本质上是编码格式的转换，比如 der 和 pem 的转换。

PEM 转 DER 格式：

```bash
openssl x509 -inform pem -in certificate.pem -outform der -out certificate.der
```

DER 转 PEM 格式：

```bash
openssl x509 -inform der -in certificate.der -outform pem -out certificate.pem
```

生成证书的请求文件

```bash
openssl req -new -key server.key -out server.csr
```

还是将 openssl 模拟为 CA 机构，用上述生成的 ca.crt 根证书来签发新的证书。

```bash
openssl x509 -req -days 3000 -sha1 -extensions v3_req -CA ca.crt -CAkey ca.key -CAserial ca.srl -CAcreateserial -in server.csr -out server.crt
```

- -CA：指定CA证书的路径
- -CAkey：指定 CA 证书的私钥路径
- -CAserial：指定证书序列号文件的路径
- -CAcreateserial：表示创建证书序列号文件（即上方提到的 serial 文件），创建的序列号文件默认名称为 -CA，指定的证书名称后加上 .srl 后缀

证书校验，用下面命令来校验是否签发成功

```bash
openssl verify -CAfile ca.crt server.crt
# server.crt: OK
```


