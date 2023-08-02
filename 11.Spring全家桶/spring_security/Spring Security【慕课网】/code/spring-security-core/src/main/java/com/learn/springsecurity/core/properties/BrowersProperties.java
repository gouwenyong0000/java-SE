package com.learn.springsecurity.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * autor:liman
 * createtime:2021/7/9
 * comment:
 */
//@ConfigurationProperties()
public class BrowersProperties {

    //自定义登录页面
    private String loginPage = "/self-login.html";

    //第三方用户绑定时的注册页面
    private String siguUpPage = "/self-register.html";

    //退出登录后的跳转页面
    private String logoutPage = "/self-logout.html";

    //登录类型
    private LoginType loginType = LoginType.JSON;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public String getSiguUpPage() {
        return siguUpPage;
    }

    public void setSiguUpPage(String siguUpPage) {
        this.siguUpPage = siguUpPage;
    }

    public String getLogoutPage() {
        return logoutPage;
    }

    public void setLogoutPage(String logoutPage) {
        this.logoutPage = logoutPage;
    }
}
