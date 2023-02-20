package com.g.netty.buf;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class NettyByteBuffer02 {

    public static void main(String[] args) {
        //创建byteBuffer
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world", StandardCharsets.UTF_8);

        //使用相关的方法
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            String msg = new String(content);
            System.out.println(msg);//hello world
            System.out.println(byteBuf.arrayOffset());//0
            System.out.println(byteBuf.readerIndex());// 0
            System.out.println(byteBuf.writerIndex());//11
            System.out.println("可读取字节" + byteBuf.readableBytes());// 可读取字节 = 11

            System.out.println(byteBuf.getCharSequence(0, 4, StandardCharsets.UTF_8));// hell
        }


    }
}
