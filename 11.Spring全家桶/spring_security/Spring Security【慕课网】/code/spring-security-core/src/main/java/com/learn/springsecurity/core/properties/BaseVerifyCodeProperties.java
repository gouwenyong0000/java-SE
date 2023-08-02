package com.learn.springsecurity.core.properties;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment:短信验证码的配置
 */
public class BaseVerifyCodeProperties {

    private int length=6;
    private int expireSecond=60;
    //需要校验图形验证码的url
    private String urls = "";

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireSecond() {
        return expireSecond;
    }

    public void setExpireSecond(int expireSecond) {
        this.expireSecond = expireSecond;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
