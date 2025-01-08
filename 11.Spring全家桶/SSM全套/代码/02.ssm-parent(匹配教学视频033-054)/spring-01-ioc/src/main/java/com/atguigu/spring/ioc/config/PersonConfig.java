package com.atguigu.spring.ioc.config;

import ch.qos.logback.core.CoreConstants;
import com.atguigu.spring.ioc.bean.Person;
import com.atguigu.spring.ioc.condition.MacCondition;
import com.atguigu.spring.ioc.condition.WindowsCondition;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.*;




@Configuration //告诉Spring容器，这是一个配置类
public class PersonConfig {

    //场景：判断当前电脑的操作系统是windows还是mac
    //  windows 系统，容器中有 bill
    //  mac 系统，容器中有 joseph
    @Conditional(MacCondition.class)
    @Bean("joseph")
    public Person joseph(){
        Person person = new Person();
        person.setName("乔布斯");
        person.setAge(20);
        person.setGender("男");
        return person;
    }

    @Conditional(WindowsCondition.class)
    @Bean("bill")
    public Person bill(){
        Person person = new Person();
        person.setName("比尔盖茨");
        person.setAge(20);
        person.setGender("男");
        return person;
    }

    @Primary //主组件：默认组件
    @Bean("zhangsan")
    public Person haha() {
        Person person = new Person();
        person.setName("张三2");
        person.setAge(20);
        person.setGender("男");
        return person;
    }
    //3、给容器中注册一个自己的组件； 容器中的每个组件都有自己的名字，方法名就是组件的名字
    @Bean("zhangsan")
    public Person zhangsan() {
        Person person = new Person();
        person.setName("张三1");
        person.setAge(20);
        person.setGender("男");
        return person;
    }
    @Bean("lisi")
    public Person lisi() {
        Person person = new Person();
        person.setName("李四");
        person.setAge(20);
        person.setGender("男");
        return person;
    }

}
