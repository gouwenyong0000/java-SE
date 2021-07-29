package com.g.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NIOFileChannel01 {

    public static void main(String[] args) throws IOException {

        //创建一个输入流 -> channel
        File file = new File("d:\\file01.txt");
        FileInputStream inputStream = new FileInputStream(file);

        //通过fileinputstream获取对应的Filechannel->实际类型FilechannelImpl
        FileChannel channel = inputStream.getChannel();

        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());

        //将通道的数据读入到Buffer   返回-1表示读取完毕
        channel.read(buffer);

        //将byteBuffer的字节数据转成string    array()返回内部的数组
        System.out.println(new String(buffer.array()));

        inputStream.close();
    }
}
