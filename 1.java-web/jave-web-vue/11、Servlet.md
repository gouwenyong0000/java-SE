11.1 什么是 Servlet
----------------

Servlet（Server Applet）是 Java Servlet 的简称，称为小服务程序或服务连接器，用 Java 编写的服务器端程序，具有独立于平台和协议的特性，主要功能在于交互式地浏览和生成数据，生成动态 Web 内容。 是 java 实现前后端交互的主要机制.

11.2 Servlet 工作的原理
------------------

原理说明:  

1. 客户端发起 http 请求  
2. Servlet 机制接收用户请求, 并且交给后端业务进行处理  
3. 业务层根据逻辑实现数据 CURD 操作.  
   ![](https://img-blog.csdnimg.cn/82843c381c0f40be9f425d830da9fdc9.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

11.3 Servlet 入门案例
-----------------

### 11.3.1 新建 web 项目

![](https://img-blog.csdnimg.cn/e5de94bfd26b440fa30036c252f4d2c3.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_11,color_FFFFFF,t_70,g_se,x_16)

### 11.3.2 导入 jar 包文件

![](https://img-blog.csdnimg.cn/0c48d9d1cebb449ba5ddc9eec0c65502.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_11,color_FFFFFF,t_70,g_se,x_16)  
![](https://img-blog.csdnimg.cn/38a8b18f908540619f5cad7a50c7bec2.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_17,color_FFFFFF,t_70,g_se,x_16)

### 11.3.3 创建 HelloServlet

```
package com.atguigu.servlet;

import javax.servlet.*;
import java.io.IOException;

public class HelloServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    /**
     * 处理客户端的请求和响应
     * @param servletRequest
     * @param servletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("这是一个Servlet入门案例方法");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
```

### 11.3.4 编辑 web.xml 配置文件

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <!--定义Servlet的配置信息-->
    <servlet>
        <!--1.1 定义Servlet名称 一般都是类名首字母小写 必须添加名称-->
        <servlet-name>helloServlet</servlet-name>
        <!--1.2 必须指定Servlet的全路径 -->
        <servlet-class>com.atguigu.servlet.HelloServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <!--1.3 定义Servlet的映射关系  名称必须相同 -->
        <servlet-name>helloServlet</servlet-name>
        <!--1.4 定义servlet的请求路径 必须以/ 开头-->
        <url-pattern>/hello</url-pattern>
    </servlet-mapping>
</web-app>
```

### 11.3.5 配置 tomcat 服务器

![](https://img-blog.csdnimg.cn/ee76eced28c743418478cf40d09a8f27.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 11.3.6 编辑 html 页面

```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>测试Servlet请求</title>
</head>
<body>
    <h1>测试Servlet操作</h1>
    <!-- 注意事项: 由于项目名称前缀,所以 href采用相对路径的写法 -->
    <a href="hello">HelloServlet测试</a>
    <!--<a href="/hello">HelloServlet测试</a>-->
</body>
</html>
```

11.4 Servlet 作用总结
-----------------

*   接收请求 【解析请求报文中的数据：请求参数】

*   处理请求 【DAO 和数据库交互】

*   完成响应 【设置响应报文】

![](https://img-blog.csdnimg.cn/eac73d352bfd4e7891b408b9fb1fc579.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

11.5 Servlet 生命周期
-----------------

### 11.5.1 Servlet 生命周期概述

*   应用程序中的对象不仅在空间上有层次结构的关系，在时间上也会因为处于程序运行过程中的不同阶段而表现出不同状态和不同行为——这就是对象的生命周期。
*   简单的叙述生命周期，就是对象在容器中从开始创建到销毁的过程。

### 11.5.2 Servlet 容器

Servlet 对象是 Servlet 容器创建的，生命周期方法都是由容器调用的。这一点和我们之前所编写的代码有很大不同。在今后的学习中我们会看到，越来越多的对象交给容器或框架来创建，越来越多的方法由容器或框架来调用，开发人员要尽可能多的将精力放在业务逻辑的实现上。

### 11.5.3 Servlet 生命周期的主要过程

![](https://img-blog.csdnimg.cn/c4e89cfa059347a7929b5a6c489007e8.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

#### 11.5.3.1 Servlet 对象的创建：构造器

*   默认情况下，**Servlet 容器第一次收到 HTTP 请求时创建对应 Servlet 对象。**
*   容器之所以能做到这一点是由于我们在注册 Servlet 时提供了全类名，容器使用反射技术创建了 Servlet 的对象。

#### 11.5.3.2 Servlet 对象初始化：init()

*   Servlet 容器 ** 创建 Servlet 对象之后，会调用 init(ServletConfig config)** 方法。
*   作用：是在 Servlet 对象创建后，执行一些初始化操作。例如，读取一些资源文件、配置文件，或建立某种连接（比如：数据库连接）
*   init() 方法只在创建对象时执行一次，以后再接到请求时，就不执行了
*   在 javax.servlet.Servlet 接口中，public void init(ServletConfig config) 方法要求容器将 ServletConfig 的实例对象传入，这也是我们获取 ServletConfig 的实例对象的根本方法。

#### 11.5.3.3 处理请求：service()

*   在 javax.servlet.Servlet 接口中，定义了 **service(ServletRequest req, ServletResponse res)** 方法处理 HTTP 请求。
*   在每次接到请求后都会执行。
*   上一节提到的 Servlet 的作用，主要在此方法中体现。
*   同时要求容器将 ServletRequest 对象和 ServletResponse 对象传入。

#### 11.5.3.4 Servlet 对象销毁：destroy()

*   服务器重启、服务器停止执行或 Web 应用卸载时会销毁 Servlet 对象，会调用 public void destroy() 方法。
*   此方法用于销毁之前执行一些诸如释放缓存、关闭连接、保存内存数据持久化等操作。

### 11.5.4 Servlet 请求过程

*   第一次请求
    *   调用构造器，创建对象
    *   执行 init() 方法
    *   执行 service() 方法
*   后面请求
    *   执行 service() 方法
*   对象销毁前
    *   执行 destroy() 方法

11.6 Servlet 使用方式
-----------------

### 11.6.1 GenericServlet

说明: 使用 GenericServlet 只需要编辑 service() 方法即可

```
public class MyServlet extends GenericServlet {

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("使用GenericServlet实现业务调用");
    }
}
```

### 11.6.2 HttpServlet

说明: 使用 HttpServlet 只需要专注 GET/POST 请求即可

```
public class MyServlet2 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        System.out.println("这里是get提交");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        System.out.println("这里是post提交");
    }
}
```

11.7 Servlet 中接口调用
------------------

### 11.7.1 ServletConfig 接口说明

说明: 每个 Servlet 都有各自的 ServletConfig 对象

*   **ServletConfig 接口封装了 Servlet 配置信息**，这一点从接口的名称上就能够看出来。

*   **每一个 Servlet 都有一个唯一对应的 ServletConfig 对象**，代表当前 Servlet 的配置信息。

*   对象由 Servlet 容器创建，并传入生命周期方法 init(ServletConfig config) 中。可以直接获取使用。

*   代表当前 Web 应用的 ServletContext 对象也封装到了 ServletConfig 对象中，使 ServletConfig 对象成为了获取 ServletContext 对象的一座桥梁。

*   ServletConfig 对象的主要功能

    *   **获取 Servlet 名称：getServletName()**

    *   **获取全局上下文 ServletContext 对象：getServletContext()**

    *   **获取 Servlet 初始化参数：getInitParameter(String) / getInitParameterNames()。**

    *   使用如下：  
        ![](https://img-blog.csdnimg.cn/ac7560b2cc8644909011e2d394d19eea.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

```
@Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("第二步: 定义初始化init方法");
        /*1.通过servletConfig对象获取配置文件信息*/
        String servletName = servletConfig.getServletName();
        /*2.通过servlet获取上下文信息*/
        ServletContext servletContext = servletConfig.getServletContext();
        /*3.获取初始化参数的值*/
        String initValue = servletConfig.getInitParameter("springmvc");
        Enumeration  enumeration = servletConfig.getInitParameterNames();
        while (enumeration.hasMoreElements()){
            System.out.println("获取多个初始化名称:"+enumeration.nextElement());
        }
        System.out.println("获取servlet名称:"+servletName);
        System.out.println("获取上下文信息:"+servletContext);
        System.out.println("获取初始化参数:"+initValue);

    }
