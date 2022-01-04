package com.example.jekinsdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class IndecController {

    @RequestMapping("hello")
    public String get(String msg) {

        return new Date().toString() + " : " + msg;

    }

}
