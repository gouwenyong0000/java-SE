package com.atguigu.spring.aop;

import com.atguigu.spring.aop.calculator.MathCalculator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Spring02AopApplicationTests {

    //设计模式：依赖倒置：依赖接口，而不是依赖实现。实现可能会经常变。
    @Autowired
    MathCalculator mathCalculator;

    @Test
    void contextLoads() {
        int add = mathCalculator.add(1, 2);
        System.out.println(add);
    }

}
