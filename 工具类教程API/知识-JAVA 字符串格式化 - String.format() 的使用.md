`String.format` 是一个在多种编程语言中都非常有用的方法，尤其是在 Java 中，它被用来创建格式化的字符串。简单来说，它允许你通过一个**格式字符串**和一个或多个**参数**来生成一个新的字符串，其中格式字符串中可以包含占位符，这些占位符会被参数替换，并且可以根据特定的格式进行格式化。

以下是对 `String.format` 的详细解释：

### 1. 作用和目的

`String.format` 的主要作用是：

- **格式化输出**:  将不同类型的数据（如数字、字符串、日期等）按照指定的格式转换为字符串。
- **提高可读性**:  相比于使用字符串连接符（`+`），`String.format` 可以使代码更简洁、更易读，尤其是在处理复杂格式的字符串时。
- **本地化支持**:  在一些语言中，`String.format` 可以支持本地化设置，例如根据不同的地区显示不同的日期、时间或货币格式。`String.format(Locale.US, "%x", 15);`

### 2. 语法

`String.format` 的基本语法通常如下：

```Java
String formattedString = String.format(format, arguments);
```

- **`format` (格式字符串)**:  一个字符串，包含了固定文本和**格式说明符**。格式说明符以百分号 (`%`) 开头，用于指示参数应该如何格式化并插入到字符串中。
- **`arguments` (参数)**:  一个或多个对象，它们的值将替换格式字符串中的格式说明符。参数的数量和类型应与格式字符串中的格式说明符相匹配。

### 3. 格式说明符 (Format Specifiers)

格式说明符是 `String.format` 的核心，它定义了参数如何被格式化。一个格式说明符通常具有以下结构：

```
%[flags][width][.precision]conversion-character
```

让我们分解一下各个部分：

- **`%` (百分号)**:  格式说明符的起始标志，必须以 `%` 开头。

- **`[flags]` (标志)** (可选):  用于修改输出格式，例如对齐方式、正负号显示、填充字符等。常见的标志包括：

  - `-` (减号):  左对齐 (默认是右对齐)。
  - `+` (加号):  强制显示正号 (`+`) 用于正数。
  - `0` (零):  用零填充空白区域 (用于数字)。
  - `,` (逗号):  数字分组分隔符 (例如千位分隔符)。
  - ` ` (空格):  在正数之前添加空格。

- **`[width]` (宽度)** (可选):  指定输出的最小宽度。如果输出的字符数少于宽度，则会进行填充。

- **`[.precision]` (精度)** (可选):  精度取决于转换类型：

  - 对于浮点数 (`f`, `e`, `g`)，精度指定小数点后的位数。
  - 对于字符串 (`s`)，精度指定字符串的最大长度。如果字符串超过精度，则会被截断。

- **`conversion-character` (转换字符)** (必需):  指定参数的数据类型以及如何格式化。一些常见的转换字符包括：

  | 转换字符 | 数据类型     | 描述                                         | 示例                                           |
  | :------- | :----------- | :------------------------------------------- | :--------------------------------------------- |
  | %s       | 字符串       | 字符串                                       | String.format("姓名: %s", "张三")              |
  | %d       | 整数         | 十进制整数                                   | String.format("年龄: %d", 30)                  |
  | %f       | 浮点数       | 浮点数                                       | String.format("身高: %.2f", 1.75)              |
  | %e 或 %E | 浮点数       | 指数形式的浮点数                             | String.format("科学计数法: %e", 1234.56)       |
  | %g 或 %G | 浮点数       | 通用浮点格式 (根据数值大小自动选择 %f 或 %e) | String.format("通用浮点: %g", 0.000123)        |
  | %b       | 布尔值       | 布尔值 (true 或 false)                       | String.format("是否成年: %b", true)            |
  | %c       | 字符         | Unicode 字符                                 | String.format("字符: %c", 'A')                 |
  | %t 或 %T | 日期/时间    | 日期和时间格式 (需要配合其他字符使用)        | String.format("日期: %tY-%tm-%td", new Date()) |
  | %%       | 无参数       | 输出百分号 %                                 | String.format("百分比: %%50")                  |
  | %n       | 无参数       | 换行符 (平台相关的换行符)                    | String.format("第一行%n第二行")                |
  | %h 或 %H | 任何数据类型 | 输出参数的哈希码 (十六进制)                  | String.format("哈希码: %h", "hello")           |

