package com.learn.springsecurity.core.properties;

/**
 * autor:liman
 * createtime:2021/8/9
 * comment:
 */
public class OAuth2Properties {

    //用于签名的密钥配置
    private String jwtSigninKey = "self-security-jwt-key";

    private OAuth2ClientProperties[] clients = {};

    public OAuth2ClientProperties[] getClients() {
        return clients;
    }

    public void setClients(OAuth2ClientProperties[] clients) {
        this.clients = clients;
    }

    public String getJwtSigninKey() {
        return jwtSigninKey;
    }

    public void setJwtSigninKey(String jwtSigninKey) {
        this.jwtSigninKey = jwtSigninKey;
    }
}
