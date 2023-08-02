package com.learn.springsecurity.sso.client02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * autor:liman
 * createtime:2021/8/16
 * comment:
 */
@SpringBootApplication
@EnableOAuth2Sso//让sso生效
@RestController
public class SsoClient02Application {

    @GetMapping("/user")
    public Authentication user(Authentication user) {
        return user;
    }

    public static void main(String[] args) {
        SpringApplication.run(SsoClient02Application.class, args);
    }
}
