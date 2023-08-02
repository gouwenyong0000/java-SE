package com.learn.springsecurity.core.authentication.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * autor:liman
 * createtime:2021/7/11
 * comment: 短信验证码登录的token（短信验证码登录的登录信息）
 * 登录之前，存放的是用户的手机号，登录成功之后，其中会存放用户信息
 * 需要参考 UsernamePasswordAuthenticationToken
 */
public class SmsVerifyCodeAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    //用于存放认证信息的，登录之前存放手机号，登录成功之后，这里会存放用户信息
    private final Object principal;
//    private Object credentials;//这个用于存放密码（用不上）

    /**
     *
     */
    public SmsVerifyCodeAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
//        this.credentials = credentials;
        setAuthenticated(false);
    }

    /**
     *
     * @param principal
     * @param authorities
     */
    public SmsVerifyCodeAuthenticationToken(Object principal,
                                               Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
//        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    public Object getCredentials() {
//        return this.credentials;
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
//        credentials = null;
    }
}