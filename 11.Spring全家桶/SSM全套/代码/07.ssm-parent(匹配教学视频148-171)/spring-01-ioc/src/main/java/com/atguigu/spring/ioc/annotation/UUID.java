package com.atguigu.spring.ioc.annotation;


import java.lang.annotation.*;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UUID {


}
