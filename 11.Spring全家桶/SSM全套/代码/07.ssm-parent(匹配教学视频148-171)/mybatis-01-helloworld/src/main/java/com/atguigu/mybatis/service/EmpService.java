package com.atguigu.mybatis.service;


import com.atguigu.mybatis.bean.Emp;
import com.atguigu.mybatis.mapper.EmpDynamicSQLMapper;
import com.atguigu.mybatis.mapper.EmpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpService {

    @Autowired
    EmpDynamicSQLMapper empDynamicSQLMapper;

    @Autowired
    EmpMapper empMapper;


    public List<Emp> getAll(){
       return empMapper.getAll();
    }


    /**
     * 一级缓存：默认事务期间，会开启事务级别缓存；
     * 1、同一个事务期间，前面查询的数据，后面如果再要执行相同查询，会从一级缓存中获取数据，不会给数据库发送SQL
     *
     * 二级缓存：
     *
     * 多级缓存机制？
     */
    @Transactional //默认 可重复读
    public void find(){

        Emp empById = empMapper.getEmpById(1);
        System.out.println("员工："+empById);;

        System.out.println("================");


        empMapper.deleteEmpById(7);
        //有时候缓存会失效（缓存不命中）。
        //失效几种情况
        // 1、查询的东西不一样。
        // 2、两次查询之间，进行了一次增删改（由于增删改会引起数据库变化，Mybatis认为，数据有可能变了，它就要再发一次查询）
        Emp emp = empMapper.getEmpById(1);
        System.out.println("员工："+emp);

    }

    @Transactional
    public void updateBatch(List<Emp> emps){


    }
}
