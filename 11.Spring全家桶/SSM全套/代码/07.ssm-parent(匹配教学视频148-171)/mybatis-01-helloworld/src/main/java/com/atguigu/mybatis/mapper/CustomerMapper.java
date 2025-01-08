package com.atguigu.mybatis.mapper;


import com.atguigu.mybatis.bean.Customer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerMapper {


    Customer getCustomerByIdWithOrders(Long id);


    // 查询所有客户所有订单
    List<Customer> getAllCustomersWithOrders();


}
