package com.learn.springsecurity.core.authentication;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * autor:liman
 * createtime:2021/9/5
 * comment:AuthorizeConfigProvider接口，提供默认的antMatcers的配置
 */
public interface AuthorizeConfigProvider {

    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry);

}
