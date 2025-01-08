package com.atguigu.boot.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {




    @Value("${hello.msg}")
    String name;

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot!" + name;
    }
}
