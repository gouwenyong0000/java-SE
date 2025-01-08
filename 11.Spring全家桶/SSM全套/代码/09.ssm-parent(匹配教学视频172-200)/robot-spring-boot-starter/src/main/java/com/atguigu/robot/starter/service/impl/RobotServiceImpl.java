package com.atguigu.robot.starter.service.impl;


import com.atguigu.robot.starter.properties.RobotProperties;
import com.atguigu.robot.starter.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RobotServiceImpl implements RobotService {


    @Autowired
    RobotProperties robotProperties;

    @Override
    public String sayHello() {
        return "我是机器人【"+robotProperties.getName()+"】，使用底层大模型：【"+robotProperties.getModel()+"】；我们开始聊天吧";
    }
}