**日期/时间格式说明符 (%t 或 %T 结合其他字符):**

| 字符   | 描述                  | 示例 (假设日期是 2025年2月11日) |
| :----- | :-------------------- | :------------------------------ |
| Y      | 四位年份              | 2025                            |
| y      | 两位年份              | 25                              |
| m      | 月份 (数字)           | 02                              |
| B      | 月份 (完整名称)       | February                        |
| b 或 h | 月份 (缩写名称)       | Feb                             |
| d      | 日期 (两位数字)       | 11                              |
| e      | 日期 (一位或两位数字) | 11                              |
| A      | 星期几 (完整名称)     | Tuesday                         |
| a      | 星期几 (缩写名称)     | Tue                             |
| H      | 小时 (24小时制)       | 03                              |
| k      | 小时 (24小时制, 0-23) | 3                               |
| I      | 小时 (12小时制)       | 03                              |
| l      | 小时 (12小时制, 1-12) | 3                               |
| M      | 分钟                  | 32                              |
| S      | 秒                    | 44                              |
| p      | 上午/下午 (AM/PM)     | AM                              |
| z      | 时区偏移              | +0800                           |
| Z      | 时区名称              | CST                             |
| s      | 从纪元开始的秒数      | 1739225564                      |
| Q      | 从纪元开始的毫秒数    | 1739225564000                   |

### 4. 示例

以下是一些 `String.format` 的使用示例，展示了不同的格式说明符和标志：

**基本类型格式化:**

```Java
int age = 30;
String name = "李华";
double height = 1.75;

String info = String.format("姓名: %s, 年龄: %d, 身高: %.2f米", name, age, height);
System.out.println(info); // 输出: 姓名: 李华, 年龄: 30, 身高: 1.75米
```

**宽度和对齐:**



```Java
String formatWidth = String.format("|%10s|%-10s|", "右对齐", "左对齐");
System.out.println(formatWidth); // 输出: |     右对齐|左对齐     |

String formatNumberWidth = String.format("|%5d|", 123);
System.out.println(formatNumberWidth); // 输出: |  123| (右对齐，宽度为5)

String formatZeroPadding = String.format("|%05d|", 123);
System.out.println(formatZeroPadding); // 输出: |00123| (用零填充)
```

**精度:**

```Java
double pi = 3.1415926;
String formatPrecision = String.format("%.3f", pi);
System.out.println(formatPrecision); // 输出: 3.142 (保留三位小数)

String formatStringPrecision = String.format("%.5s", "Hello World");
System.out.println(formatStringPrecision); // 输出: Hello (字符串截取前5个字符)
```

**日期和时间:**

```Java
import java.util.Date;

Date now = new Date();
String formatDate = String.format("%tY年%tm月%td日 %tA", now, now, now, now);
System.out.println(formatDate); // 输出: 2025年02月11日 星期二 (日期会根据当前时间变化)

String formatTime = String.format("%tH:%tM:%tS", now, now, now);
System.out.println(formatTime); // 输出: 03:32:44 (时间会根据当前时间变化)

String formatDateTime = String.format("%tc", now);
System.out.println(formatDateTime); // 输出: Tue Feb 11 03:32:44 CST 2025 (完整日期时间)
```

**多次引用同一个参数**

`String.format` 允许你在格式字符串中多次引用同一个参数。这可以通过**索引说明符**来实现。索引说明符允许你指定要格式化的参数的位置。

在 `String.format` 中，索引说明符的语法是 `"%<index>$<conversion>"`，其中 `<index>` 是参数的索引（从 1 开始），而 `<conversion>` 是转换字符（例如 `%s`, `%d`, `%f` 等）。

**示例：**

假设我们有一个名字 "小明"，我们想在格式化字符串中多次使用它。

Java

```
String name = "小明";
String message = String.format("欢迎您，%1$s！您好，%1$s！再次欢迎，%1$s！", name);
System.out.println(message);
```

**其他标志:**

```Java
int positiveNumber = 123;
int negativeNumber = -123;

String formatPlusSign = String.format("%+d %+d", positiveNumber, negativeNumber);
System.out.println(formatPlusSign); // 输出: +123 -123 (正数显示正号)

String formatComma = String.format("%,d", 1234567);
System.out.println(formatComma); // 输出: 1,234,567 (千位分隔符)
```

