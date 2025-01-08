package com.atguigu.spring.ioc.bean;


import com.atguigu.spring.ioc.annotation.UUID;
import lombok.Data;

@Data
public class Car {


    @UUID
    private String id;
}
