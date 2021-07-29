package com.g.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuffer01 {

    public static void main(String[] args) {
        //创建一个ByteBuf
        /*
        说明：
        1.创建对象，该对象包含一个数组arr，是一个byte[10]
        2.netty的buffer中，不需要使用flip进行反转
            底层维护了一个readerIndex和writerIndex
            buffer.getByte(i)不会造成readerIndex变化
            buffer.readByte()会造成readerIndex变化
        3.通过readerIndex和writerIndex和capacity将buffer分成三个区域
            0 - readerIndex             已经读取区域
            readerIndex - writerIndex   可读区域
            writerIndex - capacity      可写区域
         */
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        //输出
        System.out.println("buffer.capacity() = " + buffer.capacity());//10，初始化的容量
        for (int i = 0; i < buffer.capacity(); i++) {
            //System.out.println("buffer.getByte(i) = " + buffer.getByte(i));
            System.out.println("buffer.readByte() = " + buffer.readByte());
            System.out.println(buffer.readerIndex());
        }
    }
}
