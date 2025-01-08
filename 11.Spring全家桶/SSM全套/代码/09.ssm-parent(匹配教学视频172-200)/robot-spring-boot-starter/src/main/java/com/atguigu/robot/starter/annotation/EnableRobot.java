package com.atguigu.robot.starter.annotation;


import com.atguigu.robot.starter.RobotAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RobotAutoConfiguration.class)
public @interface EnableRobot {


}