```

### 11.7.2 ServletContext 接口说明

![](https://img-blog.csdnimg.cn/a531e70122ff48ad879c0ebbf166e3ba.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_19,color_FFFFFF,t_70,g_se,x_16)

* Web 容器在启动时，它会为**每个 Web 应用程序都创建一个唯一对应的 ServletContext 对象**，意思是 Servlet 上下文，**代表当前 Web 应用。**

* 由于**一个 Web 应用程序中的所有 Servlet 都共享同一个 ServletContext 对象**，所以 ServletContext 对象也被称为 application 对象（Web 应用程序对象）。

* **对象由 Servlet 容器在项目启动时创建**，通过 ServletConfig 对象的 getServletContext() 方法获取。在项目卸载时销毁。

* ServletContext 对象的主要功能

  ① 获取项目的上下文路径 (带 / 的项目名): **getContextPath()**

  ```
  @Override
  public void init(ServletConfig config) throws ServletException {
  	ServletContext application = config.getServletContext();
  	System.out.println("全局上下文对象："+application);
  	String path = application.getContextPath();
  	System.out.println("全局上下文路径："+path);// /06_Web_Servlet
  }
  ```

  ② 获取虚拟路径所映射的本地真实路径：**getRealPath(String path)**

  * 虚拟路径：浏览器访问 Web 应用中资源时所使用的路径。

  * 本地路径：资源在文件系统中的实际保存路径。

  * 作用：将用户上传的文件通过流写入到服务器硬盘中。

    ```
    @Override
    public void init(ServletConfig config) throws ServletException {
    	//1.获取ServletContext对象
    	ServletContext context = config.getServletContext();
    	//2.获取index.html的本地路径
    	//index.html的虚拟路径是“/index.html”,其中“/”表示当前Web应用的根目录，
    	//即WebContent目录
    	String realPath = context.getRealPath("/index.html");
    	//realPath=D:\DevWorkSpace\MyWorkSpace\.metadata\.plugins\
    	//org.eclipse.wst.server.core\tmp0\wtpwebapps\MyServlet\index.html
    	System.out.println("realPath="+realPath);
    }
    ```

  ③ 获取 WEB 应用程序的全局初始化参数（基本不用）

  * 设置 Web 应用初始化参数的方式是在 web.xml 的根标签下加入如下代码

    ```
    <web-app>
    	<!-- Web应用初始化参数 -->
    	<context-param>
    		<param-name>ParamName</param-name>
    		<param-value>ParamValue</param-value>
    	</context-param>
    </web-app>
    ```

  * 获取 Web 应用初始化参数

    ```
    @Override
    public void init(ServletConfig config) throws ServletException {
    	//1.获取ServletContext对象
    	ServletContext application = config.getServletContext();
    	//2.获取Web应用初始化参数
    	String paramValue = application.getInitParameter("ParamName");
    	System.out.println("全局初始化参数paramValue="+paramValue);
    }
    ```

  ④ 作为域对象共享数据

  *   作为最大的域对象在整个项目的不同 web 资源内共享数据。

![](https://img-blog.csdnimg.cn/669eb95fea1148309534e424e72a3ca0.png)

其中，

*   setAttribute(key,value)：以后可以在任意位置取出并使用
*   getAttribute(key)：取出设置的 value 值

11.8 处理请求响应两个重要的接口
------------------

### 11.8.1 HttpServletRequest 接口

*   该接口是 ServletRequest 接口的子接口，封装了 HTTP 请求的相关信息。

*   浏览器请求服务器时会封装请求报文交给服务器，服务器接受到请求会将请求报文解析生成 request 对象。

*   由 Servlet 容器创建其实现类对象并传入 service(HttpServletRequest req, HttpServletResponse res) 方法中。

*   以下我们所说的 HttpServletRequest 对象指的是容器提供的 HttpServletRequest 实现类对象。

### 11.8.2 获取请求参数

*   什么是请求参数？

*   请求参数就是浏览器向服务器提交的数据。

*   浏览器向服务器如何发送数据？

① 附在 url 后面 (和 get 请求一致，拼接的形式就行请求数据的绑定)，如：  
http://localhost:8080/MyServlet/MyHttpServlet?userId=20

② 通过表单提交

```
<form action="MyHttpServlet" method="post">
	你喜欢的足球队<br /><br />
	巴西<input type="checkbox"  />
	德国<input type="checkbox"  />
	荷兰<input type="checkbox"  />
	<input type="submit" value="提交" />
