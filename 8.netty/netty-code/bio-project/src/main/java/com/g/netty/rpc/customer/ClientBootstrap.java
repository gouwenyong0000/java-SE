package com.g.netty.rpc.customer;

import com.g.netty.rpc.netty.NettyClient;
import com.g.netty.rpc.publicinterface.HelloService;

import java.time.LocalDateTime;

public class ClientBootstrap {
    //这里定义协议头
    private static final String providerName = "HelloService#helLo#";

    public static void main(String[] args) {
        //创建一个消费者
        NettyClient customer = new NettyClient();

        //创建代理对象
        HelloService bean = (HelloService) customer.getBean(HelloService.class, providerName);

        for (int i = 0; i < 100; i++) {
            //通过代理对象调用服务提供者的方法（服务）
            String hello = bean.hello("你好" + LocalDateTime.now());
            System.out.println(hello);
        }
    }
}
