package com.atguigu.spring.aop.service.impl;

import com.atguigu.spring.aop.annotation.MyAn;
import com.atguigu.spring.aop.service.UserService;
import org.springframework.stereotype.Component;


@MyAn
@Component
public class UserServiceImpl implements UserService {
    @Override
    public void saveUser() {
        System.out.println("业务：保存用户");
    }

    @Override
    public void getUserHaha(int i, int j) {
        System.out.println("业务：查询用户");
    }


    @MyAn
    @Override
    public void updateUser() {
        System.out.println("哈哈哈.....有@MyAn");
    }
}
