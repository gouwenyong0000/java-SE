package com.learn.springsecurity.app.resourceconfig;

import com.learn.springsecurity.app.authenticationhandler.SelfAuthenticationFailureHandler;
import com.learn.springsecurity.app.authenticationhandler.SelfAuthenticationSuccessHandler;
import com.learn.springsecurity.app.social.openid.OpenIdAuthenticationSecurityConfig;
import com.learn.springsecurity.core.authentication.AuthorizeConfigManager;
import com.learn.springsecurity.core.verify.ImageVerifyCodeFilter;
import com.learn.springsecurity.core.verify.SmsVerifyCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * autor:liman
 * createtime:2021/7/23
 * comment:
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //自定义登录成功处理器
    @Autowired
    private SelfAuthenticationSuccessHandler selfAuthenticationSuccessHandler;
    @Autowired
    private SelfAuthenticationFailureHandler selfAuthenticationFailureHandler;

    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        ImageVerifyCodeFilter imageVerifyCodeFilter = new ImageVerifyCodeFilter();
//        imageVerifyCodeFilter.setAuthenticationFailureHandler(selfAuthenticationFailureHandler);
//        imageVerifyCodeFilter.setSecurityProperties(securityProperties);
//        imageVerifyCodeFilter.afterPropertiesSet();
//
//        SmsVerifyCodeFilter smsVerifyCodeFilter = new SmsVerifyCodeFilter();
//        smsVerifyCodeFilter.setAuthenticationFailureHandler(selfAuthenticationFailureHandler);
//        smsVerifyCodeFilter.setSecurityProperties(securityProperties);
//        smsVerifyCodeFilter.afterPropertiesSet();



        http
                .apply(openIdAuthenticationSecurityConfig)
                .and()
//                .addFilterBefore(imageVerifyCodeFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(smsVerifyCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()//采用表单登录
                .loginPage("/authentication/require")//指定登录的页面
                .loginProcessingUrl("/authentication/form")//覆盖 UsernamePasswordAuthenticationFilter 中的请求配置，但最终处理这个请求的还是 UsernamePasswordAuthenticationFilter
                .successHandler(selfAuthenticationSuccessHandler)//自定义登录成功处理器
                .failureHandler(selfAuthenticationFailureHandler)
                .and()
                //.authorizeRequests()//并且要认证请求
                //.antMatchers("/authentication/require",
                //        "/authentication/sessiontimeout",//session失效的路径放开登录校验
                //        "/verifycode/*","/authentication/openid").permitAll()//登录页的请求不需要认证
                //.anyRequest()//对任意的请求
                //.authenticated()//都需要做认证
                //.and()
                .csrf().disable();//关闭csrf
        authorizeConfigManager.config(http.authorizeRequests());
    }
}
