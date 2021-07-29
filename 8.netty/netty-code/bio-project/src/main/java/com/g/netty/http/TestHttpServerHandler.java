package com.g.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * 1. SimpleChannelInboundHandler 是ChannelInboundHandlerAdapter 的子类
 * 2. HttpObject 表示客户端和服务器端相互通讯的数据被封装成HttpObject类型
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        //判断msg是不是一个httpRequest请求
        if (msg instanceof HttpRequest) {
            System.out.println("msg.getClass() = " + msg.getClass());
            System.out.println("客户端地址：" + ctx.channel().remoteAddress());


            //对浏览器请求服务器资源过滤    图标进行拦截
            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.uri());
            if("favicon.ico".equals(uri.getPath())){
                System.out.println("请求了favicon.ico，不做响应" );
                return;
            }

            //回复信息给浏览器  【http协议不是长链接，使用一次即断开】
            ByteBuf content = Unpooled.copiedBuffer("hello 我是服务器".getBytes(StandardCharsets.UTF_8));
            //构造-个http的相应，即httpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1
                    , HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将构建好的response返回
            ctx.writeAndFlush(response);

        }
    }
}
