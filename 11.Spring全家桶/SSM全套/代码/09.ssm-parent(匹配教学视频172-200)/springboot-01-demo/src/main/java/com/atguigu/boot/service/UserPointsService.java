package com.atguigu.boot.service;


import com.atguigu.boot.event.UserLoginSuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service //用户积分服务
public class UserPointsService {



    @Async //异步
    @EventListener(UserLoginSuccessEvent.class)
    public void listen(UserLoginSuccessEvent event){
        log.info("用户积分服务 == 监听到 UserLoginSuccessEvent 事件");
        givePoints(event.getUsername());
    }

    public void givePoints(String username) {
        log.info("用户【{}】获得积分", username);
    }
}
