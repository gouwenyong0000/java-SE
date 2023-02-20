13.1 Thymeleaf 的概念
------------------

Thymeleaf 是一款用于渲染 XML/XHTML/HTML5 内容的模板引擎。类似 JSP，Velocity，FreeMaker 等， 它也可以轻易的与 Spring MVC 等 Web 框架进行集成作为 Web 应用的模板引擎。它的主要作用是在静态页面上渲染显示动态数据

13.2 Thymeleaf 的优势
------------------

*   SpringBoot 官方推荐使用的视图模板技术，和 SpringBoot 完美整合。

*   不经过服务器运算仍然可以直接查看原始值，对前端工程师更友好。

```
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

    <p th:text="${username}">Original Value</p>

</body>
</html>
```

13.3 物理视图和逻辑视图
--------------

### 13.3.1 物理视图

在 Servlet 中，将请求转发到一个 HTML 页面文件时，使用的完整的转发路径就是物理视图  
![](https://img-blog.csdnimg.cn/5e66557bc2cb497daea1e8ca85de2f1e.png)  
如果我们把所有的 HTML 页面都放在某个统一的目录下，那么转发地址就会呈现出明显的规律：

> /pages/user/login.html  
> /pages/user/login_success.html  
> /pages/user/regist.html  
> /pages/user/regist_success.html
>
> ……

路径的开头都是：/pages/user/

路径的结尾都是：.html

所以，路径开头的部分我们称之为**视图前缀** 路径结尾的部分我们称之为**视图后缀**

### 13.3.2 逻辑视图

物理视图 = 视图前缀 + 逻辑视图 + 视图后缀

上面的例子中：

<table><thead><tr><th>视图前缀</th><th>逻辑视图</th><th>视图后缀</th><th>物理视图</th></tr></thead><tbody><tr><td>/pages/user/</td><td>login</td><td>.html</td><td>/pages/user/login.html</td></tr><tr><td>/pages/user/</td><td>login_success</td><td>.html</td><td>/pages/user/login_success.html</td></tr></tbody></table>

13.4 Thymeleaf 入门案例
-------------------

### 13.4.1 导入 jar 包

![](https://img-blog.csdnimg.cn/3bbdf56f8de141c580c1d471811fcfed.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 13.4.2 配置上下文参数

![](https://img-blog.csdnimg.cn/a817197a66f34365ae6681efefa72bc2.png)

物理视图 = 视图前缀 + 逻辑视图 + 视图后缀

```
<!-- 在上下文参数中配置视图前缀和视图后缀 -->
<context-param>
    <param-name>view-prefix</param-name>
    <param-value>/WEB-INF/view/</param-value>
</context-param>
<context-param>
    <param-name>view-suffix</param-name>
    <param-value>.html</param-value>
</context-param>
```

说明：param-value 中设置的前缀、后缀的值不是必须叫这个名字，可以根据实际情况和需求进行修改。

> 为什么要放在 WEB-INF 目录下？
>
> 原因：WEB-INF 目录不允许浏览器直接访问，所以我们的视图模板文件放在这个目录下，是一种保护。以免外界可以随意访问视图模板文件。
>
> 访问 WEB-INF 目录下的页面，都必须通过 Servlet 转发过来，简单说就是：不经过 Servlet 访问不了。
>
> 这样就方便我们在 Servlet 中检查当前用户是否有权限访问。
>
> 那放在 WEB-INF 目录下之后，重定向进不去怎么办？
>
> 重定向到 Servlet，再通过 Servlet 转发到 WEB-INF 下。

### 13.4.3 创建 Servlet 基类

这个类大家直接复制粘贴即可，将来使用框架后，这些代码都将被取代。

```
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewBaseServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {

        // 1.获取ServletContext对象
        ServletContext servletContext = this.getServletContext();

        // 2.创建Thymeleaf解析器对象
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);

        // 3.给解析器对象设置参数
        // ①HTML是默认模式，明确设置是为了代码更容易理解
        templateResolver.setTemplateMode(TemplateMode.HTML);

        // ②设置前缀
        String viewPrefix = servletContext.getInitParameter("view-prefix");

        templateResolver.setPrefix(viewPrefix);

        // ③设置后缀
        String viewSuffix = servletContext.getInitParameter("view-suffix");

        templateResolver.setSuffix(viewSuffix);

        // ④设置缓存过期时间（毫秒）
        templateResolver.setCacheTTLMs(60000L);

        // ⑤设置是否缓存
        templateResolver.setCacheable(true);

        // ⑥设置服务器端编码方式
        templateResolver.setCharacterEncoding("utf-8");

        // 4.创建模板引擎对象
        templateEngine = new TemplateEngine();

        // 5.给模板引擎对象设置模板解析器
        templateEngine.setTemplateResolver(templateResolver);

    }

    protected void processTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 1.设置响应体内容类型和字符集
        resp.setContentType("text/html;charset=UTF-8");

        // 2.创建WebContext对象
        WebContext webContext = new WebContext(req, resp, getServletContext());

        // 3.处理模板数据
        templateEngine.process(templateName, webContext, resp.getWriter());
    }
}
```

### 13.4.4 创建 index.html 页面

```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>系统首页</title>
    <base href="http://localhost:8090/07_thymeleaf/">
</head>
<body>
    <a href="helloServlet">跳转到hello.html页面</a>
</body>
</html>
```

### 13.4.5 创建 HelloServlet

1.  编辑 Servlet

```
package com.auguigu.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends ViewBaseServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("已经访问了Serlvet了");
        request.setAttribute("msg","我是服务器数据!!!!!!!!");
        this.processTemplate("hello",request,response);
    }
}
```

2.  编辑 web.xml 配置

```
<servlet>
        <servlet-name>HelloServlet</servlet-name>
        <servlet-class>com.auguigu.servlet.HelloServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>HelloServlet</servlet-name>
        <url-pattern>/helloServlet</url-pattern>
    </servlet-mapping>
```

### 13.4.6 编辑 Thymeleaf 页面

```
<!DOCTYPE html>

<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>这是一个欢迎页面</title>
</head>
<body>
    <h1 th:text="${msg}"></h1>
</body>
</html>
```

13.5 Thymeleaf 基本语法
-------------------

### 13.5.1 th 名称空间

![](https://img-blog.csdnimg.cn/826be8cba4ad4043b726c203683002d5.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 13.5.2 th:text 作用

代码示例：

```
<p th:text="标签体新值">标签体原始值</p>
```

*   不经过服务器解析，直接用浏览器打开 HTML 文件，看到的是『标签体原始值』
*   经过服务器解析，Thymeleaf 引擎根据 th:text 属性指定的『标签体新值』去替换『标签体原始值』

### 13.5.3 修改指定属性值

代码示例：

```
<input type="text"  />
```

语法：任何 HTML 标签原有的属性，前面加上『th:』就都可以通过 Thymeleaf 来设定新值。

### 13.5.4 解析 URL 地址

代码示例：

```
<!--
使用Thymeleaf解析url地址
-->
<a th:href="@{/index.html}">访问index.html</a>
```

经过解析后得到：

> /webday08/index.html

所以 @{} 的作用是在字符串前附加『上下文路径』

> 这个语法的好处是：实际开发过程中，项目在不同环境部署时，Web 应用的名字有可能发生变化。所以上下文路径不能写死。而通过 @{} 动态获取上下文路径后，不管怎么变都不怕啦！

### 13.5.5 关于 index.html 访问说明

1.  为什么在 index.html 使用 th 标签失效  
    ![](https://img-blog.csdnimg.cn/6b2564a0cf7047ae9ef11a9331cdc656.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)
2.  原理说明  
    ![](https://img-blog.csdnimg.cn/41274b8f5e1e427592272e44f3371cb1.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)  
    如果我们直接访问 index.html 本身，那么 index.html 是不需要通过 Servlet，当然也不经过模板引擎，所以 index.html 上的 Thymeleaf 的任何表达式都不会被解析。

解决办法：通过 Servlet 访问 index.html，这样就可以让模板引擎渲染页面了：  
![](https://img-blog.csdnimg.cn/7c7dc499f5ae4a64a9e3fccfd8267eb9.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

> 进一步的好处：  
> 通过上面的例子我们看到，所有和业务功能相关的请求都能够确保它们通过 Servlet 来处理，这样就方便我们统一对这些请求进行特定规则的限定。

3.  完成 IndexServlet

```
public class IndexServlet extends ViewBaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //跳转到index.html页面中
        System.out.println("这里是idnexServlet的位置");
        request.setAttribute("msg","欢迎页面信息");
        this.processTemplate("index",request,response);
    }
}
```

4.  配置 servlet 路径

```
<servlet>
        <servlet-name>IndexServlet</servlet-name>
        <servlet-class>com.auguigu.servlet.IndexServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>IndexServlet</servlet-name>
        <url-pattern>/index.html</url-pattern>
    </servlet-mapping>
```

13.6 关于域的说明
-----------

### 13.6.1 请求域 (Request 域)

在请求转发的场景下，我们可以借助 HttpServletRequest 对象内部给我们提供的存储空间，帮助我们携带数据，把数据发送给转发的目标资源。

请求域：HttpServletRequest 对象内部给我们提供的存储空间  
总结: 在同一个请求中实现数据共享  
![](https://img-blog.csdnimg.cn/f691dc0159444ba289ee5dd1ca3c9ea7.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 13.6.2 会话域 (Session 域)

说明: 在同一个会话下, 可以发起多个交互, 在同一个会话下实现数据共享.  
![](https://img-blog.csdnimg.cn/6c6fb53dc35d4dae8f0a1c16155fa823.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 13.6.3 应用域 (Application 域)

说明: 在同一个 tocmat 服务器中实现数据共享  
![](https://img-blog.csdnimg.cn/ace823233a7945b5bdaa6911ade09f50.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 13.6.4 Thymeleaf 中操作域对象

我们通常的做法是，在 Servlet 中将数据存储到域对象中，而在使用了 Thymeleaf 的前端页面中取出域对象中的数据并展示

1.  编辑 Servlet 将数据保存到与对象中

```
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.向request域中添加数据
        request.setAttribute("requestMsg","request域中的数据");
        //2.向Session域中添加数据
        request.getSession().setAttribute("sessionMsg","Session域中的数据");
        //3.向Application域中添加数据
        request.getServletContext().setAttribute("contextMsg","ConText域中的数据");

        this.processTemplate("value",request,response);
    }
```

2.  从域中获取数据

```
<!--1.从request域中获取数据-->
    <h1 th:text="${requestMsg}"></h1>

    <!--2.从Session域中获取数据-->
    <h1 th:text="${session.sessionMsg}"></h1>

    <!--3.从Application域中获取数据-->
    <h1 th:text="${application.contextMsg}"></h1>
```

![](https://img-blog.csdnimg.cn/4b35235a2cf54f2ca117c3fe56590c80.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

13.7 获取请求参数
-----------

### 13.7.1 获取请求参数的语法

```
${param.参数名}
```

### 13.7.2 页面效果展现

1.  编辑页面

```
<!--thymeleaf参数获取测试 -->
    <a href="paramServlet?id=1&>参数取值操作</a>
```

2.  页面参数获取

```
<h1>获取参数信息</h1>
    <p th:text="${param.id}"></p>
    <p th:text="${param.name}"></p>
```

### 13.7.3 同名参数提交说明

1.  编辑页面

```
<!--thymeleaf参数获取测试 -->
 <a href="paramServlet?id=1&>参数取值操作</a>
```

2.  页面取值

```
<!--获取同名参数 获取数组,按照数组类型输出 -->
    <p th:text="${param.hobby}"></p>
    <p th:text="${param.hobby[0]}"></p>
    <p th:text="${param.hobby[1]}"></p>
    <p th:text="${param.hobby[2]}"></p>
```

![](https://img-blog.csdnimg.cn/4aeb3185764246afa00ce84d6feedb72.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_14,color_FFFFFF,t_70,g_se,x_16)

13.8 内置对象 (了解即可)
----------------

### 13.8.1 内置对象的概念

所谓内置对象其实就是在 Thymeleaf 的表达式中可以直接使用的对象

### 13.8.2 基本内置对象

![](https://img-blog.csdnimg.cn/0d7ea017b0274593b985181703bef4bb.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)  
用法举例：

```
<h3>表达式的基本内置对象</h3>
<p th:text="${#request.getContextPath()}">调用#request对象的getContextPath()方法</p>
<p th:text="${#request.getAttribute('helloRequestAttr')}">调用#request对象的getAttribute()方法，读取属性域</p>
```

基本思路：

*   如果不清楚这个对象有哪些方法可以使用，那么就通过 getClass().getName() 获取全类名，再回到 Java 环境查看这个对象有哪些方法
*   内置对象的方法可以直接调用
*   调用方法时需要传参的也可以直接传入参数

### 13.8.3 公共内置对象

![](https://img-blog.csdnimg.cn/6bc461faba4e4420bae0213e5ccc3a94.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)  
Servlet 中将 List 集合数据存入请求域：

```
request.setAttribute("aNotEmptyList", Arrays.asList("aaa","bbb","ccc"));
request.setAttribute("anEmptyList", new ArrayList<>());
```

页面代码：

```
<p>#list对象isEmpty方法判断集合整体是否为空aNotEmptyList：<span th:text="${#lists.isEmpty(aNotEmptyList)}">测试#lists</span></p>
<p>#list对象isEmpty方法判断集合整体是否为空anEmptyList：<span th:text="${#lists.isEmpty(anEmptyList)}">测试#lists</span></p>
```

13.9 OGNL 表达式
-------------

### 13.9.1 OGNL 的概念

OGNL：Object-Graph Navigation Language 对象 - 图 导航语言

### 13.9.2 对象图的概念

从根对象触发，通过特定的语法，逐层访问对象的各种属性。  
![](https://img-blog.csdnimg.cn/517f68918bc94d90bbe43a07e00b5a29.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 13.9.3 OGNL 语法

#### ① 起点

在 Thymeleaf 环境下，${} 中的表达式可以从下列元素开始：

*   访问属性域的起点
    *   请求域属性名
    *   session
    *   application
*   param
*   内置对象
    *   request
    *   session
    *   lists
    *   strings

#### ② 属性访问语法

*   访问对象属性：使用 getXxx()、setXxx() 方法定义的属性
    *   对象. 属性名
*   访问 List 集合或数组
    *   集合或数组 [下标]
*   访问 Map 集合
    *   Map 集合. key
    *   Map 集合 [‘key’]

13.10 分支与迭代
-----------

### 13.10.1 if 和 unless

让标记了 th:if、th:unless 的标签根据条件决定是否显示。  
语法说明:  
当 th:if 判断为真时, 显示标签体的内容. 否则不显示, 与 unless 的恰好相反.  
if 配合 not 关键词和 unless 配合原表达式效果是一样的，看自己的喜好。

1.  编辑 Servlet

```
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("num",1);
        this.processTemplate("ifOrFor",request,response);
    }
```

2.  编辑页面 html

```
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>分支语法</title>
</head>
<body>
    <h1>分支语法结构</h1>
    <!--当判断结果为真时 显示标签体内容-->
    <p th:text="${num}">num为100</p>
    <p th:if="${num > 5}">num为100 >5</p>
    <p th:if="${not(num > 5)}">num > 5 not</p>
    <p th:unless="${num > 5}"> num > 5 unless</p>


</body>
</html>
```

### 13.10.2 switch

语法:  

1. th:switch  
2. th:case

```
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("num",1);
        request.setAttribute("level",3);
        this.processTemplate("ifOrFor",request,response);
    }
```

```
<h3>switch分支语法</h3>
    <div th:switch="${level}">
        <p th:case="1">一级</p>
        <p th:case="2">二级</p>
        <p th:case="3">三级</p>
    </div>
```

### 13.10.3 迭代用法

语法:

```
<tr th:each="teacher,status : ${teacherList}">
            <td th:text="${status.count}">这里显示编号</td>
            <td th:text="${teacher.teacherName}">这里显示老师的名字</td>
        </tr>
```

在迭代过程中，可以参考下面的说明使用迭代状态：  
![](https://img-blog.csdnimg.cn/86302caa24444227a3e4da5655a6c183.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

```
<!--遍历显示请求域中的teacherList-->
<table border="1" cellspacing="0" width="500">
    <tr>
        <th>编号</th>
        <th>姓名</th>
    </tr>
    <tbody th:if="${#lists.isEmpty(teacherList)}">
        <tr>
            <td colspan="2">教师的集合是空的!!!</td>
        </tr>
    </tbody>

    <!--
集合不为空，遍历展示数据
-->
    <tbody th:unless="${#lists.isEmpty(teacherList)}">
        <!--
使用th:each遍历
用法:
1. th:each写在什么标签上？ 每次遍历出来一条数据就要添加一个什么标签，那么th:each就写在这个标签上
2. th:each的语法    th:each="遍历出来的数据,数据的状态 : 要遍历的数据"
3. status表示遍历的状态，它包含如下属性:
 index 遍历出来的每一个元素的下标
 count 遍历出来的每一个元素的计数
-->
        <tr th:each="teacher,status : ${teacherList}">
            <td th:text="${status.count}">这里显示编号</td>
            <td th:text="${teacher.teacherName}">这里显示老师的名字</td>
        </tr>
    </tbody>
</table>
```

13.11 Thymeleaf 包含其他模板文件
------------------------

### 13.11.1 应用场景

说明: 由于头部标签每个页面都会使用, 如果重复编辑则耦合性高, 则需要将页面信息进行抽取, 之后统一进行引用.  
![](https://img-blog.csdnimg.cn/4ec99f302c8a4366bee9e3725e56846a.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 13.11.2 编辑头部页面

```
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>头部</title>
</head>
<body>
    <div th:fragment="jd_head">
      我是京东页面头部
    </div>
</body>
</html>
```

### 13.11.2 编辑页面

<table><thead><tr><th>语法</th><th>效果</th><th>特点</th></tr></thead><tbody><tr><td>th:insert</td><td>把目标的代码片段整个插入到当前标签内部</td><td>它会保留页面自身的标签</td></tr><tr><td>th:replace</td><td>用目标的代码替换当前标签</td><td>它不会保留页面自身的标签</td></tr><tr><td>th:include</td><td>把目标的代码片段去除最外层标签，然后再插入到当前标签内部</td><td>它会去掉片段外层标记，同时保留页面自身标记</td></tr></tbody></table>

页面代码举例：

```
<!-- 代码片段所在页面的逻辑视图 :: 代码片段的名称 -->
<div id="badBoy" th:insert="segment :: header">
    div标签的原始内容
</div>

<div id="worseBoy" th:replace="segment :: header">
    div标签的原始内容
</div>

<div id="worstBoy" th:include="segment :: header">
    div标签的原始内容
</div>
```

13.12 综合项目练习
------------

### 13.12.1 完成列表展现

1.  初始化页面  
    ![](https://img-blog.csdnimg.cn/3b1c658a0c524a9fbfc07276d15843c9.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)  
    2. 列表展现页面  
       ![](https://img-blog.csdnimg.cn/3c76d3eb7516455ca3e21d546a9130ac.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 13.12.2 用户新增页面

说明: 点击提交按钮时, 完成数据提交, 并且重定向到列表页面  
![](https://img-blog.csdnimg.cn/c1e1eb2b5dcb48e3bea9a9c0f64114a8.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 13.12.3 用户修改操作

说明: 当点击修改按钮时, 实现数据回显, 默认密码不显示, 如果赋值默认修改.  
![](https://img-blog.csdnimg.cn/818a8416362442b79688c364b827fda2.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 13.12.4 完成用户删除操作

提示: 根据 ID 删除数据 可以实现数据的动态传递. 具体代码参数文档资料

```
<td>
                <a th:href="@{/toUpdateUser(id=${user.id})}"><button>编辑</button></a>
                <a th:href="@{/deleteUserById(id=${user.id})}"><button>删除</button></a>
     </td>
```

