package com.atguigu.mybatis;


import com.atguigu.mybatis.bean.Customer;
import com.atguigu.mybatis.bean.Order;
import com.atguigu.mybatis.mapper.CustomerMapper;
import com.atguigu.mybatis.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JoinQueryTest {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    CustomerMapper customerMapper;


    @Test
    void test02(){
        Customer customer = customerMapper.getCustomerByIdWithOrders(1L);
        System.out.println("customer = " + customer);
    }

    @Test
    void test01(){
        Order order = orderMapper.getOrderByIdWithCustomer(3L);

        System.out.println("order = " + order);
    }
}
