package com.learn.springsecurity.app.config;

import com.learn.springsecurity.app.jwt.SelfJwtTokenEnhancer;
import com.learn.springsecurity.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * autor:liman
 * createtime:2021/8/9
 * comment: token存储的配置
 */
@Configuration
public class TokenStoreConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
//    @Autowired
//    private DataSource dataSource;

    @Bean
    //如果是存在self.security.core.oauth2.storeType的配置项，且该配置的值为 redis，如下配置才会生效，matchIfMissing=false 表示如果没有这个配置项，这个不生效
    @ConditionalOnProperty(prefix = "self.security.core.oauth2",name="storeType",havingValue = "redis")
    public TokenStore redisTokenStore(){
//        new JdbcTokenStore(dataSource);
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Configuration
    //如果是存在self.security.core.oauth2.storeType的配置项，且该配置的值为jwt，如下配置才会生效，matchIfMissing=true表示如果没有这个配置项，这个也生效
    @ConditionalOnProperty(prefix = "self.security.core.oauth2",name="storeType",havingValue = "jwt",matchIfMissing = true)
    public static class JwtTokenStoreConfig{

        @Autowired
        private SecurityProperties securityProperties;

        @Bean
        public TokenStore tokenStore(){
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter(){
            JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
            String signKey = securityProperties.getOauth2().getJwtSigninKey();
            accessTokenConverter.setSigningKey(signKey);//设置生成签名的密钥
            return accessTokenConverter;
        }

        @Bean
        @ConditionalOnMissingBean(name = "jwtTokenEnhancer")//没有配置jwtTokenEnhancer，则会提供一个默认的实现
        public TokenEnhancer jwtTokenEnhancer(){
            return new SelfJwtTokenEnhancer();
        }
    }

}
