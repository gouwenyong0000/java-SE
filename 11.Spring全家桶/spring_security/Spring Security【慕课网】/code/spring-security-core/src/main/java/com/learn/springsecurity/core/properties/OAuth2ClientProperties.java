package com.learn.springsecurity.core.properties;

/**
 * autor:liman
 * createtime:2021/8/9
 * comment:
 */
public class OAuth2ClientProperties {

    private String clientId;

    private String clientSecret;

    private int accessTokenValidSeconds;

    private String grantTypes;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public int getAccessTokenValidSeconds() {
        return accessTokenValidSeconds;
    }

    public void setAccessTokenValidSeconds(int accessTokenValidSeconds) {
        this.accessTokenValidSeconds = accessTokenValidSeconds;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }
}
