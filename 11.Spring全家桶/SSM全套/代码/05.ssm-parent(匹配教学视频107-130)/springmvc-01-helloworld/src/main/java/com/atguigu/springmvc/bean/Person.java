package com.atguigu.springmvc.bean;


import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * username=张三&password=111111&cellphone=122223334&agreement=on
 * &address.province=陕西&address.city=西安市&address.area=雁塔&
 * sex=男&hobby=足球&hobby=篮球&grade=二年级
 */
@Data //JavaBean 定死的数据模型； 定不死的写Map
public class Person {
    // username=zhangsan&password=123456&cellphone=1234&agreement=on
    private String username = "zhangsan"; // request.getParameter("username")
    private String password; // request.getParameter("password")
    private String cellphone;
    private boolean agreement;
    private Address address;
    private String sex;
    private String[] hobby; // request.getParameterValues("hobby")
    private String grade;
}


@Data
class Address {
    private String province;
    private String city;
    private String area;
}
