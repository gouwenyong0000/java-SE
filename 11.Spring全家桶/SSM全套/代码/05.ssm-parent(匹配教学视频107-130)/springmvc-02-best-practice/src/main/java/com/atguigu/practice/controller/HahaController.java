package com.atguigu.practice.controller;


import com.atguigu.practice.common.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
public class HahaController {


    @GetMapping("/haha")
    public R haha() throws FileNotFoundException {
//        int i = 10/0;
        new FileInputStream("1.txt");
        return R.ok();
    }


    /**
     * 异常处理优先级：
     *      本类 》 全局
     *      精确 》 模糊
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    public R error(ArithmeticException e){
        System.out.println("【本类】 - ArithmeticException 处理");
        return R.error(500,e.getMessage());
    }
}
