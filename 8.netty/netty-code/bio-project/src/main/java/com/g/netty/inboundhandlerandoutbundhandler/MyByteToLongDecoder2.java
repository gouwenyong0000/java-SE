package com.g.netty.inboundhandlerandoutbundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder2======== Byte  >> Long===============");
        //在ReplayingDecoder不需要判断数据是否足够读取，内部会进行处理判断
       // if (in.readableBytes() >= 8) {
            long l = in.readLong();
            out.add(l);
     //   }
    }
}
