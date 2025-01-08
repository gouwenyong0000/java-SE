package com.atguigu.practice.config;


import com.atguigu.practice.interceptor.MyHandlerInterceptor0;
import com.atguigu.practice.interceptor.MyHandlerInterceptor1;
import com.atguigu.practice.interceptor.MyHandlerInterceptor2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 1、容器中需要有这样一个组件：【WebMvcConfigurer】
 *     1)、@Bean 放一个 WebMvcConfigurer
 *     2)、配置类实现 WebMvcConfigurer
 */
@Configuration //专门对SpringMVC 底层做一些配置
public class MySpringMVCConfig  implements WebMvcConfigurer{

    @Autowired
    MyHandlerInterceptor0 myHandlerInterceptor0;


    @Autowired
    MyHandlerInterceptor1 myHandlerInterceptor1;


    @Autowired
    MyHandlerInterceptor2 myHandlerInterceptor2;
    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myHandlerInterceptor0)
                .addPathPatterns("/**"); //拦截所有请求
        registry.addInterceptor(myHandlerInterceptor1)
                .addPathPatterns("/**"); //拦截所有请求

        registry.addInterceptor(myHandlerInterceptor2)
                .addPathPatterns("/**");
    }


    //    @Bean
//    WebMvcConfigurer webMvcConfigurer(){
//        return new WebMvcConfigurer() {
//            @Override
//            public void addInterceptors(InterceptorRegistry registry) {
//
//            }
//        };
//    }
}
