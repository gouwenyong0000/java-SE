package com.atguigu.mybatis.controller;


import com.atguigu.mybatis.bean.Emp;
import com.atguigu.mybatis.bean.Order;
import com.atguigu.mybatis.mapper.OrderCustomerStepMapper;
import com.atguigu.mybatis.service.EmpService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderRestController {



    @Autowired
    OrderCustomerStepMapper orderCustomerStepMapper;

    @Autowired
    EmpService empService;




    @GetMapping("/emp/page")
    public PageInfo getPage(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        PageHelper.startPage(pageNum, 5);
        List<Emp> all = empService.getAll();
        return new PageInfo<>(all);
    }

    @GetMapping("/order/{id}")
    public Order getOrder(@PathVariable("id") Long id) {

        Order order = orderCustomerStepMapper.getOrderByIdAndCustomerAndOtherOrdersStep(id);
        return order;
    }
}
