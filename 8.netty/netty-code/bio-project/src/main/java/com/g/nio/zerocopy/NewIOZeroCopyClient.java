package com.g.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOZeroCopyClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 7001));

        //得到一个文件channel
        String fileName = "protoc-3.6.1-win32.zip";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        FileChannel fileChannel = fileInputStream.getChannel();

        //准备发送
        long start = System.currentTimeMillis();
        //在linux下一个transferTo 方法就可以完成传输
        //在windows 下一次调用transferTo 只能发送8m，就需要分段传输文件，而且要主要
        //传输时的位置   long position  从哪里开始传, long count,传输多少个
        fileChannel.transferTo(0,fileChannel.size(),socketChannel);

        long end = System.currentTimeMillis();
        System.out.println("发送总字节数：" + fileChannel.size()+" 耗 时 " +( end-start));

        //关闭
        fileChannel.close();
    }
}
