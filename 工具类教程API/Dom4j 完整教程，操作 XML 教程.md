1、DOM4J 简介
==========

    DOM4J 是 dom4j.org 出品的一个开源 XML 解析包。DOM4J 应用于 Java 平台，采用了 Java 集合框架并完全支持 DOM，SAX 和 JAXP。

    DOM4J 使用起来非常简单。只要你了解基本的 XML-DOM 模型，就能使用。

    Dom：把整个文档作为一个对象。

  DOM4J 最大的特色是使用大量的接口。它的主要接口都在 org.dom4j 里面定义：

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top" width="166"><p>Attribute</p></td><td valign="top" width="390"><p align="left">定义了&nbsp;XML&nbsp;的属性。</p></td></tr><tr><td valign="top" width="166"><p>Branch</p></td><td valign="top" width="390"><p align="left">指能够包含子节点的节点。如 XML 元素 (Element) 和文档 (Docuemnts) 定义了一个公共的行为</p></td></tr><tr><td valign="top" width="166"><p>CDATA</p></td><td valign="top" width="390"><p align="left">定义了&nbsp;XML CDATA&nbsp;区域</p></td></tr><tr><td valign="top" width="166"><p>CharacterData</p></td><td valign="top" width="390"><p align="left">是一个标识接口，标识基于字符的节点。如 CDATA，Comment, Text.</p></td></tr><tr><td valign="top" width="166"><p>Comment</p></td><td valign="top" width="390"><p align="left">定义了&nbsp;XML&nbsp;注释的行为</p></td></tr><tr><td valign="top" width="166"><p>Document</p></td><td valign="top" width="390"><p align="left">定义了 XML&nbsp;文档</p></td></tr><tr><td valign="top" width="166"><p>DocumentType</p></td><td valign="top" width="390"><p align="left">定义&nbsp;XML DOCTYPE&nbsp;声明</p></td></tr><tr><td valign="top" width="166"><p>Element</p></td><td valign="top" width="390"><p align="left">定义 XML&nbsp;元素</p></td></tr><tr><td valign="top" width="166"><p>ElementHandler</p></td><td valign="top" width="390"><p align="left">定义了 Element&nbsp;对象的处理器</p></td></tr><tr><td valign="top" width="166"><p>ElementPath</p></td><td valign="top" width="390"><p align="left">被&nbsp;ElementHandler&nbsp;使用，用于取得当前正在处理的路径层次信息</p></td></tr><tr><td valign="top" width="166"><p>Entity</p></td><td valign="top" width="390"><p align="left">定义&nbsp;XML entity</p></td></tr><tr><td valign="top" width="166"><p>Node</p></td><td valign="top" width="390"><p align="left">为 dom4j 中所有的 XML 节点定义了多态行为</p></td></tr><tr><td valign="top" width="166"><p>NodeFilter</p></td><td valign="top" width="390"><p align="left">定义了在 dom4j&nbsp;节点中产生的一个滤镜或谓词的行为（predicate）</p></td></tr><tr><td valign="top" width="166"><p>ProcessingInstruction</p></td><td valign="top" width="390"><p align="left">定义&nbsp;XML&nbsp;处理指令</p></td></tr><tr><td valign="top" width="166"><p>Text</p></td><td valign="top" width="390"><p align="left">定义&nbsp;XML&nbsp;文本节点</p></td></tr><tr><td valign="top" width="166"><p>Visitor</p></td><td valign="top" width="390"><p align="left">用于实现&nbsp;Visitor 模式</p></td></tr><tr><td valign="top" width="166"><p>XPath</p></td><td valign="top" width="390"><p align="left">在分析一个字符串后会提供一个&nbsp;XPath&nbsp;表达式</p></td></tr></tbody></table>

2、XML 文档操作 1
============

2.1、读取 XML 文档：
--------------

     读写 XML 文档主要依赖于 org.dom4j.io 包，有 DOMReader 和 SAXReader 两种方式。因为利用了相同的接口，它们的调用方式是一样的。



```java
public static Document load(String filename) {  
    Document document = null;  
    try {  
        SAXReader saxReader = new SAXReader();  
        document = saxReader.read(new File(filename)); // 读取XML文件,获得document对象  
    } catch (Exception ex) {  
        ex.printStackTrace();  
    }  
    return document;  
}  
  
public static Document load(URL url) {  
    Document document = null;  
    try {  
        SAXReader saxReader = new SAXReader();  
        document = saxReader.read(url); // 读取XML文件,获得document对象  
    } catch (Exception ex) {  
        ex.printStackTrace();  
    }  
    return document;  
}
```



