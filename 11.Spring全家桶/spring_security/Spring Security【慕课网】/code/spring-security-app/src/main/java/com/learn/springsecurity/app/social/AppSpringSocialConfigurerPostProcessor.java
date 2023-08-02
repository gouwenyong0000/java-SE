package com.learn.springsecurity.app.social;

import com.learn.springsecurity.core.config.social.SelfSpringSocialConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * autor:liman
 * createtime:2021/8/7
 * comment:由于app端的社交用户绑定，不能采用跳转，也不能操作会话，需要用自定义的providerSignUpUtils工具类
 * 因此需要定义一个后置处理器，针对SpringSocialConfigurer进行一些后置处理
 */
@Component
public class AppSpringSocialConfigurerPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if(StringUtils.equals(beanName,"selfSocialSecurityConfig")){
            SelfSpringSocialConfig configurer = (SelfSpringSocialConfig) bean;
            //复写掉原有的SelfSpringSocialConfig的signupUrl
            configurer.signupUrl("/app/social/signup");
            return configurer;
        }
        return bean;
    }
}
