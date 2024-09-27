package com.phei.netty._6_frame._5_field_length;

import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;

import java.net.SocketAddress;

/**
 * 模拟测试对象
 */
public class ConstructTestObject implements ChannelHandlerContext {
    @Override
    public Channel channel() {
        return null;
    }

    @Override
    public EventExecutor executor() {
        return null;
    }

    @Override
    public ChannelHandlerInvoker invoker() {
        return null;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public ChannelHandler handler() {
        return null;
    }

    @Override
    public boolean isRemoved() {
        return false;
    }

    @Override
    public ChannelHandlerContext fireChannelRegistered() {
        return null;
    }

    @Override
    public ChannelHandlerContext fireChannelActive() {
        return null;
    }

    @Override
    public ChannelHandlerContext fireChannelInactive() {
        return null;
    }

    @Override
    public ChannelHandlerContext fireExceptionCaught(Throwable cause) {
        return null;
    }

    @Override
    public ChannelHandlerContext fireUserEventTriggered(Object event) {
        return null;
    }

    @Override
    public ChannelHandlerContext fireChannelRead(Object msg) {
        return null;
    }

    @Override
    public ChannelHandlerContext fireChannelReadComplete() {
        return null;
    }

    @Override
    public ChannelHandlerContext fireChannelWritabilityChanged() {
        return null;
    }

    @Override
    public ChannelFuture bind(SocketAddress localAddress) {
        return null;
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress) {
        return null;
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return null;
    }

    @Override
    public ChannelFuture disconnect() {
        return null;
    }

    @Override
    public ChannelFuture close() {
        return null;
    }

    @Override
    public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
        return null;
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
        return null;
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        return null;
    }

    @Override
    public ChannelFuture disconnect(ChannelPromise promise) {
        return null;
    }

    @Override
    public ChannelFuture close(ChannelPromise promise) {
        return null;
    }

    @Override
    public ChannelHandlerContext read() {
        return null;
    }

    @Override
    public ChannelFuture write(Object msg) {
        return null;
    }

    @Override
    public ChannelFuture write(Object msg, ChannelPromise promise) {
        return null;
    }

    @Override
    public ChannelHandlerContext flush() {
        return null;
    }

    @Override
    public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
        return null;
    }

    @Override
    public ChannelFuture writeAndFlush(Object msg) {
        return null;
    }

    @Override
    public ChannelPipeline pipeline() {
        return null;
    }

    @Override
    public ByteBufAllocator alloc() {
        return new ByteBufAllocator() {
            @Override
            public ByteBuf buffer() {
                return null;
            }

            @Override
            public ByteBuf buffer(int initialCapacity) {
                return Unpooled.buffer(initialCapacity);
            }

            @Override
            public ByteBuf buffer(int initialCapacity, int maxCapacity) {
                return null;
            }

            @Override
            public ByteBuf ioBuffer() {
                return null;
            }

            @Override
            public ByteBuf ioBuffer(int initialCapacity) {
                return null;
            }

            @Override
            public ByteBuf ioBuffer(int initialCapacity, int maxCapacity) {
                return null;
            }

            @Override
            public ByteBuf heapBuffer() {
                return null;
            }

            @Override
            public ByteBuf heapBuffer(int initialCapacity) {
                return null;
            }

            @Override
            public ByteBuf heapBuffer(int initialCapacity, int maxCapacity) {
                return null;
            }

            @Override
            public ByteBuf directBuffer() {
                return null;
            }

            @Override
            public ByteBuf directBuffer(int initialCapacity) {
                return null;
            }

            @Override
            public ByteBuf directBuffer(int initialCapacity, int maxCapacity) {
                return null;
            }

            @Override
            public CompositeByteBuf compositeBuffer() {
                return null;
            }

            @Override
            public CompositeByteBuf compositeBuffer(int maxNumComponents) {
                return null;
            }

            @Override
            public CompositeByteBuf compositeHeapBuffer() {
                return null;
            }

            @Override
            public CompositeByteBuf compositeHeapBuffer(int maxNumComponents) {
                return null;
            }

            @Override
            public CompositeByteBuf compositeDirectBuffer() {
                return null;
            }

            @Override
            public CompositeByteBuf compositeDirectBuffer(int maxNumComponents) {
                return null;
            }

            @Override
            public boolean isDirectBufferPooled() {
                return false;
            }
        };
    }

    @Override
    public ChannelPromise newPromise() {
        return null;
    }

    @Override
    public ChannelProgressivePromise newProgressivePromise() {
        return null;
    }

    @Override
    public ChannelFuture newSucceededFuture() {
        return null;
    }

    @Override
    public ChannelFuture newFailedFuture(Throwable cause) {
        return null;
    }

    @Override
    public ChannelPromise voidPromise() {
        return null;
    }

    @Override
    public <T> Attribute<T> attr(AttributeKey<T> key) {
        return null;
    }
}
