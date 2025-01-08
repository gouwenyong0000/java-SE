package com.atguigu.practice.controller;


import com.atguigu.practice.common.R;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * 测试声明式异常处理
 */
@RestController
public class HelloController {


    //编程式的异常处理；
    //如果大量业务都需要加异常处理代码的话，会很麻烦
//        try {
//            //执行业务
//
//        }catch (Exception e){
//            return R.error(100,"执行异常");
//        }
    @GetMapping("/hello")
    public R hello(@RequestParam(value = "i",defaultValue = "0") Integer i) throws FileNotFoundException {
        int j = 10 / i;

//        FileInputStream inputStream = new FileInputStream("D:\\123.txt");
        String s = null;
        s.length();
        return R.ok(j);
    }


    /**
     * 1、如果Controller本类出现异常，会自动在本类中找有没有@ExceptionHandler标注的方法，
     *    如果有，执行这个方法，它的返回值，就是客户端收到的结果
     *  如果发生异常，多个都能处理，就精确优先
     * @return
     */
    @ResponseBody
    @ExceptionHandler(ArithmeticException.class)
    public R handleArithmeticException(ArithmeticException ex){
        System.out.println("【本类】 - ArithmeticException 异常处理");
        return R.error(100,"执行异常：" + ex.getMessage());
    }


    @ExceptionHandler(FileNotFoundException.class)
    public R handleException(FileNotFoundException ex){
        System.out.println("【本类】 - FileNotFoundException 异常处理");
        return R.error(300,"文件未找到异常：" + ex.getMessage());
    }


    @ExceptionHandler(Throwable.class)
    public R handleException02(Throwable ex){
        System.out.println("【本类】 - Throwable 异常处理");
        return R.error(500,"其他异常：" + ex.getMessage());
    }

}
