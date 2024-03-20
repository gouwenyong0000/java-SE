> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [blog.csdn.net](https://blog.csdn.net/whb3299065/article/details/79130365)

在网络传输时，我们接收到的数据都是 10 的形式，八个 01(big) 构成了一个 byte 字节，一个或多个 byte 又构成了不同的数据类型，接收到这些 byte 数组后，我们就需要对数组进行[反序列化](https://so.csdn.net/so/search?q=%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96&spm=1001.2101.3001.7020)为 java 中的对象。而通过 javolution.io.Struct 就可以很容易的实现反序列化（序列化）  
首先，我们要按照数据顺序定义结构体（java 的 class 文件）：该类型必须继承 [Struct](https://so.csdn.net/so/search?q=Struct&spm=1001.2101.3001.7020)，byte 数组中，不同的类型按照 byte 数，一一对应  
如 char 在传输中占用 8bit，所以我们就用 Unsigned8 （无符号 8bit）来接收，int 在占位 32 位，于是我们用 Unsigned32 来接收，char[] 我们通过 UTF8String 来接收。这样我们就定义好了如果接收一个 byte 数组序列了，可以通过以下两个函数指定大小端。

```xml
<dependency>
      <groupId>org.javolution</groupId>
      <artifactId>javolution-core-java</artifactId>
      <version>7.0.0</version>
  </dependency>
```



```java
public class BaseDataDef extends Struct{
    Unsigned8 unsigned8=new Unsigned8();
    Unsigned16 unsigned16=new Unsigned16();
    Unsigned32 Unsigned32=new Unsigned32();
    Signed8 signed8 =new Signed8();
    Signed16 Signed16 =new Signed16();
    Signed32 Signed32 =new Signed32();
    Signed64 Signed64 =new Signed64();
    UTF8String utf8String=new UTF8String(10);
    // 一定要加上这个，不然会出现对齐的问题
    @Override
    public boolean isPacked() {
        return true;
    }

    // 设置为小端数据
    @Override
    public ByteOrder byteOrder() {
        return ByteOrder.LITTLE_ENDIAN;
    }
}
```

然后我们可以将 byte 数据填入到这个类中

```java
@Test
    public void setUp() throws Exception {
        //这里模拟了一下数据的输入
        byte[] data = new byte[221];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        ByteBuffer b = ByteBuffer.wrap(data);
        //小端
        b.order(ByteOrder.LITTLE_ENDIAN);
        BaseDataDef info=new BaseDataDef ();
        //设置byte数组
        info.setByteBuffer(b, 0);
        //打印输出，这里输出byte字符串
        System.out.println(info);
        //如果想打印每一个项，直接
        System.out.println(info.unsigned16);

        System.out.println("#########################");
    }
```

*