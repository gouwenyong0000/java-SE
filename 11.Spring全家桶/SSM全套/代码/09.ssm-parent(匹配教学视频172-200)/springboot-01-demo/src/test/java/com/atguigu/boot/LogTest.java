package com.atguigu.boot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;



@Slf4j
@SpringBootTest
public class LogTest {
    //1、获取一个日志记录器
//    Logger logger = LoggerFactory.getLogger(LogTest.class);


    @Test
    void test02() throws InterruptedException {
        int i = 0;
//        while (true) {
//            log.info("info日志.... 数字:【{}】，ok={}", i++, "哈哈");
//            Thread.sleep(3);
//        }
    }

    @Test
    void test01(){


//        System.out.println("djkaljdalkjdklaj");
        //2、记录日志
        //级别：由低到高：ALL -- TRACE -- DEBUG -- INFO -- WARN -- ERROR -- OFF
        //越打印，越粗糙； 日志有一个默认级别（INFO）；只会打印这个级别之上的所有信息；

        log.trace("追踪日志......");
        if("1".equals(log)){
            log.debug("调试日志.......");
            //业务流程

            try {
                //关键点
                log.info("信息日志........");

                //容易出问题点
//                aa.bb(){
//                    log.warn("警告日志........");
//                };

            }catch (Exception e){
                log.error("错误日志......"+e.getMessage());
            }

        }




        //格式： 时间  级别  进程id --- 项目名 --- 线程名 --- 当前类名: 日志内容

    }
}
