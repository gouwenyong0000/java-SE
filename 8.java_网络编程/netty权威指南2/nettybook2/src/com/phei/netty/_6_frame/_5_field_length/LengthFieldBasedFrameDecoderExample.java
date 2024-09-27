package com.phei.netty._6_frame._5_field_length;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * LengthFieldBasedFrameDecoder
 * <p>
 * 源码解码过程：
 * ①  frameLength  //从协议头中读取的长度  通过 lengthFieldOffset、lengthFieldLength定位解析出
 * ②  frameLength += lengthAdjustment + lengthFieldEndOffset【lengthFieldOffset + lengthFieldLength】 // 协议帧总长度
 * ③  in.skipBytes(initialBytesToStrip);丢弃开头的字节数  initialBytesToStrip，调整读取索引的位置
 * ④  actualFrameLength = frameLengthInt - initialBytesToStrip;// 从byeBuff中截取最终长度
 * <p>
 * 示例：
 * lengthFieldOffset   =  1
 * lengthFieldLength   =  2
 * lengthAdjustment    = -3 (= the length of HDR1 + LEN, negative)
 * initialBytesToStrip =  3
 * <p>
 * 解码前(16 bytes)                               解码后 (13 bytes)
 * +------+--------+------+----------------+      +------+----------------+
 * | HDR1 | Length | HDR2 | Actual Content |----->| HDR2 | Actual Content |
 * | 0xCA | 0x0010 | 0xFE | "HELLO, WORLD" |      | 0xFE | "HELLO, WORLD" |
 * +------+--------+------+----------------+      +------+----------------+
 * <p>
 * 结合源码结算过程： 命令帧16byte，包含HDR1、Length、HDR2、Actual Content
 * ①  0x0010 = 16
 * ②  16 + lengthAdjustment 【-3】+ lengthFieldEndOffset【lengthFieldOffset【1】 + lengthFieldLength【2】】 = 16
 * <p>
 * ③调整readIndex位置
 * ④ 命令帧减去丢弃的字节数，得到实际帧长度 13
 */
public class LengthFieldBasedFrameDecoderExample {
    /**
     * 主函数入口
     * 本函数演示了如何使用LengthFieldBasedFrameDecoder对字节缓冲区进行解码
     * 通过反射机制调用LengthFieldBasedFrameDecoder的decode方法来处理缓冲区数据
     *
     * @param args 命令行参数
     * @throws Exception 可能抛出的异常
     */
    public static void main(String[] args) throws Exception {
        // 获取LengthFieldBasedFrameDecoder类 反射获取decode方法
        Class cls = LengthFieldBasedFrameDecoder.class;
        // 获取LengthFieldBasedFrameDecoder类中的decode方法
        Method decode = cls.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class);
        // 设置decode方法可访问
        decode.setAccessible(true);

        // 创建并初始化一个ByteBuf对象 构建命令帧
        ByteBuf buffer = Unpooled.buffer(16);
        buffer.writeByte(0xCA);
        buffer.writeShort(0x0010);
        buffer.writeByte(0xFE);
        buffer.writeBytes("HELLO, WORLD".getBytes());
        // 创建测试对象
        ConstructTestObject testObject = new ConstructTestObject();

        // 打印buffer的内容
        System.out.println(Arrays.toString(buffer.array()));

        // 创建LengthFieldBasedFrameDecoder实例
        LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder = new LengthFieldBasedFrameDecoder(
                65535, // 最大帧长度
                1, // 长度字段偏移量
                2, // 长度字段长度
                -3, // 长度调整值
                0 // 初始字节剥离量
        );

        // 使用反射调用decode方法
        ByteBuf result = (ByteBuf) decode.invoke(lengthFieldBasedFrameDecoder, testObject, buffer);
        // 打印解码后的结果
        System.out.println(Arrays.toString(result.array()));

//*lengthFieldOffset = 1
//* lengthFieldLength = 2
//* lengthAdjustment = -3 ( = the length of HDR1 + LEN, negative)
//*initialBytesToStrip = 3
//src：         [-54, 0, 16, -2, 72, 69, 76, 76, 79, 44, 32, 87, 79, 82, 76, 68]
//afterDecoder：[-2, 72, 69, 76, 76, 79, 44, 32, 87, 79, 82, 76, 68]

//*lengthFieldOffset = 1
//* lengthFieldLength = 2
//* lengthAdjustment = -3 ( = the length of HDR1 + LEN, negative)
//*initialBytesToStrip = 0
//src：         [-54, 0, 16, -2, 72, 69, 76, 76, 79, 44, 32, 87, 79, 82, 76, 68]
//afterDecoder：[-54, 0, 16, -2, 72, 69, 76, 76, 79, 44, 32, 87, 79, 82, 76, 68]

    }
}