</form>
```

* 使用 HttpServletRequest 对象获取请求参数

  ```
  //一个name对应一个值
  String userId = request.getParameter("userId");
  ```

  ```
  //一个name对应一组值
  String[] soccerTeams = request.getParameterValues("soccerTeam");
  for(int i = 0; i < soccerTeams.length; i++){
  	System.out.println("team "+i+"="+soccerTeams[i]);
  }
  ```

### 11.8.3 获取 url 地址参数

```
String path = request.getContextPath();//重要
System.out.println("上下文路径："+path);
System.out.println("端口号："+request.getServerPort());
System.out.println("主机名："+request.getServerName());
System.out.println("协议："+request.getScheme());
```

### 11.8.4 获取请求头信息

```
String header = request.getHeader("User-Agent");
System.out.println("user-agent:"+header);
String referer = request.getHeader("Referer");
System.out.println("上个页面的地址："+referer);//登录失败，返回登录页面让用户继续登录
```

### 11.8.5 请求的转发

将请求转发给另外一个 URL 地址，

```
//获取请求转发对象
RequestDispatcher dispatcher = request.getRequestDispatcher("success.html");
dispatcher.forward(request, response);//发起转发
```

### 11.8.6 向请求域中保存数据

```
//将数据保存到request对象的属性域中
request.setAttribute("attrName", "attrValueInRequest");
//两个Servlet要想共享request对象中的数据，必须是转发的关系
request.getRequestDispatcher("/ReceiveServlet").forward(request, response);
```

```
//从request属性域中获取数据
Object attribute = request.getAttribute("attrName");
System.out.println("attrValue="+attribute);
```

11.9 编程题
--------

### 功能 1：登录

说明:

1. 系统中只有一个用户 (用户名: admin, 密码: 123456)

2. 相关资源:

   login.html : 登录表单页面

   LoginServlet: 处理登录请求的 Servlet

   login_success.html : 登录成功页面 (提示: 登录成功)

   login_error.html : 登录失败页面 (提示: 用户名或密码不正确)

### 功能 2：注册

说明:

1. 系统中只有一个用户 (用户名: admin, 密码: 123456)

2. 相关资源:

   register.html : 注册表单页面

   RegistServlet: 处理注册请求的 Servlet

   regist_success.html : 注册成功页面 (提示: 注册成功)

   regist_error.html : 注册失败页面 (提示: 用户名已存在)

### 功能 3：连接数据库操作

完成代码: 参见作业

11.10 HttpServletResponse 接口
----------------------------

### 11.10.1 HttpServletResponse 介绍

*   该接口是 ServletResponse 接口的子接口，封装了服务器针对于 HTTP 响应的相关信息。(暂时只有服务器的配置信息，没有具体的和响应体相关的内容)

*   由 Servlet 容器创建其实现类对象，并传入 service(HttpServletRequest req, HttpServletResponse res) 方法中。

*   后面我们所说的 HttpServletResponse 对象指的是容器提供的 HttpServletResponse 实现类对象。

HttpServletResponse 对象的主要功能有：

### 11.10.2 使用 PrintWriter 对象向浏览器输出数据

```
//通过PrintWriter对象向浏览器端发送响应信息
PrintWriter writer = res.getWriter();
writer.write("Servlet response");
writer.close();
```

*   写出的数据可以是页面、页面片段、字符串等

*   当写出的数据包含中文时，浏览器接收到的响应数据就可能有乱码。为了避免乱码，可以使用 Response 对象在向浏览器输出数据前设置响应头。

### 11.10.3 设置响应头

*   响应头就是浏览器解析页面的配置。比如：告诉浏览器使用哪种编码和文件格式解析响应体内容

```
response.setHeader("Content-Type", "text/html;charset=UTF-8");
```

*   设置好以后，会在浏览器的响应报文中看到设置的响应头中的信息。

### 11.10.4 重定向请求

*   实现请求重定向，
*   举例：用户从 login.html 页面提交登录请求数据给 LoginServlet 处理。如果账号密码正确，需要让用户跳转到成功页面，通过 servlet 向响应体中写入成功页面过于复杂，通过重定向将成功页面的地址交给浏览器并设置响应状态码为 302，浏览器会自动进行跳转。

```
//注意路径问题，加上/会失败，会以主机地址为起始，重定向一般需要加上项目名
response.sendRedirect(“success.html”);
```

11.11 请求的转发
-----------

### 11.11.1 转发和重定向说明

请求的转发与重定向是 web 应用页面跳转的主要手段，在 Web 应用中使用非常广泛。所以我们一定要搞清楚他们的区别。  
![](https://img-blog.csdnimg.cn/474e9cfcd63e4e1c8e4d6659659fba81.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_17,color_FFFFFF,t_70,g_se,x_16)

### 11.11.2 请求的转发

![](https://img-blog.csdnimg.cn/bb8c59b7e44a4b8ab07119afe70673a4.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

*   第一个 Servlet 接收到了浏览器端的请求，进行了一定的处理，然后没有立即对请求进行响应，而是将请求 “交给下一个 Servlet” 继续处理，下一个 Servlet 处理完成之后对浏览器进行了响应。** 在服务器内部将请求 “交给” 其它组件继续处理就是请求的转发。** 对浏览器来说，一共只发了一次请求，服务器内部进行的 “转发” 浏览器感觉不到，同时浏览器地址栏中的地址不会变成 “下一个 Servlet” 的虚拟路径。
*   HttpServletRequest 代表 HTTP 请求，对象由 Servlet 容器创建。转发的情况下，两个 Servlet 可以共享同一个 Request 对象中保存的数据。
*   当需要将后台获取的数据传送到 JSP 上显示的时候，就可以先将数据存放到 Request 对象中，再转发到 JSP 从属性域中获取。此时由于是 “转发”，所以它们二者共享 Request 对象中的数据。
*   转发的情况下，可以访问 WEB-INF 下的资源。
*   **转发以 “/” 开始表示项目根路径，重定向以”/”开始表示主机地址。**
*   功能：
    *   获取请求参数
    *   获取请求路径即 URL 地址相关信息
    *   在请求域中保存数据
    *   转发请求
*   代码举例

```
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取用户的参数
        int id = Integer.valueOf(request.getParameter("id"));
        String name = request.getParameter("name");
        System.out.println(id+"|"+name);
        //2.利用request域保存数据
        request.setAttribute("key","UUID密钥");

        //3.将请求转发给 twoServlet
        request.getRequestDispatcher("/twoServlet").forward(request,response);
    }
