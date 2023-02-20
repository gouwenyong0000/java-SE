11.1 Servlet 组件注解
-----------------

```
package com.atguigu.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//name:servlet名称可以省略 默认为类名
//value 表示对应的url路径
@WebServlet(name = "AnnoServlet",value = "/anno")
public class AnnoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("我是注解方式的Servlet");
    }
}
```

11.2 Filter 组件注解
----------------

```
package com.atguigu.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
@WebFilter(filterName = "AnnoFilter",value = "/*")
public class AnnoFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("过滤器执行前");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("过滤器执行后");
    }

    @Override
    public void destroy() {
        System.out.println("容器对象销毁");
    }
}
```

11.3 Listener 组件注解
------------------

```
package com.atguigu.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListerner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("容器初始化操作");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("容器销毁操作");
    }
}
```

11.4 全局字符集处理
------------

说明: 利用 Filter 过滤器 实现全局字符集处理

```
package com.atguigu.filter;/**
 * @className EncodingFilter
 * @description TODO
 * @author 刘昱江
 * @date 2022/4/1 9:23
 */

import javafx.application.Application;
import javafx.stage.Stage;

import javax.naming.Name;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
@WebFilter(filterName = "EncodingFilter",value = "/*")
public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    //解决请求乱码问题
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //解决post请求乱码问题
        servletRequest.setCharacterEncoding("utf-8");
        servletResponse.setCharacterEncoding("utf-8");
        servletResponse.setContentType("text/html;charset=utf-8");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
```


---------