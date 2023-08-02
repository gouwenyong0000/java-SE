package com.learn.springsecurity.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * autor:liman
 * createtime:2021/7/9
 * comment:
 */
@ConfigurationProperties(prefix = "self.security.core")
public class SecurityProperties {

    private BrowersProperties browser = new BrowersProperties();

    private VerifyCodeProperties verifyCode = new VerifyCodeProperties();

    private SocialLoginProperties social = new SocialLoginProperties();

    private OAuth2Properties oauth2 = new OAuth2Properties();

    public BrowersProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowersProperties browser) {
        this.browser = browser;
    }

    public VerifyCodeProperties getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(VerifyCodeProperties verifyCode) {
        this.verifyCode = verifyCode;
    }

    public SocialLoginProperties getSocial() {
        return social;
    }

    public void setSocial(SocialLoginProperties social) {
        this.social = social;
    }

    public OAuth2Properties getOauth2() {
        return oauth2;
    }

    public void setOauth2(OAuth2Properties oauth2) {
        this.oauth2 = oauth2;
    }
}
