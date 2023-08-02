package com.learn.springsecurity.core.social.weixin.connect;

import com.learn.springsecurity.core.social.weixin.api.WeixinLoginInterface;
import com.learn.springsecurity.core.social.weixin.api.WeixinUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * autor:liman
 * createtime:2021/7/18
 * comment: 微信端的ConnectionFactory
 */
public class WeixinConnectionFactory extends OAuth2ConnectionFactory<WeixinLoginInterface> {
    /**
     * Create a {@link OAuth2ConnectionFactory}.
     *
     * @param providerId      the provider id e.g. "facebook"
     * @param serviceProvider the ServiceProvider model for conducting the authorization flow and obtaining a native service API instance.
     * @param apiAdapter      the ApiAdapter for mapping the provider-specific service API model to the uniform {@link Connection} interface.
     */
    public WeixinConnectionFactory(String providerId, String appId,String appSecret) {
        super(providerId, new WeixinServiceProvider(appId,appSecret), new WeixinAdapter());
    }

    /**
     * 复写了connectionFactory中获取第三方用户id的方法
     * @param accessGrant
     * @return
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if(accessGrant instanceof WeixinAccessGrant){
            return ((WeixinAccessGrant)accessGrant).getOpenId();
        }
        return null;
    }


    /**
     * 复写了关于connection的创建
     * @param accessGrant
     * @return
     */

    @Override
    public Connection<WeixinLoginInterface> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<WeixinLoginInterface>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
    }

    /* (non-Javadoc)
     * @see org.springframework.social.connect.support.OAuth2ConnectionFactory#createConnection(org.springframework.social.connect.ConnectionData)
     */
    public Connection<WeixinLoginInterface> createConnection(ConnectionData data) {
        return new OAuth2Connection<WeixinLoginInterface>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
    }

    private ApiAdapter<WeixinLoginInterface> getApiAdapter(String providerUserId) {
        return new WeixinAdapter(providerUserId);//微信的apiAdapter是一个多实例的
    }

    private OAuth2ServiceProvider<WeixinLoginInterface> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<WeixinLoginInterface>) getServiceProvider();
    }
}