// 读取指定的 xml 文件之后返回一个 Document 对象，这个对象代表了整个 XML 文档，用于各种 Dom 运算。执照 XML 文件头所定义的编码来转换。

2.2、获取根节点
---------

根节点是 xml 分析的开始，任何 xml 分析工作都需要从根开始

```java
Xml xml = new Xml();  
  
Document dom = xml.load(path + "/" + file);  
  
Element root = dom.getRootElement();
```

2.3、. 新增一个节点以及其下的子节点与数据
-----------------------



```java
Element menuElement = root.addElement("menu");  
  
Element engNameElement = menuElement.addElement("engName");  
  
engNameElement.setText(catNameEn);  
  
Element chiNameElement = menuElement.addElement("chiName");  
  
chiNameElement.setText(catName);
```



2.4、 写入 XML 文件
--------------

注意文件操作的包装类是乱码的根源



```java
public static boolean doc2XmlFile(Document document, String filename) {  
    boolean flag = true;  
    try {  
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(  
                new FileOutputStream(filename), "UTF-8"));  
        writer.write(document);  
        writer.close();  
    } catch (Exception ex) {  
        flag = false;  
        ex.printStackTrace();  
    }  
    System.out.println(flag);  
    return flag;  
}
```



```java
Dom4j通过XMLWriter将Document对象表示的XML树写入指定的文件，并使用OutputFormat格式对象指定写入的风格和编码方法。
调用OutputFormat.createPrettyPrint()方法可以获得一个默认的pretty print风格的格式对象。对OutputFormat对象调用setEncoding()方法可以指定XML文件的编码方法。
```



```java
public void writeTo(OutputStream out, String encoding)  
        throws UnsupportedEncodingException, IOException {  
    OutputFormat format = OutputFormat.createPrettyPrint();  
  
    format.setEncoding("gb2312");  
  
    XMLWriter writer = new XMLWriter(System.out, format);  
  
    writer.write(doc);  
  
    writer.flush();  
  
    return;  
  
}
```



## 2.5、 遍历 xml 节点

---------------

       对 Document 对象调用 getRootElement()方法可以返回代表根节点的 Element 对象。拥有了一个 Element 对象后，可以对该对象调用 elementIterator()方法获得它的子节点的 Element 对象们的一个迭代器。使用 (Element)iterator.next() 方法遍历一个 iterator 并把每个取出的元素转化为 Element 类型。



```java
public boolean isOnly(String catNameEn, HttpServletRequest request,  
        String xml) {  
    boolean flag = true;  
    String path = request.getRealPath("");  
    Document doc = load(path + "/" + xml);  
    Element root = doc.getRootElement();  
    for (Iterator i = root.elementIterator(); i.hasNext();) {  
        Element el = (Element) i.next();  
        if (catNameEn.equals(el.elementTextTrim("engName"))) {  
            flag = false;  
            break;  
        }  
    }  
    return flag;  
}
```



2.6、创建 xml 文件
-------------



```java
public static void main(String args[]) {  
  
    String fileName = "c:/text.xml";  
  
    Document document = DocumentHelper.createDocument();// 建立document对象，用来操作xml文件  
  
    Element booksElement = document.addElement("books");// 建立根节点  
  
    booksElement.addComment("This is a test for dom4j ");// 加入一行注释  
  
    Element bookElement = booksElement.addElement("book");// 添加一个book节点  
  
    bookElement.addAttribute("show", "yes");// 添加属性内容  
  
    Element titleElement = bookElement.addElement("title");// 添加文本节点  
  
    titleElement.setText("ajax in action");// 添加文本内容  
  
    try {  
  
        XMLWriter writer = new XMLWriter(new FileWriter(new File(fileName)));  
  		writer.writer(document);
        writer.close();  
  
    } catch (Exception e) {  
  
        e.printStackTrace();  
  
    }  
  
}
```



2.7、修改节点属性
----------



