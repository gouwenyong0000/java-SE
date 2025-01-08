package com.atguigu.spring.aop.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD,ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAn {
    String value() default "";
}
