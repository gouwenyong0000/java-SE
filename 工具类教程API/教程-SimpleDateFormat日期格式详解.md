> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.cnblogs.com](https://www.cnblogs.com/yyy-blog/p/10564710.html)

**要点导航**

[TOC]



### 前言

java 中使用 SimpleDateFormat 类的构造函数 SimpleDateFormat(String str) 构造格式化日期的格式, 通过 format(Date date) 方法将指定的日期对象格式化为指定格式的字符串.



### 实战样例

#### 中文

```java
public static void main(String args[]) {
    //设置时间格式
    SimpleDateFormat sdf1 = new SimpleDateFormat("y-M-d h:m:s a E");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yy-MM-dd hh:mm:ss a E");
    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MMM-ddd hhh:mmm:sss a E");
    SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyy-MMMM-dddd hhhh:mmmm:ssss a E");
    SimpleDateFormat sdf5 = new SimpleDateFormat("y年M月d日 晚上h时m分s秒 E");

    //获取的时间，是本机的时间
    String formatDate1 = sdf1.format(new Date());
    String formatDate2 = sdf2.format(new Date());
    String formatDate3 = sdf3.format(new Date());
    String formatDate4 = sdf4.format(new Date());
    String formatDate5 = sdf5.format(new Date());

    System.out.println(formatDate1);
    System.out.println(formatDate2);
    System.out.println(formatDate3);
    System.out.println(formatDate4);
    System.out.println(formatDate5);
}
```

**结果**

```
2019-3-20 2:23:20 下午 星期三  
19-03-20 02:23:20 下午 星期三  
2019 - 三月 - 020 002:023:020 下午 星期三  
02019 - 三月 - 0020 0002:0023:0020 下午 星期三  
2019 年 3 月 20 日 晚上 2 时 23 分 20 秒 星期三
```



#### 英文

```java
SimpleDateFormat sdf1 = new SimpleDateFormat("y-M-d h:m:s a E", Locale.ENGLISH);
SimpleDateFormat sdf2 = new SimpleDateFormat("yy-MM-dd hh:mm:ss a E", Locale.ENGLISH);
SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MMM-ddd hhh:mmm:sss a E", Locale.ENGLISH);
SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyy-MMMM-dddd hhhh:mmmm:ssss a E", Locale.ENGLISH);
SimpleDateFormat sdf5 = new SimpleDateFormat("y年M月d日 晚上h时m分s秒 E", Locale.ENGLISH);

-------------------------------
设置english区域后格式

2023-7-28 1:43:27 AM Fri
23-07-28 01:43:27 AM Fri
2023-Jul-028 001:043:027 AM Fri
02023-July-0028 0001:0043:0027 AM Fri
2023年7月28日 晚上1时43分27秒 Fri
```



### 24/12 小时制设置

```java
SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//12小时制  

SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
```



### 格式详解

字符串 "yyyy-MM-dd hh:mm:ss", 其中:

**yyyy : 代表年 (不去区分大小写) 假设年份为 2017**

    "y" , "yyy" , "yyyy" 匹配的都是 4 位完整的年 如 : "2017"

    "yy" 匹配的是年分的后两位 如 : "15"

    超过 4 位, 会在年份前面加 "0" 补位 如 "YYYYY" 对应 "02017"

**MM : 代表月 (只能使用大写) 假设月份为 9**

    "M" 对应 "9"

    "MM" 对应 "09"

    "MMM" 对应 "Sep"

    "MMMM" 对应 "Sep"

    超出 3 位, 仍然对应 "September"

**dd : 代表日 (只能使用小写) 假设为 13 号**

    "d" , "dd" 都对应 "13"

    超出 2 位, 会在数字前面加 "0" 补位. 例如 "dddd" 对应 "0013"

**hh : 代表时 (区分大小写, 大写为 24 进制计时, 小写为 12 进制计时) 假设为 15 时**

    "H" , "HH" 都对应 "15" , 超出 2 位, 会在数字前面加 "0" 补位. 例如 "HHHH" 对应 "0015"

    "h" 对应 "3"

    "hh" 对应 "03" , 超出 2 位, 会在数字前面加 "0" 补位. 例如 "hhhh" 对应 "0003"

**mm : 代表分 (只能使用小写) 假设为 32 分**

    "m" , "mm" 都对应 "32" ,  超出 2 位, 会在数字前面加 "0" 补位. 例如 "mmmm" 对应 "0032"

**ss : 代表秒 (只能使用小写) 假设为 15 秒**

    "s" , "ss" 都对应 "15" , 超出 2 位, 会在数字前面加 "0" 补位. 例如 "ssss" 对应 "0015"

**E : 代表星期 (只能使用大写) 假设为 Sunday**

    "E" , "EE" , "EEE" 都对应 "Sun"

    "EEEE" 对应 "Sunday" , 超出 4 位 , 仍然对应 "Sunday"

**a : 代表上午还是下午, 如果是上午就对应 "AM" , 如果是下午就对应 "PM"**

注：其中的分隔符 "-" 可以替换成其他非字母的任意字符 (也可以是汉字), 例如: