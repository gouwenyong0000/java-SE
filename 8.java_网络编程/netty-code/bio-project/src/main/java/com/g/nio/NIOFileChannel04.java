package com.g.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    public static void main(String[] args) throws IOException {
        File file = new File("test.png");

        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("test_bak.png");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        long position = 0;
        while (true) {

            long l = fileOutputStreamChannel.transferFrom(inputStreamChannel, position, 512);
            position += l;
            if (l==0){
                break;
            }
        }

        //关闭相应的流
        fileInputStream.close();
        fileOutputStream.close();
    }
}

