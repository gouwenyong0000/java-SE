package com.atguigu.imperial.court.controller;

import com.atguigu.imperial.court.MySQLProvider;
import com.atguigu.imperial.court.entity.Emp;
import com.atguigu.imperial.court.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    // 1、本地使用 @Autowired 注解装配远程接口类型即可实现方法的远程调用，
    // 看起来就像是调用本地方法一样，我们管这种特性叫方法的声明式远程调用。
    // 2、凭啥通过 @Autowired 注解就能够导入远程接口对应的 bean
    //      ①当前环境包含 Feign 相关 jar 包。
    //      ②当前微服务的主启动类上标记 @EnableFeignClients
    //      ③符合 SpringBoot 自动扫描包的约定规则：默认情况下主启动类所在的包、以及主启动类所在包的子包都会被自动扫描
    //          主启动类所在包：     com.atguigu.imperial.court
    //          被扫描的接口所在的包：com.atguigu.imperial.court.api
    @Autowired
    private MySQLProvider mySQLProvider;

    @RequestMapping("/consumer/do/login")
    public String doLogin(@RequestParam("loginAccount") String loginAccount,
                          @RequestParam("loginPassword") String loginPassword, HttpSession session, Model model) {

        // 1、调用远程接口根据登录账号、密码查询 Emp 对象
        ResultEntity<Emp> resultEntity = mySQLProvider.getEmpByLoginInfo(loginAccount, loginPassword);

        // 2、验证远程接口调用是否成功
        String result = resultEntity.getResult();

        if ("SUCCESS".equals(result)) {

            // 3、从 ResultEntity 中获取查询得到的 Emp 对象
            Emp emp = resultEntity.getData();

            // 4、将 Emp 对象存入 Session 域
            session.setAttribute("loginInfo", emp);

            // 5、前往 target 页面
            return "target";
        } else {

            // 6、获取失败消息
            String message = resultEntity.getMessage();

            // 7、将失败消息存入模型
            model.addAttribute("message", message);

            // 8、回到登录页面
            return "index";

        }
    }

}
