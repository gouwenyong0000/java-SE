package com.atguigu.mybatis.mapper;


import com.atguigu.mybatis.bean.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper {


    Customer getCustomerByIdWithOrders(Long id);


}
