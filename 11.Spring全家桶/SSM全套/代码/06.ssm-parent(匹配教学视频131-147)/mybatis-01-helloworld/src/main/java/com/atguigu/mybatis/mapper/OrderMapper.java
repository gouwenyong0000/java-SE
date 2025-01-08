package com.atguigu.mybatis.mapper;


import com.atguigu.mybatis.bean.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {


    //按照id查询订单以及下单的客户信息
    Order getOrderByIdWithCustomer(Long id);

}
