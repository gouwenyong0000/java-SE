package com.learn.springsecurity.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

/**
 * autor:liman
 * createtime:2021/7/1
 * comment:自定义的耗时filter
 */
//@Slf4j
////@Component
//public class TimeFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        log.info("filter init");
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        log.info("time filter start");
//        long start = new Date().getTime();
//        chain.doFilter(request, response);
//        long now = new Date().getTime();
//        log.info("time filter 耗时:{}", now - start);
//        log.info("time filter finished");
//    }
//
//    @Override
//    public void destroy() {
//        log.info("self time filter destory");
//    }
//}
