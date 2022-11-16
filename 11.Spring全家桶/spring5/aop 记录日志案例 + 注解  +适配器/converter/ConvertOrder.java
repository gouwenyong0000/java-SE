package com.example.springboot.aop.converter;

import com.example.springboot.aop.service.Order;


public class ConvertOrder implements Converter<Order> {
    @Override
    public String converterToId(Order o) {
        return o.getOrderId();
    }
}
