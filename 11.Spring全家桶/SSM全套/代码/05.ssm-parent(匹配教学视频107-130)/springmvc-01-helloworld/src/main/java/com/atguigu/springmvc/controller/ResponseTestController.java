package com.atguigu.springmvc.controller;


import com.atguigu.springmvc.bean.Person;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
public class ResponseTestController {


    /**
     * 会自动的把返回的对象转为json格式
     *
     * @return
     */
//    @ResponseBody //把返回的内容。写到响应体中
    @RequestMapping("/resp01")
    public Person resp01() {
        Person person = new Person();
        person.setUsername("张三");
        person.setPassword("1111");
        person.setCellphone("22222");
        person.setAgreement(false);
        person.setSex("男");
        person.setHobby(new String[]{"足球", "篮球"});
        person.setGrade("三年级");

        return person;
    }


    /**
     * 文件下载
     * HttpEntity：拿到整个请求数据
     * ResponseEntity：拿到整个响应数据（响应头、响应体、状态码）
     *
     * @return
     */
    @RequestMapping("/download")
    public ResponseEntity<InputStreamResource> download() throws IOException {

        //以上代码永远别改
        FileInputStream inputStream = new FileInputStream("C:\\Users\\53409\\Pictures\\Saved Pictures\\必应壁纸（1200张）\\AutumnNeuschwanstein_EN-AU10604288553_1920x1080.jpg");
        //一口气读会溢出
//        byte[] bytes = inputStream.readAllBytes();
        //1、文件名中文会乱码：解决：
        String encode = URLEncoder.encode("哈哈美女.jpg", "UTF-8");
        //以下代码永远别改
        //2、文件太大会oom（内存溢出）
        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                //内容类型：流
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                //内容大小
                .contentLength(inputStream.available())
                //  Content-Disposition ：内容处理方式
                .header("Content-Disposition", "attachment;filename="+encode)
                .body(resource);
    }
}
