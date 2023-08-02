package com.learn.springsecurity.core.verify.processor;

import com.learn.springsecurity.core.verify.BaseVerifyCode;
import com.learn.springsecurity.core.verify.smssender.ISmsCoderSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment: 短信验证码的处理器
 */
@Component("smsVerifyCodeProcessor")
@Slf4j
public class SmsVerifyCodeProcessor extends AbstractIVerifyCodeProcessor<BaseVerifyCode> {

    @Autowired
    private ISmsCoderSender smsCoderSender;

    @Override
    protected void sendVerifyCode(ServletWebRequest request, BaseVerifyCode verifyCode) throws Exception{

    }
}
