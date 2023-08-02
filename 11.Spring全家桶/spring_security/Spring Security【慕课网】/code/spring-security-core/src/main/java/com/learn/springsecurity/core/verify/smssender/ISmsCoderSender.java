package com.learn.springsecurity.core.verify.smssender;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment:发送短信验证码的接口
 */
public interface ISmsCoderSender {

    public void sendSms(String mobile,String verifyCode);

}
