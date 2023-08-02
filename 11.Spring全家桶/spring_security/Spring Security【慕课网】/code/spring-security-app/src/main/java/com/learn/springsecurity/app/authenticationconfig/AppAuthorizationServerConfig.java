package com.learn.springsecurity.app.authenticationconfig;

import com.learn.springsecurity.app.jwt.SelfJwtTokenEnhancer;
import com.learn.springsecurity.core.properties.OAuth2ClientProperties;
import com.learn.springsecurity.core.properties.SecurityProperties;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * autor:liman
 * createtime:2021/7/22
 * comment: app的认证服务器配置类
 * <p>
 * http://www.ictgu.cn/oauth/authorize?response_type=code&client_id=selfclientid&redirect_uri=http://example.com&scope=all
 * 这里包含服务端token生成规则的配置
 */
@Configuration
@EnableAuthorizationServer
public class AppAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    /**
     * 要给那些客户端发送appid和Appsecret，通过配置指定
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        OAuth2ClientProperties[] clientArrays = securityProperties.getOauth2().getClients();

        if (ArrayUtils.isNotEmpty(clientArrays)) {
            for (OAuth2ClientProperties oauth2ClientConfig : clientArrays) {
                String[] grantTypes = oauth2ClientConfig.getGrantTypes().split(",");
                builder.withClient(oauth2ClientConfig.getClientId())
                        .secret(oauth2ClientConfig.getClientSecret())
                        .accessTokenValiditySeconds(oauth2ClientConfig.getAccessTokenValidSeconds())
                        .authorizedGrantTypes(grantTypes)
                        .scopes("all", "read", "write");
            }
        }
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)//将token存入到redis
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
        if (null != jwtAccessTokenConverter && null != jwtTokenEnhancer) {
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enchancerList = new ArrayList<>();
            enchancerList.add(jwtTokenEnhancer);
            enchancerList.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enchancerList);

            endpoints
                    .tokenEnhancer(enhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }
}
