package com.learn.springsecurity.core.verify.processor;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment:由于图形验证码和短信验证都是分为三步
 * 1、生成。2、存入session。3、发送。完全可以抽象成模板方法模式
 * 这个就是顶层接口
 */
public interface IVerifyCodeProcessor {

    //验证码前缀
    public static final String SESSOIN_VERIFY_CODE_PREFIX = "SESSION_VERIFY_CODE_FOR_";

    /**
     * 创建验证码
     * @param request
     * @throws Exception
     */
    public void createVerifyCode(ServletWebRequest request) throws Exception;

}