```java
public static void modifyXMLFile() {  
  
    String oldStr = "c:/text.xml";  
  
    String newStr = "c:/text1.xml";  
  
    Document document = null;  
  
    //修改节点的属性  
  
    try {  
  
    SAXReader saxReader = new SAXReader(); // 用来读取xml文档  
  
    document = saxReader.read(new File(oldStr)); // 读取xml文档  
  
    List list = document.selectNodes("/books/book/@show");// 用xpath查找节点book的属性  
  
    Iterator iter = list.iterator();  
  
    while (iter.hasNext()) {  
  
    Attribute attribute = (Attribute) iter.next();  
  
    if (attribute.getValue().equals("yes"))   
  
        attribute.setValue("no");  
  
    }  
  
    } catch (Exception e) {  
  
        e.printStackTrace();  
  
    }  
  
    //修改节点的内容  
  
    try {  
  
    SAXReader saxReader = new SAXReader(); // 用来读取xml文档  
  
    document = saxReader.read(new File(oldStr)); // 读取xml文档  
  
    List list = document.selectNodes("/books/book/title");// 用xpath查找节点book的内容  
  
    Iterator iter = list.iterator();  
  
    while (iter.hasNext()) {  
  
    Element element = (Element) iter.next();  
  
    element.setText("xxx");// 设置相应的内容  
  
    }  
  
    } catch (Exception e) {  
  
        e.printStackTrace();  
  
    }  
  
       
  
    try {  
  
    XMLWriter writer = new XMLWriter(new FileWriter(new File(newStr)));  
  
    writer.write(document);  
  
    writer.close();  
  
    } catch (Exception ex) {  
  
        ex.printStackTrace();  
  
    }  
  
}
```



2.8、删除节点
--------



```java
public static void removeNode() {  
  
    String oldStr = "c:/text.xml";  
  
    String newStr = "c:/text1.xml";  
  
    Document document = null;  
  
    try {  
  
        SAXReader saxReader = new SAXReader();// 用来读取xml文档  
  
        document = saxReader.read(new File(oldStr));// 读取xml文档  
  
        List list = document.selectNodes("/books/book");// 用xpath查找对象  
  
        Iterator iter = list.iterator();  
  
        while (iter.hasNext()) {  
  
            Element bookElement = (Element) iter.next();  
  
            // 创建迭代器，用来查找要删除的节点,迭代器相当于指针，指向book下所有的title节点  
  
            Iterator iterator = bookElement.elementIterator("title");  
  
            while (iterator.hasNext()) {  
  
                Element titleElement = (Element) iterator.next();  
  
                if (titleElement.getText().equals("ajax in action")) {  
  
                    bookElement.remove(titleElement);  
  
                }  
  
            }  
  
        }  
  
    } catch (Exception e) {  
  
        e.printStackTrace();  
  
    }  
  
    try {  
  
        XMLWriter writer = new XMLWriter(new FileWriter(new File(newStr)));  
  
        writer.write(document);  
  
        writer.close();  
  
    } catch (Exception ex) {  
  
        ex.printStackTrace();  
  
    }  
  
}
```



2、XML 文档操作 2
============

2.1、Document 对象相关        
-------------------------

### 1、读取 XML 文件, 获得 document 对象.    

```
SAXReader reader = new SAXReader();  
Document   document = reader.read(new File("input.xml"));
```

### 2、解析 XML 形式的文本, 得到 document 对象.    

```
String text = "<members></members>";      
Document document = DocumentHelper.parseText(text);
```

### 3、主动创建 document 对象.

```
Document document = DocumentHelper.createDocument();      
Element root = document.addElement("members");// 创建根节点
```

2.2、节点相关        
----------------

### 1、获取文档的根节点.      

```
Element rootElm = document.getRootElement();
```

### 2、取得某节点的单个子节点.      

```
Element memberElm=root.element("member");// "member"是节点名
```

### 3. 取得节点的文字      

```
String text=memberElm.getText();     
String text=root.elementText("name");这个是取得根节点下的name字节点的文字.
```

### 4. 取得某节点下指定名称的所有节点并进行遍历.      

```
List nodes = rootElm.elements("member");      
for (Iterator it = nodes.iterator(); it.hasNext();) {      
    Element elm = (Element) it.next();      
   // do something      
}
```

### 5. 对某节点下的所有子节点进行遍历.      

```
for(Iterator it=root.elementIterator();it.hasNext();){      
       Element element = (Element) it.next();      
        // do something      
}
```

### 6. 在某节点下添加子节点.      

```
Element ageElm = newMemberElm.addElement("age");
```

### 7. 设置节点文字.      

```
ageElm.setText("29");
```

### 8. 删除某节点.      

```
parentElm.remove(childElm);    // childElm是待删除的节点,parentElm是其父节点
```

### 9. 添加一个 CDATA 节点.      

```
Element contentElm = infoElm.addElement("content");      
contentElm.addCDATA(diary.getContent());     
```

2.3、属性相关.     
--------------

