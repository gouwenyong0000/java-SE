package com.g.netty.rpc.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;


public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;//上下文
    private String result;//返回的结果
    private String para;//客户端调用方法时，传入的参数

    //与服务器的连接创建后，就会被调用  这个方法是第一个被调用【1】
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接到远程服务器" + ctx.channel().remoteAddress());
        context = ctx;//因为我们在其它方法会使用到ctx
    }

    //收到服务器的数据后，调用方法【4】
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("NettyClientHandler.channelRead----------1");
        result = msg.toString();
        System.out.println(result);
        System.out.println("NettyClientHandler.channelRead----------2");
        notifyAll();//唤醒等待的线程
    }

    @Override
    public synchronized void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    //被代理对象调用，发送数据给服务器，-> wait->等待被唤醒->返回结果   【3】 结束等待拿到锁 【5】
    @Override
    public synchronized Object call() throws Exception {

        System.out.println("NettyClientHandler.call---1");
        //连接时已经初始化了连接
        context.writeAndFlush(Unpooled.copiedBuffer(para, StandardCharsets.UTF_8));
        System.out.println("发送消息成功");
        wait();//等待channelRead方法获取到服务器的结果后，唤醒
        System.out.println("NettyClientHandler.call---2");
        return result;//服务方返回的结果

    }

    //设置参数【2】
    public void setPara(String para) {
        this.para = para;
    }
}
