package com.learn.springsecurity.core.social.weixin.config;

import com.learn.springsecurity.core.properties.SecurityProperties;
import com.learn.springsecurity.core.properties.WeixinSocialLoginProperties;
import com.learn.springsecurity.core.social.weixin.connect.WeixinConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * autor:liman
 * createtime:2021/7/18
 * comment:微信登录的配置类
 */
@Configuration
//配置了微信的app-id，这个配置类才生效
@ConditionalOnProperty(prefix="self.security.core.social.weixin",name = "app-id")
public class WeixinAutoConfiguration extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;


    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        WeixinSocialLoginProperties weixinConfig = securityProperties.getSocial().getWeixin();
        return new WeixinConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(),
                weixinConfig.getAppSecret());
    }
}
