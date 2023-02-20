package com.g.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();
        /*加入一个netty提供的httpservercodec codec =>[coder-decoder]
        HttpServerCodec说明
        1.Httpservercodec 是netty提供的处理http的编-解码器*/
        pipeline.addLast("MyHttpServerCoded",new HttpServerCodec());
        //2.增加一个自定义的编解码器
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());

    }
}
