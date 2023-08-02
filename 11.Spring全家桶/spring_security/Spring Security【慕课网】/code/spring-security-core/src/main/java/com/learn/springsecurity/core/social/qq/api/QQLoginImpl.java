package com.learn.springsecurity.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

/**
 * autor:liman
 * createtime:2021/7/12
 * comment:
 * AbstractOAuth2ApiBinding中有两个关键的属性
 * 1、accessToken用于存储前几步下来获取的访问令牌
 * 2、restTemplate 一个发起HTTP请求的工具类
 */
@Slf4j
public class QQLoginImpl extends AbstractOAuth2ApiBinding implements QQLoginInterface {

    //根据AccessToken获取用户OPENID的url，Accesstoken是根据授权码去QQ获取的
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
    //根据openId获取用户信息的url
    private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String appId;

    private String openId;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 构造函数中获取openId
     * @param accessToken
     * @param appId
     */
    public QQLoginImpl(String accessToken, String appId) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);//将token作为参数放到url后面
        this.appId = appId;

        String url = String.format(URL_GET_OPENID, accessToken);
        String result = getRestTemplate().getForObject(url, String.class);//获取openId的结果
        log.info("获取openId的返回数据为:{}", result);
        //截取获取openid
        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
    }

    @Override
    public QQUserInfo getQQUserInfo() {
        try {
            String url = String.format(URL_GET_USERINFO, appId, openId);
            String result = getRestTemplate().getForObject(url, String.class);
            log.info("读取到的用户信息为:{}", result);
            QQUserInfo qqUserInfo = objectMapper.readValue(result, QQUserInfo.class);
            qqUserInfo.setOpenId(openId);
            return qqUserInfo;
        } catch (Exception e) {
            log.error("获取用户信息出现异常，异常信息为:{}", e);
            return null;
        }
    }
}
