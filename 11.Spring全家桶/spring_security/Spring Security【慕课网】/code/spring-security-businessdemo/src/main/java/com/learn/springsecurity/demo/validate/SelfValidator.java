package com.learn.springsecurity.demo.validate;

import org.hibernate.validator.internal.xml.ValidatedByType;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * autor:liman
 * createtime:2021/6/30
 * comment:自定义校验注解
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=SelfValidatorImpl.class)
public @interface SelfValidator {
    String message();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String field() default "";
}
