package com.learn.springsecurity.core.social.qq.connect;

import com.learn.springsecurity.core.social.qq.api.QQLoginInterface;
import com.learn.springsecurity.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * autor:liman
 * createtime:2021/7/14
 * comment:QQAdapter需要适配QQ获取的用户信息
 */
public class QQAdapter implements ApiAdapter<QQLoginInterface> {
    @Override
    public boolean test(QQLoginInterface api) {
        return true;
    }

    @Override
    public void setConnectionValues(QQLoginInterface api, ConnectionValues values) {
        QQUserInfo userInfo = api.getQQUserInfo();

        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        values.setProfileUrl(null);
        values.setProviderUserId(userInfo.getOpenId());//用户在服务商的openId
    }

    /**
     * 用于解绑和绑定
     * @param api
     * @return
     */
    @Override
    public UserProfile fetchUserProfile(QQLoginInterface api) {
        return null;
    }

    /**
     * 某些社交网站上，可以更新微博的存在，这里不需要
     * @param api
     * @param message
     */
    @Override
    public void updateStatus(QQLoginInterface api, String message) {

    }
}
