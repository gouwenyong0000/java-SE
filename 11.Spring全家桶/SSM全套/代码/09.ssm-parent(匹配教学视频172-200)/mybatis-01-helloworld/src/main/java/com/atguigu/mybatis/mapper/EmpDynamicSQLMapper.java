package com.atguigu.mybatis.mapper;


import com.atguigu.mybatis.bean.Emp;
import com.atguigu.mybatis.bean.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 测试动态SQL
 */

@Mapper
public interface EmpDynamicSQLMapper {

    //1、按照 empName 和 empSalary 查询员工。
    List<Emp> queryEmpByNameAndSalary(@Param("name") String name,
                                     @Param("salary") BigDecimal salary);


    void updateEmp(Emp emp);



    List<Emp> queryEmpByNameAndSalaryWhen(@Param("name") String name,
                                      @Param("salary") BigDecimal salary);



    // 查询指定id集合中的员工
    List<Emp> getEmpsByIdIn(List<Integer> ids);


    //批量插入一批员工
    void addEmps(List<Emp> emps);


    //批量修改


    void updateBatchEmp(List<Emp> emps);

}
