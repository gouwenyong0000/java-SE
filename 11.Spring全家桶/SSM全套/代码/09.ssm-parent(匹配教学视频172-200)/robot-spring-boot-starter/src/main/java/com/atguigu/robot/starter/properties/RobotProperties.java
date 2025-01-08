package com.atguigu.robot.starter.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "robot")
@Data
public class RobotProperties {

    private String name;
    private String model;
}
