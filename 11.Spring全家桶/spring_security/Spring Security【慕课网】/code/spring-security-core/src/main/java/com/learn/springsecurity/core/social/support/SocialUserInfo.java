package com.learn.springsecurity.core.social.support;

import lombok.Data;
import lombok.ToString;

/**
 * autor:liman
 * createtime:2021/7/18
 * comment: 第三方用户信息的实体
 */
@Data
@ToString
public class SocialUserInfo {

    private String providerId;

    private String providerUserId;

    private String nickName;

    private String headImg;

}
