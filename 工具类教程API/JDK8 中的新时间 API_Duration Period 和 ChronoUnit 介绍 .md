# 时间差计算

最近工作中遇到需要计算时间差，搜索了几种计算时间差的方法，这里总结一下

1、java 7 中的日历类 [Calendar](https://so.csdn.net/so/search?q=Calendar&spm=1001.2101.3001.7020)
-------------------------------------------------------------------------------------------

Calendar 类使用其静态的 getInstance() 方法获取一个日历实例，该实例为当前的时间；如果想改变时间，可以通过其 setTime 方法传入一个 Date 对象，即可获得 Date 对象所表示时间的 Calendar 对象

```java
/**
 *使用Calendar对象计算时间差，可以按照需求定制自己的计算逻辑
 * @param strDate
 * @throws ParseException
 */
public static void calculateTimeDifferenceByCalendar(String strDate) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    Date date = formatter.parse(strDate);

    Calendar c1 = Calendar.getInstance();   //当前日期
    Calendar c2 = Calendar.getInstance();
    c2.setTime(date);   //设置为另一个时间

    int year = c1.get(Calendar.YEAR);
    int oldYear = c2.get(Calendar.YEAR);

    //这里只是简单的对两个年份数字进行相减，而没有考虑月份的情况
    System.out.println("传入的日期与今年的年份差为：" + (year - oldYear));
}
```

输出为：

```
传入的日期与今年的年份差为：12
```

2、java 8 中的周期类 Period
---------------------

通过调用 Period 类的静态方法 between，传入两个待比较的 LocalDate 对象 today 与 oldDate，得到的 Period 的对象 p 中就包含了 today 与 oldDate 两个日期相差的年、月、日信息，可以通过 p.getYears() 等方法取出

```java
/**
 * 使用java 8的Period的对象计算两个LocalDate对象的时间差，严格按照年、月、日计算，如：2018-03-12 与 2014-05-23 相差 3 年 9 个月 17 天
 * @param year
 * @param month
 * @param dayOfMonth
 */
public static void calculateTimeDifferenceByPeriod(int year, Month month, int dayOfMonth) {
    LocalDate today = LocalDate.now();
    System.out.println("Today：" + today);
    LocalDate oldDate = LocalDate.of(year, month, dayOfMonth);
    System.out.println("OldDate：" + oldDate);

    Period p = Period.between(oldDate, today);
    System.out.printf("目标日期距离今天的时间差：%d 年 %d 个月 %d 天\n", p.getYears(), p.getMonths(), p.getDays());
}
```

输出为：

```
Today：2018-03-13
OldDate：2014-05-23
目标日期距离今天的时间差：3 年 9 个月 18 天
```

3、java 8 中的 Duration 类
----------------------

Duration 与 Period 相对应，Period 用于处理日期，而 Duration 计算时间差还可以处理具体的时间，也是通过调用其静态的 between 方法，该方法的签名是 between(Temporal startInclusive, Temporal endExclusive)，因此可以传入两个 Instant 的实例（Instant 实现了 Temporal 接口），并可以以毫秒（toMillis）、秒（getSeconds）等多种形式表示得到的时间差

```java
public static void calculateTimeDifferenceByDuration() {
    Instant inst1 = Instant.now();  //当前的时间
    System.out.println("Inst1：" + inst1);
    Instant inst2 = inst1.plus(Duration.ofSeconds(10));     //当前时间+10秒后的时间
    System.out.println("Inst2：" + inst2);
    Instant inst3 = inst1.plus(Duration.ofDays(125));       //当前时间+125天后的时间
    System.out.println("inst3：" + inst3);

    System.out.println("以毫秒计的时间差：" + Duration.between(inst1, inst2).toMillis());

    System.out.println("以秒计的时间差：" + Duration.between(inst1, inst3).getSeconds());
}
```

输出为：

```
Inst1：2018-03-13T09:06:00.691Z
Inst2：2018-03-13T09:06:10.691Z
inst3：2018-07-16T09:06:00.691Z
以毫秒计的时间差：10000
以秒计的时间差：10800000
```

4、java 8 中的 ChronoUnit 类
------------------------

ChronoUnit 的 between 方法签名为，between(Temporal temporal1Inclusive, Temporal temporal2Exclusive)，需要注意的是，如果要以不同的单位展示时间差，between 入参中的时间对象必须包含有对应的时间信息，否则会抛出 java.time.temporal.UnsupportedTemporalTypeException: Unsupported unit XXX 的异常

```java
/**
 * 使用java 8的ChronoUnit，ChronoUnit可以以多种单位（基本涵盖了所有的，看源码发现竟然还有“FOREVER”这种单位。。）表示两个时间的时间差
 */
public static void calculateTimeDifferenceByChronoUnit() {
    LocalDate startDate = LocalDate.of(2003, Month.MAY, 9);
    System.out.println("开始时间：" + startDate);

    LocalDate endDate = LocalDate.of(2015, Month.JANUARY, 26);
    System.out.println("结束时间：" + endDate);

    long daysDiff = ChronoUnit.DAYS.between(startDate, endDate);
    System.out.println("两个时间之间的天数差为：" + daysDiff);
//  long hoursDiff = ChronoUnit.HOURS.between(startDate, endDate);  //这句会抛出异常，因为LocalDate表示的时间中不包含时分秒等信息
}
```

输出为：

```
开始时间：2003-05-09
结束时间：2015-01-26
两个时间之间的天数差为：4280
```

5、传统的 SimpleDateFormat 类
------------------------

用 SimpleDateFormat 计算时间差的方法，网上找了一份，自己跑了一遍，可以使用，贴在下面

```java
/**
* 用SimpleDateFormat计算时间差
* @throws ParseException 
*/
public static void calculateTimeDifferenceBySimpleDateFormat() throws ParseException {
    SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    /*天数差*/
    Date fromDate1 = simpleFormat.parse("2018-03-01 12:00");  
    Date toDate1 = simpleFormat.parse("2018-03-12 12:00");  
    long from1 = fromDate1.getTime();  
    long to1 = toDate1.getTime();  
    int days = (int) ((to1 - from1) / (1000 * 60 * 60 * 24));  
    System.out.println("两个时间之间的天数差为：" + days);

    /*小时差*/
    Date fromDate2 = simpleFormat.parse("2018-03-01 12:00");  
    Date toDate2 = simpleFormat.parse("2018-03-12 12:00");  
    long from2 = fromDate2.getTime();  
    long to2 = toDate2.getTime();  
    int hours = (int) ((to2 - from2) / (1000 * 60 * 60));
    System.out.println("两个时间之间的小时差为：" + hours);

    /*分钟差*/
    Date fromDate3 = simpleFormat.parse("2018-03-01 12:00");  
    Date toDate3 = simpleFormat.parse("2018-03-12 12:00");  
    long from3 = fromDate3.getTime();  
    long to3 = toDate3.getTime();  
    int minutes = (int) ((to3 - from3) / (1000 * 60));  
    System.out.println("两个时间之间的分钟差为：" + minutes);
}
```

输出为：

```
两个时间之间的天数差为：11
两个时间之间的小时差为：264
两个时间之间的分钟差为：15840
```

总结
--

传统的 SimpleDateFormat 和 Java 7 中的 Calendar 在使用的时候需要自己写一个计算时间差的逻辑，比较麻烦，但是却比较灵活，方便根据自己具体的需求来定制（比如，我想两个日期的天数差 15 天就算满一个月，不满 15 天不算一个月，如 2018-01-04 到 2018-02-20，算 2 个月的时间差）；而 Java 8 中的几个计算时间差的类更加方便、精确，可以以不同的单位表示得到的时间差，但要注意几个类所包含的时间信息的区别：

```java
System.out.println(LocalDate.now());        //只包含日期信息
System.out.println(LocalTime.now());        //只包含时间信息
System.out.println(LocalDateTime.now());        //包含日期、时间信息
```

输出为：

```
2018-03-13
17:13:26.134
2018-03-13T17:13:26.135
```

以上总结的几个方法只是个例子，具体使用的时候可能需要传入一个或者两个时间进行比较，可能会涉及到这些时间对象的相互转换，Instant、Date、LocalDate 等等。。。我就不列举了。。。  
另外在使用 SimpleDateFormat 对 String 类型的日期进行 parse 的时候，如果传入的日期为：2017-08-60，这种错误的日期，Java 默认会按照日期的信息对其进行转换，`formatter.parse("2017-08-60");`，得到的日期为 2017-09-29，而如果不想进行这种转换，而直接将其判定为输入错误，则可以设置`formatter.setLenient(false);`，这时就会抛出 java.text.ParseException 异常了

```java
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
formatter.setLenient(false);
try {
    Date date = formatter.parse("2017-08-60");  //抛出转换异常
    System.out.println(date);
} catch (ParseException e) {
    e.printStackTrace();
}
```



# 基础介绍

在 JDK8 中，引入了三个非常有用的时间相关的 API：Duration，Period 和 ChronoUnit。

他们都是用来对时间进行统计的，本文将会详细讲解一下这三个 API 的使用。

## Duration

Duration 主要用来衡量秒级和纳秒级的时间，使用于时间精度要求比较高的情况。

先来看看 Duration 的定义：

```java
public final class Duration
        implements TemporalAmount, Comparable<Duration>, Serializable
```

可以看到，Duration 是一个 final class，并且它是可序列化和可比较的。我们注意，Duration 还实现了 TemporalAmount 接口。

那么 TemporalAmount 接口是什么呢？

TemporalAmount 是 Duration 和 Period 的父接口。

![](https://ask.qcloudimg.com/http-save/7774611/2m5nrwh09m.png)

它定义了 4 个必须要实现的方法：

```java
long get(TemporalUnit unit);
List<TemporalUnit> getUnits();
Temporal addTo(Temporal temporal);
Temporal subtractFrom(Temporal temporal);
```

其中 TemporalUnit 代表的是时间对象的单位，比如：years, months, days, hours, minutes 和 seconds. 而 Temporal 代表的是对时间对象的读写操作。

我们看下 Duration 的一些基本操作：

```java
Instant start = Instant.parse("2020-08-03T10:15:30.00Z");
        Instant end = Instant.parse("2020-08-03T10:16:30.12Z");
        Duration duration = Duration.between(start, end);
        log.info("{}",duration.getSeconds());
        log.info("{}",duration.getNano());
        log.info("{}",duration.getUnits());
```

上面我们创建了两个 Instant，然后使用 Duration.between 方法来测算他们之间的差异。

其中秒部分的差异，使用 duration.getSeconds() 来获取，而秒以下精度部分的差异，我们使用 duration.getNano() 来获取。

最后我们使用 duration.getUnits() 来看一下 duration 支持的 TemporalUnit（时间单位）。

看下执行结果：

```java
INFO com.flydean.time - 60
 INFO com.flydean.time - 120000000
 INFO com.flydean.time - [Seconds, Nanos]
```

除了 Instance，我们还可以使用 LocalTime：

```java
LocalTime start2 = LocalTime.of(1, 20, 25, 1314);
        LocalTime end2 = LocalTime.of(3, 22, 27, 1516);
        Duration.between(start2, end2).getSeconds();
```

我们还可以对 Duration 做 plus 和 minus 操作，并且通过使用 isNegative 来判断两个时间的先后顺序：

```java
duration.plusSeconds(60);
duration.minus(30, ChronoUnit.SECONDS);
log.info("{}",duration.isNegative());
```

除此之外，我们方便的使用 Duration.of 方法来方便的创建 Duration：

```java
Duration fromDays = Duration.ofDays(1);
Duration fromMinutes = Duration.ofMinutes(60);
```

## Period 

Period 的单位是 year, month 和 day 。

操作基本上和 Duration 是一致的。

先看下定义：

```java
public final class Period
        implements ChronoPeriod, Serializable
```

其中 ChronoPeriod 是 TemporalAmount 的子接口。

同样的，我们可以使用 Period.between 从 LocalDate 来构建 Period：

```java
LocalDate startDate = LocalDate.of(2020, 2, 20);
        LocalDate endDate = LocalDate.of(2021, 1, 15);

        Period period = Period.between(startDate, endDate);
        log.info("{}",period.getDays());
        log.info("{}",period.getMonths());
        log.info("{}",period.getYears());
```

也可以直接从 Period.of 来构建：

```java
Period fromUnits = Period.of(3, 10, 10);
        Period fromDays = Period.ofDays(50);
        Period fromMonths = Period.ofMonths(5);
        Period fromYears = Period.ofYears(10);
        Period fromWeeks = Period.ofWeeks(40);
```

最后我们还可以使用 plus 或者 minus 的操作：

```java
period.plusDays(50);
period.minusMonths(2);
```

## ChronoUnit 

ChronoUnit 是用来表示时间单位的，但是也提供了一些非常有用的 between 方法来计算两个时间的差值：

```java
LocalDate startDate = LocalDate.of(2020, 2, 20);
        LocalDate endDate = LocalDate.of(2021, 1, 15);
        long years = ChronoUnit.YEARS.between(startDate, endDate);
        long months = ChronoUnit.MONTHS.between(startDate, endDate);
        long weeks = ChronoUnit.WEEKS.between(startDate, endDate);
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        long hours = ChronoUnit.HOURS.between(startDate, endDate);
        long minutes = ChronoUnit.MINUTES.between(startDate, endDate);
        long seconds = ChronoUnit.SECONDS.between(startDate, endDate);
        long milis = ChronoUnit.MILLIS.between(startDate, endDate);
        long nano = ChronoUnit.NANOS.between(startDate, endDate);
```





