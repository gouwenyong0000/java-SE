package com.learn.springsecurity.demo.security;

import com.learn.springsecurity.demo.userdetails.MyUserDetailService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * autor:liman
 * createtime:2021/7/18
 * comment: 自定义第三方静默注册的逻辑
 */
@Component
public class SelfConnectionSignUp implements ConnectionSignUp {

//    @Autowired
//    private MyUserDetailService userDetailService;

    /**
     * 根据第三方用户信息静默注册的逻辑
     * @param connection
     * @return
     */
    @Override
    public String execute(Connection<?> connection) {
        //根据第三方用户信息创建用户，并返回静默注册之后的唯一本地用户标示
        String newUserId = connection.getKey().getProviderUserId();//这里简单处理的，直接以第三方用户id为唯一标示
        return newUserId;
    }
}
