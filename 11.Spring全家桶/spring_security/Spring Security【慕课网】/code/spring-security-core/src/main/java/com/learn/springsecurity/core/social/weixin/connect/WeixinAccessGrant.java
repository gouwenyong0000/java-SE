package com.learn.springsecurity.core.social.weixin.connect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.social.oauth2.AccessGrant;

import javax.validation.constraints.Size;

/**
 * autor:liman
 * createtime:2021/7/18
 * comment:
 * 微信的access_token信息。与标准OAuth2协议不同，微信在获取access_token时会同时返回openId,
 * 并没有单独的通过accessToken换取openId的服务
 * 所以在这里继承了标准 AccessGrant，添加了openId字段，作为对微信access_token信息的封装。
 * 在spring-social的基础上自定义了一个AccessGrant
 */
@Slf4j
public class WeixinAccessGrant extends AccessGrant {

    private static final long serialVersionUID = -1L;

    private String openId;

    public WeixinAccessGrant() {
        super("");
    }

    public WeixinAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn) {
        super(accessToken, scope, refreshToken, expiresIn);
    }

    /**
     * @return the openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId the openId to set
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
