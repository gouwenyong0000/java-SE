package com.learn.springsecurity.demo.async;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * autor:liman
 * createtime:2021/7/2
 * comment: 模拟deferredResult的消息队列
 */
@Component
@Slf4j
public class MockQueue {

    private String placeOrder;

    private String completeOrder;


    public String getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(String placeOrder) throws Exception {
        new Thread(() -> {
            log.info("接到下单的请求");
            this.placeOrder = placeOrder;
            try {
                Thread.sleep(1000);//模拟下单的处理
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.completeOrder = placeOrder;
            log.info("下单请求处理完毕");
        }).start();
    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }
}
