package com.atguigu.springmvc.controller;


import com.atguigu.springmvc.bean.Person;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


@RestController
public class RequestTestController {


    /**
     * 请求参数：username=zhangsan&password=12345&cellphone=12345456&agreement=on
     * 要求：变量名和参数名保持一致
     * 1、没有携带：包装类型自动封装为null，基本类型封装为默认值
     * 2、携带：自动封装
     * @return
     */
    @RequestMapping("/handle01")
    public String handle01(String username,
                           String password,
                           String cellphone,
                           boolean agreement){
        System.out.println(username);
        System.out.println(password);
        System.out.println(cellphone);
        System.out.println(agreement);
        return "ok";
    }

    /**
     * username=zhangsan&password=123456&cellphone=1234&agreement=on
     * @RequestParam: 取出某个参数的值，默认一定要携带。
     *      required = false：非必须携带；
     *      defaultValue = "123456"：默认值，参数可以不带。
     *
     * 无论请求参数带到了 请求体中还是 url? 后面，他们都是请求参数。都可以直接用@RequestParam或者同一个变量名获取到
     * @param name
     * @param pwd
     * @param phone
     * @param ok
     * @return
     */
    @RequestMapping("/handle02")
    public String handle02(@RequestParam("username") String name,
                           @RequestParam(value = "password",defaultValue = "123456") String pwd,
                           @RequestParam("cellphone") String phone,
                           @RequestParam(value = "agreement",required = false) boolean ok){
        System.out.println(name);
        System.out.println(pwd);
        System.out.println(phone);
        System.out.println(ok);

        return "ok";
    }


    /**
     * 如果目标方法参数是一个 pojo；SpringMVC 会自动把请求参数 和 pojo 属性进行匹配；
     * 效果：
     *      1、pojo的所有属性值都是来自于请求参数
     *      2、如果请求参数没带，封装为null；
     * @param person
     * @return
     */
    //请求体：username=zhangsan&password=111111&cellphone=222222&agreement=on
    @RequestMapping("/handle03")
    public String handle03(Person person){
        System.out.println(person);
        return "ok";
    }


    /**
     * @RequestHeader：获取请求头信息
     * @param host
     * @param ua
     * @return
     */
    @RequestMapping("/handle04")
    public String handle04(@RequestHeader(value = "host",defaultValue = "127.0.0.1") String host,
                           @RequestHeader("user-agent") String ua){
        System.out.println(host);
        System.out.println(ua);
        return "ok~"+host;
    }


    /**
     * @CookieValue：获取cookie值
     * @param haha
     * @return
     */
    @RequestMapping("/handle05")
    public String handle05(@CookieValue("haha") String haha){
        return "ok：cookie是：" + haha;
    }

    /**
     * 使用pojo级联封装复杂属性
     * @param person
     * @return
     */
    @RequestMapping("/handle06")
    public String handle06(Person person){
        System.out.println(person);
        return "ok";
    }


    /**
     * @RequestBody: 获取请求体json数据，自动转为person对象
     * 测试接受json数据
     * 1、发出：请求体中是json字符串，不是k=v
     * 2、接受：@RequestBody Person person
     *
     * @RequestBody Person person
     *      1、拿到请求体中的json字符串
     *      2、把json字符串转为person对象
     * @param person
     * @return
     */
    @RequestMapping("/handle07")
    public String handle07(@RequestBody Person person){
        System.out.println(person);
        //自己把字符串转为对象。
        return "ok";
    }


    /**
     * 文件上传；
     * 1、@RequestParam 取出文件项，封装为MultipartFile，就可以拿到文件内容
     * @param person
     * @return
     */
    @RequestMapping("/handle08")
    public String handle08(Person person,
                           @RequestParam("headerImg") MultipartFile headerImgFile,
                           @RequestPart("lifeImg") MultipartFile[] lifeImgFiles) throws IOException {

        //1、获取原始文件名
        String originalFilename = headerImgFile.getOriginalFilename();
        //2、文件大小
        long size = headerImgFile.getSize();
        //3、获取文件流
        InputStream inputStream = headerImgFile.getInputStream();
        System.out.println(originalFilename + " ==> " + size);
        //4、文件保存
        headerImgFile.transferTo(new File("D:\\img\\" + originalFilename));
        System.out.println("===============以上处理了头像=================");
        if (lifeImgFiles.length > 0) {
            for (MultipartFile imgFile : lifeImgFiles) {
                imgFile.transferTo(new File("D:\\img\\" + imgFile.getOriginalFilename()));
            }
            System.out.println("=======生活照保存结束==========");
        }
        System.out.println(person);
        return "ok!!!";
    }


    /**
     * HttpEntity：封装请求头、请求体； 把整个请求拿过来
     *    泛型：<String>：请求体类型； 可以自动转化
     *
     *
     * @return
     */
    @RequestMapping("/handle09")
    public String handle09(HttpEntity<Person> entity){

        //1、拿到所有请求头
        HttpHeaders headers = entity.getHeaders();
        System.out.println("请求头："+headers);
        //2、拿到请求体
        Person body = entity.getBody();
        System.out.println("请求体："+body);
        return "Ok~~~";
    }


    /**
     * 接受原生 API
     * @param request
     * @param response
     */
    @RequestMapping("/handle10")
    public void handle10(HttpServletRequest request,
                           HttpServletResponse response,
                         HttpMethod method) throws IOException {
        System.out.println("请求方式："+method);
        String username = request.getParameter("username");
        System.out.println(username);
        response.getWriter().write("ok!!!"+username);
    }



}
