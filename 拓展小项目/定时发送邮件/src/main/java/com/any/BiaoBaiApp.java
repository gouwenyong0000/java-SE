package com.any;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @describe
 * @author: AnyWhere
 * @date 2020/12/15 12:15
 */
@EnableScheduling
@SpringBootApplication
public class BiaoBaiApp {
    public static void main(String[] args) {
        SpringApplication.run(BiaoBaiApp.class,args);
    }
}
