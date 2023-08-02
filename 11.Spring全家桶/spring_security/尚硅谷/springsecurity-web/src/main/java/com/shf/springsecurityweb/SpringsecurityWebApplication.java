package com.shf.springsecurityweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@MapperScan("com.shf")
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true) // 开启权限注解
public class SpringsecurityWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringsecurityWebApplication.class, args);
	}

}
