package com.learn.springsecurity.app;

import com.learn.springsecurity.core.api.BaseResponse;
import com.learn.springsecurity.core.api.StatusCode;
import com.learn.springsecurity.core.social.AppSignUpUtils;
import com.learn.springsecurity.core.social.support.SocialUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * autor:liman
 * createtime:2021/8/7
 * comment:App端引导用户绑定的controller
 */
@RestController
@Slf4j
public class AppSecurityController {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;
    @Autowired
    private AppSignUpUtils appSignUpUtils;

    @GetMapping("/app/social/signup")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse getSocialUserInfo(HttpServletRequest request){
        BaseResponse result = new BaseResponse(StatusCode.Success);
        log.info("【app模式】开始获取会话中的第三方用户信息");
        //先从其中拿出数据，毕竟这个时候还没有完全跳转，下一个会话，就没有该数据了
        Connection<?> connectionFromSession = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        SocialUserInfo socialUserInfo = new SocialUserInfo();
        socialUserInfo.setProviderId(connectionFromSession.getKey().getProviderId());
        socialUserInfo.setProviderUserId(connectionFromSession.getKey().getProviderUserId());
        socialUserInfo.setNickName(connectionFromSession.getDisplayName());
        socialUserInfo.setHeadImg(connectionFromSession.getImageUrl());

        //转存到自己的工具类中
        appSignUpUtils.saveConnectionData(new ServletWebRequest(request),connectionFromSession.createData());
        result.setData(socialUserInfo);
        return result;
    }

}
