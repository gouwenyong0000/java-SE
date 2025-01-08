package com.atguigu.spring.ioc.bean;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;



//说明属性来源： 把指定的文件导入容器中，供我们取值使用

// 1、classpath:cat.properties；从自己的项目类路径下找
// 2、classpath*:Log4j-charsets.properties；从所有包的类路径下找
@PropertySource("classpath:conf/cat.properties")
@Data
@Component
public class Cat {

    @Value("${cat.name:Tom}") // : 后面是取不到的时候的默认值；
    private String name;
    @Value("${cat.age:20}")
    private int age;

}
