package com.learn.springsecurity.app.jwt;

import org.bouncycastle.jcajce.provider.digest.MD2;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * autor:liman
 * createtime:2021/8/11
 * comment:自定义token的enhancer，用于JWT中生成扩展信息
 */
public class SelfJwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Map<String,Object> info = new HashMap<String,Object>();
        info.put("self_extention_info","这个是JWT的扩展信息");

        //扩展信息放入accessToken中
        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);


        return accessToken;
    }
}
