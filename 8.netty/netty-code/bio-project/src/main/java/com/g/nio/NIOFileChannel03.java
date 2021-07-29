package com.g.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {

        File file = new File("1.txt");

        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();


        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        while (true){

            //这里有一个重要的操作，一定不要忘了
            /* 重置标志位，清空buffer，重置position位置，之前写出是position等于limit
                public final Buffer clear() {
                    position = 0;
                    limit = capacity;
                    mark = -1;
                    return this;
                }
             */
            byteBuffer.clear();

            //从通道读取数据到缓冲区
            int len = inputStreamChannel.read(byteBuffer);
            if (len == -1){//表示读取完毕
                break;
            }
            //反转 position
            byteBuffer.flip();

            //将缓冲区数据写入到通道
            fileOutputStreamChannel.write(byteBuffer);
        }

        //关闭相应的流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
