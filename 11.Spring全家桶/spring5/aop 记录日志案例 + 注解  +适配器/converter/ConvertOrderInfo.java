package com.example.springboot.aop.converter;


import com.example.springboot.aop.service.OrderInfo;

public class ConvertOrderInfo implements Converter<OrderInfo>{


    @Override
    public String converterToId(OrderInfo o) {
        return o.getId();
    }
}
