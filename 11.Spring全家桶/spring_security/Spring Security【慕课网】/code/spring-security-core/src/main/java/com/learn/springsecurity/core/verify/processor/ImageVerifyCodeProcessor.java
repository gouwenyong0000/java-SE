package com.learn.springsecurity.core.verify.processor;

import com.learn.springsecurity.core.verify.ImageVerifyCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment:图形验证码的处理器
 */
@Component("imageVerifyCodeProcessor")
@Slf4j
public class ImageVerifyCodeProcessor extends AbstractIVerifyCodeProcessor<ImageVerifyCode> {

    @Override
    protected void sendVerifyCode(ServletWebRequest request, ImageVerifyCode verifyCode) throws Exception {
        ImageIO.write(verifyCode.getImage(),"JPEG",request.getResponse().getOutputStream());
    }
}