```

11.12 重定向
---------

![](https://img-blog.csdnimg.cn/9c308eaf547c4bb1b1b266a883b8be9c.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

* 第一个 Servlet 接收到了浏览器端的请求，进行了一定的处理，然后给浏览器一个特殊的响应消息，这个特殊的响应消息会通知浏览器去访问另外一个资源，这个动作是服务器和浏览器自动完成的。**整个过程中浏览器端会发出两次请求**，且在**浏览器地址栏里面能够看到地址的改变**，改变为下一个资源的地址。

* 重定向的情况下，原 Servlet 和目标资源之间就不能共享请求域数据了。

* HttpServletResponse 代表 HTTP 响应，对象由 Servlet 容器创建。

* 功能：

  *   向浏览器输出数据
  *   重定向请求

* 重定向的响应报文的头

  ```
  HTTP/1.1 302 Found
  Location: success.html
  ```

* 应用：

  * 用户从 login.html 页面提交登录请求数据给 LoginServlet 处理。

    如果账号密码正确，需要让用户跳转到成功页面，通过 servlet 向响应体中写入成功页面过于复杂，通过重定向将成功页面的地址交给浏览器并设置响应状态码为 302，浏览器会自动进行跳转

* 代码举例：

```
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.从转发来的请求获取数据
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        System.out.println("跳转到第二个Servlet|"+id+"|"+name);

        //2.从域中获取数据
        String msg = (String) request.getAttribute("key");
        System.out.println("打印域中的数据:"+msg);
        //将域中的数据清除
        request.removeAttribute("key");

        //3.重定向到成功页面
        response.sendRedirect("success.html");
    }
