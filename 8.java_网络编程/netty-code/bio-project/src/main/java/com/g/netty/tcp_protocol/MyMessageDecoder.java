package com.g.netty.tcp_protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder.decode");

        if (in.readableBytes() > 0) {
            //需要将得到二进制字节码> MessageProtocol 数据包（对象
            int msgLen = in.readInt();
            byte[] bytes = new byte[msgLen];
            in.readBytes(bytes);

            //封装成MessageProtocol对象，放入out，传递下一个handler业务处理
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(msgLen);
            messageProtocol.setContent(bytes);

            out.add(messageProtocol);
        }

    }
}
