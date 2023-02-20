package com.g.netty.tcp_protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送10条数据
        for (int i = 0; i < 5; i++) {
            String msg = "hello 服务器" + i;
            byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);

            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(bytes.length);
            messageProtocol.setContent(bytes);

            ctx.writeAndFlush(messageProtocol);

        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        byte[] bytes = new byte[msg.getLen()];

        String s = new String(msg.getContent(), StandardCharsets.UTF_8);

        System.out.println("客户端接收到数据 = " + s);
        System.out.println("服务器接受消息量 = " + (++count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
