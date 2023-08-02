package com.learn.springsecurity.core.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * autor:liman
 * createtime:2021/9/5
 * comment:默认的AuthorizeConfigManager
 */
@Component
public class DefaultAuthorizeConfigManager implements AuthorizeConfigManager{

    @Autowired
    private Set<AuthorizeConfigProvider> authorizeConfigProviders;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {

        for(AuthorizeConfigProvider authorizeConfigProvider:authorizeConfigProviders){
            authorizeConfigProvider.config(expressionInterceptUrlRegistry);
        }
        //除了配置中不需要认证即可访问的url之外，其余的都需要认证才能访问
        expressionInterceptUrlRegistry.anyRequest().authenticated();
    }
}
