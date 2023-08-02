package com.learn.springsecurity.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * autor:liman
 * createtime:2021/7/1
 * comment:
 */
@Aspect
@Component
@Slf4j
public class TimeAspect {

//    @Around("execution(* com.learn.springsecurity.demo.controller.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint point) throws Throwable {
        Long startTime = new Date().getTime();
        Object[] args = point.getArgs();
        for(Object obj:args){
            log.info("目标方法的参数为:{}",obj);
        }
        Object object = point.proceed();
        Long endTime = new Date().getTime();
        log.info("【aop】目标接口调用耗时:{}",endTime-startTime);
        log.info("【aop】切面结束");
        return object;
    }

}
