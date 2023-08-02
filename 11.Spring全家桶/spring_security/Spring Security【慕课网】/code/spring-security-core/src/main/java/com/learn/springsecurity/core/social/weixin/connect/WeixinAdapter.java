package com.learn.springsecurity.core.social.weixin.connect;

import com.learn.springsecurity.core.social.weixin.api.WeixinLoginInterface;
import com.learn.springsecurity.core.social.weixin.api.WeixinUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * autor:liman
 * createtime:2021/7/18
 * comment: 微信Adapter 这个与QQ中的adapter差别不大
 */
@Slf4j
public class WeixinAdapter implements ApiAdapter<WeixinLoginInterface> {

    private String openId;

    public WeixinAdapter() {
    }

    public WeixinAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(WeixinLoginInterface api) {
        return true;
    }

    @Override
    public void setConnectionValues(WeixinLoginInterface api, ConnectionValues values) {
        WeixinUserInfo weixinUserInfo = api.getWeixinUserInfo(openId);
        values.setProviderUserId(weixinUserInfo.getOpenid());
        values.setDisplayName(weixinUserInfo.getNickname());
        values.setImageUrl(weixinUserInfo.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(WeixinLoginInterface api) {
        return null;
    }

    @Override
    public void updateStatus(WeixinLoginInterface api, String message) {

    }
}
