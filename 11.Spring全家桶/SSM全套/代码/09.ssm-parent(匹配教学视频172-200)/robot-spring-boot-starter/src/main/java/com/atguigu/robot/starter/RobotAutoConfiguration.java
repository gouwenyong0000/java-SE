package com.atguigu.robot.starter;


import com.atguigu.robot.starter.controller.RobotController;
import com.atguigu.robot.starter.properties.RobotProperties;
import com.atguigu.robot.starter.service.RobotService;
import com.atguigu.robot.starter.service.impl.RobotServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@EnableConfigurationProperties(RobotProperties.class)
@Configuration //把这个场景要用的所有组件导入到容器中
public class RobotAutoConfiguration {

    @Bean
    public RobotController robotController() {
        return new RobotController();
    }

    @Bean
    public RobotService  robotService() {
        return new RobotServiceImpl();
    }

}
