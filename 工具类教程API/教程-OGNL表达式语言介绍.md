> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.cnblogs.com](https://www.cnblogs.com/yw-ah/p/5760192.html)

## 概述

OGNL 是 Object-Graph Navigation Language 的缩写，它是一种功能强大的表达式语言（Expression Language，简称为 EL），通过它简单一致的表达式语法，可以存取对象的任意属性，调用对象的方法，遍历整个对象的结构图，实现字段类型转化等功能。它使用相同的表达式去存取对象的属性。　　　　　　　　　　　　　　　　------- 百度百科

从语言角度来说：它是一个功能强大的表达式语言，用来获取和设置 java 对象的属性 ，它旨在提供一个更高抽象度语法来对 java 对象图进行导航。另外，java 中很多可以做的事情，也可以使用 OGNL 来完成，例如：列表映射和选择。对于开发者来说，使用 OGNL，可以用简洁的语法来完成对 java 对象的导航。通常来说：通过一个 “路径” 来完成对象信息的导航，这个 “路径” 可以是到 java bean 的某个属性，或者集合中的某个索引的对象，等等，而不是直接使用 get 或者 set 方法来完成。

　**首先来介绍下 OGNL 的三要素：**

　　一、表达式：

　　　　表达式（Expression）是整个 OGNL 的核心内容，所有的 OGNL 操作都是针对表达式解析后进行的。通过表达式来告诉 OGNL 操作到底要干些什么。因此，表达式其实是一个带有语法含义的字符串，整个字符串将规定操作的类型和内容。OGNL 表达式支持大量的表达式，如 “链式访问对象”、表达式计算、甚至还支持 Lambda 表达式。

　　二、Root 对象：

　　　　OGNL 的 Root 对象可以理解为 OGNL 的操作对象。当我们指定了一个表达式的时候，我们需要指定这个表达式针对的是哪个具体的对象。而这个具体的对象就是 Root 对象，这就意味着，如果有一个 OGNL 表达式，那么我们需要针对 Root 对象来进行 OGNL 表达式的计算并且返回结果。

　　三、上下文环境：

　　　　有个 Root 对象和表达式，我们就可以使用 OGNL 进行简单的操作了，如对 Root 对象的赋值与取值操作。但是，实际上在 OGNL 的内部，所有的操作都会在一个特定的数据环境中运行。这个数据环境就是上下文环境（Context）。OGNL 的上下文环境是一个 Map 结构，称之为 OgnlContext。Root 对象也会被添加到上下文环境当中去。

## **OGNL 的基本语法：**

### 1、对 Root 对象的访问

OGNL 使用的是一种链式的风格进行对象的访问。具体代码如下：

```java
@Test
    public void testOgnl()
    {
        User user = new User("rcx", "123");
        Address address = new Address("110003", "沈阳市和平区");
        user.setAddress(address);
        try
        {
            System.out.println(Ognl.getValue("name", user));
            System.out.println(Ognl.getValue("address", user));
            System.out.println(Ognl.getValue("address.port", user));

            //输出结果：
         //rcx
           //com.rcx.ognl.Address@dda25b
           //110003
        }
        catch (OgnlException e)
        {
            e.printStackTrace();
        }
    }
```



### 2、对上下文对象的访问

　　使用 OGNL 的时候如果不设置上下文对象，系统会自动创建一个上下文对象，如果传入的参数当中包含了上下文对象则会使用传入的上下文对象。当访问上下文环境当中的参数时候，需要在表达式前面加上'#'，表示了与访问 Root 对象的区别。具体代码如下：



```java
@Test
    public void testOgnl1()
    {
        User user = new User("rcx", "123");
        Address address = new Address("110003", "沈阳市和平区");
        user.setAddress(address);
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("init", "hello");
        context.put("user", user);
        try
        {
            System.out.println(Ognl.getValue("#init", context, user));
            System.out.println(Ognl.getValue("#user.name", context, user));
            System.out.println(Ognl.getValue("name", context, user));
            //输出结果：
          //hello
            //rcx
            //rcx       
         }
        catch (OgnlException e)
        {
            e.printStackTrace();
        }
    }
```

　这段代码很好的区分了访问 Root 对象和访问上下文对象的区别。

### 3、对静态变量的访问

在 OGNL 表达式当中也可以访问静态变量或者调用静态方法，格式如 @[class]@[field/method()]。具体代码如下：

```java
public class Constant
{
    public final static String ONE = "one";
    
    public static void get()
    {}
    
    public static String getString()
    {
        return "string";
    }
}

    @Test
    public void testOgnl2()
    {
        try
        {
            Object object = Ognl.getValue("@com.rcx.ognl.Constant@ONE", null);
            
            Object object1 = Ognl.getValue("@com.rcx.ognl.Constant@get()", null);
            
            Object object2 = Ognl.getValue("@com.rcx.ognl.Constant@getString()", null);
            
            System.out.println(object);
            System.out.println(object1);
            System.out.println(object2);
            //one
            //null 当返回值是void的时候输出的是null
            //string
        }
        catch (OgnlException e)
        {
            e.printStackTrace();
        }
    }
```

### 4、方法的调用

如果需要调用 Root 对象或者上下文对象当中的方法也可以使用.+ 方法的方式来调用。甚至可以传入参数。代码如下：

```java
@Test
    public void testOgnl3()
    {
        User user = new User();
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "rcx");
        context.put("password", "password");
        try
        {
            System.out.println(Ognl.getValue("getName()", context, user));
            Ognl.getValue("setName(#name)", context, user);
            System.out.println(Ognl.getValue("getName()", context, user));
            //输出结果
            //null
            //rcx
        }
        catch (OgnlException e)
        {
            e.printStackTrace();
        }
    }
```



从代码可以看出来，赋值的时候可以选择上下文当中的元素进行给 Root 对象的 name 属性赋值。

### 5、对数组和集合的访问

OGNL 支持对数组按照数组下标的顺序进行访问。此方式也适用于对集合的访问，对于 Map 支持使用键进行访问。代码如下：

```java
@Test
    public void testOgnl4()
    {
        User user = new User();
        Map<String, Object> context = new HashMap<String, Object>();
        String[] strings  = {"aa", "bb"};
        ArrayList<String> list = new ArrayList<String>();
        list.add("aa");
        list.add("bb");
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        context.put("list", list);
        context.put("strings", strings);
        context.put("map", map);
        try
        {
            System.out.println(Ognl.getValue("#strings[0]", context, user));
            System.out.println(Ognl.getValue("#list[0]", context, user));
            System.out.println(Ognl.getValue("#list[0 + 1]", context, user));
            System.out.println(Ognl.getValue("#map['key1']", context, user));
            System.out.println(Ognl.getValue("#map['key' + '2']", context, user));
            //输出如下：
            //aa
            //aa
            //bb
            //value1
            //value2
        }
        catch (OgnlException e)
        {
            e.printStackTrace();
        }
    }
```



　从上面代码不仅看到了访问数组与集合的方式同时也可以看出来 OGNL 表达式当中支持操作符的简单运算。有如下所示：

　　2 + 4 // 整数相加（同时也支持减法、乘法、除法、取余 [% / mod]、）

　　"hell" + "lo" // 字符串相加

　　i++ // 递增、递减

　　i == j // 判断

　　var in list // 是否在容器当中

### 6、投影与选择

　　OGNL 支持类似数据库当中的选择与投影功能。

　　投影：选出集合当中的相同属性组合成一个新的集合。语法为 collection.{XXX}，XXX 就是集合中每个元素的公共属性。

　　选择：选择就是选择出集合当中符合条件的元素组合成新的集合。语法为 collection.{Y XXX}，其中 Y 是一个选择操作符，XXX 是选择用的逻辑表达式。

　　　　选择操作符有 3 种：

　　　　　　? ：选择满足条件的所有元素

　　　　　　^：选择满足条件的第一个元素

　　　　　　$：选择满足条件的最后一个元素

　  示例代码如下：



```java
@Test
    public void testOgnl5()
    {
        Person p1 = new Person(1, "name1");
        Person p2 = new Person(2, "name2");
        Person p3 = new Person(3, "name3");
        Person p4 = new Person(4, "name4");
        Map<String, Object> context = new HashMap<String, Object>();
        ArrayList<Person> list = new ArrayList<Person>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        context.put("list", list);
        try
        {
            ArrayList<Integer> list2 = (ArrayList<Integer>) Ognl.getValue("#list.{id}", context,
                    list);
            ArrayList<String> list3 = (ArrayList<String>) Ognl.getValue("#list.{id + '-' + name}",
                    context, list);
            ArrayList<Person> list4 = (ArrayList<Person>) Ognl.getValue("#list.{? #this.id > 2}",
                    context, list);
            ArrayList<Person> list5 = (ArrayList<Person>) Ognl.getValue("#list.{^ #this.id > 2}",
                    context, list);
            ArrayList<Person> list6 = (ArrayList<Person>) Ognl.getValue("#list.{$ #this.id > 2}",
                    context, list);
            System.out.println(list2);
            System.out.println(list3);
            System.out.println(list4);
            System.out.println(list5);
            System.out.println(list6);
            //[1, 2, 3, 4]
            //[1-name1, 2-name2, 3-name3, 4-name4]
            //[Person [id=3, name=name3], Person [id=4, name=name4]]
            //[Person [id=3, name=name3]]
            //[Person [id=4, name=name4]]
            //
        }
        catch (OgnlException e)
        {
            e.printStackTrace();
        }
    }
```



### 7、创建对象

　　OGNL 支持直接使用表达式来创建对象。主要有三种情况：

　　构造 List 对象：使用 {}, 中间使用','进行分割如 {"aa", "bb", "cc"}

　　构造 Map 对象：使用 #{}，中间使用', 进行分割键值对，键值对使用':'区分，如 #{"key1":"value1","key2":"value2"}

　　构造任意对象：直接使用已知的对象的构造方法进行构造。

　　示例代码如下：



```java
@Test
    public void testOgnl6()
    {
        try
        {
            Map<String, String> map = (Map<String, String>)Ognl.getValue("#{'key1':'value1'}", null);
            System.out.println(map);
            List<String> list = (List<String>)Ognl.getValue("{'key1','value1'}", null);
            System.out.println(list);
            Object object = Ognl.getValue("new java.lang.Object()", null);
            System.out.println(object);
            //{key1=value1}
            //[key1, value1]
            //java.lang.Object@dda25b
        }
        catch (OgnlException e)
        {
            e.printStackTrace();
        }
    }
```



##  补充:

表达式通常用来访问页面中的各种变量, 进行结果输出.

    struts2 中共支持以下几种表达式语言 (默认的是 OGNL)：

   OGNL：可以方便地操作对象属性的开源表达式语言；

   JSTL：(JSP Standard Tag Library)：JSP2.0 集成的标准表达式语言；

   Groovy：基于 [Java](http://lib.csdn.net/base/17 "Java EE知识库") 平台的动态语言，它具有时下比较流行的动态语言的一些特性；

    Velocity：一种基于 Java 的模板匹配引擎。

一、什么是 OGNL

  名称：全称是 Object-Graph Navigation Language

   用途：是一个用来获取 和设置 java 对象属性的表达式语言。

   应用场合：通过使用表达式语法导航对象图，而不是直接调用对象的获取和设置方法可以提供许多应用。比如在 XML 文件 或者脚本文件中嵌入 OGNL 表达式语法，在 JSP 页面 使用 OGNL 表达式语法。

操作对象：基于当前对象的上下文。

二、OGNL 引用方式

    属性名称：如对象 user 的属性 username, 可以使用 user.username 来获取.

    方法调用：可以使用 user.hashCode() 返回当前对象的哈希码.

    数组元素：对于 userlist 数组对象, 可以使用 userlist[0] 来引用其中的某一个元素.

三、OGNL 相对其他表达式语言具有下面几大优势

    1) 支持对象方法调用：如 xxx.doSomeSpecial()

     2) 支持类静态的方法 | 值调用：格式为 "@[类全名 (包括包路径)]@[方法名 | 值名]"。如：

