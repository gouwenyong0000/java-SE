package com.learn.springsecurity.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * autor:liman
 * createtime:2021/6/28
 * comment:
 */
@SpringBootApplication
@RestController
@EnableSwagger2
//如果引入的是app项目，则打开如下注解
//@ComponentScan(basePackages = {"com.learn.springsecurity.app","com.learn.springsecurity.demo"
//,"com.learn.springsecurity.core"})
//@EnableWebSecurity
//如果引入的是browser项目，则打开如下注解
@ComponentScan(basePackages = {"com.learn.springsecurity.browser","com.learn.springsecurity.demo"
,"com.learn.springsecurity.core"})
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60*10,redisNamespace = "self#security#session")
public class SpringSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoApplication.class);
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello spring springsecurity";
    }

}
