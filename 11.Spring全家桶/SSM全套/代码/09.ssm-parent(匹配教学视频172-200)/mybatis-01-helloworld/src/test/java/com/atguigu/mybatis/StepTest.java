package com.atguigu.mybatis;


import com.atguigu.mybatis.bean.Customer;
import com.atguigu.mybatis.bean.Order;
import com.atguigu.mybatis.mapper.CustomerMapper;
import com.atguigu.mybatis.mapper.OrderCustomerStepMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class StepTest {


    @Autowired
    OrderCustomerStepMapper orderCustomerStepMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void test06(){
        List<Customer> orders = customerMapper.getAllCustomersWithOrders();
        orders.forEach(System.out::println);
    }

    @Test
    void test05() throws InterruptedException {
        Order order = orderCustomerStepMapper.getOrderByIdAndCustomerStep(1L);
//        System.out.println("order = " + order);
        System.out.println("================================");
        System.out.println("order = " + order.getAmount());
        System.out.println("================================");

        Thread.sleep(3000);
        //用到客户信息了，才会继续发送分步查询sql
        Customer customer = order.getCustomer();
        System.out.println("customer = " + customer.getCustomerName());
    }




    @Test
    void  testStep04(){
        Order order = orderCustomerStepMapper.getOrderByIdAndCustomerAndOtherOrdersStep(1L);
        System.out.println("order = " + order);
    }

    @Test
    void testStep03(){
        Order order = orderCustomerStepMapper.getOrderByIdAndCustomerStep(1L);
        System.out.println("order = " + order);
    }


    //MyBatis 自动分步查询机制；自动调用
    @Test
    void testStep02(){
        Customer customer = orderCustomerStepMapper.getCustomerByIdAndOrdersStep(1L);
        System.out.println(customer.getCustomerName());
    }


    //原生分步，需要我们手动调用两次方法
    @Test
    void testStep01(){
        //1、按照id查询客户
        Customer customer = orderCustomerStepMapper.getCustomerById(1L);
        //2、他下的所有订单
        List<Order> orders = orderCustomerStepMapper.getOrdersByCustomerId(customer.getId());
        //3、组合一起
        customer.setOrders(orders);
        System.out.println(customer);

    }
}
