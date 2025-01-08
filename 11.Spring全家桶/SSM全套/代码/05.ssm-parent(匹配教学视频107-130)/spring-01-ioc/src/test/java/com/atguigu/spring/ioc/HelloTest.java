package com.atguigu.spring.ioc;


import com.atguigu.spring.ioc.bean.User;
import com.atguigu.spring.ioc.dao.DeliveryDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class HelloTest {


    @Autowired
    User user;

    @Autowired
    DeliveryDao deliveryDao;


    @Test
    void test02(){
        String string = UUID.randomUUID().toString();
        System.out.println("string = " + string);
    }

    @Test
    void test01(){
//        System.out.println("user = " + user);
        deliveryDao.saveDelivery();
    }


}
