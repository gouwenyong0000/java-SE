package com.learn.springsecurity.demo.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * autor:liman
 * createtime:2021/7/2
 * comment: 模拟监听的线程
 */
@Component
@Slf4j
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MockQueue mockQueue;
    @Autowired
    private DefferredResultHolder defferredResultHolder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        new Thread(()->{
            while(true){
                if(StringUtils.isNotBlank(mockQueue.getCompleteOrder())){
                    String orderNo = mockQueue.getCompleteOrder();
                    log.info("返回订单处理结果:{}",orderNo);
                    defferredResultHolder.getMap().get(orderNo).setResult("place order success");
                    mockQueue.setCompleteOrder(null);
                }else{
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
