package com.atguigu.mybatis;


import com.atguigu.mybatis.bean.Emp;
import com.atguigu.mybatis.mapper.EmpParamMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
public class ParamTest {


    @Autowired
    EmpParamMapper empParamMapper;


    @Test
    void testParam03(){
        Map<String, Object> params = new HashMap<>();
        params.put("name", "bbbb");
        Emp emp = new Emp();
        emp.setEmpSalary(1000.0D);
        empParamMapper.getEmployHaha(1L, params, Arrays.asList(19L,20L,21L,22L,34L), emp);
    }

    @Test
    void testParam02(){
        Emp 张三 = empParamMapper.getEmployByIdAndName(1L, "张三");
        System.out.println("张三 = " + 张三);
    }

    @Test
    void testParam1(){
//        Emp employ = empParamMapper.getEmploy(1L);
//
//        empParamMapper.getEmploy02(Arrays.asList(1L,2L,3L,4L));

        Emp emp = new Emp();

        emp.setEmpName("aaaa");
        emp.setAge(10);
        emp.setEmpSalary(0.0D);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "bbbb");
        params.put("age", 111);

        empParamMapper.addEmploy2(params);

    }
}
