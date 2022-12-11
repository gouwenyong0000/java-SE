package com.atguigu.distributedlock.controller;

import com.atguigu.distributedlock.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class StockController {
    @Resource
    StockService stockService;

    @GetMapping("/stock/deduct")
    public String deduct() {
//        stockService.deduct1();
//        stockService.checkAndLock();
        stockService.zkReentrantLock();

        return "ok";
    }


    @GetMapping("test/semaphore")
    public String testSemaphore() {
        this.stockService.testSemaphore();
        return "测试信号量";
    }
}
