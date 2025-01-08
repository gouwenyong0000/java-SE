package com.atguigu.boot;

import com.atguigu.robot.starter.RobotAutoConfiguration;
import com.atguigu.robot.starter.annotation.EnableRobot;
import com.atguigu.robot.starter.controller.RobotController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


/**
 * spring-boot-start-web
 *
 * 为什么导入  robot-spring-boot-starter ，访问 controller 是 404？
 * 原因：主程序只会扫描到自己所在的包及其子包下的所有组件
 *
 * 自定义starter：
 * 1、第一层抽取：编写一个自动配置类，别人导入我的starter，
 *              无需关心需要给容器中导入哪些组件，只需要导入自动配置类，
 *              自动配置类帮你给容器中导入所有这个场景要用的组件
 *               @import(RobotAutoConfiguration.class)
 * 2、第二层抽取：只需要标注功能开关注解。@EnableRobot
 * 3、第三层抽取：只需要导入starter，所有功能就绪
 */
@SpringBootApplication
public class Springboot02DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot02DemoApplication.class, args);
    }

}
