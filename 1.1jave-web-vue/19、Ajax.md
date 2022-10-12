6.1 Ajax 介绍
-----------

### 6.1.1 服务器端渲染

![](https://img-blog.csdnimg.cn/3e7c25a6e4e24b2a9511dbc7c7309e6d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 6.1.2 Ajax 渲染（局部更新）

![](https://img-blog.csdnimg.cn/f0f4b674aa1f4c2f956cf760ae269358.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 6.1.3 前后端分离

真正的前后端分离是前端项目和后端项目分服务器部署，在我们这里我们先理解为彻底舍弃服务器端渲染，数据全部通过 Ajax 方式以 JSON 格式 / xml 格式来传递

6.2 同步与异步
---------

Ajax 本身就是 Asynchronous JavaScript And XML 的缩写，直译为：异步的 JavaScript 和 XML。  
在实际应用中 Ajax 指的是：  

1. 不刷新浏览器窗口  
2. 不做页面跳转  
3. 局部更新页面内容的技术。

### 6.2.1 什么是同步

多个操作按顺序执行前面的操作没有完成，后面的操作就必须等待 所以同步操作通常是串行的。  
![](https://img-blog.csdnimg.cn/1f8d637d3cdf437599f99093554ed1a0.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_14,color_FFFFFF,t_70,g_se,x_16)

### 6.2.2 什么是异步

多个操作相继开始并发执行, 即使开始的先后顺序不同，但是由于它们各自是在自己**独立**的进程或线程中完成，所以**互不干扰**谁也不用等谁  
![](https://img-blog.csdnimg.cn/15772943b1224c4fa728c60633c0d683.png)

6.3 Axios
---------

### 6.3.1 Axios 介绍

使用原生的 JavaScript 程序执行 Ajax 极其繁琐，所以一定要使用框架来完成。而 Axios 就是目前最流行的前端 Ajax 框架。  
Axios 是一个基于 promise 的 HTTP 库，可以用在浏览器和 node.js 中。

官方网址: `http://www.axios-js.com/`

### 6.3.2 Axios 特点

1.  从浏览器中创建 XMLHttpRequests
2.  从 node.js 创建 http 请求
3.  支持 Promise API
4.  拦截请求和响应
5.  转换请求数据和响应数据
6.  取消请求
7.  自动转换 JSON 数据
8.  客户端支持防御 XSRF

### 6.3.3 导入 JS 的方式

```
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
```

![](https://img-blog.csdnimg.cn/5121be48459d4867a464ecb786168f25.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

6.3 Axios 入门案例
--------------

### 6.3.1 导入 JS

说明: 根据课前资料中的文件 导入 vue.js/axios.js 其中 axios.min.js 是生产环境的要求  
![](https://img-blog.csdnimg.cn/85b037f8af924e4d92d4f6071747dd61.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 6.3.2 编辑 index.html

```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Axios入门案例</title>
    <script src="js/vue.js"></script>
    <script src="js/axios.js"></script>
</head>
<body>
  <h1>Axios入门案例用法</h1>

  <script>
      //1.发起ajax-get请求
      axios({
          url: "axiosGet",
          method: "get",
          params: {
              id: 200,
              name: "tomcat"
          }
      }).then(function (promise){
          console.log(promise)
          console.log(promise.data)
          //获取服务器返回值业务数据
          //console.log(promise.data.name)
          //遍历数据
          for(let i=0;i<promise.data.length;i++){
              console.log(promise.data[i])
          }
      }).catch(function (error){
          alert(error)
      })
  </script>
</body>
</html>
```

### 6.3.3 实现数据获取

```
package com.atguigu.servler;

import com.atguigu.bean.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AxiosGetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取Ajax提交的数据
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        System.out.println(id+":"+name);

        //2.响应数据
        response.setContentType("text/html;charset=utf-8");
        /*response.getWriter().write("服务器响应数据!!!");*/

        //3.手动封装JSON数据
        //String json = "{\"id\":100,\"name\":\"Axios练习\"}";

        //4. jackson用法
        User user1 = new User();
        user1.setId(1001);
        user1.setName("Axios练习测试1");

        User user2 = new User();
        user2.setId(1002);
        user2.setName("Axios练习测试2");

        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);
        response.getWriter().write(json);

        //JSON转化为对象
        List<User> list2 = mapper.readValue(json,ArrayList.class);
        System.out.println(list2);
    }
}
```

### 6.3.4 额外扩展

[VUE 入门案例](https://harrylyj.blog.csdn.net/article/details/116209877)

