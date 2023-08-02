package com.learn.springsecurity.app.authenticationhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.springsecurity.core.api.BaseResponse;
import com.learn.springsecurity.core.properties.LoginType;
import com.learn.springsecurity.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment:自定义登录失败的处理
 */
@Component("selfAuthenticationFailureHandler")
@Slf4j
public class SelfAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException exception) throws IOException, ServletException {
//        log.info("自定义认证失败处理器");
//        response.setContentType("application/json;charset=utf-8");
//        response.getWriter().write(objectMapper.writeValueAsString(exception));//将 exception 作为json写到前端

        log.info("自定义登录成功的处理器");
        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {//如果配置的登录方式是json返回
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(new BaseResponse(999999,exception.getMessage())));//将authentication作为json写到前端
        }else{//如果不是json的登录返回方式，则调用父类的方式进行跳转（这也是为啥要继承SimpleUrlAuthenticationSuccessHandler的原因）
            super.onAuthenticationFailure(request,response,exception);
        }
    }
}
