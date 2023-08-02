package com.learn.springsecurity.core.authentication;

import com.learn.springsecurity.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * autor:liman
 * createtime:2021/9/5
 * comment: 安全核心模块默认的antMatcher实现
 */
@Component
public class DefaultAuthorizeConifgProvider implements AuthorizeConfigProvider {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        expressionInterceptUrlRegistry.antMatchers("/authentication/require",
                securityProperties.getBrowser().getLoginPage(),
                securityProperties.getBrowser().getSiguUpPage(),//第三方认证跳转的注册页不需要登录认证
                "/authentication/sessiontimeout",//session失效的路径放开登录校验
                "/verifycode/*", "/login/weixin").permitAll();//登录页的请求不需要认证
    }
}
