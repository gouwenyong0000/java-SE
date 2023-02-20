package com.g.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


public class MyServer {

    public static void main(String[] args) throws InterruptedException {

        //创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))//在bossGroup增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            //因为基于http协议，使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写，添加ChunkedWrite处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                           /*
                            说明:
                             1. 因为http的数据在传输过程中是分段的，HttpObjectAggregator就是可以将多个段聚合起来
                             2.这就就是为什么，当浏览器发送大量数据时，就会发出多次http请求
                           */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                            说明：
                            1.对于websocket，它的数据是以帧(frame)形式传递
                            2.可以看到WebSocketFrame下面有六个子类
                            3．浏览器请求时ws://lLocalhost:7000/hello表示请求的uri
                            4.WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议，保持长连接
                            5.是通过一个状态码 101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义的handler  处理业务逻辑
                            pipeline.addLast(new MyWebSocketFrameHandler());
                        }
                    });
            //启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            //关闭服务器
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