```

11.13 对比请求的转发与重定向
-----------------

<table><thead><tr><th></th><th>转发</th><th>重定向</th></tr></thead><tbody><tr><td>浏览器感知</td><td>在服务器内部完成，浏览器感知不到</td><td>服务器以 302 状态码通知浏览器访问新地址，浏览器有感知</td></tr><tr><td>浏览器地址栏</td><td>不改变</td><td>改变</td></tr><tr><td>整个过程发送请求次数</td><td>一次</td><td>两次</td></tr><tr><td>能否共享 request 对象数据</td><td>能</td><td>否</td></tr><tr><td>WEB-INF 下的资源</td><td>能访问</td><td>不能访问</td></tr><tr><td>目标资源</td><td>必须是当前 web 应用中的资源</td><td>不局限于当前 web 应用</td></tr></tbody></table>

> 说明 1：默认情况下，浏览器是不能访问服务器 web-inf 下的资源的，而服务器是可以访问的。
>
> 说明 2：浏览器默认的绝对路径：http://localhost:8080/
>
>  服务器项目的代码中的绝对路径：http://localhost:8080 / 项目名 /

11.14 请求与响应中的字符编码设置
-------------------

11.14.1 字符编码问题
--------------

*   我们 web 程序在接收请求并处理过程中，如果不注意编码格式及解码格式，很容易导致中文乱码，引起这个问题的原因到底在哪里？如何解决？我们这个小节将会讨论此问题。
*   说到这个问题我们先来说一说字符集。
    *   什么是字符集，就是各种字符的集合，包括汉字，英文，标点符号等等。各国都有不同的文字、符号。这些文字符号的集合就叫字符集。
    *   现有的字符集 ASCII、GB2312、BIG5、GB18030、Unicode、UTF-8、ISO-8859-1 等
*   这些字符集，集合了很多的字符，然而，字符要以二进制的形式存储在计算机中，我们就需要对其进行编码，将编码后的二进制存入。取出时我们就要对其解码，将二进制解码成我们之前的字符。这个时候我们就需要制定一套编码解码标准。否则就会导致出现混乱，也就是我们的乱码。- 我们 web 程序在接收请求并处理过程中，如果不注意编码格式及解码格式，很容易导致中文乱码，引起这个问题的原因到底在哪里？如何解决？我们这个小节将会讨论此问题。
*   说到这个问题我们先来说一说字符集。
    *   什么是字符集，就是各种字符的集合，包括汉字，英文，标点符号等等。各国都有不同的文字、符号。这些文字符号的集合就叫字符集。
    *   现有的字符集 ASCII、GB2312、BIG5、GB18030、Unicode、UTF-8、ISO-8859-1 等
*   这些字符集，集合了很多的字符，然而，字符要以二进制的形式存储在计算机中，我们就需要对其进行编码，将编码后的二进制存入。取出时我们就要对其解码，将二进制解码成我们之前的字符。这个时候我们就需要制定一套编码解码标准。否则就会导致出现混乱，也就是我们的乱码。

11.14.2 编码与解码
-------------

*   编码：将字符转换为二进制数

<table><thead><tr><th>汉字</th><th>编码方式</th><th>编码</th><th>二进制</th></tr></thead><tbody><tr><td>‘中’</td><td><strong>GB2312</strong></td><td><strong>D6D0</strong></td><td><strong>1101 0110-1101 0000</strong></td></tr><tr><td>‘中’</td><td><strong>UTF-16</strong></td><td><strong>4E2D</strong></td><td><strong>0100 1110-0010 1101</strong></td></tr><tr><td>‘中’</td><td><strong>UTF-8</strong></td><td><strong>E4B8AD</strong></td><td><strong>1110</strong> <strong>0100-</strong> <strong>1011</strong> <strong>1000-1010 1101</strong></td></tr></tbody></table>

*   解码：将二进制数转换为字符

1110 0100-1011 1000-1010 1101 → E4B8AD → ’中’

*   乱码：一段文本，使用 A 字符集编码，使用 B 字符集解码，就会产生乱码。所以解决乱码问题的根本方法就是统一编码和解码的字符集。

11.15 解决请求乱码问题
--------------

解决乱码的方法：就是统一字符编码。

### 11.15.1 GET 请求（Tomcat7 及以下的需要处理）

GET 请求参数是在地址后面的。我们需要修改 tomcat 的配置文件。需要在 server.xml 文件修改 Connector 标签，添加 URIEncoding="utf-8" 属性。  
如果使用 tomcat8 及以上的版本, z

![](https://img-blog.csdnimg.cn/f5c6f7619cd94491989b2d5a7f103a78.png)  
一旦配置好以后，可以解决当前工作空间中所有的 GET 请求的乱码问题。

### 11.15.2 POST 请求

* post 请求提交了中文的请求体，服务器解析出现问题。

* 解决方法：在获取参数值之前，设置请求的解码格式，使其和页面保持一致。

  ```
  request.setCharacterEncoding("utf-8");
  ```

* POST 请求乱码问题的解决，只适用于当前的操作所在的类中。不能类似于 GET 请求一样统一解决。因为请求体有可能会上传文件。不一定都是中文字符。

### 11.15.3 解决响应乱码问题

* 向浏览器发送响应的时候，要告诉浏览器，我使用的字符集是哪个，浏览器就会按照这种方式来解码。如何告诉浏览器响应内容的字符编码方案。很简单。

* 解决方法一：

  ```
  response.setHeader("Content-Type", "text/html;charset=utf-8");
  ```

* 解决方法二

  ```
  response.setContentType("text/html;charset=utf-8");
  ```

  > 说明：有的人可能会想到使用 response.setCharacterEncoding(“utf-8”)，设置 reponse 对象将 UTF-8 字符串写入到响应报文的编码为 UTF-8。只这样做是不行的，还必须手动在浏览器中设置浏览器的解析用到的字符集。

11.16 Web 应用路径设置
----------------

### 11.16.1 url 的概念

url 是`uniform Resource Locater`的简写，中文翻译为`统一资源定位符`，它是某个互联网资源的唯一访问地址，客户端可以通过 url 访问到具体的互联网资源  
完整的 url 构成如下图：  
![](https://img-blog.csdnimg.cn/1365a7f61e0f4c3e88bd27112b861d6f.png)

uri 是`统一资源标志符(Uniform Resource Identifier， URI)` 表示的是 web 上每一种可用的资源 只包含上图中的 path/TestServlet 不包含服务器地址

**相对路径和绝对路径**

**相对路径：虚拟路径如果不以 “/” 开始，就是相对路径**，浏览器会以当前资源所在的虚拟路径为基准对相对路径进行解析，从而生成最终的访问路径。此时如果通过转发进入其他目录，再使用相对路径访问资源就会出错。所以为了防止路径出错，我们经常将相对路径转化为绝对路径的形式进行请求。

**绝对路径：虚拟路径以 “/” 开始，就是绝对路径。**  
**① 在服务器端**：虚拟路径最开始的 “/” 表示当前 Web 应用的根目录。只要是服务端解析的绝对路径，都是以 web 根目录为起始的。由服务器解析的路径包括：<1> web.xml 的配置路径、<2>request 转发的路径。  
**② 在浏览器端**：虚拟路径最开始的 “/” 表示当前主机地址。  
例如：链接地址 “/Path/dir/b.html” 经过浏览器解析后为：  
相当于 http://localhost:8989/Path/dir/b.html  
由浏览器解析的路径包括：  
<1> 重定向操作：response.sendRedirect(“/xxx”)  
<2> 所有 HTML 标签：<a href="/xxx"> 、<form action="/xxx"> 、link、img、script 等  
这些最后的访问路径都是 http://localhost:8989/xxx

所以我们可以看出，如果是浏览器解析的路径，我们必须加上项目名称才可以正确的指向资源。[http://localhost:8989](http://localhost:8989/Path/xxx)[/Path](http://localhost:8989/Path/xxx)[/xxx](http://localhost:8989/Path/xxx)

<1> 重定向操作：

```
response.sendRedirect(request.getContextPath()+"/xxx");
```

<2> 所有 HTML 标签：<a href=“/ 项目名 / xxx”>； <form action=“/ 项目名 / xxx">

*   在浏览器端，除了使用绝对路径之外，我们还可以使用 base 标签 + 相对路径的方式来确定资源的访问有效。
*   base 标签影响当前页面中的所有相对路径，不会影响绝对路径。相当于给相对路径设置了一个基准地址。
*   习惯上在 html 的标签内，声明：

```
<!-- 给页面中的相对路径设置基准地址 -->
<base href="http://localhost:8080/Test_Path/"/>
```

接着 html 中的路径就可以使用相对路径的方式来访问。比如：

```
<h4> base+相对路径</h4>
<!-- <base href="http://localhost:8080/Test_Path/"/> -->
<a href="1.html">1.html</a><br/>
<a href="a/3.html">a/3.html</a><br/>
<!-- servlet映射到了项目根目录下，可以直接访问 -->
<a href="PathServlet">PathServlet</a><br/>
```


--------------