package com.learn.springsecurity.core.social.qq.connect;

import com.learn.springsecurity.core.social.qq.api.QQLoginImpl;
import com.learn.springsecurity.core.social.qq.api.QQLoginInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * autor:liman
 * createtime:2021/7/12
 * comment:QQ服务提供者
 */
@Slf4j
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQLoginInterface> {

    private String appId;

    /**
     * 获取授权码的url
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
    /**
     * 根据授权码获取token的url
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    /**
     * Create a new {@link OAuth2ServiceProvider}.
     *
     * @param oauth2Operations the OAuth2Operations template for conducting the OAuth 2 flow with the provider.
     */
    public QQServiceProvider(String appId, String appSecret) {
        /**
         * 有四个参数
         * 第一个参数：appId,注册之后被分配的
         * 第二个参数：appSecret，注册之后被分配的
         * 第三个参数：去QQ获取授权码的url，万年不变的，可以写成固定值
         * 第四个参数：去QQ获取token的url，也是万年不变的
         */
//        super(new OAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;

    }

    @Override
    public QQLoginInterface getApi(String accessToken) {
        //QQLoginImpl每次返回的时候都得重新实例化一个
        return new QQLoginImpl(accessToken, appId);
    }
}
