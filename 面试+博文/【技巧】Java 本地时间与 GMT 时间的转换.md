1. 把本地时间转换成 GMT 时间

```java
Date d=new Date();
DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
format.setTimeZone(TimeZone.getTimeZone("GMT"));
System.out.println(format.format(d));
```

输出结果：

```java
DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
format.setTimeZone(TimeZone.getTimeZone("GMT"));
System.out.println(format.parse("Mon, 03 Jun 2013 07:01:29 GMT").toString());
```

注意：`北京时区 = GMT+8`

2. 把字符串形式的 GMT 时间转化为本地时间

```java
DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
format.setTimeZone(TimeZone.getTimeZone("GMT"));
System.out.println(format.parse("Mon, 03 Jun 2013 07:01:29 GMT").toString());
```