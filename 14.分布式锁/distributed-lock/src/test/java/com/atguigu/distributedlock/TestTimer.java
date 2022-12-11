package com.atguigu.distributedlock;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TestTimer {

    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                   System.out.println(LocalDateTime.now());


            }
        },5,10000);


        Thread.sleep(30000);
        timer.cancel();
    }
}
