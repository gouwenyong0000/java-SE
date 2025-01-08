package com.atguigu.mybatis.mapper;


import com.atguigu.mybatis.bean.Customer;
import com.atguigu.mybatis.bean.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderCustomerStepMapper {

    //需求：按照id查询客户 以及 他下的所有订单
    //1. 查询客户
    Customer getCustomerById(Long id);

    //2. 查询订单
    List<Order> getOrdersByCustomerId(Long cId);


    /**
     *
     * @param id 客户id
     * @return
     */
    //3、分步查询：自动做两步 = 查询客户 + 查询客户下的订单
    Customer getCustomerByIdAndOrdersStep(Long id);


    /**
     *
     * @param id 订单id
     * @return
     */
    //4、分步查询：自动做两步 = 按照id查询订单 + 查询下单的客户
    Order getOrderByIdAndCustomerStep(Long id);


    // 【超级分步】案例3：按照id查询订单 以及 下单的客户 以及 此客户的所有订单
    Order getOrderByIdAndCustomerAndOtherOrdersStep(Long id);


}
