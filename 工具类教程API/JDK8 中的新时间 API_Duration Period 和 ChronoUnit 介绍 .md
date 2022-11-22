> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [cloud.tencent.com](https://cloud.tencent.com/developer/article/1746121)

> 在 JDK8 中，引入了三个非常有用的时间相关的 API：Duration，Period 和 ChronoUnit。

在 JDK8 中，引入了三个非常有用的时间相关的 API：Duration，Period 和 ChronoUnit。

他们都是用来对时间进行统计的，本文将会详细讲解一下这三个 API 的使用。

# Duration

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

# Period 

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

# ChronoUnit 

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