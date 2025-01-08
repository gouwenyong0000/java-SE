package com.atguigu.mybatis;


import com.atguigu.mybatis.mapper.EmpMapper;
import com.atguigu.mybatis.service.EmpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CacheTest {

    @Autowired
    EmpService empService;

    @Test
    void test(){
        empService.find();
    }
}
