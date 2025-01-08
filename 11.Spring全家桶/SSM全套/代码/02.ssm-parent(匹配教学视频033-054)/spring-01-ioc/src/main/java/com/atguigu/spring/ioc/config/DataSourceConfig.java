package com.atguigu.spring.ioc.config;


import com.atguigu.spring.ioc.datasource.MyDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
//@Profile("dev") //整体激活
@Configuration
public class DataSourceConfig {

    //1、定义环境标识：自定义【dev、test、prod】； 默认【default】
    //2、激活环境标识：
    //      明确告诉Spring当前处于什么环境。
    //      你要不说是啥环境，就是 default 环境

    //利用条件注解，只在某种环境下激活一个组件。
    @Profile({"dev","default"})  //  @Profile("环境标识")。当这个环境被激活的时候，才会加入如下组件。
    @Bean
    public MyDataSource dev(){
        MyDataSource myDataSource = new MyDataSource();
        myDataSource.setUrl("jdbc:mysql://localhost:3306/dev");
        myDataSource.setUsername("dev_user");
        myDataSource.setPassword("dev_pwd");

        return myDataSource;
    }


    @Profile("test")
    @Bean
    public MyDataSource test(){
        MyDataSource myDataSource = new MyDataSource();
        myDataSource.setUrl("jdbc:mysql://localhost:3306/test");
        myDataSource.setUsername("test_user");
        myDataSource.setPassword("test_pwd");

        return myDataSource;
    }


    @Profile("prod")
    @Bean
    public MyDataSource prod(){
        MyDataSource myDataSource = new MyDataSource();
        myDataSource.setUrl("jdbc:mysql://localhost:3306/prod");
        myDataSource.setUsername("prod_user");
        myDataSource.setPassword("prod_pwd");

        return myDataSource;
    }
}
