package com.atguigu.boot.service;


import com.atguigu.boot.event.UserLoginSuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


// 本地消息（事件）模式;
// 分布式消息（事件）模式;
@Slf4j
@Service // 优惠券服务
public class CouponService {



    @Async
    @EventListener(UserLoginSuccessEvent.class)
    public void listen(UserLoginSuccessEvent event){
        log.info("优惠券服务 = 监听到：UserLoginSuccessEvent 事件");
        String username = event.getUsername();
        //调用业务
        giveCoupon(username);
    }

    public void giveCoupon(String username) {
        log.info("发放给用户【{}】一张优惠券",username);
    }
}
