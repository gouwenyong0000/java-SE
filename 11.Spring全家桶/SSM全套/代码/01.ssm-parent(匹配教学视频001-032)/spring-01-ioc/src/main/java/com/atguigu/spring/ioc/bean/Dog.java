package com.atguigu.spring.ioc.bean;


import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;


@ToString
@Data
@Component
public class Dog {

//    @Autowired // 自动注入组件的。基本类型，自己搞。


    /**
     * 1、@Value("字面值"): 直接赋值
     * 2、@Value("${key}")：动态从配置文件中取出某一项的值。
     * 3、@Value("#{SpEL}")：Spring Expression Language；Spring 表达式语言
     *      更多写法：https://docs.spring.io/spring-framework/reference/core/expressions.html
     *
     */
    @Value("旺财")
    private String name;
    @Value("${dog.age}")
    private Integer age;

    @Value("#{10*20}")
    private String color;

    @Value("#{T(java.util.UUID).randomUUID().toString()}")
    private String id;

    @Value("#{'Hello World!'.substring(0, 5)}")
    private String msg;

    @Value("#{new String('haha').toUpperCase()}")
    private String flag;

    @Value("#{new int[] {1, 2, 3}}")
    private int[] hahaha;

    public Dog() {

        String string = UUID.randomUUID().toString();

        System.out.println("Dog构造器...");
    }
}
