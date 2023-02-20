package com.g.netty.tcp_protocol;


import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.Date;


public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接收到数据，并处理
        String s = new String(msg.getContent(), StandardCharsets.UTF_8);
        System.out.println("内容 =  " + s);
        System.out.println("长度 =  " + msg.getLen());
        System.out.println(++count);

        //服务器回送数据给客户端，构建一个协议包MessageProtocol  --> 到编码器 OutboundHandler
        MessageProtocol sendMsg = new MessageProtocol();
        byte[] bytes = (new Date().toLocaleString() + "收到消息").getBytes(StandardCharsets.UTF_8);
        sendMsg.setLen(bytes.length);
        sendMsg.setContent(bytes);

        ctx.writeAndFlush(sendMsg);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
