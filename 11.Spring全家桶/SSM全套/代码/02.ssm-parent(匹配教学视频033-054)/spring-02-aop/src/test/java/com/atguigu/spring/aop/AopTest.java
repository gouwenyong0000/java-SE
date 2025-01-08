package com.atguigu.spring.aop;


import com.atguigu.spring.aop.annotation.MyAn;
import com.atguigu.spring.aop.calculator.MathCalculator;
import com.atguigu.spring.aop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AopTest {


    @Autowired //容器中是它的代理对象
    MathCalculator mathCalculator;

    @Autowired
    UserService userService;



    @Test
    void test02() {
        mathCalculator.div(10, 2);
    }

    @Test
    void test01() {
//        System.out.println(mathCalculator.getClass()); //实现类
//
//        mathCalculator.add(10, 20);
//        // 增强器链： 切面中的所有通知方法其实就是增强器。他们被组织成一个链路放到集合中。目标方法真正执行前后，
//        // 会去增强器链中执行哪些需要提前执行的方法。
        //AOP 的底层原理
        //1、Spring会为每个被切面切入的组件创建代理对象(Spring CGLIB 创建的代理对象，无视接口)。
        //2、代理对象中保存了切面类里面所有通知方法构成的增强器链。
        //3、目标方法执行时，会先去执行增强器链中拿到需要提前执行的通知方法去执行

        mathCalculator.add(10, 2);
//        System.out.println("============");
//        mathCalculator.div(10, 2);

        userService.getUserHaha(1, 2);

        System.out.println("============");
        userService.updateUser();


    }
}
