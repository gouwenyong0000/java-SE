package com.atguigu.mybatis;


import com.atguigu.mybatis.bean.TEmployee;
import com.atguigu.mybatis.mapper.TEmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GeneratorTest {


    @Autowired
    TEmployeeMapper tEmployeeMapper;

    @Test
    void test01(){
        TEmployee tEmployee = tEmployeeMapper.selectByPrimaryKey(1L);
        System.out.println("tEmployee = " + tEmployee);


        TEmployee update = new TEmployee();
        update.setEmployeeId(1L);
        update.setActualName("超级管理员");

        tEmployeeMapper.updateByPrimaryKeySelective(update);

        //作业： CRUD
        //1、技术栈： SpringBoot + Spring + SpringMVC + MyBatis
        //2、要求：
        //  1）、员工表 CRUD
        //  2）、基本CRUD + 分页查询
        //  3）、RESTful风格
        //  4）、数据校验
        //  5）、全局统一异常处理（业务异常、校验异常）
        //  6）、引入vo分层模型
        //  7）、引入swagger文档
        //  8）、解决跨越
        //  9）、写完以后在接口文档中测试
        //  未来：前端工程化学完后，为你的后台写一个前端
    }
}
