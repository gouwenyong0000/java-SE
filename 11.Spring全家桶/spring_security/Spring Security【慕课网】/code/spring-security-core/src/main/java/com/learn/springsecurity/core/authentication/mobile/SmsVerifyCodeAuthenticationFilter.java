package com.learn.springsecurity.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * autor:liman
 * createtime:2021/7/11
 * comment:用于短信验证码登录的过滤器
 */
public class SmsVerifyCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SELF_SMS_LOGIN_MOBILE = "mobile";

    private String mobileParameter = SELF_SMS_LOGIN_MOBILE;
    private boolean postOnly = true;//当前过滤器只处理post请求


    public SmsVerifyCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/authentication/mobile", "POST"));
    }


    /**
     * 真正的认证流程
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {//是否是post请求
            throw new AuthenticationServiceException(
                    "认证的方法不支持" + request.getMethod());
        }

        String mobile = obtainMobile(request);

        if (mobile == null) {
            mobile = "";
        }
        mobile = mobile.trim();

        //实例化token
        SmsVerifyCodeAuthenticationToken authRequest = new SmsVerifyCodeAuthenticationToken(
                mobile);

        // 将请求的信息设置到token中
        setDetails(request, authRequest);

        //条用AuthenticationManager进行验证
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取用户名（这里是mobile）
     * @param request so that request attributes can be retrieved
     *
     */
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication
     * request's details property.
     *
     * @param request that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details
     * set
     */
    protected void setDetails(HttpServletRequest request,
                              SmsVerifyCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     */
    public void setMobileParameter(String mobileParameter) {
        Assert.hasText(mobileParameter, "手机号不能为空");
        this.mobileParameter = mobileParameter;
    }

    /**
     * 只支持post
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }
}