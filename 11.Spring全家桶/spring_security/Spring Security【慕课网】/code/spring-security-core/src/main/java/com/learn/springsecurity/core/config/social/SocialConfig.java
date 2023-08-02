package com.learn.springsecurity.core.config.social;

import com.learn.springsecurity.core.properties.SecurityProperties;
import com.learn.springsecurity.core.social.support.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * autor:liman
 * createtime:2021/7/14
 * comment: 社交登录的配置类
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private SecurityProperties securityProperties;
//    @Autowired
//    private ConnectionFactoryLocator connectionFactoryLocator;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;
    @Autowired(required = false)
    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;


    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        //第二个参数是一个，这个参数会被传递进来，这个是根据条件自动查找相关的ConnectionFactory ,如果是微信登录，则查找微信的ConnectionFactory
        //第三个是加解密
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        repository.setTablePrefix("imooc_");
        if(connectionSignUp!=null){
            repository.setConnectionSignUp(connectionSignUp);
        }
        return repository;
    }

    @Bean
    public SpringSocialConfigurer selfSocialSecurityConfig(){
        String processFilterUrl = securityProperties.getSocial().getProcessFilterUrl();
        SelfSpringSocialConfig selfSpringSocialConfig = new SelfSpringSocialConfig(processFilterUrl);

        //指定第三方用户信息认证不存在的注册页
        selfSpringSocialConfig.signupUrl(securityProperties.getBrowser().getSiguUpPage());
        selfSpringSocialConfig.setConnectionSignUp(connectionSignUp);
        selfSpringSocialConfig.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
        return selfSpringSocialConfig;
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator){
        return new ProviderSignInUtils(connectionFactoryLocator,
                getUsersConnectionRepository(connectionFactoryLocator));
    }
}
