package com.g.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
  /*  定义一个channle组，管理所有的channel
    GlobalEventExecutor.INSTANCE是一个全局的事件执行器，是一个单例*/
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf=  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //handlerAdded 表示链接建立，一旦连接上，第一个被执行
    //将当前channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        /*
        将该客户加入聊天的信息推送给其他的客户端
            该方法会将channelGroup 中所有的channel遍历，并发送消息，
            我们不需要自己遍历
         */
        channelGroup.writeAndFlush("客户端" + channel.remoteAddress() + "加入聊天\n");
        channelGroup.add(channel);
    }

    //表示channel处于活动状态，提示xx上
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了\n");
    }
    //表示channel处于不活动状态，提示xx下线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了\n");
    }
    //断开连接 将xx客户离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("客户端" + channel.remoteAddress() + "离开聊天\n");
       // channelGroup会自动去掉该channel
        System.out.println("在线人数" + channelGroup.size());
    }
    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到当前channel
        Channel channel = ctx.channel();

        //遍历channelGroup 根据不同的情况会送不同的消息
        channelGroup.forEach(ch -> {
            if (ch != channel){//不是当前的channel，转发消息
                ch.writeAndFlush("[客户 " + channel.remoteAddress()+" ] -->" + msg);
            }else {
                ch.writeAndFlush("[我 " +" ] <--" + msg);
            }
        });
    }
    //发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
