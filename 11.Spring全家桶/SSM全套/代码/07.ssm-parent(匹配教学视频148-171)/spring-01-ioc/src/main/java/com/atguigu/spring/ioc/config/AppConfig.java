package com.atguigu.spring.ioc.config;


import ch.qos.logback.core.CoreConstants;
import com.atguigu.spring.ioc.bean.Car;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;



@Import({CoreConstants.class})
@Configuration
@ComponentScan(basePackages = "com.atguigu.spring") //组件批量扫描； 只扫利用Spring相关注解注册到容器中的组件
public class AppConfig {


}
