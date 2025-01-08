package com.atguigu.boot.service;


import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String sayHello() {
        return "Hello";
    }

    public void hello() {
//        int i = 10/0;
        System.out.println("Hello");
    }
}
