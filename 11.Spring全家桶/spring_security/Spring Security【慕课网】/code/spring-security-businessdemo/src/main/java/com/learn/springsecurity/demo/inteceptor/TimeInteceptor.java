//package com.learn.springsecurity.demo.inteceptor;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Date;
//
///**
// * autor:liman
// * createtime:2021/7/1
// * comment: 记录耗时的拦截器
// */
//@Slf4j
//@Component//声明成component并不能使这个inteceptor起作用
//public class TimeInteceptor implements HandlerInterceptor {
//
//
//    /**
//     * 在调用目标controller之前这个方法会被调用
//     * @param request
//     * @param response
//     * @param handler
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        log.info("pre handle");
//        request.setAttribute("startTime",new Date().getTime());
//        String requestClassName = ((HandlerMethod) handler).getBean().getClass().getName();
//        String requestMethodName = ((HandlerMethod) handler).getMethod().getName();
//        log.info("处理目标请求的类和方法：{}，{}",requestClassName,requestMethodName);
//        return true;
//    }
//
//    /**
//     * 目标controller调用之后，这个被调用，但是如果出现异常，这个不会被调用
//     * @param request
//     * @param response
//     * @param handler
//     * @param modelAndView
//     * @throws Exception
//     */
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//        log.info("posthanlder");
//        Long startTime = (Long) request.getAttribute("startTime");
//        Long endTime = new Date().getTime();
//        log.info("总耗时:{}",endTime - startTime);
//
//    }
//
//    /**
//     * 不论出现异常或者没有出现异常，这个方法都会被调用
//     * @param request
//     * @param response
//     * @param handler
//     * @param ex
//     * @throws Exception
//     */
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//
//    }
//}