```
@java.lagn.String@format('foo%s','bar')--调用类静态方法

@tutorial.MyConstant@APP_NAME--访问类的静态值
```

    3) 支持赋值操作和表达式串联 ：如 price=100,discount=0.8,calculatePrice(), 这个表达式会返回 80

    4) 访问 OGNL 上下文 (OGNL context) 和 ActionContext

    5) 操作集合对象

四、使用 OGNL 表达式

1) 要使用 OGNL 表达式，首先需要在 web.xml 中添加 ActionContextCleanUp 过滤器



```
<filter>

<filter-name>struts-cleanup</filter-name>

<filter-class>org.apache.struts2.dispatcher.ActionContextCleanUp</filter-class>

</filter>

<filter-mapping>

<filter-name>struts-cleanup</filter-name>

<url-pattern>/*</url-pattern>

</filter-mapping>
```



五、'#'运算符

用途一般有三种：

1）访问非根对象属性，由于 Struts 2 中值栈被视为根对象，所以访问其他非根对象时，需要加 #前缀 。实际上，# 相当于 ActionContext. getContext()。

            parameters：包含当前 HTTP 请求参数的 Map,#parameters.id[0], 等价于 request.getParameter("id");

            request：包含当前 HttpServletRequest 的属性的 Map,#request.userName, 等价于 request.getAttribute("username");

            session：包含当前 HttpSession 的属性的 Map,#session.userName, 等价于 session.getAttribute("username");

           application：包含当前应用的 ServletContext 的属性的 Map,#application.userName, 等价于 application.getAttribute("username");

           attr：用于按 request→session→application 顺序访问某个属性,#attr.userName, 等价于按顺序在 request,session,application 范围内读取 userName 属性, 直到找到为止。

