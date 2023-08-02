package com.learn.springsecurity.demo.async;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

/**
 * autor:liman
 * createtime:2021/7/2
 * comment: 异步处理的controller
 */
@RestController
@Slf4j
public class AsyncController {

    @Autowired
    private MockQueue mockQueue;

    @Autowired
    private DefferredResultHolder defferredResultHolder;

    @RequestMapping("/order")
    public String order() throws InterruptedException {
        log.info("主线程开始下单操作");
        Thread.sleep(1000);
        log.info("主线程下单处理完成");
        return "success";
    }

    /**
     * callable的处理
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/order/callable")
    public Callable<String> orderCallable() throws InterruptedException {
        log.info("主线程开始下单操作");
        Callable<String> result = new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("副线程开始");
                Thread.sleep(1000);
                log.info("副线程结束");
                return "success";
            }
        };
        log.info("主线程下单处理完成");
        return result;
    }

    /**
     * defferedResult的处理
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/order/defferresult")
    public DeferredResult<String> orderDefferresult() throws Exception {
        log.info("主线程开始下单操作");
        String randomNo = RandomStringUtils.randomNumeric(8);
        mockQueue.setPlaceOrder(randomNo);

        DeferredResult<String> result = new DeferredResult<>();
        defferredResultHolder.getMap().put(randomNo,result);
        log.info("主线程下单处理完成");
        return result;
    }

}
