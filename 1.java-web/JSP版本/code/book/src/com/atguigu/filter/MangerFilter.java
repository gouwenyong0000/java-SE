package com.atguigu.filter;

import com.atguigu.pojo.User;
import com.atguigu.utils.JdbcUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MangerFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //从session判断是否登陆
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {//未登陆转发到登陆页面
            request.getRequestDispatcher("/pages/user/login.jsp").forward(servletRequest, servletResponse);
        } else {//以登陆则放行
            filterChain.doFilter(servletRequest, servletResponse);
        }


        Connection connection = JdbcUtils.getConnection();
        try {
            connection.setAutoCommit(false);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void destroy() {

    }
}
