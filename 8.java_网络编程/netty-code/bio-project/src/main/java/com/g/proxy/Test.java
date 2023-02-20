package com.g.proxy;

import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.SortedMap;

interface II {
    String hello(String msg);
}

public class Test {

    public static void main(String[] args) {
        II ii = (II) getBean();
        System.out.println("ii.hello(\"你好\") = " + ii.hello("你好"));
    }

    public static Object getBean() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader()
                , new Class[]{II.class},
                (proxy, method, args) -> {
                    for (int i = 0; i < args.length; i++) {
                        System.out.println("args[i] = " + args[i]);

                    }
                    System.out.println("args = " + args);
                    System.out.println("method = " + method);
//                    System.out.println("proxy = " + proxy);

                    return LocalDateTime.now().toString();
                }
        );
    }
}
