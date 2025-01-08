package com.atguigu.mybatis.bean;


import lombok.Data;

import java.math.BigDecimal;


// toString：本来就是打印所有属性，代表所有都用到了
@Data
public class Order {

    private Long id;
    private String address;
    private BigDecimal amount;
    private Long customerId;

    //订单对应的客户
    private Customer customer;
}
