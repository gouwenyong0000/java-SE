# Cassandra 如何使用Netty

## 是什么

![image-20240703002822717](images/11、开源源码如何使用netty/image-20240703002822717.png)![image-20240703002843448](images/11、开源源码如何使用netty/image-20240703002843448.png)



## Cassandra 传输数据结构

![image-20240703002907110](images/11、开源源码如何使用netty/image-20240703002907110.png)

## Cassandra 使用Netty 概况

![image-20240703003018531](images/11、开源源码如何使用netty/image-20240703003018531.png)

![image-20240703003043352](images/11、开源源码如何使用netty/image-20240703003043352.png)

![image-20240703003058509](images/11、开源源码如何使用netty/image-20240703003058509.png)

## Cassandra 中使用Netty 的一些技巧

![image-20240703003149908](images/11、开源源码如何使用netty/image-20240703003149908.png)

![image-20240703003214294](images/11、开源源码如何使用netty/image-20240703003214294.png)

![image-20240703003249238](images/11、开源源码如何使用netty/image-20240703003249238.png)

![image-20240703003345929](images/11、开源源码如何使用netty/image-20240703003345929.png)

![image-20240703003410782](images/11、开源源码如何使用netty/image-20240703003410782.png)





# Dubbo 如何使用Netty？

## Dubbo 是什么？

![image-20240703004625530](images/11、开源源码如何使用netty/image-20240703004625530.png)

## Dubbo 传输数据结构

![image-20240703004644102](images/11、开源源码如何使用netty/image-20240703004644102.png)

## Dubbo 使用Netty 概况

### 总览

![image-20240703004655155](images/11、开源源码如何使用netty/image-20240703004655155.png)

### Pipeline

![image-20240703004706243](images/11、开源源码如何使用netty/image-20240703004706243.png)

### 线程模型

![image-20240703004714431](images/11、开源源码如何使用netty/image-20240703004714431.png)

![image-20240703004731649](images/11、开源源码如何使用netty/image-20240703004731649.png)

## Dubbo 使用Netty 的一些技巧

![image-20240703004740133](images/11、开源源码如何使用netty/image-20240703004740133.png)

![image-20240703004751652](images/11、开源源码如何使用netty/image-20240703004751652.png)

![image-20240703004808917](images/11、开源源码如何使用netty/image-20240703004808917.png)

![image-20240703004822423](images/11、开源源码如何使用netty/image-20240703004822423.png)

![image-20240703004842690](images/11、开源源码如何使用netty/image-20240703004842690.png)

![image-20240703004850066](images/11、开源源码如何使用netty/image-20240703004850066.png)

![image-20240703004902715](images/11、开源源码如何使用netty/image-20240703004902715.png)

![image-20240703004909693](images/11、开源源码如何使用netty/image-20240703004909693.png)

# Hadoop 如何使用Netty？

## Hadoop 是什么？

![image-20240703014433350](images/11、开源源码如何使用netty/image-20240703014433350.png)

## Hadoop 使用Netty 概况

![image-20240703014444765](images/11、开源源码如何使用netty/image-20240703014444765.png)

## Hadoop 如何使用Netty 做http 服务器

![image-20240703014456155](images/11、开源源码如何使用netty/image-20240703014456155.png)

![image-20240703014506272](images/11、开源源码如何使用netty/image-20240703014506272.png)

![image-20240703014516670](images/11、开源源码如何使用netty/image-20240703014516670.png)

![image-20240703014524613](images/11、开源源码如何使用netty/image-20240703014524613.png)

![image-20240703014533883](images/11、开源源码如何使用netty/image-20240703014533883.png)



![image-20240703014544653](images/11、开源源码如何使用netty/image-20240703014544653.png)

![image-20240703014554389](images/11、开源源码如何使用netty/image-20240703014554389.png)

![image-20240703014604786](images/11、开源源码如何使用netty/image-20240703014604786.png)



# 小结：Casssandra/Dubbo/Hadoop 对Netty 使用

![image-20240703014612541](images/11、开源源码如何使用netty/image-20240703014612541.png)



# 贡献代码的七个起点

![image-20240704015441899](images/11、开源源码如何使用netty/image-20240704015441899.png)

1、来源于项目真实需求，解决实际问题

![image-20240704015513840](images/11、开源源码如何使用netty/image-20240704015513840.png)

2、使用测试时发现的问题

![image-20240704015537592](images/11、开源源码如何使用netty/image-20240704015537592.png)





# 代码贡献准则

1、风格统一

![image-20240704020049644](images/11、开源源码如何使用netty/image-20240704020049644.png)

![image-20240704020101222](images/11、开源源码如何使用netty/image-20240704020101222.png)

2、全局观，改动要从全局分析影响

![image-20240704020128335](images/11、开源源码如何使用netty/image-20240704020128335.png)

3、易于使用，注释明确，方便使用

![image-20240704020211012](images/11、开源源码如何使用netty/image-20240704020211012.png)

4、兼容性：打破用户的行为

![image-20240704020253927](images/11、开源源码如何使用netty/image-20240704020253927.png)

5、证明它：单元测试、性能测试报告

![image-20240704020306008](images/11、开源源码如何使用netty/image-20240704020306008.png)



6、小步前进，尽量小而完整，单次功能提交不超过400行

![image-20240704020323477](images/11、开源源码如何使用netty/image-20240704020323477.png)

7、Pr commit规范  包括动机 、修改 、结果等几部分，多提交合一

![image-20240704020518027](images/11、开源源码如何使用netty/image-20240704020518027.png)

# 进阶学习资料

![image-20240704015005433](images/11、开源源码如何使用netty/image-20240704015005433.png)

3、难用 ，提升易用性

![image-20240704015745937](images/11、开源源码如何使用netty/image-20240704015745937.png)

4、源码阅读

![image-20240704015755416](images/11、开源源码如何使用netty/image-20240704015755416.png)

5、解决issues，解决功能性问题

![image-20240704015811153](images/11、开源源码如何使用netty/image-20240704015811153.png)

6、解决todo、fix等遗留问题

![image-20240704015846185](images/11、开源源码如何使用netty/image-20240704015846185.png)

7、按问题分类解决

![image-20240704015911482](images/11、开源源码如何使用netty/image-20240704015911482.png)