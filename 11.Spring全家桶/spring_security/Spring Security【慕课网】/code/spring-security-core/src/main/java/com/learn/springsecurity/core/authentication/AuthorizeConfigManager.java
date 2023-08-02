package com.learn.springsecurity.core.authentication;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * autor:liman
 * createtime:2021/9/5
 * comment:管理，并获取容器中所有的AuthorizeConfigProvider
 */
public interface AuthorizeConfigManager {

    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry);

}
