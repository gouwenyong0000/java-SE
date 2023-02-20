package com.g.netty.inboundhandlerandoutbundhandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer  extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //入站的handler进行解码MyByteToLongDecoder
        pipeline.addLast(new MyByteToLongDecoder());

        //出站的handler进行编码
        pipeline.addLast(new MyLongToByteEncoder());

        //自定义
        pipeline.addLast(new MyServerHandler());
    }
}
