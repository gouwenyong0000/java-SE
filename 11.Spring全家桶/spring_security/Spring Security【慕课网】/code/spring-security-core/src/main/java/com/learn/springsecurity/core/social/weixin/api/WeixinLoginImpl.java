package com.learn.springsecurity.core.social.weixin.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * autor:liman
 * createtime:2021/7/18
 * comment: 微信获取用户信息的实现类
 */
@Slf4j
public class WeixinLoginImpl extends AbstractOAuth2ApiBinding implements WeixinLoginInterface {

    /**
     * 获取用户信息的url
     */
    private static final String URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    private ObjectMapper objectMapper = new ObjectMapper();

    public WeixinLoginImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);//将token拼装到请求url的后面，而不是请求体中
    }

    /**
     * 由于默认注册的StringHttpMessageConverter字符集为ISO-8859-1，而微信返回的是UTF-8的，所以覆盖了原来的方法。
     *
     * @return
     */
    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        //去掉原有的ISO-8859-1，新增一个UTF-8
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return messageConverters;
    }

    @Override
    public WeixinUserInfo getWeixinUserInfo(String openId) {

        String url = URL_GET_USER_INFO + openId;//根据openId获取用户信息
        String responseData = getRestTemplate().getForObject(url,String.class);//获取返回数据
        log.info("从微信返回的数据为:{}",responseData);
        if(StringUtils.contains(responseData,"errcode")){
            log.info("获取微信用户异常");
            return null;
        }

        WeixinUserInfo weixinUserInfo = null;

        try {
            weixinUserInfo = objectMapper.readValue(responseData, WeixinUserInfo.class);
        } catch (IOException e) {
            log.info("解析微信用户数据出现异常，异常信息为:{}",e);
        }
        return weixinUserInfo;
    }
}
