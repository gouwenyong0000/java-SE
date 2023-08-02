package com.learn.springsecurity.core.properties;

import lombok.Data;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment:
 */
@Data
public class VerifyCodeProperties {

    //图形验证码的配置
    private ImageVerifyCodeProperties image = new ImageVerifyCodeProperties();

    //短信验证码的配置
    private BaseVerifyCodeProperties sms = new BaseVerifyCodeProperties();

}
