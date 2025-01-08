package com.atguigu.mybatis.config;


import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@MapperScan("com.atguigu.mybatis.mapper") //批量只扫描mapper
@Configuration
public class MyBatisConfig {

    @Bean
    PageInterceptor pageInterceptor(){
        //1、创建 分页插件 对象
        PageInterceptor interceptor = new PageInterceptor();
        //2、设置 参数
        //.......
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");

        interceptor.setProperties(properties);
        return interceptor;
    }

}
