package com.atguigu.spring.aop;

import com.atguigu.spring.aop.calculator.MathCalculator;
import com.atguigu.spring.aop.calculator.impl.MathCalculatorImpl;
import com.atguigu.spring.aop.proxy.dynamic.DynamicProxy;
import com.atguigu.spring.aop.proxy.statics.CalculatorStaticProxy;
import com.atguigu.spring.aop.service.UserService;
import com.atguigu.spring.aop.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MathTest {




    @Test
    void test03(){
        MathCalculator proxyInstance = (MathCalculator) DynamicProxy.getProxyInstance(new MathCalculatorImpl());

        proxyInstance.add(1, 2);

        System.out.println("===================================");
        UserService instance = (UserService) DynamicProxy.getProxyInstance(new UserServiceImpl());
        instance.saveUser();

        System.out.println("===================================");
        proxyInstance.div(10,0);


    }

    @Test
    void test02() {
        //1、原生对象
        MathCalculator target = new MathCalculatorImpl();
        MathCalculator chenglong = new MathCalculatorImpl();
        MathCalculator lilianjie = new MathCalculatorImpl();
        MathCalculator wangyuan = new MathCalculatorImpl();
//        target.add(1, 2);

        //2、一行代码搞定所有； 动态代理是Java原生支持；


        InvocationHandler h = new InvocationHandler() {

            /**
             * proxy： 代理对象： 明星经纪人
             * method： 代理对象准备调用目标对象的这个方法；
             * args：方法调用传递的参数
             * @return
             * @throws Throwable
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //目标方法执行
                System.out.println("瞅瞅参数："+ Arrays.asList(args));
                args[1] = 0;
                System.out.println("改后参数："+ Arrays.asList(args));

                //真正执行之前可以拦截
                Object result = method.invoke(wangyuan, args);


                return result;
            }
        };

        /**
         * ClassLoader loader,  类加载器(目标对象)
         * Class<?>[] interfaces, 目标对象实现的接口
         * InvocationHandler h：
         */
        //3、创建动态代理
        MathCalculator proxyInstance = (MathCalculator) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                // InvocationHandler：类似于拦截器
                h
        );

        int add = proxyInstance.add(1, 2);
        System.out.println("最终结果..."+add);


    }


    @Test
    void test01() {

        //真正有用的人，目标对象
        MathCalculator target = new MathCalculatorImpl();
        target.add(1, 2);
        System.out.println("============");


        //1、创建静态代理对象
        MathCalculator proxy = new CalculatorStaticProxy(target);
        int add = proxy.add(1, 2);
        System.out.println(add);

    }
}
