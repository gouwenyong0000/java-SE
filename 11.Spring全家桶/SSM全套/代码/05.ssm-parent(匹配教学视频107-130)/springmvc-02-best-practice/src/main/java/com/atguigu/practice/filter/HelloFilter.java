package com.atguigu.practice.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@WebFilter("/hello")  //原生的web注解就没用了
@Component // 默认拦截所有请求
public class HelloFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter 前置.....");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("filter 后置.....");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
