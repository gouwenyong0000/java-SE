package com.learn.springsecurity.core.config;

import com.learn.springsecurity.core.properties.SecurityProperties;
import com.learn.springsecurity.core.verify.smssender.DefaultISmsSender;
import com.learn.springsecurity.core.verify.smssender.ISmsCoderSender;
import com.learn.springsecurity.core.verify.update.IVerifyCodeGenerator;
import com.learn.springsecurity.core.verify.update.ImageVerifyCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment:验证码的配置类，使得验证码的功能可配置
 */
@Configuration
public class VerifyCodeGeneratorConfig {
    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnMissingBean(name = "imageVerifyCodeGenerator")//启动时在容器中如果找到imageVerifyCodeGenerator的bean，则不会调用下面的逻辑
    public IVerifyCodeGenerator imageVerifyCodeGenerator() {
        ImageVerifyCodeGenerator imageVerifyCodeGenerator = new ImageVerifyCodeGenerator();
        imageVerifyCodeGenerator.setSecurityProperties(securityProperties);
        return imageVerifyCodeGenerator;
    }

//    @Bean
//    @ConditionalOnMissingBean(name = "imageVerifyCodeGenerator")//启动时在容器中如果找到imageVerifyCodeGenerator的bean，则不会调用下面的逻辑
//    public IVerifyCodeGenerator smsVerifyCodeGenerator() {
//        ImageVerifyCodeGenerator imageVerifyCodeGenerator = new ImageVerifyCodeGenerator();
//        imageVerifyCodeGenerator.setSecurityProperties(securityProperties);
//        return imageVerifyCodeGenerator;
//    }

    /**
     * 默认的短信发送方式
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ISmsCoderSender.class)
    public ISmsCoderSender smsCoderSender(){
        return new DefaultISmsSender();
    }
}
