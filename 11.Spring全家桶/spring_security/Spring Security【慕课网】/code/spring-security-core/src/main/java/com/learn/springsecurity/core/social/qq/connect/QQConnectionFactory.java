package com.learn.springsecurity.core.social.qq.connect;

import com.learn.springsecurity.core.social.qq.api.QQLoginInterface;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * autor:liman
 * createtime:2021/7/14
 * comment:
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQLoginInterface> {

    /**
     * Create a {@link OAuth2ConnectionFactory}.
     *
     * @param providerId      the provider id e.g. "facebook"
     * @param serviceProvider the ServiceProvider model for conducting the authorization flow and obtaining a native service API instance.
     * @param apiAdapter      the ApiAdapter for mapping the provider-specific service API model to the uniform {@link Connection} interface.
     *
     * 有三个参数：
     * 第一个为服务提供商id，自己配置的
     * 第二个为我们构建的serviceProvider
     * 第三个为我们构建的apiAdapter
     *
     */
    public QQConnectionFactory(String providerId, String appId,String appSecret) {
        super(providerId, new QQServiceProvider(appId,appSecret), new QQAdapter());
    }
}
