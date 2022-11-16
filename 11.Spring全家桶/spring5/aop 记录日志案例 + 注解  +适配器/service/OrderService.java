package com.example.springboot.aop.service;

import com.example.springboot.aop.AopAnno;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @AopAnno(desc = "保存", convert = ConvertOrder.class)
    public void saveOrder(Order order){

        System.out.println("save order -->" + order);

    }

    @AopAnno(desc = "更新", convert = ConvertOrderInfo.class)
    public void updateOrder(OrderInfo order) {

        System.out.println("update order -->" + order);

    }
}
