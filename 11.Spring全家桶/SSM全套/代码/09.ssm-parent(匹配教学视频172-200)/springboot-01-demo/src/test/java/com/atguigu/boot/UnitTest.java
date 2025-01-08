package com.atguigu.boot;


import com.atguigu.boot.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
public class UnitTest {


    @Autowired
    HelloService helloService;


    @DisplayName("第一个测试")
    @Test
    void test01(){
        log.info("测试通过");
    }



    @Test
    void test02(){
        //1、业务规定，返回hello字符串才算成功，否则就是失败
        String result = helloService.sayHello();
        //2、断言：判断字符串是否等于hello
//        Assertions.assertEquals("hello",result,"helloservice并没有返回hello");




        Assertions.assertThrows(ArithmeticException.class, () -> {
            helloService.hello();
        });



    }

}
