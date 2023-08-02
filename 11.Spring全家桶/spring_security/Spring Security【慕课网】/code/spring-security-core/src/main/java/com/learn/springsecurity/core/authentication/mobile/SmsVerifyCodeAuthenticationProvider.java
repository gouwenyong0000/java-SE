package com.learn.springsecurity.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * autor:liman
 * createtime:2021/7/11
 * comment:短信验证码登录的认证逻辑
 */
public class SmsVerifyCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsVerifyCodeAuthenticationToken smsVerifyCodeAuthenticationToken = (SmsVerifyCodeAuthenticationToken) authentication;
        String mobile = (String) smsVerifyCodeAuthenticationToken.getPrincipal();
        UserDetails userInfo = userDetailsService.loadUserByUsername(mobile);//根据手机号读取用户信息

        if (null == userInfo) {
            throw new InternalAuthenticationServiceException("用户信息不存在");
        }

        //登录成功，则需要重新构造SmsVerifyCodeAuthenticationToken
        SmsVerifyCodeAuthenticationToken authenticationResult = new SmsVerifyCodeAuthenticationToken(userInfo, userInfo.getAuthorities());
        authenticationResult.setDetails(smsVerifyCodeAuthenticationToken.getDetails());//未认证的token中detail信息也要复制到认证之后的请求信息

        return authenticationResult;
    }

    /**
     * 当前provider 支持的认证类型是哪一种
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        //判断传进来的是否是SmsVerifyCodeAuthenticationToken
        return SmsVerifyCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