### 1. 取得节点的指定的属性      

```
Element root=document.getRootElement();          
Attribute attribute=root.attribute("size");    // 属性名name    
```

### 2. 取得属性的文字      

```
String text=attribute.getText();    
String text2=root.element("name").attributeValue("firstname");
//这个是取得根节点下name字节点的firstname属性的值.      
```

### 3. 遍历某节点的所有属性      



```
Element root=document.getRootElement();
for(Iterator it=root.attributeIterator();it.hasNext();){
    Attribute attribute = (Attribute) it.next();
    String text=attribute.getText();
    System.out.println(text);
}
```



### 4. 设置某节点的属性和文字.      

```
newMemberElm.addAttribute("name", "sitinspring");
```

### 5. 设置属性的文字      

```
Attribute attribute=root.attribute("name");      
attribute.setText("sitinspring");
```

### 6. 删除某属性      

```
Attribute attribute=root.attribute("size");// 属性名name      
root.remove(attribute);     
```

2.4、将文档写入 XML 文件.     
----------------------

### 1. 文档中全为英文, 不设置编码, 直接写入.      

```
XMLWriter writer = new XMLWriter(new FileWriter("output.xml"));      
writer.write(document);      
writer.close();
```

### 2. 文档中含有中文, 设置编码格式再写入.     

```
OutputFormat format = OutputFormat.createPrettyPrint();
format.setEncoding("GBK");    // 指定XML编码
XMLWriter writer = new XMLWriter(new FileWriter("output.xml"),format);
writer.write(document);
writer.close();
```

2.5、字符串与 XML 的转换      
----------------------

### 1. 将字符串转化为 XML      

```
String text = "<members> <member>sitinspring</member> </members>";      
Document document = DocumentHelper.parseText(text);     
```

### 2. 将文档或节点的 XML 转化为字符串.     



```
SAXReader reader = new SAXReader();
Document document = reader.read(new File("input.xml"));
Element root=document.getRootElement();
String docXmlText=document.asXML();
String rootXmlText=root.asXML();
Element memberElm=root.element("member");
String memberXmlText=memberElm.asXML();      
```



3、dom4j 的事件处理模型涉及的类和接口：
=======================

3.1、类：SAXReader
---------------

     当解析到 path 指定的路径时，将调用参数 handler 指定的处理器。针对不同的节点可以添加多个 handler 实例。

　 或者调用默认的 Handler setDefaultHandler(ElementHandler handler);

3.2、接口 ElementHandler
---------------------

     该方法在解析到元素的开始标签时被调用。

     该方法在解析到元素的结束标签时被调用

3.3、接口：ElementPath （假设有参数：ElementPath path）

     该方法与 SAXReader 类中的 addHandler() 方法的作用相同。路径 path 可以是绝对路径（路径以 / 开头），也可以是相对路径（假设是当前路径的子节点路径）。

     移除指定路径上的 ElementHandler 实例。路径可以是相对路径，也可以是绝对路径。

     该方法得到当前节点的路径。该方法返回的是完整的绝对路径

     该方法得到当前节点。

3.3、Element 类
-------------

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top"><p align="left">getQName()</p></td><td valign="top"><p align="left">元素的 QName 对象</p></td></tr><tr><td valign="top"><p align="left">getNamespace()</p></td><td valign="top"><p align="left">元素所属的 Namespace 对象</p></td></tr><tr><td valign="top"><p align="left">getNamespacePrefix()</p></td><td valign="top"><p align="left">元素所属的 Namespace 对象的 prefix</p></td></tr><tr><td valign="top"><p align="left">getNamespaceURI()</p></td><td valign="top"><p align="left">元素所属的 Namespace 对象的 URI</p></td></tr><tr><td valign="top"><p align="left">getName()</p></td><td valign="top"><p align="left">元素的 local name</p></td></tr><tr><td valign="top"><p align="left">getQualifiedName()</p></td><td valign="top"><p align="left">元素的 qualified name</p></td></tr><tr><td valign="top"><p align="left">getText()</p></td><td valign="top"><p align="left">元素所含有的 text 内容，如果内容为空则返回一个空字符串而不是 null</p></td></tr><tr><td valign="top"><p align="left">getTextTrim()</p></td><td valign="top"><p align="left">元素所含有的 text 内容，其中连续的空格被转化为单个空格，该方法不会返回 null</p></td></tr><tr><td valign="top"><p align="left">attributeIterator()</p></td><td valign="top"><p align="left">元素属性的 iterator，其中每个元素都是 Attribute 对象</p></td></tr><tr><td valign="top"><p align="left">attributeValue()</p></td><td valign="top"><p align="left">元素的某个指定属性所含的值</p></td></tr><tr><td valign="top"><p align="left">elementIterator()</p></td><td valign="top"><p align="left">元素的子元素的 iterator，其中每个元素都是 Element 对象</p></td></tr><tr><td valign="top"><p align="left">element()</p></td><td valign="top"><p align="left">元素的某个指定（qualified name 或者 local name）的子元素</p></td></tr><tr><td valign="top"><p align="left">elementText()</p></td><td valign="top"><p align="left">元素的某个指定（qualified name 或者 local name）的子元素中的 text 信息</p></td></tr><tr><td valign="top"><p align="left">getParent</p></td><td valign="top"><p align="left">元素的父元素</p></td></tr><tr><td valign="top"><p align="left">getPath()</p></td><td valign="top"><p align="left">元素的 XPath 表达式，其中父元素的 qualified name 和子元素的 qualified name 之间使用 "/" 分隔</p></td></tr><tr><td valign="top"><p align="left">isTextOnly()</p></td><td valign="top"><p align="left">是否该元素只含有 text 或是空元素</p></td></tr><tr><td valign="top"><p align="left">isRootElement()</p></td><td valign="top"><p align="left">是否该元素是 XML 树的根节点</p></td></tr></tbody></table>

