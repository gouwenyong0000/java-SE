package com.learn.springsecurity.browser.authenticationhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.springsecurity.core.api.BaseResponse;
import com.learn.springsecurity.core.api.StatusCode;
import com.learn.springsecurity.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * autor:liman
 * createtime:2021/7/18
 * comment: 自定义退出登录的处理
 */
@Slf4j
public class SelfLogoutSuccessHandler implements LogoutSuccessHandler {

    private SecurityProperties securityProperties;

    private ObjectMapper objectMapper = new ObjectMapper();

    public SelfLogoutSuccessHandler(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String logoutPage = securityProperties.getBrowser().getLogoutPage();



        //如果没有配置退出页面
        if(StringUtils.isBlank(logoutPage)){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new BaseResponse(StatusCode.Success)));
        }else{
            response.sendRedirect(logoutPage);//配置了，就重定向
        }
        log.info("退出登录成功");
    }
}
