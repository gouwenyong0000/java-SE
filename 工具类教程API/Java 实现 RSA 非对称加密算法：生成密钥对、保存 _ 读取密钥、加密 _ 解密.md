

RSA 加密算法是一种非对称加密算法，即 RSA 拥有一对密钥（公钥 和 私钥），公钥可公开。公钥加密的数据，只能由私钥解密；私钥加密的数据只能由公钥解密。

为了方便读取和保存密钥，先创建一个 IO 工具类（`IOUtils.java`）：

```java
package com.xiets.rsa;

import java.io.*;

/**
 * IO 工具类, 读写文件
 * 
 */
public class IOUtils {

    public static void writeFile(String data, File file) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(data.getBytes());
            out.flush();
        } finally {
            close(out);
        }
    }

    public static String readFile(File file) throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
            byte[] data = out.toByteArray();
            return new String(data);
        } finally {
            close(in);
            close(out);
        }
    }

    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                // nothing
            }
        }
    }

}
```

主要两个方法：

*   把文本保存到文件（String -> File）: `IOUtils.writeFile(String data, File file)`
*   读取文件中的文本（File -> String）: `IOUtils.readFile(File file)`

# 1、生成 RSA 密钥对

-------------

Java [加密](https://so.csdn.net/so/search?q=%E5%8A%A0%E5%AF%86&spm=1001.2101.3001.7020)安全相关的类在 JDK 的`java.security.*`包下，以及下面使用到的类均为 JDK 内置的类。

### 1.1 生成密钥对

```java
// 获取指定算法的密钥对生成器
KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");

// 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
gen.initialize(2048);

// 随机生成一对密钥（包含公钥和私钥）
KeyPair keyPair = gen.generateKeyPair();

// 获取 公钥 和 私钥
PublicKey pubKey = keyPair.getPublic();
PrivateKey priKey = keyPair.getPrivate();
```

### 1.2 保存密钥

#### 1.2.1 Base64 编码保存

将密钥编码转换为 Base64 文本格式保存到文件：

```java
// 获取 公钥和私钥 的 编码格式（通过该 编码格式 可以反过来 生成公钥和私钥对象）
byte[] pubEncBytes = pubKey.getEncoded();
byte[] priEncBytes = priKey.getEncoded();

// 把 公钥和私钥 的 编码格式 转换为 Base64文本 方便保存
String pubEncBase64 = new BASE64Encoder().encode(pubEncBytes);
String priEncBase64 = new BASE64Encoder().encode(priEncBytes);

// 保存 公钥和私钥 到指定文件
IOUtils.writeFile(pubEncBase64, new File("pub.txt"));
IOUtils.writeFile(priEncBase64, new File("pri.txt"));

/* 通过该方法保存的密钥, 通用性较好, 使用其他编程语言也可以读取使用（推荐） */
```

#### 1.2.2 对象序列化保存

PublicKey 和 PrivateKey 均实现了 java.io.Serializable 接口，可以直接将整个密钥对象序列化保存。通过该方法保存的密钥只能用 Java 代码读取反序列化重新生成对象使用。

```java
// 创建对象输出流, 保存到指定的文件
ObjectOutputStream pubOut = new ObjectOutputStream(new FileOutputStream("pub.obj"));
ObjectOutputStream priOut = new ObjectOutputStream(new FileOutputStream("pri.obj"));

// 将 公钥/私钥 对象序列号写入 对象输出流
pubOut.writeObject(pubKey);
priOut.writeObject(priKey);

// 刷新并关闭流
pubOut.flush();
priOut.flush();
pubOut.close();
priOut.close();
```

### 1.3 读取密钥

#### 1.3.1 读取密钥的 Base64 文本生成密钥对象

*   读取公钥：

```java
// 从 公钥保存的文件 读取 公钥的Base64文本
String pubKeyBase64 = IOUtils.readFile(new File("pub.txt"));

// 把 公钥的Base64文本 转换为已编码的 公钥bytes
byte[] encPubKey = new BASE64Decoder().decodeBuffer(pubKeyBase64);

// 创建 已编码的公钥规格
X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);

// 获取指定算法的密钥工厂, 根据 已编码的公钥规格, 生成公钥对象
PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(encPubKeySpec);
```

*   读取私钥：

```java
// 从 私钥保存的文件 读取 私钥的base文本
String priKeyBase64 = IOUtils.readFile(new File("pri.txt"));

// 把 私钥的Base64文本 转换为已编码的 私钥bytes
byte[] encPriKey = new BASE64Decoder().decodeBuffer(priKeyBase64);

// 创建 已编码的私钥规格
PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(encPriKey);

// 获取指定算法的密钥工厂, 根据 已编码的私钥规格, 生成私钥对象
PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(encPriKeySpec);
```

#### 1.3.2 反序列化生成密钥对象

[公钥](https://so.csdn.net/so/search?q=%E5%85%AC%E9%92%A5&spm=1001.2101.3001.7020)和私钥对象被序列号保存后，可以通过反序列化生成回对象。

```java
// 创建对象输如流, 读取保存到指定文件的序列化对象
ObjectInputStream pubIn = new ObjectInputStream(new FileInputStream("pub.obj"));
ObjectInputStream priIn = new ObjectInputStream(new FileInputStream("pri.obj"));

// 从读取输如流读取对象, 反序列化生成 公钥/私钥 对象
PublicKey pubKey = (PublicKey) pubIn.readObject();
PrivateKey priKey = (PrivateKey) priIn.readObject();

// 关闭流
pubIn.close();
priIn.close();
```

# 2、RSA 加密 / 解密数据

----------------

RSA 非对称加密在使用中通常公钥公开，私钥保密，使用公钥加密，私钥解密。例如 客户端 给 服务端 加密发送数据：

1.  客户端从服务端获取公钥；
2.  客户端用公钥先加密要发送的数据，加密后发送给服务端；
3.  服务端拿到加密后的数据，用私钥解密得到原文。

公钥加密后的数据，只有用私钥才能解，只有服务端才有对应的私钥，因此只有服务端能解密，中途就算数据被截获，没有私钥依然不知道数据的原文内容，因此达到数据安全传输的目的。



 `Cipher.ENCRYPT_MODE|Cipher.DECRYPT_MODE`  设置对应模式

### 2.1 公钥加密

```java
// 获取指定算法的密码器
Cipher cipher = Cipher.getInstance("RSA");

// 初始化密码器（公钥加密模型）
cipher.init(Cipher.ENCRYPT_MODE, pubKey); // Cipher.ENCRYPT_MODE|Cipher.DECRYPT_MODE

// 加密数据, 返回加密后的密文
byte[] cipherData = cipher.doFinal(plainData);
```

### 2.2 私钥解密

```java
// 获取指定算法的密码器
Cipher cipher = Cipher.getInstance("RSA");

// 初始化密码器（私钥解密模型）
cipher.init(Cipher.DECRYPT_MODE, priKey);

// 解密数据, 返回解密后的明文
byte[] plainData = cipher.doFinal(cipherData);
```

### 2.3 加密 / 解密 完整代码实例：

```java

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Main {

    public static void main(String[] args) throws Exception {
        // 随机生成一对密钥（包含公钥和私钥）
        KeyPair keyPair = generateKeyPair();
        // 获取 公钥 和 私钥
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey priKey = keyPair.getPrivate();

        // 原文数据
        String data = "你好, World!";

        // 客户端: 用公钥加密原文, 返回加密后的数据
        byte[] cipherData = encrypt(data.getBytes(), pubKey);

        // 服务端: 用私钥解密数据, 返回原文
        byte[] plainData = decrypt(cipherData, priKey);

        // 输出查看解密后的原文
        System.out.println(new String(plainData));  // 结果打印: 你好, World!
    }

    /**
     * 随机生成密钥对（包含公钥和私钥）
     */
    private static KeyPair generateKeyPair() throws Exception {
        // 获取指定算法的密钥对生成器
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");

        // 初始化密钥对生成器（密钥长度要适中, 太短不安全, 太长加密/解密速度慢）
        gen.initialize(2048);

        // 随机生成一对密钥（包含公钥和私钥）
        return gen.generateKeyPair();
    }

    /**
     * 公钥加密数据
     */
    private static byte[] encrypt(byte[] plainData, PublicKey pubKey) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance("RSA");

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        // 加密数据, 返回加密后的密文
        return cipher.doFinal(plainData);
    }

    /**
     * 私钥解密数据
     */
    private static byte[] decrypt(byte[] cipherData, PrivateKey priKey) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance("RSA");

        // 初始化密码器（私钥解密模型）
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        // 解密数据, 返回解密后的明文
        return cipher.doFinal(cipherData);
    }

}
```

# 3、封装 RSA 工具类

-------------

为了在实践中方便使用 RSA 加密 / 解密数据，把 RSA 的 生成密钥对、保存密钥、读取密钥、加密 / 解密 等操作封装到一个工具类中，方便直接使用。

### 3.1 工具类: RSAUtils.java

引用文章开头封装的 IO 工具类：`IOUtils.java`

RSA 工具类（`RSAUtils.java`）完整源码：

```java
package com.xiets.rsa;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 工具类（生成/保存密钥对、加密、解密）
 * 
 */
public class RSAUtils {

    /** 算法名称 */
    private static final String ALGORITHM = "RSA";

    /** 密钥长度 */
    private static final int KEY_SIZE = 2048;

    /**
     * 随机生成密钥对（包含公钥和私钥）
     */
    public static KeyPair generateKeyPair() throws Exception {
        // 获取指定算法的密钥对生成器
        KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM);

        // 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
        gen.initialize(KEY_SIZE);

        // 随机生成一对密钥（包含公钥和私钥）
        return gen.generateKeyPair();
    }

    /**
     * 将 公钥/私钥 编码后以 Base64 的格式保存到指定文件
     */
    public static void saveKeyForEncodedBase64(Key key, File keyFile) throws IOException {
        // 获取密钥编码后的格式
        byte[] encBytes = key.getEncoded();

        // 转换为 Base64 文本
        String encBase64 = new BASE64Encoder().encode(encBytes);

        // 保存到文件
        IOUtils.writeFile(encBase64, keyFile);
    }

    /**
     * 根据公钥的 Base64 文本创建公钥对象
     */
    public static PublicKey getPublicKey(String pubKeyBase64) throws Exception {
        // 把 公钥的Base64文本 转换为已编码的 公钥bytes
        byte[] encPubKey = new BASE64Decoder().decodeBuffer(pubKeyBase64);

        // 创建 已编码的公钥规格
        X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);

        // 获取指定算法的密钥工厂, 根据 已编码的公钥规格, 生成公钥对象
        return KeyFactory.getInstance(ALGORITHM).generatePublic(encPubKeySpec);
    }

    /**
     * 根据私钥的 Base64 文本创建私钥对象
     */
    public static PrivateKey getPrivateKey(String priKeyBase64) throws Exception {
        // 把 私钥的Base64文本 转换为已编码的 私钥bytes
        byte[] encPriKey = new BASE64Decoder().decodeBuffer(priKeyBase64);

        // 创建 已编码的私钥规格
        PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(encPriKey);

        // 获取指定算法的密钥工厂, 根据 已编码的私钥规格, 生成私钥对象
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(encPriKeySpec);
    }

    /**
     * 公钥加密数据
     */
    public static byte[] encrypt(byte[] plainData, PublicKey pubKey) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        // 加密数据, 返回加密后的密文
        return cipher.doFinal(plainData);
    }

    /**
     * 私钥解密数据
     */
    public static byte[] decrypt(byte[] cipherData, PrivateKey priKey) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（私钥解密模型）
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        // 解密数据, 返回解密后的明文
        return cipher.doFinal(cipherData);
    }

}
```

`RSAUtils`工具类中包含的静态方法：

```java
// 随机生成密钥对（包含公钥和私钥）
KeyPair generateKeyPair()
// 将 公钥/私钥 编码后以 Base64 的格式保存到指定文件
void saveKeyForEncodedBase64(Key key, File keyFile)

// 根据公钥的 Base64 文本创建公钥对象
PublicKey getPublicKey(String pubKeyBase64)
// 根据私钥的 Base64 文本创建私钥对象
PrivateKey getPrivateKey(String priKeyBase64)

// 公钥加密数据
byte[] encrypt(byte[] plainData, PublicKey pubKey)
// 私钥解密数据
byte[] decrypt(byte[] cipherData, PrivateKey priKey)
```

### 3.2 RSAUtils 使用实例

引用文章开头封装的 IO 工具类：`IOUtils.java`

```java
package com.xiets.rsa;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Main {

    public static void main(String[] args) throws Exception {
        // 随机生成一对密钥（包含公钥和私钥）
        KeyPair keyPair = RSAUtils.generateKeyPair();
        // 获取 公钥 和 私钥
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey priKey = keyPair.getPrivate();

        // 保存 公钥 和 私钥
        RSAUtils.saveKeyForEncodedBase64(pubKey, new File("pub.txt"));
        RSAUtils.saveKeyForEncodedBase64(priKey, new File("pri.txt"));

        /*
         * 上面代码是事先生成密钥对保存,
         * 下面代码是在实际应用中, 客户端和服务端分别拿现成的公钥和私钥加密/解密数据。
         */

        // 原文数据
        String data = "你好, World!";

        // 客户端: 加密
        byte[] cipherData = clientEncrypt(data.getBytes(), new File("pub.txt"));
        // 服务端: 解密
        byte[] plainData = serverDecrypt(cipherData, new File("pri.txt"));

        // 输出查看原文
        System.out.println(new String(plainData));  // 结果打印: 你好, World!
    }

    /**
     * 客户端加密, 返回加密后的数据
     */
    private static byte[] clientEncrypt(byte[] plainData, File pubFile) throws Exception {
        // 读取公钥文件, 创建公钥对象
        PublicKey pubKey = RSAUtils.getPublicKey(IOUtils.readFile(pubFile));

        // 用公钥加密数据
        byte[] cipher = RSAUtils.encrypt(plainData, pubKey);

        return cipher;
    }

    /**
     * 服务端解密, 返回解密后的数据
     */
    private static byte[] serverDecrypt(byte[] cipherData, File priFile) throws Exception {
        // 读取私钥文件, 创建私钥对象
        PrivateKey priKey = RSAUtils.getPrivateKey(IOUtils.readFile(priFile));

        // 用私钥解密数据
        byte[] plainData = RSAUtils.decrypt(cipherData, priKey);

        return plainData;
    }

}
```