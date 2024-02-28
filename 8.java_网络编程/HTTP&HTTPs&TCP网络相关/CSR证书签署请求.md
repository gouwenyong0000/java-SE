# 证书签署请求

在[公开密钥基础架构](https://zh.wikipedia.org/wiki/公开密钥基础架构)（PKI）系统，**证书签署请求**（certificate signing request，**CSR**或**certification request**）是申请者发给[证书颁发机构](https://zh.wikipedia.org/wiki/证书颁发机构)（ca）的消息，用于申请[公开密钥证书](https://zh.wikipedia.org/wiki/公开密钥认证)。通常包含用于签发证书的公钥、用于辨识的信息（如域名）、完整性保护（如数字签名）。当证书被签署时，这两部分都会被插入到证书中。CSR最常用格式是[PKCS](https://zh.wikipedia.org/wiki/PKCS) #10规范。

## 过程

在创建CSR之前，申请者先产生一个[密钥对](https://zh.wikipedia.org/wiki/公开密钥加密)，保持私钥为秘密。CSR保护申请者的身份信息（如[X.509](https://zh.wikipedia.org/wiki/X.509)证书中[区分名称](https://zh.wikipedia.org/w/index.php?title=区分名称&action=edit&redlink=1)）用公钥加密。CSR还包含了这个公钥。

CSR中的典型DN信息：

| [Distinguished_Name](https://zh.wikipedia.org/w/index.php?title=Distinguished_Name&action=edit&redlink=1)[[1\]](https://zh.wikipedia.org/wiki/证书签署请求#cite_note-1) |              信息              |                             描述                             |            例子            |
| :----------------------------------------------------------: | :----------------------------: | :----------------------------------------------------------: | :------------------------: |
|                             `CN`                             |          Common Name           | 通用名称。通常是需要证书签名的完全合格域名(Fully Qualified Domain Name)（FQDN） |      *.wikipedia.org       |
|                             `O`                              |    商业名字 / Organization     |                    通常是法律上的商业名字                    | Wikimedia Foundation, Inc. |
|                             `OU`                             | 部门名字 / Organizational Unit |                      如HR, Finance, IT                       |                            |
|                             `L`                              |              城镇              |                                                              |       San Francisco        |
|                             `ST`                             |             省、州             |                        不应该使用缩写                        |         California         |
|                             `C`                              |            国家名字            |                      两字母ISO国家代码                       |             US             |
|                            `MAIL`                            |           Email地址            |               该组织中的证书管理人的email地址                |                            |

如果申请成功，[证书颁发机构](https://zh.wikipedia.org/wiki/证书颁发机构)（ca）用自己的私钥签发一个[公开密钥证书](https://zh.wikipedia.org/wiki/公开密钥认证)并发给申请者。

## 结构

证书包含三部分：[[2\]](https://zh.wikipedia.org/wiki/证书签署请求#cite_note-rfc2986-2) Thus the private key is needed to produce, but it is not part of, the CSR.[[3\]](https://zh.wikipedia.org/wiki/证书签署请求#cite_note-3)

- 证书请求信息，包含了公钥。为[ASN.1](https://zh.wikipedia.org/wiki/ASN.1)的类型 *CertificationRequestInfo*[[2\]](https://zh.wikipedia.org/wiki/证书签署请求#cite_note-rfc2986-2)
- 签名算法标识
- 证书请求信息的数字签名

## 例子

PKCS#10 标准化了使用[X.509](https://zh.wikipedia.org/wiki/X.509)的CSR的二进制格式。使用[OpenSSL](https://zh.wikipedia.org/wiki/OpenSSL)的命令行工具可以解读：

```sh
openssl asn1parse -i -in your_request
```

[Base64](https://zh.wikipedia.org/wiki/Base64)编码的CSR的例子如下：

```
-----BEGIN CERTIFICATE REQUEST-----
MIICzDCCAbQCAQAwgYYxCzAJBgNVBAYTAkVOMQ0wCwYDVQQIDARub25lMQ0wCwYD
VQQHDARub25lMRIwEAYDVQQKDAlXaWtpcGVkaWExDTALBgNVBAsMBG5vbmUxGDAW
BgNVBAMMDyoud2lraXBlZGlhLm9yZzEcMBoGCSqGSIb3DQEJARYNbm9uZUBub25l
LmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMP/U8RlcCD6E8AL
PT8LLUR9ygyygPCaSmIEC8zXGJung3ykElXFRz/Jc/bu0hxCxi2YDz5IjxBBOpB/
kieG83HsSmZZtR+drZIQ6vOsr/ucvpnB9z4XzKuabNGZ5ZiTSQ9L7Mx8FzvUTq5y
/ArIuM+FBeuno/IV8zvwAe/VRa8i0QjFXT9vBBp35aeatdnJ2ds50yKCsHHcjvtr
9/8zPVqqmhl2XFS3Qdqlsprzbgksom67OobJGjaV+fNHNQ0o/rzP//Pl3i7vvaEG
7Ff8tQhEwR9nJUR1T6Z7ln7S6cOr23YozgWVkEJ/dSr6LAopb+cZ88FzW5NszU6i
57HhA7ECAwEAAaAAMA0GCSqGSIb3DQEBBAUAA4IBAQBn8OCVOIx+n0AS6WbEmYDR
SspR9xOCoOwYfamB+2Bpmt82R01zJ/kaqzUtZUjaGvQvAaz5lUwoMdaO0X7I5Xfl
sllMFDaYoGD4Rru4s8gz2qG/QHWA8uPXzJVAj6X0olbIdLTEqTKsnBj4Zr1AJCNy
/YcG4ouLJr140o26MhwBpoCRpPjAgdYMH60BYfnc4/DILxMVqR9xqK1s98d6Ob/+
3wHFK+S7BRWrJQXcM8veAexXuk9lHQ+FgGfD0eSYGz0kyP26Qa2pLTwumjt+nBPl
rfJxaLHwTQ/1988G0H35ED0f9Md5fzoKi5evU1wG5WRxdEUPyt3QUXxdQ69i0C+7
-----END CERTIFICATE REQUEST-----
```

openssl解读为ASN.1结构，其中第一个数是字节偏移值，d=深度, hl=当前类型的头部长度, l=内容长度:

```
    0:d=0  hl=4 l= 716 cons: SEQUENCE          
    4:d=1  hl=4 l= 436 cons:  SEQUENCE          
    8:d=2  hl=2 l=   1 prim:   INTEGER           :00
   11:d=2  hl=3 l= 134 cons:   SEQUENCE          
   14:d=3  hl=2 l=  11 cons:    SET               
   16:d=4  hl=2 l=   9 cons:     SEQUENCE          
   18:d=5  hl=2 l=   3 prim:      OBJECT            :countryName
   23:d=5  hl=2 l=   2 prim:      PRINTABLESTRING   :EN
   27:d=3  hl=2 l=  13 cons:    SET               
   29:d=4  hl=2 l=  11 cons:     SEQUENCE          
   31:d=5  hl=2 l=   3 prim:      OBJECT            :stateOrProvinceName
   36:d=5  hl=2 l=   4 prim:      UTF8STRING        :none
   42:d=3  hl=2 l=  13 cons:    SET               
   44:d=4  hl=2 l=  11 cons:     SEQUENCE          
   46:d=5  hl=2 l=   3 prim:      OBJECT            :localityName
   51:d=5  hl=2 l=   4 prim:      UTF8STRING        :none
   57:d=3  hl=2 l=  18 cons:    SET               
   59:d=4  hl=2 l=  16 cons:     SEQUENCE          
   61:d=5  hl=2 l=   3 prim:      OBJECT            :organizationName
   66:d=5  hl=2 l=   9 prim:      UTF8STRING        :Wikipedia
   77:d=3  hl=2 l=  13 cons:    SET               
   79:d=4  hl=2 l=  11 cons:     SEQUENCE          
   81:d=5  hl=2 l=   3 prim:      OBJECT            :organizationalUnitName
   86:d=5  hl=2 l=   4 prim:      UTF8STRING        :none
   92:d=3  hl=2 l=  24 cons:    SET               
   94:d=4  hl=2 l=  22 cons:     SEQUENCE          
   96:d=5  hl=2 l=   3 prim:      OBJECT            :commonName
  101:d=5  hl=2 l=  15 prim:      UTF8STRING        :*.wikipedia.org
  118:d=3  hl=2 l=  28 cons:    SET               
  120:d=4  hl=2 l=  26 cons:     SEQUENCE          
  122:d=5  hl=2 l=   9 prim:      OBJECT            :emailAddress
  133:d=5  hl=2 l=  13 prim:      IA5STRING         :none@none.com
  148:d=2  hl=4 l= 290 cons:   SEQUENCE          
  152:d=3  hl=2 l=  13 cons:    SEQUENCE          
  154:d=4  hl=2 l=   9 prim:     OBJECT            :rsaEncryption
  165:d=4  hl=2 l=   0 prim:     NULL              
  167:d=3  hl=4 l= 271 prim:    BIT STRING        
  442:d=2  hl=2 l=   0 cons:   cont [ 0 ]        
  444:d=1  hl=2 l=  13 cons:  SEQUENCE          
  446:d=2  hl=2 l=   9 prim:   OBJECT            :md5WithRSAEncryption
  457:d=2  hl=2 l=   0 prim:   NULL              
  459:d=1  hl=4 l= 257 prim:  BIT STRING        
```

## 使用openssl创建证书签署请求

### 生成一个私钥和一个 CSR

从头开始创建一个 2048 位的私钥（domain.key）和一个 CSR（domain.csr）：

```sh
openssl req -newkey rsa:2048 -nodes -keyout domain.key -out domain.csr
```

其中选项-newkey rsa:2048指定密钥应该是2048位，使用RSA算法生成。选项-nodes指定私钥没有用密码加密。这里没有包含-new选项，而是隐含在其中，表示正在生成一个CSR。还需要继续回答 CSR 信息提问，完成该过程：

```
---
Country Name (2 letter code) [AU]:US
State or Province Name (full name) [Some-State]:New York
Locality Name (eg, city) []:Brooklyn
Organization Name (eg, company) [Internet Widgits Pty Ltd]:Example Brooklyn Company
Organizational Unit Name (eg, section) []:Technology Division
Common Name (e.g. server FQDN or YOUR name) []:examplebrooklyn.com
Email Address []:
```

在这个命令中也可以使用-subj选项非交互式地添加CSR信息：

```
-subj "/C=US/ST=New York/L=Brooklyn/O=Example Brooklyn Company/CN=examplebrooklyn.com"
```

### 从现有的私钥中生成一个 CSR

如果已经有了私钥，想用它向 CA 申请证书，使用这个方法。该命令基于现有的私钥（domain.key）创建一个新的 CSR（domain.csr）：

```
openssl req -key domain.key -new -out domain.csr
```

其中选项 -key 指定一个现有的私钥（domain.key)，它将被用来生成一个新的 CSR。选项 -new 表示正在生成一个 CSR。继续回答 CSR 信息提问，完成该过程。

### 从现有的证书和私钥生成 CSR

如果想更新现有的证书，但由于某些原因，你或你的 CA 没有原始的 CSR，使用这个方法可以省去重新输入 CSR 信息的麻烦，因为是从现有证书中提取信息。该命令基于现有的证书（domain.crt）和私钥（domain.key）创建一个新的 CSR（domain.csr）：

```
openssl x509 -in domain.crt -signkey domain.key -x509toreq -out domain.csr
```

其中选项 -x509toreq 指定使用一个 X509 证书来制作 CSR。

## 参见

- [SPKAC](https://zh.wikipedia.org/w/index.php?title=SPKAC&action=edit&redlink=1)
- [X.509](https://zh.wikipedia.org/wiki/X.509)