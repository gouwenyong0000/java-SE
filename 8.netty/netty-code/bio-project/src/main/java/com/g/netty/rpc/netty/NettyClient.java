package com.g.netty.rpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NettyClient {
    //创建线程池  Runtime.getRuntime().availableProcessors()  电脑核数
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static NettyClientHandler client;

    private void initClient() {

        client = new NettyClientHandler();

        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());

                            pipeline.addLast(client);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7001).sync();

            Scanner scanner = new Scanner(System.in);
            //注意此处不能关闭客户端的通道
            // ChannelFuture future = channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //编写方法使用代理模式，获取一个代理对象
    public Object getBean(final Class<?> serviceClass, final String providerName) {

        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader()
                , new Class[]{serviceClass}
                , (proxy, method, args) -> {

                    if (client == null) {
                        initClient();
                    }

                    //设置要发给服务器端的信息
                    //providerName协议头args[1]就是客户端调用api hello（？？？），参数
                    client.setPara(providerName + args[0]);

                    return executorService.submit(client).get();
                });
    }
}
