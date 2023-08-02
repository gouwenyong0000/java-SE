package com.learn.springsecurity.browser.controller;

import com.learn.springsecurity.core.api.BaseResponse;
import com.learn.springsecurity.core.api.StatusCode;
import com.learn.springsecurity.core.properties.SecurityProperties;
import com.learn.springsecurity.core.social.support.SocialUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * autor:liman
 * createtime:2021/7/9
 * comment:
 */
@RestController
@Slf4j
public class SecurityHandlerController {

    //引发请求的缓存对象
    private RequestCache requestCache = new HttpSessionRequestCache();

    //用于跳转
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    /**
     * 需要身份认证的时候，跳转到这里
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public BaseResponse authenticationHandle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(null!=savedRequest){
            String target = savedRequest.getRedirectUrl();
            log.info("引发跳转的请求是：{}",target);
            if(StringUtils.endsWithIgnoreCase(target,".html")){
                //如果用户配置了登录页，则跳转到登录页，如果没配置，则返回401，未认证的状态码
                String loginPage = securityProperties.getBrowser().getLoginPage();
                redirectStrategy.sendRedirect(request,response,loginPage);
            }
        }

        return new BaseResponse(StatusCode.NEED_LOGIN);
    }

    @GetMapping("/socialuserinfo")
    public BaseResponse getSocialUserInfo(HttpServletRequest request){
        BaseResponse result = new BaseResponse(StatusCode.Success);
        log.info("开始获取会话中的第三方用户信息");
        //利用providerSignInUtils，从会话中读取第三方用户信息
        Connection<?> connectionFromSession = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        SocialUserInfo socialUserInfo = new SocialUserInfo();
        socialUserInfo.setProviderId(connectionFromSession.getKey().getProviderId());
        socialUserInfo.setProviderUserId(connectionFromSession.getKey().getProviderUserId());
        socialUserInfo.setNickName(connectionFromSession.getDisplayName());
        socialUserInfo.setHeadImg(connectionFromSession.getImageUrl());
        result.setData(socialUserInfo);
        return result;
    }

    @GetMapping("/authentication/sessiontimeout")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public BaseResponse invalidSession(){
        String message = "session 失效";
        BaseResponse baseResponse = new BaseResponse(StatusCode.Fail);
        baseResponse.setData(message);
        return baseResponse;
    }

}
