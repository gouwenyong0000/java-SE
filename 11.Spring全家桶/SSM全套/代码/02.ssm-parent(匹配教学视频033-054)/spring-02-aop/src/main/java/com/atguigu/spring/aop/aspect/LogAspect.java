package com.atguigu.spring.aop.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Order(10000) //数字越小，优先级越高，数字越大，优先级越低; 数字越小，越先执行，就必须套到最外层
@Component
@Aspect //告诉Spring这个组件是个切面。
public class LogAspect {


    @Pointcut("execution(int com.atguigu.spring.aop.calculator.MathCalculator.*(..))")
    public void pointCut(){};


    /**
     * 1、告诉Spring，以下通知何时何地运行？
     *      何时？
     *         @Before：方法执行之前运行。
     *         @AfterReturning：方法执行正常返回结果运行。
     *         @AfterThrowing：方法抛出异常运行。
     *         @After：方法执行之后运行
     *      何地？
     *         切入点表达式：
     *           execution(方法的全签名)：
     *             全写法：[public] int [com.atguigu.spring.aop.calculator.MathCalculator].add(int,int) [throws ArithmeticException]
     *             省略写法：int add(int i,int j)
     *             通配符：
     *               *：表示任意字符
     *               ..：
     *                   1）、参数位置：表示多个参数，任意类型
     *                   2）、类型位置：代表多个层级
     *             最省略: * *(..)
     *
     * 2、通知方法的执行顺序：
     *      1、正常链路：  前置通知->目标方法->返回通知->后置通知
     *      2、异常链路：  前置通知->目标方法->异常通知->后置通知
     *
     * 3、JoinPoint： 包装了当前目标方法的所有信息
     */
    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){
        //1、拿到方法全签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        //方法名
        String name = signature.getName();
        //目标方法传来的参数值
        Object[] args = joinPoint.getArgs();

        System.out.println("【切面 - 日志】【"+name+"】开始：参数列表：【"+ Arrays.toString(args) +"】");
    }

    @After("pointCut()")
    public void logEnd(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String name = signature.getName();
        System.out.println("【切面 - 日志】【"+name+"】后置...");
    }


    @AfterReturning(value = "pointCut()",
                    returning = "result") //returning="result" 获取目标方法返回值
    public void logReturn(JoinPoint joinPoint,Object result){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String name = signature.getName();

        System.out.println("【切面 - 日志】【"+name+"】返回：值："+result);
    }


    @AfterThrowing(
            value = "pointCut()",
            throwing = "e" //throwing="e" 获取目标方法抛出的异常
    )
    public void logException(JoinPoint joinPoint,Throwable e){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String name = signature.getName();

        System.out.println("【切面 - 日志】【"+name+"】异常：错误信息：【"+e.getMessage()+"】");
    }



    //参数带什么就切
//    @Before("args(int,int)")
    public void haha(){
        System.out.println("【切面 - 日志】哈哈哈...");
    }

    //参数上有没有标注注解
//    @Before("@args(com.atguigu.spring.aop.annotation.MyAn) && within(com.atguigu.spring.aop.service.UserService)")
    public void hehehe(){
        System.out.println("【切面 - 日志】呵呵呵...");
    }

    //方法上
//    @Before("@annotation(com.atguigu.spring.aop.annotation.MyAn)")
    public void test(){
        System.out.println("【切面 - 日志】MyAn测试...");
    }


}
