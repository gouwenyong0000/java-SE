package com.atguigu.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OwnServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletConfig servletConfig = getServletConfig();
        System.out.println("servletConfig.getServletName() = " + servletConfig.getServletName());
        System.out.println("servletConfig.getInitParameterNames() = " + servletConfig.getInitParameterNames());

        ServletContext servletContext = servletConfig.getServletContext();
        System.out.println("servletContext.getContextPath() = " + servletContext.getContextPath());

        super.doGet(req, resp);
    }



}
