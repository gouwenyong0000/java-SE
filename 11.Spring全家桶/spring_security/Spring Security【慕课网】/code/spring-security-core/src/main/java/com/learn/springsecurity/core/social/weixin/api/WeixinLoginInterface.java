package com.learn.springsecurity.core.social.weixin.api;

/**
 * autor:liman
 * createtime:2021/7/18
 * comment: 微信获取用户信息的接口
 */
public interface WeixinLoginInterface {

    /**
     * 与QQ不同，微信获取用户信息需要传一个openId
     * 因为微信登录在获取access_token这一步就会返回用户的openId
     * 这个对开发者来说是一个好事，但是对于spring-social标准的oauth实现就有点麻烦了
     * @param openId
     * @return
     */
    WeixinUserInfo getWeixinUserInfo(String openId);

}
