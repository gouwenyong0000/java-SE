package com.atguigu.robot.starter.controller;



import com.atguigu.robot.starter.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RobotController {



    @Autowired
    RobotService robotService;

    @GetMapping("/robot/hello")
    public String sayHello(){
        String msg = robotService.sayHello();
        return msg;
    }
}
