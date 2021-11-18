package com.atguigu.vod.test;

import java.io.File;

public class PrintFilename {

    public static void main(String[] args) {
        File file = new File("F:\\【Java】\\【Java】尚硅谷\\5.尚硅谷全套JAVA教程—项目实战（20.64GB）\\在线教育--谷粒学院\\视频");
        File[] files = file.listFiles();

        for (File file1 : files) {

            System.out.println(file1.getName());
            File[] files1 = file1.listFiles();
            for (File file2 : files1) {
                System.out.println( "\t\t--"+  file2.getName());

            }

        }

    }
}
