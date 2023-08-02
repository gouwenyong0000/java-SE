package com.learn.springsecurity.core.social.qq.connect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * autor:liman
 * createtime:2021/7/17
 * comment:自定义的OAuth2Template，使得其可以处理第三方服务器返回的非json数据。
 */
@Slf4j
public class QQOAuth2Template extends OAuth2Template {
    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }

    /**
     * 往RestTemplate中加入新的HTTP消息处理器
     * @return
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return super.createRestTemplate();
    }

    /**
     * 针对不同的返回参数，这里可以定制化处理（这里是按照QQ的标准）
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        log.info("从QQ获取token，的返回字符串为:{}",responseStr);
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr,"&");
        String accessToken = StringUtils.substringAfterLast(items[0],"=");
        Long expiresIn = Long.valueOf(StringUtils.substringAfterLast(items[1],"="));
        String refreshToken = StringUtils.substringAfterLast(items[2],"=");

        //构建AccessGrant
        return new AccessGrant(accessToken,null,refreshToken,expiresIn);
    }
}
