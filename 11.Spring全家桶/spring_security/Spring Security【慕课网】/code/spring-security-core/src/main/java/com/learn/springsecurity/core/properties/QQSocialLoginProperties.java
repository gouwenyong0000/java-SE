package com.learn.springsecurity.core.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * autor:liman
 * createtime:2021/7/14
 * comment:QQ社交登录的相关配置
 */
public class QQSocialLoginProperties extends SocialProperties {

    public String provideId = "QQ";//自定义服务提供商的标示

    public String getProvideId() {
        return provideId;
    }

    public void setProvideId(String provideId) {
        this.provideId = provideId;
    }
}
