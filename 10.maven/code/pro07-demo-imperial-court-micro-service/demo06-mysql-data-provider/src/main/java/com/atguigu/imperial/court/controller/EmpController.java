package com.atguigu.imperial.court.controller;

import com.atguigu.imperial.court.entity.Emp;
import com.atguigu.imperial.court.service.api.EmpService;
import com.atguigu.imperial.court.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmpController {

    @Autowired
    private EmpService empService;

    @RequestMapping("/remote/get/emp/by/login/info")
    ResultEntity<Emp> getEmpByLoginInfo(
            @RequestParam("loginAccount") String loginAccount,
            @RequestParam("loginPassword") String loginPassword) {

        try {
            // 1、调用 Service 方法根据账号、密码查询 Emp 对象
            Emp emp = empService.getEmpByLoginInfo(loginAccount, loginPassword);

            // 2、返回成功消息
            return ResultEntity.successWithData(emp);

        } catch (Exception e) {
            e.printStackTrace();

            String message = e.getMessage();

            // 3、返回失败消息
            return ResultEntity.failed(message);
        }
    }

}
