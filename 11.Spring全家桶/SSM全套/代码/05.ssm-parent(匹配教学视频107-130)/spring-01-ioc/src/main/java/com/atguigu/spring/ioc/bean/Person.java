package com.atguigu.spring.ioc.bean;


import lombok.Data;

@Data
public class Person {
    private String name;
    private int age;
    private String gender;

    public Person() {
        System.out.println("Person()构造器执行了...");
    }
}
