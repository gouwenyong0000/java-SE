package com.atguigu.spring.ioc.config;


import com.atguigu.spring.ioc.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {


    @Bean(initMethod = "initUser",destroyMethod = "destoryUser")
    public User user(){
        return new User();
    }
}
