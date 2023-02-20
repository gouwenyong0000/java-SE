package com.g.netty.rpc.provider;

import com.g.netty.rpc.publicinterface.HelloService;

import java.time.LocalDateTime;

public class HelloServiceImpl implements HelloService {
    //当有消费方调用该方法时，就返回一个结果
    @Override
    public String hello(String mes) {
        System.out.println("收到客户端参数   ：" + mes);
        //根据mes返回不同的结果
        if (mes != null) {
            return "远程结果" + LocalDateTime.now() + "【" + mes + "】";
        } else {
            return "远程结果" + LocalDateTime.now() + "【" + "】";
        }
    }
}
