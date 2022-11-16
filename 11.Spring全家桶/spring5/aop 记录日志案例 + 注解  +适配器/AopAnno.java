package com.example.springboot.aop;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import  com.example.springboot.aop.converter.Converter;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AopAnno {
    String desc() default "";
    Class<? extends Converter> convert()  ;

}
