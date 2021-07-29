package com.g.netty.inboundhandlerandoutbundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("收到" + ctx.channel().remoteAddress());
        System.out.println("msg = " + msg);
    }

    //重写channelActive 发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("发送数据");
          ctx.channel().writeAndFlush(123456L);//发送的是一个Long

        /**
         分析
         1."abcdabcdabcdabcd"是16个字节2，该处理器的前一个handler是MyLongToByteEncoder
         3.MyLongToByteEncoder 父类MessageToByteEncoder
         4.父类MessageToByteEncoder   @link{io.netty.handler.codec.MessageToByteEncoder.write()}
<pre>
        public void write (ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            ByteBuf buf = null;
            try {
                if (acceptOutboundMessage(msg)) {//判断当前写出的类型是否有相应的编码器，如果是就处理，不是就跳过encode
                    @SuppressWarnings("unchecked") I cast = (I) msg;
                    buf = allocateBuffer(ctx, cast, preferDirect);
                    try {
                        encode(ctx, cast, buf);
                    } finally {
                        ReferenceCountUtil.release(cast);
                    }

                    if (buf.isReadable()) {
                        ctx.write(buf, promise);
                    } else {
                        buf.release();
                        ctx.write(Unpooled.EMPTY_BUFFER, promise);
                    }
                    buf = null;
                } else {
                    ctx.write(msg, promise);
                }
            } catch (EncoderException e) {
                throw e;
            } catch (Throwable e) {
                throw new EncoderException(e);
            } finally {
                if (buf != null) {
                    buf.release();
                }
            }
        }
</pre>
       4，因此我们编写Encoder是要注意传入的数据类型和处理的数据类型
         写出下面数据，不会进入自定义的long解码器
         */

//        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", StandardCharsets.UTF_8));
    }
}
