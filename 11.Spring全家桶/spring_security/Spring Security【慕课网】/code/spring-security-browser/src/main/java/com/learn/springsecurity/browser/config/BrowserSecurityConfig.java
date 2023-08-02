package com.learn.springsecurity.browser.config;

import com.learn.springsecurity.browser.authenticationhandler.SelfAuthenticationFailureHandler;
import com.learn.springsecurity.browser.authenticationhandler.SelfAuthenticationSuccessHandler;
import com.learn.springsecurity.browser.authenticationhandler.SelfLogoutSuccessHandler;
import com.learn.springsecurity.browser.session.SelfExpireSessionStrategy;
import com.learn.springsecurity.core.authentication.AuthorizeConfigManager;
import com.learn.springsecurity.core.config.SmsVerifyCodeAuthenticationSecurityConfig;
import com.learn.springsecurity.core.properties.SecurityProperties;
import com.learn.springsecurity.core.verify.ImageVerifyCodeFilter;
import com.learn.springsecurity.core.verify.SmsVerifyCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * autor:liman
 * createtime:2021/7/6
 * comment:
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;
    //自定义登录成功处理器
    @Autowired
    private SelfAuthenticationSuccessHandler selfAuthenticationSuccessHandler;
    @Autowired
    private SelfAuthenticationFailureHandler selfAuthenticationFailureHandler;

    @Autowired
    private SmsVerifyCodeAuthenticationSecurityConfig smsVerifyCodeAuthenticationSecurityConfig;

    @Autowired
    private SpringSocialConfigurer selfSocialSecurityConfig;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy selfExpireSessionStrategy(){
        return new SelfExpireSessionStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public SelfLogoutSuccessHandler logoutSuccessHandler(){
        return new SelfLogoutSuccessHandler(securityProperties);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.formLogin()//采用表单登录
//                .and()
//                .authorizeRequests()//并且要认证请求
//                .anyRequest()//对任意的请求
//                .authenticated();//都需要做认证

        ImageVerifyCodeFilter imageVerifyCodeFilter = new ImageVerifyCodeFilter();
        imageVerifyCodeFilter.setAuthenticationFailureHandler(selfAuthenticationFailureHandler);
        imageVerifyCodeFilter.setSecurityProperties(securityProperties);
        imageVerifyCodeFilter.afterPropertiesSet();

        SmsVerifyCodeFilter smsVerifyCodeFilter = new SmsVerifyCodeFilter();
        smsVerifyCodeFilter.setAuthenticationFailureHandler(selfAuthenticationFailureHandler);
        smsVerifyCodeFilter.setSecurityProperties(securityProperties);
        smsVerifyCodeFilter.afterPropertiesSet();



        http
                //.addFilterBefore(imageVerifyCodeFilter, UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(smsVerifyCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()//采用表单登录
                .loginPage("/authentication/require")//指定登录的页面
                .loginProcessingUrl("/authentication/form")//覆盖 UsernamePasswordAuthenticationFilter 中的请求配置，但最终处理这个请求的还是 UsernamePasswordAuthenticationFilter
                .successHandler(selfAuthenticationSuccessHandler)//自定义登录成功处理器
                .failureHandler(selfAuthenticationFailureHandler)
                .and()
                .sessionManagement()
                .invalidSessionUrl("/authentication/sessiontimeout")//这里配置session失效的url
                .maximumSessions(1)//最大会话数
                .expiredSessionStrategy(selfExpireSessionStrategy())//自定义的淘汰会话策略
                .maxSessionsPreventsLogin(true)
                .and()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler())//这个与下面的logoutSuccessUrl互斥
                .logoutSuccessUrl("/demo-logout.html")
                .deleteCookies("JSESSIONID")
                .and()
                .authorizeRequests()//并且要认证请求

                //.antMatchers("/authentication/require", securityProperties.getBrowser().getLoginPage(),
                //        securityProperties.getBrowser().getSiguUpPage(),//第三方认证跳转的注册页不需要登录认证
                //        "/authentication/sessiontimeout",//session失效的路径放开登录校验
                //        "/verifycode/*","/login/weixin").permitAll()//登录页的请求不需要认证
                //.antMatchers(HttpMethod.POST,"/user/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/user/*").access("hasRole('ADMIN') and hasIpAddress('192.168.0.103')")
                //.anyRequest()//对其余任意的请求
                //.authenticated()//都需要做认证
                .and()
                .csrf().disable()//关闭csrf
                .apply(smsVerifyCodeAuthenticationSecurityConfig)//导入短信验证码登录的安全配置
                .and()
                .apply(selfSocialSecurityConfig);//引入社交登录的配置

        authorizeConfigManager.config(http.authorizeRequests());



    }
}
