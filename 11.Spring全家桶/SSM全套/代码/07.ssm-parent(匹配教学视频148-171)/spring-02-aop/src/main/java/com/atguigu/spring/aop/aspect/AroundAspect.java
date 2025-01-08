package com.atguigu.spring.aop.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * 【声明式】 vs 【编程式】
 * 声明式：通过注解等方式，告诉框架，我要做什么，框架会帮我做什么。
 *    优点：代码量小。
 *    缺点：封装太多。排错不容易
 * 编程式：通过代码的方式，告诉框架，我要做什么，需要自己写代码实现。
 *    优点：排错容易
 *    缺点：代码量多
 *
 *
 * AOP 的使用场景：
 * 1、日志记录【√】：
 * 在不修改业务代码的情况下，为方法调用添加日志记录功能。这有助于跟踪方法调用的时间、参数、返回值以及异常信息等。
 *
 * 2、事务管理【√】：
 * 在服务层或数据访问层的方法上应用事务管理，确保数据的一致性和完整性。通过AOP，可以自动地为需要事务支持的方法添加事务开始、提交或回滚的逻辑。
 *
 * 3、权限检查【√】：
 * 在用户访问某些资源或执行某些操作之前，进行权限检查。通过AOP，可以在不修改业务逻辑代码的情况下，为方法调用添加权限验证的逻辑。
 *
 * 4、性能监控：专业框架
 * 对方法的执行时间进行监控，以评估系统的性能瓶颈。AOP 可以帮助在不修改业务代码的情况下，为方法调用添加性能监控的逻辑。
 *
 * 5、异常处理【√】：
 * 集中处理业务逻辑中可能抛出的异常，并进行统一的日志记录或错误处理。通过AOP，可以为方法调用添加异常捕获和处理的逻辑。
 *
 * 6、缓存管理【√】：
 * 在方法调用前后添加缓存逻辑，以提高系统的响应速度和吞吐量。AOP 可以帮助实现缓存的自动加载、更新和失效等逻辑。
 *
 *
 * 7、安全审计：
 * 记录用户操作的历史记录，以便进行安全审计。通过AOP，可以在不修改业务逻辑代码的情况下，为方法调用添加安全审计的逻辑。
 *
 * 8、自动化测试：
 * 在测试阶段，通过AOP为方法调用添加模拟（mock）或桩（stub）对象，以便进行单元测试或集成测试。
 */
@Aspect
@Component
public class AroundAspect {
    @Pointcut("execution(int com.atguigu.spring.aop.calculator.MathCalculator.*(..))")
    public void pointCut(){};

    /**
     * 环绕通知固定写法如下：
     * Object: 返回值
     * ProceedingJoinPoint: 可以继续推进的切点
     */

    @Around("pointCut()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs(); // 获取目标方法的参数

        // 前置
        System.out.println("环绕 - 前置通知：参数"+ Arrays.toString(args));
        Object proceed = null;
        try {
            //接受传入参数的 proceed ，实现修改目标方法执行用的参数
            proceed = pjp.proceed(args);// 继续执行目标方法; 反射 method.invoke()
            System.out.println("环绕 - 返回通知：返回值："+proceed);
        }catch (Throwable e){
            System.out.println("环绕 - 异常通知："+e.getMessage());
            throw e;  //让别人继续感知
        }finally {
            System.out.println("环绕 - 后置通知");
        }
        //修改返回值
        return proceed;
    }
}
