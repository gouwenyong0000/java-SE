package com.learn.springsecurity.browser.session;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * autor:liman
 * createtime:2021/7/18
 * comment:自定义的会话淘汰策略
 * 实现SessionInformationExpiredStrategy接口，这个接口有一个SessionInformationExpiredEvent属性
 * 这个属性记录了最新的登录请求响应的所有数据。
 */
public class SelfExpireSessionStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        event.getResponse().setContentType("application/json;charset=UTF-8");
        event.getResponse().getWriter().write("在新的设备上登录了");
    }
}
