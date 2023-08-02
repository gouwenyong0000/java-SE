package com.learn.springsecurity.core.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * autor:liman
 * createtime:2021/7/14
 * comment:微信社交登录的相关配置
 */
public class WeixinSocialLoginProperties extends SocialProperties {

    public String providerId = "weixin";//自定义服务提供商的标示

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