### 5. 总结

`String.format` 是一个强大且灵活的字符串格式化工具，它可以帮助开发者：

- **生成结构化的字符串**:  通过预定义的格式字符串，可以轻松地将数据插入到指定位置。
- **控制输出格式**:  使用格式说明符，可以精确控制数字、字符串、日期等的显示方式，包括对齐、宽度、精度、日期时间格式等等。
- **提高代码可维护性**:  使用 `String.format` 可以将格式化的逻辑与数据分离，使代码更清晰易懂，更易于维护。

掌握 `String.format` 可以显著提高字符串处理的效率和代码质量。在需要生成复杂格式字符串的场景中，例如日志记录、报表生成、用户界面显示等，`String.format` 都是一个非常有用的工具。

**常规类型的格式化**
------------

String 类的 format() 方法用于创建格式化的字符串以及连接多个字符串对象。熟悉 C 语言的同学应该记得 C 语言的 sprintf() 方法，两者有类似之处。format() 方法有两种重载形式。

format(String format, Object... args) 新字符串使用本地语言环境，制定字符串格式和参数生成格式化的新字符串。

format(Locale locale, String format, Object... args) 使用指定的语言环境，制定字符串格式和参数生成格式化的字符串。

显示不同转换符实现不同数据类型到字符串的转换，如图所示

<table><tbody><tr><td><p>转&nbsp; 换&nbsp; 符</p></td><td><p>说&nbsp;&nbsp;&nbsp; 明&nbsp;</p></td><td><p>示&nbsp;&nbsp;&nbsp; 例</p></td></tr><tr><td><p>%s</p></td><td><p>字符串类型</p></td><td><p>"mingrisoft"</p></td></tr><tr><td><p>%c</p></td><td><p>字符类型</p></td><td><p>'m'</p></td></tr><tr><td><p>%b</p></td><td><p>布尔类型</p></td><td><p>true</p></td></tr><tr><td><p>%d</p></td><td><p>整数类型（十进制）</p></td><td><p>99</p></td></tr><tr><td><p>%x</p></td><td><p>整数类型（十六进制）</p></td><td><p>FF</p></td></tr><tr><td><p>%o</p></td><td><p>整数类型（八进制）</p></td><td><p>77</p></td></tr><tr><td><p>%f</p></td><td><p>浮点类型</p></td><td><p>99.99</p></td></tr><tr><td><p>%a</p></td><td><p>十六进制浮点类型</p></td><td><p>FF.35AE</p></td></tr><tr><td><p>%e</p></td><td><p>指数类型</p></td><td><p>9.38e+5</p></td></tr><tr><td><p>%g</p></td><td><p>通用浮点类型（f 和 e 类型中较短的）</p></td><td>&nbsp;</td></tr><tr><td><p>%h</p></td><td><p>散列码</p></td><td>&nbsp;</td></tr><tr><td><p>%%</p></td><td><p>百分比类型</p></td><td><p>％</p></td></tr><tr><td><p>%n</p></td><td><p>换行符</p></td><td>&nbsp;</td></tr><tr><td><p>%tx</p></td><td><p>日期与时间类型（x 代表不同的日期与时间转换符</p></td></tr></tbody></table>



```
public static void main(String[] args) {  
    String str=null;  
    str=String.format("Hi,%s", "王力");  
    System.out.println(str);  
    str=String.format("Hi,%s:%s.%s", "王南","王力","王张");            
    System.out.println(str);                           
    System.out.printf("字母a的大写是：%c %n", 'A');  
    System.out.printf("3>7的结果是：%b %n", 3>7);  
    System.out.printf("100的一半是：%d %n", 100/2);  
    System.out.printf("100的16进制数是：%x %n", 100);  
    System.out.printf("100的8进制数是：%o %n", 100);  
    System.out.printf("50元的书打8.5折扣是：%f 元%n", 50*0.85);  
    System.out.printf("上面价格的16进制数是：%a %n", 50*0.85);  
    System.out.printf("上面价格的指数表示：%e %n", 50*0.85);  
    System.out.printf("上面价格的指数和浮点数结果的长度较短的是：%g %n", 50*0.85);  
    System.out.printf("上面的折扣是%d%% %n", 85);  
    System.out.printf("字母A的散列码是：%h %n", 'A');  
}
```



输出结果：



