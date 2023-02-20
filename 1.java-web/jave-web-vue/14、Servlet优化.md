1.1 Servlet 优化说明
----------------

说明: 每个业务逻辑都需要一个 Servlet, 如果业务逻辑众多, 则需要更多的 Servlet 代码, 并且每个 Servlet 代码的结构几乎类似, 都包含 doGet/doPost 的方式. 那么该代码可以优化吗?  
![](https://img-blog.csdnimg.cn/8a257035c7ae45599e99ca7085339006.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_12,color_FFFFFF,t_70,g_se,x_16)  
![](https://img-blog.csdnimg.cn/85bdf8f1dad349b8919d5f15a662dc4b.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

1.2 Servlet 优化方式一
-----------------

### 1.2.1 优化策略说明

*   1.  指定统一的 Servlet
*   2.  用户传递参数时 `<a href="xxxServlet?method="业务名称"&key=value></a>`
*   3.  通过 Servlet 获取业务名称, 完成业务调用

### 1.2.2 编辑 index.html 页面

说明: 修改页面将参数提交进行修改 改为 `userServlet?method="xxxx"`的形式

```
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <base th:href="@{/}" />
    <meta charset="UTF-8">
    <title>模块练习页面</title>
</head>
<body>
    <h1>模块练习页面</h1>
    <a href="userServlet?method=findUserList"><button>点击展现用户信息</button></a>
    <hr>
    <!--编辑表格数据-->
    <table width="800px" border="1px" cellpadding="0" cellspacing="0" align="center">
        <tr>
            <th>序号</th>
            <th>ID号</th>
            <th>用户名</th>
            <th>邮箱</th>
            <th>操作</th>
        </tr>
        <tr th:if="${#lists.isEmpty(userList)}" align="center">
            <td colspan="5"><b>暂时没有用户信息请添加</b></td>
        </tr>
        <tr th:unless="${#lists.isEmpty(userList)}"  align="center" th:each="user,status : ${userList}">
            <td th:text="${status.count}"></td>
            <td th:text="${user.id}"></td>
            <td th:text="${user.username}"></td>
            <td th:text="${user.email}"></td>
            <td>
                <a th:href="@{/userServlet(method=toUpdateUser,id=${user.id})}"><button>编辑</button></a>
                <a th:href="@{/userServlet(method=deleteUserById,id=${user.id})}"><button>删除</button></a>
            </td>
        </tr>
    </table>
    <hr>
    <form action="userServlet?method=addUser" method="post">
        <table align="center">
            <tr align="center">
                <td colspan="2">
                    <h3>新增用户</h3>
                </td>
            </tr>
            <tr>
                <td>用户名:</td>
                <td><input type="text" /></td>
            </tr>
            <tr>
                <td>密码:</td>
                <td><input type="password" /></td>
            </tr>
            <tr>
                <td>邮箱:</td>
                <td><input type="text" /></td>
            </tr>
            <tr>
                <td th:cols="2">
                    <button type="submit">提交</button>
                </td>
            </tr>
        </table>
    </form>

    <form action="userServlet?method=updateUser" method="post">
        <table align="center" th:if="${updateUser != null}">
            <tr align="center">
                <td colspan="2">
                    <h3>修改用户</h3>
                </td>
            </tr>
            <tr hidden>
                <td>ID号:</td>
                <td><input type="text" ${updateUser.id}" /></td>
            </tr>
            <tr>
                <td>用户名:</td>
                <td><input type="text" ${updateUser.username}" /></td>
            </tr>
            <tr>
                <td>密码:</td>
                <td><input type="password" /></td>
            </tr>
            <tr>
                <td>邮箱:</td>
                <td><input type="text" ${updateUser.email}"/></td>
            </tr>
            <tr>
                <td th:cols="2">
                    <button type="submit">提交</button>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
```

### 1.2.3 编辑 UserServlet

```
package com.atguigu.servlet;

import com.atguigu.bean.User;
import com.atguigu.service.UserService;
import com.atguigu.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class UserServlet extends ViewBaseServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.获取指定参数的业务名称
        request.setCharacterEncoding("utf-8");
        String methodName = request.getParameter("method");
        if("findUserList".equals(methodName)){
            findUserList(request,response);
        }else if ("addUser".equals(methodName)){
            addUser(request,response);
        }else if ("toUpdateUser".equals(methodName)){
            toUpdateUser(request,response);
        }else if("updateUser".equals(methodName)){
            updateUser(request,response);
        }else if("deleteUserById".equals(methodName)){
            deleteUserById(request,response);
        }
    }

    //封装查询用户方法
    protected void findUserList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //查询所有的数据库信息
        List<User> userList = userService.findUserList();
        //System.out.println(userList);
        request.setAttribute("userList",userList);
        this.processTemplate("index",request,response);
    }

    protected void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取用户提交的数据信息
        //解决中文乱码问题
        request.setCharacterEncoding("utf-8");
        Map<String, String[]> parameterMap = request.getParameterMap();
        //将数据转化为对象
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //实现数据入库操作
        int rows = userService.addUser(user);
        if(rows>0){ //表示用户新增成功 则展现列表数据
            response.sendRedirect(request.getContextPath()+"/userServlet?method=findUserList");
        }else{      //否则跳转到新增页面
            response.sendRedirect(request.getContextPath()+"/userServlet?method=toAddUser");
        }
    }

    protected void toUpdateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //根据ID查询用户信息
        int id = Integer.valueOf(request.getParameter("id"));
        User user = userService.findUserById(id);
        request.setAttribute("updateUser",user);
        this.processTemplate("index",request,response);
    }

    protected void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        int rows = userService.updateUser(user);
        System.out.println(user);
        //不管成功还是失败都应该跳转到展现页面
        response.sendRedirect(request.getContextPath()+"/userServlet?method=findUserList");
    }

    protected void deleteUserById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //当删除成功之后,重定向到用户列表页面
        int id = Integer.valueOf(request.getParameter("id"));
        userService.deleteUserById(id);
        response.sendRedirect(request.getContextPath()+"/userServlet?method=findUserList");
    }

}
```

1.3 Servlet 优化方式 - 反射机制
-----------------------

### 1.3.1 业务说明

![](https://img-blog.csdnimg.cn/8acd9c0c09ca42cfa123db919a698876.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)  
虽然上述的代码可以在一定程度上解决耦合性的问题. 都是代码中频繁出现判断 导致代码耦合, 并且该方法只适用一个 Servlet. 如果有多个 Servlet 的都需要重复编辑.

*   问是否可以优化?
*   答: 可以通过反射机制让程序自动调用方法.

### 1.3.2 反射机制知识回顾

```
//1.获取类型
	 Class targetClass = xxxxxx.getClass();
	 //2.获取方法对象
	 Method method1 = targetClass.getDeclaredMethod("方法名称",参数类型...)
	 //获取公共方法
	 Method method2 = targetClass.getMethod("方法名称",参数类型...)
	 
	 //3.让方法执行
	 method2 .invoke("哪个对象调用",调用时传递的参数列表....)
```

### 1.3.3 反射机制 1.0 版本

```
package com.atguigu.servlet;

import com.atguigu.bean.User;
import com.atguigu.service.UserService;
import com.atguigu.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class UserServlet extends ViewBaseServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //1.获取用户传递的方法对象
        String methodName = request.getParameter("method");

        //2.获取当前类的方法对象
        Class targetClass = this.getClass();
        try {
            Method method = targetClass.getDeclaredMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
            //3.让方法执行
            method.setAccessible(true); //设置暴力反射 表示都可以访问
            method.invoke(this,request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* @Override   优化方式1
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.获取指定参数的业务名称
        request.setCharacterEncoding("utf-8");
        String methodName = request.getParameter("method");
        if("findUserList".equals(methodName)){
            findUserList(request,response);
        }else if ("addUser".equals(methodName)){
            addUser(request,response);
        }else if ("toUpdateUser".equals(methodName)){
            toUpdateUser(request,response);
        }else if("updateUser".equals(methodName)){
            updateUser(request,response);
        }else if("deleteUserById".equals(methodName)){
            deleteUserById(request,response);
        }
    }*/

    //封装查询用户方法
    protected void findUserList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //查询所有的数据库信息
        List<User> userList = userService.findUserList();
        //System.out.println(userList);
        request.setAttribute("userList",userList);
        this.processTemplate("index",request,response);
    }

    protected void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取用户提交的数据信息
        //解决中文乱码问题
        request.setCharacterEncoding("utf-8");
        Map<String, String[]> parameterMap = request.getParameterMap();
        //将数据转化为对象
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //实现数据入库操作
        int rows = userService.addUser(user);
        if(rows>0){ //表示用户新增成功 则展现列表数据
            response.sendRedirect(request.getContextPath()+"/userServlet?method=findUserList");
        }else{      //否则跳转到新增页面
            response.sendRedirect(request.getContextPath()+"/userServlet?method=toAddUser");
        }
    }

    protected void toUpdateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //根据ID查询用户信息
        int id = Integer.valueOf(request.getParameter("id"));
        User user = userService.findUserById(id);
        request.setAttribute("updateUser",user);
        this.processTemplate("index",request,response);
    }

    protected void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        int rows = userService.updateUser(user);
        System.out.println(user);
        //不管成功还是失败都应该跳转到展现页面
        response.sendRedirect(request.getContextPath()+"/userServlet?method=findUserList");
    }

    protected void deleteUserById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //当删除成功之后,重定向到用户列表页面
        int id = Integer.valueOf(request.getParameter("id"));
        userService.deleteUserById(id);
        response.sendRedirect(request.getContextPath()+"/userServlet?method=findUserList");
    }

}
```

### 1.3.4 反射机制 2.0 版本

#### 1.3.4.1 编辑 BaseServlet

```
package com.atguigu.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class BaseServlet extends ViewBaseServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //1.获取用户传递的方法对象
        String methodName = request.getParameter("method");

        //2.获取当前类的方法对象
        Class targetClass = this.getClass();
        try {
            Method method = targetClass.getDeclaredMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
            //3.让方法执行
            method.setAccessible(true); //设置暴力反射 表示都可以访问
            method.invoke(this,request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 1.3.4.2 编辑业务 Servlet

说明: 以后 UserServlet 只需要继承 BaseServlet 即可, 用户提交的参数与方法名称对应即可.  
![](https://img-blog.csdnimg.cn/edbded7fb8f4448781c856d90957dce3.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

#### 1.3.4.3 关于中文乱码问题说明

说明: 如果新增中文出现乱码问题, 则检查字符集是否设定

*   1 post 请求乱码问题  
    ![](https://img-blog.csdnimg.cn/61b27a58d2b94d18ba3fab657839641c.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

*   2 数据库连接乱码问题

```
#key=value
driverClassName=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/atweb?serverTimezone=UTC&rewriteBatchedStatements=true&characterEncoding=utf-8
username=root
password=root
initialSize=20
maxActive=50
maxWait=1000
```


--------