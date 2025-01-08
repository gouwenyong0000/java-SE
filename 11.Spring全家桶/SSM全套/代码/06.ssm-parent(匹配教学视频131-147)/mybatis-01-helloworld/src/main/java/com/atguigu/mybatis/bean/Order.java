package com.atguigu.mybatis.bean;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {

    private Long id;
    private String address;
    private BigDecimal amount;
    private Long customerId;


    //订单对应的客户
    private Customer customer;
}
