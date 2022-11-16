package com.example.springboot.aop;


import com.example.springboot.aop.converter.Converter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.*;

@Component
@Aspect
public class AopOrderService {

    //自定义线程池
    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1,
            1,
            60,
            TimeUnit.MICROSECONDS,
            new LinkedBlockingDeque(100));


    @Pointcut("@annotation(com.example.springboot.aop.AopAnno)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object join(ProceedingJoinPoint joinPoint) throws Throwable {

        Object proceed = joinPoint.proceed();

        //执行完之后记录日志
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AopAnno annotation = signature.getMethod().getAnnotation(AopAnno.class);

        String desc = annotation.desc();
        //可以自动转换成Converter 是因为定义的时候 指定泛化类型上下限
        Class<? extends Converter> covert = annotation.convert();
        Converter converter = covert.newInstance();
        String id = converter.converterToId(joinPoint.getArgs()[0]);

        poolExecutor.execute(() -> {
            System.out.println();
            System.out.println(LocalDateTime.now()  + "-->"+"desc = " + desc + "id = " + id);

        });
        return proceed;
    }

}