3.4、类 DocumentHelper 
---------------------

DocumentHelper 是用来生成生成 XML 文档的工厂类

4、通过 xpath 查找指定的节点
==================

采用 xpath 查找需要引入 jaxen-xx-xx.jar，否则会报 [java.lang.NoClassDefFoundError: org/jaxen/JaxenException](http://chinakite.javaeye.com/blog/70436) 异常。

```
List list=document.selectNodes("/books/book/@show");
```

4.1、 xpath 语法
-------------

### 1、选取节点

XPath 使用路径表达式在 XML 文档中选取节点，节点是沿着路径或者 step 来选取的。

常见的路径表达式：

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top"><p>表达式</p></td><td valign="top"><p>描述</p></td></tr><tr><td valign="top"><p>nodename</p></td><td valign="top"><p>选取当前节点的所有子节点</p></td></tr><tr><td valign="top"><p>/</p></td><td valign="top"><p>从根节点选取</p></td></tr><tr><td valign="top"><p>//</p></td><td valign="top"><p>从匹配选择的当前节点选择文档中的节点，而不考虑它们的位置</p></td></tr><tr><td valign="top"><p>.</p></td><td valign="top"><p>选取当前节点</p></td></tr><tr><td valign="top"><p>..</p></td><td valign="top"><p>选取当前节点的父节点</p></td></tr><tr><td valign="top"><p>@</p></td><td valign="top"><p>选取属性</p></td></tr></tbody></table>

实例

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top"><p>路径表达式</p></td><td valign="top"><p>结果</p></td></tr><tr><td valign="top"><p>bookstore</p></td><td valign="top"><p>选取&nbsp;bookstore&nbsp;元素的所有子节点</p></td></tr><tr><td valign="top"><p>/bookstore</p></td><td valign="top"><p>选取根元素&nbsp;bookstore</p></td></tr><tr><td valign="top"><p>bookstore/book</p></td><td valign="top"><p>选取 bookstore&nbsp;下名字为&nbsp;book 的所有子元素。</p></td></tr><tr><td valign="top"><p>//book</p></td><td valign="top"><p>选取所有&nbsp;book&nbsp;子元素，而不管它们在文档中的位置。</p></td></tr><tr><td valign="top"><p>bookstore//book</p></td><td valign="top"><p>选取 bookstore&nbsp;下名字为&nbsp;book 的所有后代元素，而不管它们位于&nbsp;bookstore&nbsp;之下的什么位置。</p></td></tr><tr><td valign="top"><p>//@lang</p></td><td valign="top"><p>选取所有名为&nbsp;lang&nbsp;的属性。</p></td></tr></tbody></table>

### 2、谓语（Predicates）

谓语用来查找某个特定的节点或者包含某个指定的值的节点。

谓语被嵌在方括号中。

实例

常见的谓语的一些路径表达式：

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top"><p>路径表达式</p></td><td valign="top"><p>结果</p></td></tr><tr><td valign="top"><p>/bookstore/book[1]</p></td><td valign="top"><p>选取属于&nbsp;bookstore&nbsp;子元素的第一个&nbsp;book&nbsp;元素。</p></td></tr><tr><td valign="top"><p>/bookstore/book[last()]</p></td><td valign="top"><p>选取属于&nbsp;bookstore&nbsp;子元素的最后一个&nbsp;book&nbsp;元素。</p></td></tr><tr><td valign="top"><p>/bookstore/book[last()-1]</p></td><td valign="top"><p>选取属于&nbsp;bookstore&nbsp;子元素的倒数第二个&nbsp;book&nbsp;元素。</p></td></tr><tr><td valign="top"><p>/bookstore/book[position()&lt;3]</p></td><td valign="top"><p>选取最前面的两个属于&nbsp;bookstore&nbsp;元素的子元素的&nbsp;book&nbsp;元素。</p></td></tr><tr><td valign="top"><p>//title[@lang]</p></td><td valign="top"><p>选取所有拥有名为&nbsp;lang&nbsp;的属性的&nbsp;title&nbsp;元素。</p></td></tr><tr><td valign="top"><p>//title[@lang='eng']</p></td><td valign="top"><p>选取所有&nbsp;title&nbsp;元素，要求这些元素拥有值为&nbsp;eng&nbsp;的&nbsp;lang&nbsp;属性。</p></td></tr><tr><td valign="top"><p>/bookstore/book[price&gt;35.00]</p></td><td valign="top"><p>选取所有&nbsp;bookstore&nbsp;元素的&nbsp;book&nbsp;元素，要求 book 元素的子元素&nbsp;price&nbsp;元素的值须大于&nbsp;35.00。</p></td></tr><tr><td valign="top"><p>/bookstore/book[price&gt;35.00]/title</p></td><td valign="top"><p>选取所有&nbsp;bookstore&nbsp;元素中的&nbsp;book&nbsp;元素的&nbsp;title&nbsp;元素，要求 book 元素的子元素&nbsp;price&nbsp;元素的值须大于&nbsp;35.00</p></td></tr></tbody></table>

### 3、选取未知节点

XPath 通配符可用来选取未知的 XML 元素。

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top"><p align="left">通配符</p></td><td valign="top"><p align="left">描述</p></td></tr><tr><td valign="top"><p align="left">*</p></td><td valign="top"><p align="left">匹配任何元素节点</p></td></tr><tr><td valign="top"><p align="left">@*</p></td><td valign="top"><p align="left">匹配任何属性节点</p></td></tr><tr><td valign="top"><p align="left">node()</p></td><td valign="top"><p align="left">匹配任何类型的节点</p></td></tr></tbody></table>

实例

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top"><p>路径表达式</p></td><td valign="top"><p>结果</p></td></tr><tr><td valign="top"><p>/bookstore/*</p></td><td valign="top"><p>选取&nbsp;bookstore&nbsp;元素的所有子节点</p></td></tr><tr><td valign="top"><p>//*</p></td><td valign="top"><p>选取文档中的所有元素</p></td></tr><tr><td valign="top"><p>//title[@*]</p></td><td valign="top"><p>选取所有带有属性的&nbsp;title&nbsp;元素。</p></td></tr></tbody></table>

### 4、选取若干路径

通过在路径表达式中使用 “|” 运算符，您可以选取若干个路径。

实例

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top" width="174"><p>路径表达式</p></td><td valign="top" width="365"><p>结果</p></td></tr><tr><td valign="top" width="174"><p>//book/title&nbsp;|&nbsp;//book/price</p></td><td valign="top" width="365"><p>选取所有&nbsp;book&nbsp;元素的&nbsp;title&nbsp;和&nbsp;price&nbsp;元素。</p></td></tr><tr><td valign="top" width="174"><p>//title&nbsp;|&nbsp;//price</p></td><td valign="top" width="365"><p>选取所有文档中的&nbsp;title&nbsp;和&nbsp;price&nbsp;元素。</p></td></tr><tr><td valign="top" width="174"><p>/bookstore/book/title|//price</p></td><td valign="top" width="365"><p>选取所有属于&nbsp;bookstore&nbsp;元素的&nbsp;book&nbsp;元素的&nbsp;title&nbsp;元素，以及文档中所有的&nbsp;price&nbsp;元素。</p></td></tr></tbody></table>

### 5、XPath 轴

轴可定义某个相对于当前节点的节点集。

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top"><p>轴名称</p></td><td valign="top"><p>结果</p></td></tr><tr><td valign="top"><p>ancestor</p></td><td valign="top"><p>选取当前节点的所有先辈（父、祖父等）</p></td></tr><tr><td valign="top"><p>ancestor-or-self</p></td><td valign="top"><p>选取当前节点的所有先辈（父、祖父等）以及当前节点本身</p></td></tr><tr><td valign="top"><p>attribute</p></td><td valign="top"><p>选取当前节点的所有属性</p></td></tr><tr><td valign="top"><p>child</p></td><td valign="top"><p>选取当前节点的所有子元素。</p></td></tr><tr><td valign="top"><p>descendant</p></td><td valign="top"><p>选取当前节点的所有后代元素（子、孙等）。</p></td></tr><tr><td valign="top"><p>descendant-or-self</p></td><td valign="top"><p>选取当前节点的所有后代元素（子、孙等）以及当前节点本身。</p></td></tr><tr><td valign="top"><p>following</p></td><td valign="top"><p>选取文档中当前节点的结束标签之后的所有节点。</p></td></tr><tr><td valign="top"><p>namespace</p></td><td valign="top"><p>选取当前节点的所有命名空间节点</p></td></tr><tr><td valign="top"><p>parent</p></td><td valign="top"><p>选取当前节点的父节点。</p></td></tr><tr><td valign="top"><p>preceding</p></td><td valign="top"><p>选取文档中当前节点的开始标签之前的所有节点。</p></td></tr><tr><td valign="top"><p>preceding-sibling</p></td><td valign="top"><p>选取当前节点之前的所有同级节点。</p></td></tr><tr><td valign="top"><p>self</p></td><td valign="top"><p>选取当前节点。</p></td></tr></tbody></table>

### 6、路径

位置路径可以是绝对的，也可以是相对的。

绝对路径起始于正斜杠 (/)，而相对路径不会这样。在两种情况中，位置路径均包括一个或多个步，每个步均被斜杠分割：

```
/step/step/...
```

```
step/step/...
```

每个步均根据当前节点集之中的节点来进行计算。

轴（axis）：定义所选节点与当前节点之间的树关系

节点测试（node-test）：识别某个轴内部的节点

零个或者更多谓语（predicate）：更深入地提炼所选的节点集

步的语法：轴名称:: 节点测试 [谓语]

实例

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top"><p>例子</p></td><td valign="top"><p>结果</p></td></tr><tr><td valign="top"><p>child::book</p></td><td valign="top"><p>选取所有属于当前节点的子元素的&nbsp;book&nbsp;节点</p></td></tr><tr><td valign="top"><p>attribute::lang</p></td><td valign="top"><p>选取当前节点的&nbsp;lang&nbsp;属性</p></td></tr><tr><td valign="top"><p>child::*</p></td><td valign="top"><p>选取当前节点的所有子元素</p></td></tr><tr><td valign="top"><p>attribute::*</p></td><td valign="top"><p>选取当前节点的所有属性</p></td></tr><tr><td valign="top"><p>child::text()</p></td><td valign="top"><p>选取当前节点的所有文本子节点</p></td></tr><tr><td valign="top"><p>child::node()</p></td><td valign="top"><p>选取当前节点的所有子节点</p></td></tr><tr><td valign="top"><p>descendant::book</p></td><td valign="top"><p>选取当前节点的所有&nbsp;book&nbsp;后代</p></td></tr><tr><td valign="top"><p>ancestor::book</p></td><td valign="top"><p>选择当前节点的所有&nbsp;book&nbsp;先辈</p></td></tr><tr><td valign="top"><p>ancestor-or-self::book</p></td><td valign="top"><p>选取当前节点的所有 book 先辈以及当前节点（假如此节点是 book 节点的话）</p></td></tr><tr><td valign="top"><p>child::*/child::price</p></td><td valign="top"><p>选取当前节点的所有&nbsp;price&nbsp;孙。</p></td></tr></tbody></table>

### 7、XPath 运算符

<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top" width="73"><p>运算符</p></td><td valign="top" width="142"><p>描述</p></td><td valign="top" width="123"><p>实例</p></td><td valign="top" width="230"><p>返回值</p></td></tr><tr><td valign="top" width="73"><p>|</p></td><td valign="top" width="142"><p>计算两个节点集</p></td><td valign="top" width="123"><p>//book | //cd</p></td><td valign="top" width="230"><p>返回所有带有&nbsp;book&nbsp;和&nbsp;ck&nbsp;元素的节点集</p></td></tr><tr><td valign="top" width="73"><p>+</p></td><td valign="top" width="142"><p>加法</p></td><td valign="top" width="123"><p>6 + 4</p></td><td valign="top" width="230"><p>10</p></td></tr><tr><td valign="top" width="73"><p>-</p></td><td valign="top" width="142"><p>减法</p></td><td valign="top" width="123"><p>6 - 4</p></td><td valign="top" width="230"><p>2</p></td></tr><tr><td valign="top" width="73"><p>*</p></td><td valign="top" width="142"><p>乘法</p></td><td valign="top" width="123"><p>6 * 4</p></td><td valign="top" width="230"><p>24</p></td></tr><tr><td valign="top" width="73"><p>div</p></td><td valign="top" width="142"><p>除法</p></td><td valign="top" width="123"><p>8 div 4</p></td><td valign="top" width="230"><p>2</p></td></tr><tr><td valign="top" width="73"><p>=</p></td><td valign="top" width="142"><p>等于</p></td><td valign="top" width="123"><p>price=9.80</p></td><td valign="top" width="230"><p>如果&nbsp;price&nbsp;是&nbsp;9.80，则返回&nbsp;true。</p><p>如果&nbsp;price&nbsp;是&nbsp;9.90，则返回&nbsp;fasle。</p></td></tr><tr><td valign="top" width="73"><p>!=</p></td><td valign="top" width="142"><p>不等于</p></td><td valign="top" width="123"><p>price!=9.80</p></td><td valign="top" width="230"><p>如果&nbsp;price&nbsp;是&nbsp;9.90，则返回&nbsp;true。</p><p>如果&nbsp;price&nbsp;是&nbsp;9.80，则返回&nbsp;fasle。</p></td></tr><tr><td valign="top" width="73"><p>&lt;&nbsp;</p></td><td valign="top" width="142"><p>小于</p></td><td valign="top" width="123"><p>price&lt;9.80</p></td><td valign="top" width="230"><p>如果&nbsp;price&nbsp;是&nbsp;9.00，则返回&nbsp;true。</p><p>如果&nbsp;price&nbsp;是&nbsp;9.90，则返回&nbsp;fasle。</p></td></tr><tr><td valign="top" width="73"><p>&lt;=</p></td><td valign="top" width="142"><p>小于或等于</p></td><td valign="top" width="123"><p>price&lt;=9.80</p></td><td valign="top" width="230"><p>如果&nbsp;price&nbsp;是&nbsp;9.00，则返回&nbsp;true。</p><p>如果&nbsp;price&nbsp;是&nbsp;9.90，则返回&nbsp;fasle。</p></td></tr><tr><td valign="top" width="73"><p>&gt;&nbsp;</p></td><td valign="top" width="142"><p>大于</p></td><td valign="top" width="123"><p>price&gt;9.80</p></td><td valign="top" width="230"><p>如果&nbsp;price&nbsp;是&nbsp;9.90，则返回&nbsp;true。</p><p>如果&nbsp;price&nbsp;是&nbsp;9.80，则返回&nbsp;fasle。</p></td></tr><tr><td valign="top" width="73"><p>&gt;=</p></td><td valign="top" width="142"><p>大于或等于</p></td><td valign="top" width="123"><p>price&gt;=9.80</p></td><td valign="top" width="230"><p>如果&nbsp;price&nbsp;是&nbsp;9.90，则返回&nbsp;true。</p><p>如果&nbsp;price&nbsp;是&nbsp;9.70，则返回&nbsp;fasle。</p></td></tr><tr><td valign="top" width="73"><p>or</p></td><td valign="top" width="142"><p>或</p></td><td valign="top" width="123"><p>price=9.80 or price=9.70</p></td><td valign="top" width="230"><p>如果&nbsp;price&nbsp;是&nbsp;9.80，则返回&nbsp;true。</p><p>如果&nbsp;price&nbsp;是&nbsp;9.50，则返回&nbsp;fasle。</p></td></tr><tr><td valign="top" width="73"><p>and</p></td><td valign="top" width="142"><p>与</p></td><td valign="top" width="123"><p>price&gt;9.00 and price&lt;9.90</p></td><td valign="top" width="230"><p>如果&nbsp;price&nbsp;是&nbsp;9.80，则返回&nbsp;true。</p><p>如果&nbsp;price&nbsp;是&nbsp;8.50，则返回&nbsp;fasle。</p></td></tr><tr><td valign="top" width="73"><p>mod</p></td><td valign="top" width="142"><p>计算除法的余数</p></td><td valign="top" width="123"><p>5 mod 2</p></td><td valign="top" width="230"><p>1</p></td></tr></tbody></table>