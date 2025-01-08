package com.atguigu.spring.ioc.service;


import com.atguigu.spring.ioc.bean.Dog;
import com.atguigu.spring.ioc.bean.Person;
import com.atguigu.spring.ioc.dao.UserDao;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Data
@ToString
@Service
public class UserService {


    /**
     * Consider marking one of the beans as @Primary,
     * updating the consumer to accept multiple beans,
     * or using @Qualifier to identify the bean that should be consumed
     */
//    @Qualifier("bill") //精确指定：如果容器中这样的组件存在多个，则使用@Qualifier精确指定组件名

    @Qualifier("bill") //精确指定：如果容器中这样的组件存在多个，且有默认组件。我们可以使用 @Qualifier 切换别的组件。
    @Autowired
    Person atom; // @Primary 一旦存在，改属性名就不能实现组件切换了。



    //面试题：@Resource 和 @Autowired 区别？
    //1、@Autowired 和 @Resource 都是做bean的注入用的，都可以放在属性上
    //2、@Resource 具有更强的通用性
    @Resource
    UserDao userDao;

//    @Autowired(required = false)

//    @Resource
//    @Autowired(required = false)
//    Dog dog;




}
