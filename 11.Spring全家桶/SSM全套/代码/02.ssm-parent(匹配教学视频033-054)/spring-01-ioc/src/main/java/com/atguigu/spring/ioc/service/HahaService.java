package com.atguigu.spring.ioc.service;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;




@Getter
@ToString
@Service
public class HahaService implements EnvironmentAware, BeanNameAware {

    private Environment environment;
    private String myName;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getOsType(){
       return environment.getProperty("OS");
    }

    @Override
    public void setBeanName(String name) {
        this.myName = name;
    }
}
