package com.atguigu.spring.ioc.bean;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


//BeanPostProcessor：Bean外挂修改器

@Data
public class User implements InitializingBean, DisposableBean {
    private String username;
    private String password;

    private Car car;



    @Autowired
    public void setCar(Car car) {
        System.out.println("【User】 ==> setter 自动注入：属性值："+car);
        this.car = car;
    }

    public User(){
        System.out.println("【User】 ==> User 构造器...");
    }


    @PostConstruct //构造器之后
    public void postConstruct(){
        System.out.println("【User】 ==> @PostConstruct....");
    }


    @PreDestroy
    public void preDestroy(){
        System.out.println("【User】 ==> @PreDestroy....");
    }

    public void initUser(){
        System.out.println("【User】 ==> @Bean 初始化：initUser");
    }

    public void destoryUser(){
        System.out.println("【User】 ==> @Bean 销毁：destoryUser");
    }


    /**
     * 属性设置之后进行调用： set赋值完成了
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("【User】 ==> 【InitializingBean】 ==== afterPropertiesSet....");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("【User】 ==> 【DisposableBean】 ==== destroy....");
    }
}
