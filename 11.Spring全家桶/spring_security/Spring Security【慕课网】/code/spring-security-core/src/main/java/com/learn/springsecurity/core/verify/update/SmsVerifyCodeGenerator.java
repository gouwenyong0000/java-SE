package com.learn.springsecurity.core.verify.update;

import com.learn.springsecurity.core.properties.SecurityProperties;
import com.learn.springsecurity.core.verify.BaseVerifyCode;
import com.learn.springsecurity.core.verify.ImageVerifyCode;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment:图形验证码生成器
 */
@Component("smsVerifyCodeGenerator")
public class SmsVerifyCodeGenerator implements IVerifyCodeGenerator{

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public BaseVerifyCode generateVerifyCode(ServletWebRequest request) {
        int defaultSmsLength = securityProperties.getVerifyCode().getSms().getLength();
        int defaultSmsExpireSecond = securityProperties.getVerifyCode().getSms().getExpireSecond();
        String verifyCode = RandomStringUtils.randomNumeric(defaultSmsLength);
        return new BaseVerifyCode(verifyCode,defaultSmsExpireSecond);
    }

}
