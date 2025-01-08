package com.atguigu.mybatis;


import com.atguigu.mybatis.bean.Emp;
import com.atguigu.mybatis.mapper.EmpReturnValueMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Map;


/**
 * 和数据库对不上的字段封装为null，
 * 如何解决？
 * 0、JavaBean 和 数据库一样 【不推荐】
 * 1、使用 列别名；
 * 2、使用 驼峰命名自动映射；
 * 3、使用 ResultMap（自定义结果集）
 */
@SpringBootTest
public class ReturnValueTest {


    @Autowired
    EmpReturnValueMapper empReturnValueMapper;

    @Test
    void test02(){
        Emp empById = empReturnValueMapper.getEmpById(1);
        System.out.println("empById = " + empById);
    }

    @Test
    void test01(){
        Long l = empReturnValueMapper.countEmp();
        System.out.println("l = " + l);

        BigDecimal empSalaryById = empReturnValueMapper.getEmpSalaryById(1);
        System.out.println("empSalaryById = " + empSalaryById);

        empReturnValueMapper.getAll().forEach(System.out::println);

        System.out.println("=====================================");

        Map<Integer, Emp> allMap = empReturnValueMapper.getAllMap();
        System.out.println("allMap = " + allMap);
        System.out.println(allMap.get(3).getClass());
        Emp emp = allMap.get(3);
        System.out.println(emp.getAge());

    }
}
