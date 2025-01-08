package com.atguigu.springmvc.controller;


import com.atguigu.springmvc.bean.Person;
import com.atguigu.springmvc.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;


/**
 * SpringBoot整合的SpringMVC默认不支持JSP
 * 1、引入 thymeleaf 作为模型引擎，渲染页面
 *         <dependency>
 *             <groupId>org.springframework.boot</groupId>
 *             <artifactId>spring-boot-starter-thymeleaf</artifactId>
 *         </dependency>
 * 2、默认规则
 *         页面：src/main/resources/templates
 *      静态资源：src/main/resources/static
 */
@Controller // 开发服务端渲染逻辑
public class PageTestController {

    //处理 / 请求，跳转到登录页

    @RequestMapping("/")
    public String index(){

        // thymeleaf 默认：去 classpath:/templates/ 找页面, 后缀为 .html
        // 页面地址 = classpath:/templates/ + 返回名字 + .html
        return "login";  //返回值就是  页面名称（视图名）
    }

    @RequestMapping("/login.mvc")
    public String login(String username,
                        String password,
                        //模型就是页面要展示的所有数据
                        Model model){
        System.out.println("用户登录："+username+","+password);

        // 去数据库查到登录的用户信息

        // 去数据库查到访客列表

        List<User> list = Arrays.asList(
                new User(1L, "张三1", 18),
                new User(2L, "张三2", 19),
                new User(3L, "张三3", 16),
                new User(4L, "张三4", 12),
                new User(5L, "张三5", 17),
                new User(6L, "张三6", 28),
                new User(7L, "张三7", 11),
                new User(8L, "张三8", 38)
        );


        model.addAttribute("users",list);
        model.addAttribute("name",username);
        model.addAttribute("age",18);

        return "page/success";
    }
}
