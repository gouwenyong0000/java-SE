package com.learn.springsecurity.core.social.qq.config;

import com.learn.springsecurity.core.properties.QQSocialLoginProperties;
import com.learn.springsecurity.core.properties.SecurityProperties;
import com.learn.springsecurity.core.social.qq.connect.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.ConnectionFactory;

/**
 * autor:liman
 * createtime:2021/7/14
 * comment:
 */
@Configuration
@ConditionalOnProperty(prefix="self.security.core.social.qq",name = "app-id")//系统中配置了app-id项，则这个配置才生效
//@EnableSocial
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 配置QQ的连接工厂
     * @return
     */
    @Override
    protected ConnectionFactory<?> createConnectionFactory() {

        QQSocialLoginProperties qqSocialLoginProperties = securityProperties.getSocial().getQq();

        return new QQConnectionFactory(qqSocialLoginProperties.getProvideId(),qqSocialLoginProperties.getAppId(),qqSocialLoginProperties.getAppSecret());
    }
}
