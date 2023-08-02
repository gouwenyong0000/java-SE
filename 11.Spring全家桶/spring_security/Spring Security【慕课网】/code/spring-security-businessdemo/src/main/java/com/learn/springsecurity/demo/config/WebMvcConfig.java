package com.learn.springsecurity.demo.config;

//import com.learn.springsecurity.demo.filter.ThirdTimeFilter;
//import com.learn.springsecurity.demo.inteceptor.TimeInteceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.FilterRegistration;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * autor:liman
 * createtime:2021/7/1
 * comment:
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

//    @Autowired
//    private TimeInteceptor timeInteceptor;

    //异步支持相关的配置
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        super.configureAsyncSupport(configurer);
//        configurer.
    }


    /**
     * 将自己定义的拦截器加入到web容器中
     * @param registry
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(timeInteceptor);
//    }

    /**
     * web容器中加入第三方的filter
     * @return
     */
//    @Bean
//    public FilterRegistrationBean initFilterChain(){
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        ThirdTimeFilter thirdTimeFilter = new ThirdTimeFilter();
//        filterRegistrationBean.setFilter(thirdTimeFilter);
//        List<String> urls = new ArrayList<>();
//        urls.add("/user/*");
//        filterRegistrationBean.setUrlPatterns(urls);
//
//        return filterRegistrationBean;
//    }

}
