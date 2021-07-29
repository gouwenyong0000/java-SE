package com.g.nio;

import java.nio.ByteBuffer;

public class ByteBufferPutGet {
    public static void main(String[] args) {
        //创建一个Buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);

        //类型化方式放入数据
        buffer.putInt(100);
        buffer.putLong(9L);
        buffer.putChar('尚');
        buffer.putShort((short) 4);

        //反转
        buffer.flip();

        //取出
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());

    }
}
