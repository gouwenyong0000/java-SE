package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
//@CrossOrigin
@RestController
@RequestMapping("/eduservice/user")
public class UserController {


    @PostMapping("login")
    public R login(String username, String password) {

        System.out.println(username + LocalDateTime.now().toLocalTime());
        return R.ok()
                .data("token", "admin");
    }

    @GetMapping("info")
    public R info(String token) {

        return R.ok()
                .data("roles", "[admin]")
                .data("name", "admin")
                .data("avatar", "https://www.3ktu.com/touxiang/keai/177146_3.html");
    }
}
