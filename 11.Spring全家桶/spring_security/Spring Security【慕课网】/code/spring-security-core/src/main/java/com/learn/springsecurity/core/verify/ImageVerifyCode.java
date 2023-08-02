package com.learn.springsecurity.core.verify;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment: 图形验证码
 */
@Data
public class ImageVerifyCode extends BaseVerifyCode {

    private BufferedImage image;


    public ImageVerifyCode(BufferedImage image, String verifyCode, int expireSecond) {
        super(verifyCode,expireSecond);
        this.image = image;
    }

    public ImageVerifyCode(BufferedImage image, String verifyCode, LocalDateTime expireTime) {
        super(verifyCode,expireTime);
        this.image = image;
    }

}
