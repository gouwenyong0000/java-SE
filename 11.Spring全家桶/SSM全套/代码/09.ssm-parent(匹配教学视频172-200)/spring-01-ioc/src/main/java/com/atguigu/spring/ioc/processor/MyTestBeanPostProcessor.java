package com.atguigu.spring.ioc.processor;

import com.atguigu.spring.ioc.bean.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;


@Component //拦截所有Bean的后置处理器
public class MyTestBeanPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("【postProcessAfterInitialization】："+beanName);
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("【postProcessBeforeInitialization】："+beanName);

        if(bean instanceof User hello){
            hello.setUsername("张三测试");
        }
        return bean;
    }
}