2）用于过滤和投影（projecting）集合

```
如person.{?#this.age>20}
```

 **?** -- 获取集合中所有满足选择逻辑的对象 (拿 sql 来做比例就是 "select * from xxx where age>20")

 **^** -- 获取集合中第一个满足选择逻辑的对象 (拿 sql 来做比例就是 "select top(1) from xxx where age>20")

 **$** -- 获取集合中最后一个满足选择逻辑的对象

3） 用来构造 Map

```
如#{'foo1':'bar1', 'foo2':'bar2'}
```

六、'%'运算符

用途是在标识的属性为字符串类型时, 计算 OGNL 表达式的值, 如：

```
<s:url value="test.jsp?age=#userlist['admin']">→test.jsp?#userlist['admin']---可见当字符串与OGNL表达式串起来时,只会被当作字符串对待,并不执行

<s:url value="test.jsp?age=%{#userlist['admin']}">→test.jsp?age=44---使用了该符号,就可以使得OGNL表达式被执行
```

七、'$'运算符

两个用途：

1）用于在国际化资源文件中, 引用 OGNL 表达式。例如在资源文件中有一个标签 fileName, 则可以在资源文件中引用：

```
validation.require=${getText(fileName)} is required
```

2）在 struts2 配置文件中引用 OGNL 表达式，如：

```
<action >

<result type="redirect">ListPhotos.action?albumId=${albumId}</result>--但这个albumId是从哪来的呢?

</action>
```