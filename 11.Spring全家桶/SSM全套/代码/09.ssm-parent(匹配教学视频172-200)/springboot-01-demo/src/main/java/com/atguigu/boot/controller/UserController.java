package com.atguigu.boot.controller;


import com.atguigu.boot.event.UserLoginSuccessEvent;
import com.atguigu.boot.service.CouponService;
import com.atguigu.boot.service.UserPointsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {


    @Autowired
    CouponService couponService;

    @Autowired
    UserPointsService userPointsService;

    @Autowired
    ApplicationEventPublisher publisher;


    //同步阻塞式；

    //事件/消息驱动；


    @GetMapping("/login")
    public String login(String username, String password){
        //执行登录
        log.info("用户[{}]登录系统", username);
        //做事；同步调用
        UserLoginSuccessEvent event = new UserLoginSuccessEvent(username);
        //发送事件
        publisher.publishEvent(event);

        //1、加积分
//        userPointsService.givePoints(username);
        //2、发优惠劵
//        couponService.giveCoupon(username);


        return "登录成功";
    }
}
