package com.atguigu.spring.ioc.dao;


import com.atguigu.spring.ioc.bean.Dog;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@ToString
@Repository
public class UserDao {


    Dog haha;


    /**
     * 推荐：构造器注入
     * @param dog
     */
    //Spring 自动去容器中找到 构造器需要的所有参数的组件值。
//    public UserDao(Dog dog){
//        System.out.println("UserDao...有参构造器："+dog);
//        this.haha = dog;
//    }
//    @Autowired
//    public void setDog(@Qualifier("dog02") Dog dog) {
//        System.out.println("setDog..."+dog);
//        this.haha = dog;
//    }
}
