package com.learn.springsecurity.demo.config;

import com.learn.springsecurity.core.authentication.AuthorizeConfigProvider;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * autor:liman
 * createtime:2021/9/5
 * comment:
 */
@Component
@Order(Integer.MAX_VALUE)
public class DemoAuthorizationProvider implements AuthorizeConfigProvider {
    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        //expressionInterceptUrlRegistry.antMatchers("/user/*")
        //        .hasRole("ROLE_ADMIN");
        //TODO：这个需要放在最后读取
        expressionInterceptUrlRegistry.anyRequest().access("@rbacService.hasPermission(request,authentication)");
    }
}
