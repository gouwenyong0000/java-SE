package com.learn.springsecurity.core.verify.update;

import com.learn.springsecurity.core.verify.BaseVerifyCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment: 验证码生成接口
 */
public interface IVerifyCodeGenerator {

    /**
     * 生成验证码的接口
     * @param request
     * @return
     */
    public BaseVerifyCode generateVerifyCode(ServletWebRequest request);

}
