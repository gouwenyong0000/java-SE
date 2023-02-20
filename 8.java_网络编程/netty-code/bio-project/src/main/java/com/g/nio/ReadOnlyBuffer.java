package com.g.nio;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {

    public static void main(String[] args)  {
        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            buffer.put((byte) i);
        }
        //读取
        buffer.flip();
        //得到一个只读的Buffer
        ByteBuffer asReadOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(asReadOnlyBuffer.getClass());

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
