package com.learn.springsecurity.core.config;

import com.learn.springsecurity.core.authentication.mobile.SmsVerifyCodeAuthenticationFilter;
import com.learn.springsecurity.core.authentication.mobile.SmsVerifyCodeAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * autor:liman
 * createtime:2021/7/11
 * comment:短信验证码的登录配置
 */
@Component
public class SmsVerifyCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity> {

    @Autowired
    private AuthenticationSuccessHandler selfAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler selfAuthenticationFailureHandler;

    //这个可以是我们自定义的用户服务类
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //实例和初始化我们自定义的短信认证Filter
        SmsVerifyCodeAuthenticationFilter smsVerifyCodeAuthenticationFilter = new SmsVerifyCodeAuthenticationFilter();
        smsVerifyCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsVerifyCodeAuthenticationFilter.setAuthenticationSuccessHandler(selfAuthenticationSuccessHandler);
        smsVerifyCodeAuthenticationFilter.setAuthenticationFailureHandler(selfAuthenticationFailureHandler);

        //实例和初始化我们自定义的认证Provider
        SmsVerifyCodeAuthenticationProvider smsVerifyCodeAuthenticationProvider = new SmsVerifyCodeAuthenticationProvider();
        smsVerifyCodeAuthenticationProvider.setUserDetailsService(userDetailsService);

        //将我们自定义的认证Provider和Filter交给spring-security进行托管，将过滤器插入到UsernamePasswordAuthenticationFilter过滤器之前
        http.authenticationProvider(smsVerifyCodeAuthenticationProvider)
                .addFilterAfter(smsVerifyCodeAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);

    }
}

