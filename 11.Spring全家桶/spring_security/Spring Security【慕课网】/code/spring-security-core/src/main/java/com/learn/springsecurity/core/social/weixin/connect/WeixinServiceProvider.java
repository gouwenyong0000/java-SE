package com.learn.springsecurity.core.social.weixin.connect;

import com.learn.springsecurity.core.social.weixin.api.WeixinLoginImpl;
import com.learn.springsecurity.core.social.weixin.api.WeixinLoginInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;

/**
 * autor:liman
 * createtime:2021/7/18
 * comment: 微信的serviceProvider
 */
@Slf4j
public class WeixinServiceProvider extends AbstractOAuth2ServiceProvider<WeixinLoginInterface> {

    /**
     * 微信获取授权码的url
     */
    private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/qrconnect";
    /**
     * 微信获取accessToken的url
     */
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 构造函数中传入appid和appSecret
     */
    public WeixinServiceProvider(String appId,String appSecret) {
        super(new WeixinOAuth2Template(appId,appSecret,URL_AUTHORIZE,URL_ACCESS_TOKEN));
    }

    @Override
    public WeixinLoginInterface getApi(String accessToken) {
        return new WeixinLoginImpl(accessToken);
    }
}
