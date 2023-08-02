package com.learn.springsecurity.sso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * autor:liman
 * createtime:2021/8/16
 * comment:
 */
@Configuration
//有了这个注解，我们的应用就是一个OAuth认证服务器了
@EnableAuthorizationServer
public class SsoAuthenticationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 配置认证服务器的appid与appSecret（认证服务器可以给哪些应用访问），这里为了简单直接配置在内存里头
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //第一个应用
                .withClient("selfSSOClient01")
                .secret("ssoClientSecret01")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")
                .and()
                //第二个应用
                .withClient("selfSSOClient02")
                .secret("ssoClientSecret02")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all");
    }

    /**
     * 配置获取签名的密钥需要通过认证
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //业务应用为了验证JWT需要获取认证服务器签发JWT的密钥
        //这里配置的就是业务应用服务器为了获取认证服务器签发JWT的密钥，需要通过认证服务器的认证操作
        security.tokenKeyAccess("isAuthenticated()");//获取签名的密钥需要认证
    }

    /**
     * 生成令牌的配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter());
    }

    //配置JWT的tokenStore
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        String signKey = "self-sso-server";
        accessTokenConverter.setSigningKey(signKey);//设置生成签名的密钥
        return accessTokenConverter;
    }
}
