package com.atguigu.springmvc.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


//@ResponseBody
//@Controller
//告诉Spring这是一个控制器（处理请求的组件）
@RestController //前后分离开发就用这个
public class HelloController {


    /**
     *
     * 精确路径必须全局唯一
     * 路径位置通配符： 多个都能匹配上，那就精确优先
     *      *: 匹配任意多个字符（0~N）； 不能匹配多个路径
     *      **： 匹配任意多层路径
     *      ?: 匹配任意单个字符（1）
     *   精确程度： 完全匹配 > ? > * > **
     *
     * @return
     */
    @ResponseBody  //把返回值放到响应体中； 每次请求进来执行目标方法
    @RequestMapping("/hello")
    public String handle() {
        System.out.println("handle()方法执行了!");
        return "Hello,Spring MVC! 你好!~~~"; //默认认为返回值是跳到一个页面
    }



    @ResponseBody
    @RequestMapping("/he?ll")
    public String handle01() {
        System.out.println("handle01方法执行了!");
        return "handle01";
    }

    // /hellr


    @RequestMapping("/he*ll")
    public String handle02() {
        System.out.println("handle02方法执行了!");
        return "handle02";
    }


    @ResponseBody //
    @RequestMapping("/he/**")
    public String handle03() {
        System.out.println("handle03方法执行了!");
        return "handle03";
    }

}
