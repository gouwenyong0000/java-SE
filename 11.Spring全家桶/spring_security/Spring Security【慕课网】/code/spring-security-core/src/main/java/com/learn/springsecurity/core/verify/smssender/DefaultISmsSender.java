package com.learn.springsecurity.core.verify.smssender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment: 默认的短信验证码发送方式
 */
@Slf4j
@Component("smsCoderSender")
public class DefaultISmsSender implements ISmsCoderSender {
    @Override
    public void sendSms(String mobile, String verifyCode) {
        log.info("向手机：{}发送短信验证码:{}",mobile,verifyCode);
    }
}
