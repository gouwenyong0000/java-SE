package com.atguigu.spring.aop.service;

import com.atguigu.spring.aop.annotation.MyAn;
import org.springframework.beans.factory.annotation.Qualifier;

public interface UserService {

    void saveUser();
    void getUserHaha(int i,int j);

    void updateUser();
}
