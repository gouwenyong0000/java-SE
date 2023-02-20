package com.atguigu.test;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ThreadLocalTest {

    public static void main(String[] args) {
        Task task = new Task();

        new Thread(task,"1").start();
        new Thread(task,"2").start();
        new Thread(task,"3").start();
    }
}


class Task implements Runnable{

    public static final ConcurrentMap<String,Object> CONCURRENT_MAP = new ConcurrentHashMap<>();

    Random random=new Random();
    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        int num = random.nextInt(1000);
        System.out.println(name + "put:" + num);
        CONCURRENT_MAP.put(name,num);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name+ "get:"+CONCURRENT_MAP.get(name));

    }
}