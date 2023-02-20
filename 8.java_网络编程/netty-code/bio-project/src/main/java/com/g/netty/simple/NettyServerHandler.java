package com.g.netty.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 说明：
 * 1.我们自定义一个handler需要继承netty规定好的handlerAdapter
 * 2.这时我们自定义一个Handler，才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**读取数据实际（这里我们可以读取客户端发送的消息）
        1.ChannelHandlercontext ctx：上下文对象，含有管道pipeline，通道channel，地址
        2.object msg：就是客户端发送的数据默认lobject
    */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//        System.out.println("服务器读取线程："+Thread.currentThread().getName());
//        System.out.println("server ctx = " + ctx);
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = channel.pipeline();//本质是一个双向链接，出站入站
//
//
//        //将msg转 成一个ByteBuf
//        //ByteBuf是Netty提供的，不是NIO的ByteBuffer.
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println("客户端消息：" + byteBuf.toString(StandardCharsets.UTF_8));
//        System.out.println("客户端地址：" + channel.remoteAddress());


        //比如这里我们有一个非常耗费时间的业务--> 异步执行  提交到该channel的NIOEventLoop的taskQueue
//        Thread.sleep(10000);
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello 客户端 阻塞的消息111",StandardCharsets.UTF_8));

        //解决方案1：用户自定义普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello 客户端 自定义task的消息222",StandardCharsets.UTF_8));
            }
        });
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);//两个任务提交到一个线程的队列中，排队执行，加上上一个任务阻塞的10 +20 =30
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello 客户端 自定义task的消息333",StandardCharsets.UTF_8));
            }
        });

        //解决方案2：用户自定义定时任务  >> 该任务是提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);//两个任务提交到一个线程的队列中，排队执行，加上上一个任务阻塞的10 +20 =30
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello 客户端 自定义task的消息444",StandardCharsets.UTF_8));
            }
        },5, TimeUnit.SECONDS);//延时5秒执行


        System.out.println("go on...");

    }
    //读取数据完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是write*flush
    //将数据写入到缓存，并刷新
    //一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello ,客户端---",StandardCharsets.UTF_8));
    }
    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
