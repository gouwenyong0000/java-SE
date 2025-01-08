package com.atguigu.spring.aop.proxy.dynamic;

import com.atguigu.spring.aop.log.LogUtils;

import java.lang.reflect.Proxy;
import java.util.Arrays;


/**
 * 动态代理： JDK动态代理；
 * 强制要求，目标对象必有接口。代理的也只是接口规定的方法。
 *
 */
public class DynamicProxy {

    //获取目标对象的代理对象
    public static Object getProxyInstance(Object target) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    String name = method.getName();
                    //记录开始
                    LogUtils.logStart(name, args);
                    Object result = null;
                    try{
                        result = method.invoke(target, args);
                        //记录返回值
                        LogUtils.logReturn(name, result);
                    }catch (Exception e){
                        //记录异常
                        LogUtils.logException(name, e);
                    }finally {
                        //记录结束
                        LogUtils.logEnd(name);
                    }
                    return result;
                }
        );
    }
}