```
Hi,王力  
Hi,王南:王力.王张  
字母a的大写是：A   
3>7的结果是：false   
100的一半是：50   
100的16进制数是：64   
100的8进制数是：144   
50元的书打8.5折扣是：42.500000 元  
上面价格的16进制数是：0x1.54p5   
上面价格的指数表示：4.250000e+01   
上面价格的指数和浮点数结果的长度较短的是：42.5000   
上面的折扣是85%   
字母A的散列码是：41
```



搭配转换符的标志
--------

如图所示：

<table><tbody><tr><td><p>标&nbsp;&nbsp;&nbsp; 志</p></td><td><p>说&nbsp;&nbsp;&nbsp; 明</p></td><td><p>示&nbsp;&nbsp;&nbsp; 例</p></td><td><p>结&nbsp;&nbsp;&nbsp; 果</p></td></tr><tr><td><p>+</p></td><td><p>为正数或者负数添加符号</p></td><td><p>("%+d",15)</p></td><td><p>+15</p></td></tr><tr><td><p>−</p></td><td><p>左对齐</p></td><td><p>("%-5d",15)</p></td><td><p>|15&nbsp;&nbsp; |</p></td></tr><tr><td><p>0</p></td><td><p>数字前面补 0</p></td><td><p>("%04d", 99)</p></td><td><p>0099</p></td></tr><tr><td><p>空格</p></td><td><p>在整数之前添加指定数量的空格</p></td><td><p>("% 4d", 99)</p></td><td><p>|&nbsp; 99|</p></td></tr><tr><td><p>,</p></td><td><p>以 “,” 对数字分组</p></td><td><p>("%,f", 9999.99)</p></td><td><p>9,999.990000</p></td></tr><tr><td><p>(</p></td><td><p>使用括号包含负数</p></td><td><p>("%(f", -99.99)</p></td><td><p>(99.990000)</p></td></tr><tr><td><p>#</p></td><td><p>如果是浮点数则包含小数点，如果是 16 进制或 8 进制则添加 0x 或 0</p></td><td><p>("%#x", 99)</p><p>("%#o", 99)</p></td><td><p>0x63</p><p>0143</p></td></tr><tr><td><p>&lt;&nbsp;</p></td><td><p>格式化前一个转换符所描述的参数</p></td><td><p>("%f 和 %&lt;3.2f", 99.45)</p></td><td><p>99.450000 和 99.45</p></td></tr><tr><td><p>$</p></td><td><p>被格式化的参数索引</p></td><td><p>("%1$d,%2$s", 99,"abc")</p></td><td><p>99,abc</p></td></tr></tbody></table>



```
public static void main(String[] args) {  
    String str=null;  
    //$使用  
    str=String.format("格式参数$的使用：%1$d,%2$s", 99,"abc");             
    System.out.println(str);                       
    //+使用  
    System.out.printf("显示正负数的符号：%+d与%d%n", 99,-99);  
    //补O使用  
    System.out.printf("最牛的编号是：%03d%n", 7);  
    //空格使用  
    System.out.printf("Tab键的效果是：% 8d%n", 7);  
    //.使用  
    System.out.printf("整数分组的效果是：%,d%n", 9989997);  
    //空格和小数点后面个数  
    System.out.printf("一本书的价格是：% 50.5f元%n", 49.8);  
}
```



输出结果



```
格式参数$的使用：99,abc  
显示正负数的符号：+99与-99  
最牛的编号是：007  
Tab键的效果是：       7  
整数分组的效果是：9,989,997  
一本书的价格是：                                          49.80000元
```



**日期和事件字符串格式化**
---------------

在程序界面中经常需要显示时间和日期，但是其显示的 格式经常不尽人意，需要编写大量的代码经过各种算法才得到理想的日期与时间格式。字符串格式中还有 %tx 转换符没有详细介绍，它是专门用来格式化日期和时 间的。%tx 转换符中的 x 代表另外的处理日期和时间格式的转换符，它们的组合能够将日期和时间格式化成多种格式。

常见日期和时间组合的格式，如图所示。

<table><tbody><tr><td><p>转&nbsp; 换&nbsp; 符</p></td><td><p>说&nbsp;&nbsp;&nbsp; 明</p></td><td><p>示&nbsp;&nbsp;&nbsp; 例</p></td></tr><tr><td><p>c</p></td><td><p>包括全部日期和时间信息</p></td><td><p>星期六 十月 27 14:21:20 CST 2007</p></td></tr><tr><td><p>F</p></td><td><p>“年 - 月 - 日” 格式</p></td><td><p>2007-10-27</p></td></tr><tr><td><p>D</p></td><td><p>“月 / 日 / 年” 格式</p></td><td><p>10/27/07</p></td></tr><tr><td><p>r</p></td><td><p>“HH:MM:SS PM” 格式（12 时制）</p></td><td><p>02:25:51 下午</p></td></tr><tr><td><p>T</p></td><td><p>“HH:MM:SS” 格式（24 时制）</p></td><td><p>14:28:16</p></td></tr><tr><td><p>R</p></td><td><p>“HH:MM” 格式（24 时制）</p></td><td><p>14:28</p></td></tr></tbody></table>



```
public static void main(String[] args) {  
    Date date=new Date();                                  
    //c的使用  
    System.out.printf("全部日期和时间信息：%tc%n",date);          
    //f的使用  
    System.out.printf("年-月-日格式：%tF%n",date);  
    //d的使用  
    System.out.printf("月/日/年格式：%tD%n",date);  
    //r的使用  
    System.out.printf("HH:MM:SS PM格式（12时制）：%tr%n",date);  
    //t的使用  
    System.out.printf("HH:MM:SS格式（24时制）：%tT%n",date);  
    //R的使用  
    System.out.printf("HH:MM格式（24时制）：%tR",date);  
}
```



输出结果：



```
全部日期和时间信息：星期一 九月 10 10:43:36 CST 2012  
年-月-日格式：2012-09-10  
月/日/年格式：09/10/12  
HH:MM:SS PM格式（12时制）：10:43:36 上午  
HH:MM:SS格式（24时制）：10:43:36  
HH:MM格式（24时制）：10:43
```



定义日期格式的转换符可以使日期通过指定的转换符生成新字符串。这些日期转换符如图所示。



```
public static void main(String[] args) {  
    Date date=new Date();                                      
    //b的使用，月份简称  
    String str=String.format(Locale.US,"英文月份简称：%tb",date);       
    System.out.println(str);                                                                              
    System.out.printf("本地月份简称：%tb%n",date);  
    //B的使用，月份全称  
    str=String.format(Locale.US,"英文月份全称：%tB",date);  
    System.out.println(str);  
    System.out.printf("本地月份全称：%tB%n",date);  
    //a的使用，星期简称  
    str=String.format(Locale.US,"英文星期的简称：%ta",date);  
    System.out.println(str);  
    //A的使用，星期全称  
    System.out.printf("本地星期的简称：%tA%n",date);  
    //C的使用，年前两位  
    System.out.printf("年的前两位数字（不足两位前面补0）：%tC%n",date);  
    //y的使用，年后两位  
    System.out.printf("年的后两位数字（不足两位前面补0）：%ty%n",date);  
    //j的使用，一年的天数  
    System.out.printf("一年中的天数（即年的第几天）：%tj%n",date);  
    //m的使用，月份  
    System.out.printf("两位数字的月份（不足两位前面补0）：%tm%n",date);  
    //d的使用，日（二位，不够补零）  
    System.out.printf("两位数字的日（不足两位前面补0）：%td%n",date);  
    //e的使用，日（一位不补零）  
    System.out.printf("月份的日（前面不补0）：%te",date);
```



输出结果



```
英文月份简称：Sep  
本地月份简称：九月  
英文月份全称：September  
本地月份全称：九月  
英文星期的简称：Mon  
本地星期的简称：星期一  
年的前两位数字（不足两位前面补0）：20  
年的后两位数字（不足两位前面补0）：12  
一年中的天数（即年的第几天）：254  
两位数字的月份（不足两位前面补0）：09  
两位数字的日（不足两位前面补0）：10  
月份的日（前面不补0）：10
```



和日期格式转换符相比，时间格式的转换符要更多、更精确。它可以将时间格式化成时、分、秒甚至时毫秒等单位。格式化时间字符串的转换符如图所示。

<table><tbody><tr><td><p>转&nbsp; 换&nbsp; 符</p></td><td><p>说&nbsp;&nbsp;&nbsp; 明</p></td><td><p>示&nbsp;&nbsp;&nbsp; 例</p></td></tr><tr><td><p>H</p></td><td><p>2 位数字 24 时制的小时（不足 2 位前面补 0）</p></td><td><p>15</p></td></tr><tr><td><p>I</p></td><td><p>2 位数字 12 时制的小时（不足 2 位前面补 0）</p></td><td><p>03</p></td></tr><tr><td><p>k</p></td><td><p>2 位数字 24 时制的小时（前面不补 0）</p></td><td><p>15</p></td></tr><tr><td><p>l</p></td><td><p>2 位数字 12 时制的小时（前面不补 0）</p></td><td><p>3</p></td></tr><tr><td><p>M</p></td><td><p>2 位数字的分钟（不足 2 位前面补 0）</p></td><td><p>03</p></td></tr><tr><td><p>S</p></td><td><p>2 位数字的秒（不足 2 位前面补 0）</p></td><td><p>09</p></td></tr><tr><td><p>L</p></td><td><p>3 位数字的毫秒（不足 3 位前面补 0）</p></td><td><p>015</p></td></tr><tr><td><p>N</p></td><td><p>9 位数字的毫秒数（不足 9 位前面补 0）</p></td><td><p>562000000</p></td></tr><tr><td><p>p</p></td><td><p>小写字母的上午或下午标记</p></td><td><p>中：下午</p><p>英：pm</p></td></tr><tr><td><p>z</p></td><td><p>相对于 GMT 的 RFC822 时区的偏移量</p></td><td><p>+0800</p></td></tr><tr><td><p>Z</p></td><td><p>时区缩写字符串</p></td><td><p>CST</p></td></tr></tbody></table>

<table><tbody><tr><td><p>s</p></td><td><p>1970-1-1 00:00:00 到现在所经过的秒数</p></td><td><p>1193468128</p></td></tr><tr><td><p>Q</p></td><td><p>1970-1-1 00:00:00 到现在所经过的毫秒数</p></td><td><p>1193468128984</p></td></tr></tbody></table>

 



```
public static void main(String[] args) {  
    Date date = new Date();  
    //H的使用  
    System.out.printf("2位数字24时制的小时（不足2位前面补0）:%tH%n", date);  
    //I的使用  
    System.out.printf("2位数字12时制的小时（不足2位前面补0）:%tI%n", date);  
    //k的使用  
    System.out.printf("2位数字24时制的小时（前面不补0）:%tk%n", date);  
    //l的使用  
    System.out.printf("2位数字12时制的小时（前面不补0）:%tl%n", date);  
    //M的使用  
    System.out.printf("2位数字的分钟（不足2位前面补0）:%tM%n", date);  
    //S的使用  
    System.out.printf("2位数字的秒（不足2位前面补0）:%tS%n", date);  
    //L的使用  
    System.out.printf("3位数字的毫秒（不足3位前面补0）:%tL%n", date);  
    //N的使用  
    System.out.printf("9位数字的毫秒数（不足9位前面补0）:%tN%n", date);  
    //p的使用  
    String str = String.format(Locale.US, "小写字母的上午或下午标记(英)：%tp", date);  
    System.out.println(str);   
    System.out.printf("小写字母的上午或下午标记（中）：%tp%n", date);  
    //z的使用  
    System.out.printf("相对于GMT的RFC822时区的偏移量:%tz%n", date);  
    //Z的使用  
    System.out.printf("时区缩写字符串:%tZ%n", date);  
    //s的使用  
    System.out.printf("1970-1-1 00:00:00 到现在所经过的秒数：%ts%n", date);  
    //Q的使用  
    System.out.printf("1970-1-1 00:00:00 到现在所经过的毫秒数：%tQ%n", date);  
}
```



输出结果



```
2位数字24时制的小时（不足2位前面补0）:11  
2位数字12时制的小时（不足2位前面补0）:11  
2位数字24时制的小时（前面不补0）:11  
2位数字12时制的小时（前面不补0）:11  
2位数字的分钟（不足2位前面补0）:03  
2位数字的秒（不足2位前面补0）:52  
3位数字的毫秒（不足3位前面补0）:773  
9位数字的毫秒数（不足9位前面补0）:773000000  
小写字母的上午或下午标记(英)：am  
小写字母的上午或下午标记（中）：上午  
相对于GMT的RFC822时区的偏移量:+0800  
时区缩写字符串:CST  
1970-1-1 00:00:00 到现在所经过的秒数：1347246232  
1970-1-1 00:00:00 到现在所经过的毫秒数：1347246232773
```

