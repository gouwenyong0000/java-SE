package com.atguigu.springmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 效果：其实是 SpringBoot 做的。
 * 1、tomcat不用整合
 * 2、servlet开发变得简单，不用实现任何接口
 * 3、自动解决了乱码等问题
 */
@SpringBootApplication
public class Springmvc01HelloworldApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springmvc01HelloworldApplication.class, args);
    }

}
