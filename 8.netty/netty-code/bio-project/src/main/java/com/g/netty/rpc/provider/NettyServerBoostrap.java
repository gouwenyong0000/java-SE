package com.g.netty.rpc.provider;

import com.g.netty.rpc.netty.NettyServer;

/**
 * ServerBootstrap会启动一个服务提供者，就是NettyServer
 */
public class NettyServerBoostrap {
    public static void main(String[] args) {
        NettyServer.startServer(7001);
    }

}
