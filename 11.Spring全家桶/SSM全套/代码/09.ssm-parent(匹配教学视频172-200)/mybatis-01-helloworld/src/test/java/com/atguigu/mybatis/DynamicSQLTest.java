package com.atguigu.mybatis;


import com.atguigu.mybatis.bean.Emp;
import com.atguigu.mybatis.mapper.EmpDynamicSQLMapper;
import com.atguigu.mybatis.service.EmpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class DynamicSQLTest {

    @Autowired
    EmpDynamicSQLMapper empDynamicSQLMapper;


    @Autowired
    EmpService empService;




    @Test
    void test09(){
        empDynamicSQLMapper.getEmpsByIdIn(Arrays.asList(1,2,3));
    }

    //分布式项目情况下，分布式事务很多不支持多SQL批量操作的回滚；
    @Test
    void test08(){
        List<Emp> emps = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Emp emp = new Emp();
            emp.setId(10+i);
            emp.setEmpName("张-"+100+i);
            emp.setAge(100+i);
            emp.setEmpSalary(90000.0D+i);
            emps.add(emp);
        }
        empService.updateBatch(emps);
        System.out.println("批量更新完成");
    }


    @Test
    void test07(){
        for (int i = 0; i < 100; i++) {
            empDynamicSQLMapper.updateEmp(new Emp());
        }

    }


    //一口气发一堆SQL效率最高。
    @Test
    void test06(){
        List<Emp> emps = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Emp emp = new Emp();
            emp.setId(10+i);
            emp.setEmpName("张-"+100+i);
            emp.setAge(100+i);
            emp.setEmpSalary(70000.0D+i);
            emps.add(emp);
        }

        /**
         * update t_emp SET emp_name = ?, emp_salary = ?, age = ? where id=? ;
         * update t_emp SET emp_name = ?, emp_salary = ?, age = ? where id=? ;
         * update t_emp SET emp_name = ?, emp_salary = ?, age = ? where id=? ;
         * update t_emp SET emp_name = ?, emp_salary = ?, age = ? where id=? ;
         */

        empDynamicSQLMapper.updateBatchEmp(emps);
    }


    @Test
    void test05(){
        List<Emp> emps = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Emp emp = new Emp();
            emp.setEmpName("张-"+i);
            emp.setAge(20+i);
            emp.setEmpSalary(10000.0D+i);
            emps.add(emp);
        }

        //批量插入  values (?, ?, ?), (?, ?, ?)
        empDynamicSQLMapper.addEmps(emps);
    }

    @Test
    void test04(){
        List<Integer> list = Arrays.asList(1, 2, 3);
        list = new ArrayList<>();
        List<Emp> idIn = empDynamicSQLMapper.getEmpsByIdIn(list);
        for (Emp emp : idIn) {
            System.out.println(emp);
        }
    }

    @Test
    void test03(){
        empDynamicSQLMapper.queryEmpByNameAndSalaryWhen("aaa",new BigDecimal("40000.00"));
    }

    @Test
    void test02(){
        Emp emp = new Emp();
        emp.setId(7);
        emp.setEmpName("哈哈222");

        // update t_emp set emp_name = ?, where id = ?
//        emp.setAge(18);
//        emp.setEmpSalary(10.0D);


        // 带了那个只更新那个
        empDynamicSQLMapper.updateEmp(emp);
    }


    @Test
    void test01(){
        // 1、 select * from t_emp where and emp_salary = ?;
//        empDynamicSQLMapper.queryEmpByNameAndSalary(null, new BigDecimal("1000.00"));

        // 2、select * from t_emp where
        empDynamicSQLMapper.queryEmpByNameAndSalary(null, null);
    }
}
