package com.learn.springsecurity.core.verify;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment: 图形验证码
 */
@Data
public class BaseVerifyCode implements Serializable {

    private String verifyCode;

    private LocalDateTime expireTime;

    public BaseVerifyCode(String verifyCode, int expireSecond) {
        this.verifyCode = verifyCode;
        this.expireTime = LocalDateTime.now().plusSeconds(expireSecond);
    }

    public BaseVerifyCode(String verifyCode, LocalDateTime expireTime) {
        this.verifyCode = verifyCode;
        this.expireTime = expireTime;
    }

    //判断是否过期
    public boolean isExpire(){
        return LocalDateTime.now().isAfter(expireTime);
    }
}
