package com.g.netty.rpc.netty;

import com.g.netty.rpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "连接...");
        ctx.writeAndFlush("你好  连接成功");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息并调用我们的服务
        System.out.println("msg = " + msg);

        //客户端在调用服务器的api时，需要定义协议:比如我们要求每次发消息是都必须以某个字符串开头"HelloService#helLo#"
        String toString = msg.toString();
        if (toString.startsWith("HelloService#helLo#")){
            String hello = new HelloServiceImpl()
                    .hello(toString.substring(toString.lastIndexOf('#')+1));
            ctx.writeAndFlush(hello);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel();
    }
}
