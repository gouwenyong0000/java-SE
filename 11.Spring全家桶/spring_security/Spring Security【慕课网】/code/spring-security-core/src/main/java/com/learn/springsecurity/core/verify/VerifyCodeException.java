package com.learn.springsecurity.core.verify;


import org.springframework.security.core.AuthenticationException;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment:自定义的验证码异常
 */
public class VerifyCodeException extends AuthenticationException {

    public VerifyCodeException(String detail) {
        super(detail);
    }
}
