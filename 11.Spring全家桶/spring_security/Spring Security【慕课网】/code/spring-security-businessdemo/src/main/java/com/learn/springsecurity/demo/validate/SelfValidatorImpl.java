package com.learn.springsecurity.demo.validate;

import com.learn.springsecurity.demo.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * autor:liman
 * createtime:2021/6/30
 * comment: 自定义校验逻辑
 */
public class SelfValidatorImpl implements ConstraintValidator<SelfValidator, Object> {

    @Autowired
    private HelloService helloService;

    @Override
    public void initialize(SelfValidator constraintAnnotation) {
        System.out.println("my validator init");
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
//        helloService.greeting("tom");
        //TODO:这里处理校验逻辑，如果校验通过，则返回true，否则返回false
        System.out.println(value);
        return false;
    }
}
