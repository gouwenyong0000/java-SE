## 8.1 配置文件

### 8.1.1 配置文件的作用

配置文件是用于给应用程序提供配置参数以及初始化设置的一些有特殊格式的文件

### 8.1.2 常见的配置文件类型

1.  properties 文件, 例如 druid 连接池就是使用 properties 文件作为配置文件
2.  XML 文件, 例如 Tomcat 就是使用 XML 文件作为配置文件
3.  YAML 文件, 例如 SpringBoot 就是使用 YAML 作为配置文件
4.  json 文件, 通常用来做文件传输，也可以用来做前端或者移动端的配置文件

8.2 properties 文件
-----------------

### 8.2.1 文件示例

```
atguigu.jdbc.url=jdbc:mysql://192.168.198.100:3306/bj1026
atguigu.jdbc.driver=com.mysql.cj.jdbc.Driver
atguigu.jdbc.username=root
atguigu.jdbc.password=root
```

### 8.2.2 语法规范

*   由键值对组成
*   键和值之间的符号是等号
*   每一行都必须顶格写，前面不能有空格之类的其他符号

8.3 XML 文件
----------

### 8.3.1 文件示例

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <!-- 配置SpringMVC前端控制器 -->
    <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>

        <!-- 在初始化参数中指定SpringMVC配置文件位置 -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-mvc.xml</param-value>
        </init-param>

        <!-- 设置当前Servlet创建对象的时机是在Web应用启动时 -->
        <load-on-startup>1</load-on-startup>

    </servlet>
    <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>

        <!-- url-pattern配置斜杠表示匹配所有请求 -->
        <!-- 两种可选的配置方式：
                1、斜杠开头：/
                2、包含星号：*.atguigu
             不允许的配置方式：前面有斜杠，中间有星号
                /*.app
         -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

### 8.3.2 概念介绍

XML 是 eXtensible Markup Language，翻译过来就是可扩展标记语言。所以很明显，XML 和 HTML 一样都是标记语言，也就是说它们的基本语法都是标签。

**可扩展**

可扩展三个字表面上的意思是 XML 允许自定义格式。但是别美，这不代表你可以随便写

![](https://img-blog.csdnimg.cn/7d4eb06194444524a112f55420168f2f.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

在 XML 基本语法规范的基础上，你使用的那些第三方应用程序、框架会通过设计『XML 约束』的方式『强制规定』配置文件中可以写什么和怎么写，规定之外的都不可以写。

XML 基本语法这个知识点的定位是：我们不需要从零开始，从头到尾的一行一行编写 XML 文档，而是在第三方应用程序、框架已提供的配置文件的基础上修改。要改成什么样取决于你的需求，而怎么改取决于 XML 基本语法和具体的 XML 约束

### 8.3.3 XML 的基本语法

*   XML 文档声明

这部分基本上就是固定格式，要注意的是文档声明一定要从第一行第一列开始写

```
<?xml version="1.0" encoding="UTF-8"?>
```

*   根标签

根标签有且只能有一个。

*   标签体

*   标签属性

    *   属性必须有值
    *   属性值必须加引号，单双都行

看到这里大家一定会发现 XML 的基本语法和 HTML 的基本语法简直如出一辙。其实这不是偶然的，XML 基本语法 + HTML 约束 = HTML 语法。在逻辑上 HTML 确实是 XML 的子集。

```
<?xml version="1.0" encoding="utf-8"?>
<users>
    <user id="1">
        <id>100</id>
        <name>张三</name>
        <age>18</age>
    </user>
    <user id="2">
        <id>200</id>
        <name>李四</name>
        <age>25</age>
    </user>
    <user id="3">
        <id>300</id>
        <name>王五</name>
        <age>30</age>
    </user>
</users>
```

```
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
```

从 HTML4.01 版本的文档类型声明中可以看出，这里使用的 DTD 类型的 XML 约束。也就是说 http://www.w3.org/TR/html4/loose.dtd 这个文件定义了 HTML 文档中可以写哪些标签，标签内可以写哪些属性，某个标签可以有什么样的子标签。

### 8.3.4 XML 的约束 (稍微了解)

将来我们主要就是根据 XML 约束中的规定来编写 XML 配置文件，而且会在我们编写 XML 的时候根据约束来提示我们编写, 而 XML 约束主要包括 DTD 和 Schema 两种。

*   DTD

*   Schema

Schema 约束要求我们一个 XML 文档中，所有标签，所有属性都必须在约束中有明确的定义。

下面我们以 web.xml 的约束声明为例来做个说明：

```
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
```

### 8.3.5 XML 解析

##### 8.3.5.1 XML 解析的作用

用 Java 代码读取 xml 中的数据

##### 8.3.5.2 DOM4J 的使用步骤

1.  导入 jar 包 dom4j.jar
2.  创建解析器对象 (SAXReader)
3.  解析 xml 获得 Document 对象
4.  获取根节点 RootElement
5.  获取根节点下的子节点

##### 8.3.5.3 DOM4J 的 API 介绍

1.  创建 SAXReader 对象

```
SAXReader saxReader = new SAXReader();
```

2.  解析 XML 获取 Document 对象: 需要传入要解析的 XML 文件的字节输入流

```
InputStream inputStream = 类名.class.getClassLoader().getResourceAsStream("user.xml");
Document document = reader.read(inputStream);
```

3.  获取文档的根标签

```
Element rootElement = documen.getRootElement()
```

4.  获取标签的子标签

```
//获取所有子标签
List<Element> sonElementList = rootElement.elements();
//获取指定标签名的子标签
List<Element> sonElementList = rootElement.elements("标签名");
```

5.  获取标签体内的文本

```
String text = element.getText();
```

6.  获取标签的某个属性的值

```
String value = element.AttributeValue("属性名");
```

##### 8.3.5.4 案例讲解

1.API 练习

```
public static void main(String[] args) throws DocumentException {
        //1.创建解析器对象
        SAXReader saxReader = new SAXReader();
        //2.通过类加载器获取流数据
        InputStream inputStream =
                SaxDemo.class.getClassLoader().getResourceAsStream("user.xml");
        //3.获取document对象
        Document document = saxReader.read(inputStream);
        //4.获取根标签内容
        Element rootElement = document.getRootElement();
        //5.获取所有标签内容
        List<Element> list = rootElement.elements();
        //默认获取第一个元素标签
        Element element= rootElement.element("user");
        //获取name标签
        Element nameEle = element.element("name");
        //获取name标签体内容
        String name = nameEle.getText();
        System.out.println(name);
    }
```

2.  API 练习二

```
//获取所有标签的name属性
    public static void main(String[] args) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        InputStream inputStream = SaxDemo.class.getClassLoader().getResourceAsStream("user.xml");
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.elements("user");
        for (Element element : list){
            //获取属性类型
            Attribute attribute = element.attribute("id");
            int idAttr = Integer.valueOf(attribute.getValue());
            int id = Integer.valueOf(element.element("id").getText());
            String name = element.element("name").getText();
            int age = Integer.valueOf(element.element("age").getText());
            System.out.println("获取数据:"+id+"|"+name+"|"+age+"|"+idAttr);
        }
    }
```


-------------