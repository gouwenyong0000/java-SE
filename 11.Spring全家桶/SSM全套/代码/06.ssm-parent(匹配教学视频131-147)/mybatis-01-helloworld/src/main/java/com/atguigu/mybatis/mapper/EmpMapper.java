package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.bean.Emp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper  //告诉Spring，这是MyBatis操作数据库用的接口; Mapper接口
public interface EmpMapper {

    Emp getEmpById02(Integer id,String tableName);

    //按照id查询
    Emp getEmpById(Integer id);


    //查询所有员工
    List<Emp> getAll();


    //添加员工
    void addEmp(Emp emp);


    //更新员工
    void updateEmp(Emp emp);

    //删除员工
    void deleteEmpById(Integer id);

}
