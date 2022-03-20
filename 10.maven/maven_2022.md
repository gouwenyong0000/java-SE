# 第一章 Maven概述

## 第一节 为什么要学习Maven？

## [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse01.html#_1、maven-作为依赖管理工具)1、Maven 作为依赖管理工具

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse01.html#_1jar-包的规模)①jar 包的规模

随着我们使用越来越多的框架，或者框架封装程度越来越高，项目中使用的jar包也越来越多。项目中，一个模块里面用到上百个jar包是非常正常的。



比如下面的例子，我们只用到 SpringBoot、SpringCloud 框架中的三个功能：

- Nacos 服务注册发现
- Web 框架环境
- 图模板技术 Thymeleaf



最终却导入了 106 个 jar 包：

> org.springframework.security:spring-security-rsa:jar:1.0.9.RELEASE:compile
> com.netflix.ribbon: ribbon:jar:2.3.0:compile
> org.springframework.boot:spring-boot-starter-thymeleaf:jar:2.3.6.RELEASE:compile
> commons-configuration:commons-configuration:jar:1.8:compile
> org.apache.logging.log4j:log4j-api:jar:2.13.3:compile
> org.springframework:spring-beans:jar:5.2.11.RELEASE:compile
> org.springframework.cloud:spring-cloud-starter-netflix-ribbon:jar:2.2.6.RELEASE:compile
> org.apache.tomcat.embed:tomcat-embed-websocket:jar:9.0.39:compile
> com.alibaba.cloud:spring-cloud-alibaba-commons:jar:2.2.6.RELEASE:compile
> org.bouncycastle:bcprov-jdk15on:jar:1.64:compile
> org.springframework.security:spring-security-crypto:jar:5.3.5.RELEASE:compile
> org.apache.httpcomponents:httpasyncclient:jar:4.1.4:compile
> com.google.j2objc:j2objc-annotations:jar:1.3:compile
> com.fasterxml.jackson.core:jackson-databind:jar:2.11.3:compile
> io.reactivex:rxjava:jar:1.3.8:compile
> ch.qos.logback:logback-classic:jar:1.2.3:compile
> org.springframework:spring-web:jar:5.2.11.RELEASE:compile
> io.reactivex:rxnetty-servo:jar:0.4.9:runtime
> org.springframework:spring-core:jar:5.2.11.RELEASE:compile
> io.github.openfeign.form:feign-form-spring:jar:3.8.0:compile
> io.github.openfeign.form:feign-form:jar:3.8.0:compile
> com.netflix.ribbon:ribbon-loadbalancer:jar:2.3.0:compile
> org.apache.httpcomponents:httpcore:jar:4.4.13:compile
> org.thymeleaf.extras:thymeleaf-extras-java8time:jar:3.0.4.RELEASE:compile
> org.slf4j:jul-to-slf4j:jar:1.7.30:compile
> com.atguigu.demo:demo09-base-entity:jar:1.0-SNAPSHOT:compile
> org.yaml:snakeyaml:jar:1.26:compile
> org.springframework.boot:spring-boot-starter-logging:jar:2.3.6.RELEASE:compile
> io.reactivex:rxnetty-contexts:jar:0.4.9:runtime
> org.apache.httpcomponents:httpclient:jar:4.5.13:compile
> io.github.openfeign:feign-core:jar:10.10.1:compile
> org.springframework.boot:spring-boot-starter-aop:jar:2.3.6.RELEASE:compile
> org.hdrhistogram:HdrHistogram:jar:2.1.9:compile
> org.springframework:spring-context:jar:5.2.11.RELEASE:compile
> commons-lang:commons-lang:jar:2.6:compile
> io.prometheus:simpleclient:jar:0.5.0:compile
> ch.qos.logback:logback-core:jar:1.2.3:compile
> org.springframework:spring-webmvc:jar:5.2.11.RELEASE:compile
> com.sun.jersey:jersey-core:jar:1.19.1:runtime
> javax.ws.rs:jsr311-api:jar:1.1.1:runtime
> javax.inject:javax.inject:jar:1:runtime
> org.springframework.cloud:spring-cloud-openfeign-core:jar:2.2.6.RELEASE:compile
> com.netflix.ribbon:ribbon-core:jar:2.3.0:compile
> com.netflix.hystrix:hystrix-core:jar:1.5.18:compile
> com.netflix.ribbon:ribbon-transport:jar:2.3.0:runtime
> org.springframework.boot:spring-boot-starter-json:jar:2.3.6.RELEASE:compile
> org.springframework.cloud:spring-cloud-starter-openfeign:jar:2.2.6.RELEASE:compile
> com.fasterxml.jackson.module:jackson-module-parameter-names:jar:2.11.3:compile
> com.sun.jersey.contribs:jersey-apache-client4:jar:1.19.1:runtime
> io.github.openfeign:feign-hystrix:jar:10.10.1:compile
> io.github.openfeign:feign-slf4j:jar:10.10.1:compile
> com.alibaba.nacos:nacos-client:jar:1.4.2:compile
> org.apache.httpcomponents:httpcore-nio:jar:4.4.13:compile
> com.sun.jersey:jersey-client:jar:1.19.1:runtime
> org.springframework.cloud:spring-cloud-context:jar:2.2.6.RELEASE:compile
> org.glassfish:jakarta.el:jar:3.0.3:compile
> org.apache.logging.log4j:log4j-to-slf4j:jar:2.13.3:compile
> com.fasterxml.jackson.datatype:jackson-datatype-jsr310:jar:2.11.3:compile
> org.springframework.cloud:spring-cloud-commons:jar:2.2.6.RELEASE:compile
> org.aspectj:aspectjweaver:jar:1.9.6:compile
> com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:jar:2.2.6.RELEASE:compile
> com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile
> com.alibaba.spring:spring-context-support:jar:1.0.10:compile
> jakarta.annotation:jakarta.annotation-api:jar:1.3.5:compile
> org.bouncycastle:bcpkix-jdk15on:jar:1.64:compile
> com.netflix.netflix-commons:netflix-commons-util:jar:0.3.0:runtime
> com.fasterxml.jackson.core:jackson-annotations:jar:2.11.3:compile
> com.google.guava:guava:jar:29.0-jre:compile
> com.google.guava:failureaccess:jar:1.0.1:compile
> org.springframework.boot:spring-boot:jar:2.3.6.RELEASE:compile
> com.fasterxml.jackson.datatype:jackson-datatype-jdk8:jar:2.11.3:compile
> com.atguigu.demo:demo08-base-api:jar:1.0-SNAPSHOT:compile
> org.springframework.cloud:spring-cloud-starter-netflix-archaius:jar:2.2.6.RELEASE:compile
> org.springframework.boot:spring-boot-autoconfigure:jar:2.3.6.RELEASE:compile
> org.slf4j:slf4j-api:jar:1.7.30:compile
> commons-io:commons-io:jar:2.7:compile
> org.springframework.cloud:spring-cloud-starter:jar:2.2.6.RELEASE:compile
> org.apache.tomcat.embed:tomcat-embed-core:jar:9.0.39:compile
> io.reactivex:rxnetty:jar:0.4.9:runtime
> com.fasterxml.jackson.core:jackson-core:jar:2.11.3:compile
> com.google.code.findbugs:jsr305:jar:3.0.2:compile
> com.netflix.archaius:archaius-core:jar:0.7.6:compile
> org.springframework.boot:spring-boot-starter-web:jar:2.3.6.RELEASE:compile
> commons-codec:commons-codec:jar:1.14:compile
> com.netflix.servo:servo-core:jar:0.12.21:runtime
> com.google.errorprone:error_prone_annotations:jar:2.3.4:compile
> org.attoparser:attoparser:jar:2.0.5.RELEASE:compile
> com.atguigu.demo:demo10-base-util:jar:1.0-SNAPSHOT:compile
> org.checkerframework:checker-qual:jar:2.11.1:compile
> org.thymeleaf:thymeleaf-spring5:jar:3.0.11.RELEASE:compile
> commons-fileupload:commons-fileupload:jar:1.4:compile
> com.netflix.ribbon:ribbon-httpclient:jar:2.3.0:compile
> com.netflix.netflix-commons:netflix-statistics:jar:0.1.1:runtime
> org.unbescape:unbescape:jar:1.1.6.RELEASE:compile
> org.springframework:spring-jcl:jar:5.2.11.RELEASE:compile
> com.alibaba.nacos:nacos-common:jar:1.4.2:compile
> commons-collections:commons-collections:jar:3.2.2:runtime
> javax.persistence:persistence-api:jar:1.0:compile
> com.alibaba.nacos:nacos-api:jar:1.4.2:compile
> org.thymeleaf:thymeleaf:jar:3.0.11.RELEASE:compile
> org.springframework:spring-aop:jar:5.2.11.RELEASE:compile
> org.springframework.boot:spring-boot-starter:jar:2.3.6.RELEASE:compile
> org.springframework.boot:spring-boot-starter-tomcat:jar:2.3.6.RELEASE:compile
> org.springframework.cloud:spring-cloud-netflix-ribbon:jar:2.2.6.RELEASE:compile
> org.springframework:spring-expression:jar:5.2.11.RELEASE:compile
> org.springframework.cloud:spring-cloud-netflix-archaius:jar:2.2.6.RELEASE:compile

而如果使用 Maven 来引入这些 jar 包只需要配置三个『**依赖**』：

```xml
    <!-- Nacos 服务注册发现启动器 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>

    <!-- web启动器依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- 视图模板技术 thymeleaf -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
```

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse01.html#_2jar-包的来源)②jar 包的来源

- 这个jar包所属技术的官网。官网通常是英文界面，网站的结构又不尽相同，甚至找到下载链接还发现需要通过特殊的工具下载。
- 第三方网站提供下载。问题是不规范，在使用过程中会出现各种问题。
  - jar包的名称
  - jar包的版本
  - jar包内的具体细节
- 而使用 Maven 后，依赖对应的 jar 包能够**自动下载**，方便、快捷又规范。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse01.html#_3jar-包之间的依赖关系)③jar 包之间的依赖关系

框架中使用的 jar 包，不仅数量庞大，而且彼此之间存在错综复杂的依赖关系。依赖关系的复杂程度，已经上升到了完全不能靠人力手动解决的程度。另外，jar 包之间有可能产生冲突。进一步增加了我们在 jar 包使用过程中的难度。

下面是前面例子中 jar 包之间的依赖关系：

![images](maven_2022.assets/img006.ab4f2e31.png)

而实际上 jar 包之间的依赖关系是普遍存在的，如果要由程序员手动梳理无疑会增加极高的学习成本，而这些工作又对实现业务功能毫无帮助。

而使用 Maven 则几乎不需要管理这些关系，极个别的地方调整一下即可，极大的减轻了我们的工作量。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse01.html#_2、maven-作为构建管理工具)2、Maven 作为构建管理工具

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse01.html#_1你没有注意过的构建)①你没有注意过的构建

你可以不使用 Maven，但是构建必须要做。当我们使用 IDEA 进行开发时，构建是 IDEA 替我们做的。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse01.html#_2脱离-ide-环境仍需构建)②脱离 IDE 环境仍需构建

![images](maven_2022.assets/img010.74e515e5.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse01.html#_3、结论)3、结论

- **管理规模庞大的 jar 包，需要专门工具。**
- **脱离 IDE 环境执行构建操作，需要专门工具。**

## 第二节 什么是 Maven？

Maven 是 Apache 软件基金会组织维护的一款专门为 Java 项目提供**构建**和**依赖**管理支持的工具。

![./images](maven_2022.assets/images_maven_ico.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse02.html#_1、构建)1、构建

Java 项目开发过程中，构建指的是使用**『原材料生产产品』**的过程。

- 原材料

  - Java 源代码

  - 基于 HTML 的 Thymeleaf 文件

  - 图片

  - 配置文件

  - ###### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse02.html#)……

- 产品

  - 一个可以在服务器上运行的项目

**构建过程包含的主要的环节：**

- 清理：删除上一次构建的结果，为下一次构建做好准备
- 编译：Java 源程序编译成 *.class 字节码文件
- 测试：运行提前准备好的测试程序
- 报告：针对刚才测试的结果生成一个全面的信息
- 打包
  - Java工程：jar包
  - Web工程：war包
- 安装：把一个 Maven 工程经过打包操作生成的 jar 包或 war 包存入 Maven 仓库
- 部署
  - 部署 jar 包：把一个 jar 包部署到 Nexus 私服服务器上
  - 部署 war 包：借助相关 Maven 插件（例如 cargo），将 war 包部署到 Tomcat 服务器上

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse02.html#_2、依赖)2、依赖

如果 A 工程里面用到了 B 工程的类、接口、配置文件等等这样的资源，那么我们就可以说 A 依赖 B。例如：

- junit-4.12 依赖 hamcrest-core-1.3
- thymeleaf-3.0.12.RELEASE 依赖 ognl-3.1.26
  - ognl-3.1.26 依赖 javassist-3.20.0-GA
- thymeleaf-3.0.12.RELEASE 依赖 attoparser-2.0.5.RELEASE
- thymeleaf-3.0.12.RELEASE 依赖 unbescape-1.1.6.RELEASE
- thymeleaf-3.0.12.RELEASE 依赖 slf4j-api-1.7.26



依赖管理中要解决的具体问题：

- jar 包的下载：使用 Maven 之后，jar 包会从规范的远程仓库下载到本地
- jar 包之间的依赖：通过依赖的传递性自动完成
- jar 包之间的冲突：通过对依赖的配置进行调整，让某些jar包不会被导入

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter01/verse02.html#_3、maven-的工作机制)3、Maven 的工作机制

![./images](maven_2022.assets/img003.f9cc536c.png)





# 第二章 Maven 核心程序解压和配置

## 第一节 Maven核心程序解压与配置

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter02/verse01.html#_1、maven-官网地址)1、Maven 官网地址

首页：

[Maven – Welcome to Apache Maven(opens new window)](https://maven.apache.org/)

下载页面：

[Maven – Download Apache Maven(opens new window)](https://maven.apache.org/download.cgi)

下载链接：

![images](maven_2022.assets/img015.9ab3ebd3.png)

具体下载地址：https://dlcdn.apache.org/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.zip

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter02/verse01.html#_2、解压maven核心程序)2、解压Maven核心程序

核心程序压缩包：apache-maven-3.8.4-bin.zip，解压到**非中文、没有空格**的目录。例如：

![images](maven_2022.assets/imagesfsfss.png)

在解压目录中，我们需要着重关注 Maven 的核心配置文件：**conf/settings.xml**

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter02/verse01.html#_3、指定本地仓库)3、指定本地仓库

本地仓库默认值：用户家目录/.m2/repository。由于本地仓库的默认位置是在用户的家目录下，而家目录往往是在 C 盘，也就是系统盘。将来 Maven 仓库中 jar 包越来越多，仓库体积越来越大，可能会拖慢 C 盘运行速度，影响系统性能。所以建议将 Maven 的本地仓库放在其他盘符下。配置方式如下：

```xml
<!-- localRepository
| The path to the local repository maven will use to store artifacts.
|
| Default: ${user.home}/.m2/repository
<localRepository>/path/to/local/repo</localRepository>
-->
<localRepository>D:\maven-repository</localRepository>
```

本地仓库这个目录，我们手动创建一个空的目录即可。

**记住**：一定要把 localRepository 标签**从注释中拿出来**。

**注意**：本地仓库本身也需要使用一个**非中文、没有空格**的目录。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter02/verse01.html#_4、配置阿里云提供的镜像仓库)4、配置阿里云提供的镜像仓库

Maven 下载 jar 包默认访问境外的中央仓库，而国外网站速度很慢。改成阿里云提供的镜像仓库，**访问国内网站**，可以让 Maven 下载 jar 包的时候速度更快。配置的方式是：

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter02/verse01.html#_1将原有的例子配置注释掉)①将原有的例子配置注释掉

```xml
<!-- <mirror>
  <id>maven-default-http-blocker</id>
  <mirrorOf>external:http:*</mirrorOf>
  <name>Pseudo repository to mirror external repositories initially using HTTP.</name>
  <url>http://0.0.0.0/</url>
  <blocked>true</blocked>
</mirror> -->
```

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter02/verse01.html#_2加入我们的配置)②加入我们的配置

将下面 mirror 标签整体复制到 settings.xml 文件的 mirrors 标签的内部。

```xml
	<mirror>
		<id>nexus-aliyun</id>
		<mirrorOf>central</mirrorOf>
		<name>Nexus aliyun</name>
		<url>http://maven.aliyun.com/nexus/content/groups/public</url>
	</mirror>
```

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter02/verse01.html#_5、配置-maven-工程的基础-jdk-版本)5、配置 Maven 工程的基础 JDK 版本

如果按照默认配置运行，Java 工程使用的默认 JDK 版本是 1.5，而我们熟悉和常用的是 JDK 1.8 版本。修改配置的方式是：将 profile 标签整个复制到 settings.xml 文件的 profiles 标签内。

```xml
	<profile>
	  <id>jdk-1.8</id>
	  <activation>
		<activeByDefault>true</activeByDefault>
		<jdk>1.8</jdk>
	  </activation>
	  <properties>
		<maven.compiler.source>1.8</maven.compiler.source>
		<maven.compiler.target>1.8</maven.compiler.target>
		<maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
	  </properties>
	</profile>
```

## 第二节 配置环境变量

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter02/verse02.html#_1、检查-java-home-配置是否正确)1、检查 JAVA_HOME 配置是否正确

Maven 是一个用 Java 语言开发的程序，它必须基于 JDK 来运行，需要通过 JAVA_HOME 来找到 JDK 的安装位置。

![./images](maven_2022.assets/imagespath.png)

可以使用下面的命令验证：

```shell
C:\Users\Administrator>echo %JAVA_HOME%
D:\software\Java

C:\Users\Administrator>java -version
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, mixed mode)
```

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter02/verse02.html#_2、配置-maven-home)2、配置 MAVEN_HOME

![./images](maven_2022.assets/imagespathmav.png)

> TIP
>
> 配置环境变量的规律：
>
> ​	XXX_HOME 通常指向的是 bin 目录的上一级
>
> ​	PATH 指向的是 bin 目录



### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter02/verse02.html#_3、配置path)3、配置PATH

![./images](maven_2022.assets/imagennns.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter02/verse02.html#_4、验证)4、验证

```bash
C:\Users\Administrator>mvn -v
Apache Maven 3.8.4 (9b656c72d54e5bacbed989b64718c159fe39b537)
Maven home: D:\software\apache-maven-3.8.4
Java version: 1.8.0_141, vendor: Oracle Corporation, runtime: D:\software\Java\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

# 第三章 使用 Maven：命令行环境

## 第一节 实验一：根据坐标创建 Maven 工程

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_1、maven-核心概念-坐标)1、Maven 核心概念：坐标

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_1数学中的坐标)①数学中的坐标

![./images](maven_2022.assets/imagespos.png)

使用 x、y、z 三个**『向量』**作为空间的坐标系，可以在**『空间』**中唯一的定位到一个**『点』**。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_2maven中的坐标)②Maven中的坐标

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_1-向量说明)[1]向量说明

使用三个**『向量』**在**『Maven的仓库』**中**唯一**的定位到一个**『jar』**包。

- **groupId**：公司或组织的 id
- **artifactId**：一个项目或者是项目中的一个模块的 id
- **version**：版本号

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_2-三个向量的取值方式)[2]三个向量的取值方式

- groupId：公司或组织域名的倒序，通常也会加上项目名称
  - 例如：com.atguigu.maven
- artifactId：模块的名称，将来作为 Maven 工程的工程名
- version：模块的版本号，根据自己的需要设定
  - 例如：SNAPSHOT 表示快照版本，正在迭代过程中，不稳定的版本
  - 例如：RELEASE 表示正式版本

举例：

- groupId：com.atguigu.maven
- artifactId：pro01-atguigu-maven
- version：1.0-SNAPSHOT

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_3坐标和仓库中-jar-包的存储路径之间的对应关系)③坐标和仓库中 jar 包的存储路径之间的对应关系

坐标：

```xml
  <groupId>javax.servlet</groupId>
  <artifactId>servlet-api</artifactId>
  <version>2.5</version>
```

上面坐标对应的 jar 包在 Maven 本地仓库中的位置：

```text
Maven本地仓库根目录\javax\servlet\servlet-api\2.5\servlet-api-2.5.jar
```

一定要学会根据坐标到本地仓库中找到对应的 jar 包。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_2、实验操作)2、实验操作

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_1创建目录作为后面操作的工作空间)①创建目录作为后面操作的工作空间

例如：`D:\maven-workspace\space201026`

WARNING

此时我们已经有了三个目录，分别是：

- Maven 核心程序：中军大帐
- Maven 本地仓库：兵营
- 本地工作空间：战场

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_2在工作空间目录下打开命令行窗口)②在工作空间目录下打开命令行窗口

![./images](maven_2022.assets/img010.7f3addf6.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_3使用命令生成maven工程)③使用命令生成Maven工程

![images](maven_2022.assets/img008.be45c9ad.png)

运行 **`mvn archetype:generate`** 命令

下面根据提示操作

> TIP
>
> Choose a number or apply filter (format: [groupId:]artifactId, case sensitive contains): 7:*【直接回车，使用默认值】*
>
> Define value for property 'groupId': *com.atguigu.maven*
>
> Define value for property 'artifactId': *pro01-maven-java*
>
> Define value for property 'version' 1.0-SNAPSHOT: :*【直接回车，使用默认值】*
>
> Define value for property 'package' com.atguigu.maven: :*【直接回车，使用默认值】*
>
> Confirm properties configuration: groupId: com.atguigu.maven artifactId: pro01-maven-java version: 1.0-SNAPSHOT package: com.atguigu.maven Y: :*【直接回车，表示确认。如果前面有输入错误，想要重新输入，则输入 N 再回车。】*

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_4调整)④调整

Maven 默认生成的工程，对 junit 依赖的是较低的 3.8.1 版本，我们可以改成较适合的 4.12 版本。

自动生成的 App.java 和 AppTest.java 可以删除。

```xml
<!-- 依赖信息配置 -->
<!-- dependencies复数标签：里面包含dependency单数标签 -->
<dependencies>
	<!-- dependency单数标签：配置一个具体的依赖 -->
	<dependency>
		<!-- 通过坐标来依赖其他jar包 -->
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>4.12</version>
		
		<!-- 依赖的范围 -->
		<scope>test</scope>
	</dependency>
</dependencies>
```

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_5自动生成的-pom-xml-解读)⑤自动生成的 pom.xml 解读

```xml
  <!-- 当前Maven工程的坐标 -->
  <groupId>com.atguigu.maven</groupId>
  <artifactId>pro01-maven-java</artifactId>
  <version>1.0-SNAPSHOT</version>
  
  <!-- 当前Maven工程的打包方式，可选值有下面三种： -->
  <!-- jar：表示这个工程是一个Java工程  -->
  <!-- war：表示这个工程是一个Web工程 -->
  <!-- pom：表示这个工程是“管理其他工程”的工程 -->
  <packaging>jar</packaging>

  <name>pro01-maven-java</name>
  <url>http://maven.apache.org</url>

  <properties>
	<!-- 工程构建过程中读取源码时使用的字符集 -->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <!-- 当前工程所依赖的jar包 -->
  <dependencies>
	<!-- 使用dependency配置一个具体的依赖 -->
    <dependency>
	
	  <!-- 在dependency标签内使用具体的坐标依赖我们需要的一个jar包 -->
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
	  
	  <!-- scope标签配置依赖的范围 -->
      <scope>test</scope>
    </dependency>
  </dependencies>
```

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_3、maven核心概念-pom)3、Maven核心概念：POM

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_1含义)①含义

POM：**P**roject **O**bject **M**odel，项目对象模型。和 POM 类似的是：DOM（Document Object Model），文档对象模型。它们都是模型化思想的具体体现。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_2模型化思想)②模型化思想

POM 表示将工程抽象为一个模型，再用程序中的对象来描述这个模型。这样我们就可以用程序来管理项目了。我们在开发过程中，最基本的做法就是将现实生活中的事物抽象为模型，然后封装模型相关的数据作为一个对象，这样就可以在程序中计算与现实事物相关的数据。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_3对应的配置文件)③对应的配置文件

POM 理念集中体现在 Maven 工程根目录下 **pom.xml** 这个配置文件中。所以这个 pom.xml 配置文件就是 Maven 工程的核心配置文件。其实学习 Maven 就是学这个文件怎么配置，各个配置有什么用。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_4、maven核心概念-约定的目录结构)4、Maven核心概念：约定的目录结构

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_1各个目录的作用)①各个目录的作用

![./images](maven_2022.assets/img011.621b1ac3.png)

另外还有一个 target 目录专门存放构建操作输出的结果。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_2约定目录结构的意义)②约定目录结构的意义

Maven 为了让构建过程能够尽可能自动化完成，所以必须约定目录结构的作用。例如：Maven 执行编译操作，必须先去 Java 源程序目录读取 Java 源代码，然后执行编译，最后把编译结果存放在 target 目录。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse01.html#_3约定大于配置)③约定大于配置

Maven 对于目录结构这个问题，没有采用配置的方式，而是基于约定。这样会让我们在开发过程中非常方便。如果每次创建 Maven 工程后，还需要针对各个目录的位置进行详细的配置，那肯定非常麻烦。

目前开发领域的技术发展趋势就是：`约定大于配置，配置大于编码`。

## 第二节 实验二：在 Maven 工程中编写代码

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse02.html#_1、主体程序)1、主体程序

![./images](https://heavy_code_industry.gitee.io/code_heavy_industry/assets/img/img012.0bcc2c5d.png)

主体程序指的是被测试的程序，同时也是将来在项目中真正要使用的程序。

```java
package com.atguigu.maven;
	
public class Calculator {
	
	public int sum(int i, int j){
		return i + j;
	}
	
}
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse02.html#_2、测试程序)2、测试程序

![./images](maven_2022.assets/img013.8b57a581.png)

```java
package com.atguigu.maven;
	
import org.junit.Test;
import com.atguigu.maven.Calculator;
	
// 静态导入的效果是将Assert类中的静态资源导入当前类
// 这样一来，在当前类中就可以直接使用Assert类中的静态资源，不需要写类名
import static org.junit.Assert.*;
	
public class CalculatorTest{
	
	@Test
	public void testSum(){
		
		// 1.创建Calculator对象
		Calculator calculator = new Calculator();
		
		// 2.调用Calculator对象的方法，获取到程序运行实际的结果
		int actualResult = calculator.sum(5, 3);
		
		// 3.声明一个变量，表示程序运行期待的结果
		int expectedResult = 8;
		
		// 4.使用断言来判断实际结果和期待结果是否一致
		// 如果一致：测试通过，不会抛出异常
		// 如果不一致：抛出异常，测试失败
		assertEquals(expectedResult, actualResult);

	}
}
```

## 实验三：手动执行 Maven 的构建命令

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse03.html#_1、要求)1、要求

运行 Maven 中和构建操作相关的命令时，必须进入到 pom.xml 所在的目录。如果没有在 pom.xml 所在的目录运行 Maven 的构建命令，那么会看到下面的错误信息：

```java
The goal you specified requires a project to execute but there is no POM in this directory
```

> TIP
>
> `mvn -v` 命令和构建操作无关，只要正确配置了 PATH，在任何目录下执行都可以。而构建相关的命令要在 pom.xml 所在目录下运行——操作哪个工程，就进入这个工程的 pom.xml 目录。

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse03.html#_2、清理操作)2、清理操作`mvn clean`

`mvn clean`

效果：删除 target 目录

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse03.html#_3、编译操作)3、编译操作`mvn compile`、`mvn test-compile`

主程序编译：`mvn compile`

测试程序编译：`mvn test-compile`

主体程序编译结果存放的目录：target/classes

测试程序编译结果存放的目录：target/test-classes

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse03.html#_4、测试操作)4、测试操作`mvn test`

`mvn test`

测试的报告存放的目录：target/surefire-reports

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse03.html#_5、打包操作)5、打包操作`mvn package`

`mvn package`

打包的结果——jar 包，存放的目录：target

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse03.html#_6、安装操作)6、安装到本地仓库操作`mvn install`

`mvn install`

```log
[INFO] Installing D:\maven-workspace\space201026\pro01-maven-java\target\pro01-maven-java-1.0-SNAPSHOT.jar to D:\maven-rep1026\com\atguigu\maven\pro01-maven-java\1.0-SNAPSHOT\pro01-maven-java-1.0-SNAPSHOT.jar
[INFO] Installing D:\maven-workspace\space201026\pro01-maven-java\pom.xml to D:\maven-rep1026\com\atguigu\maven\pro01-maven-java\1.0-SNAPSHOT\pro01-maven-java-1.0-SNAPSHOT.pom
```

安装的效果是将本地构建过程中生成的 jar 包存入 Maven 本地仓库。这个 jar 包在 Maven 仓库中的路径是根据它的坐标生成的。

坐标信息如下：

```xml
  <groupId>com.atguigu.maven</groupId>
  <artifactId>pro01-maven-java</artifactId>
  <version>1.0-SNAPSHOT</version>
```

在 Maven 仓库中生成的路径如下：

```log
D:\maven-rep1026\com\atguigu\maven\pro01-maven-java\1.0-SNAPSHOT\pro01-maven-java-1.0-SNAPSHOT.jar
```

另外，安装操作还会将 pom.xml 文件转换为 XXX.pom 文件一起存入本地仓库。所以我们在 Maven 的本地仓库中想看一个 jar 包原始的 pom.xml 文件时，查看对应 XXX.pom 文件即可，它们是名字发生了改变，本质上是同一个文件。

## 实验四：创建 Maven 版的 Web 工程

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_1、说明)1、说明

使用 `mvn archetype:generate` 命令生成 Web 工程时，需要使用一个专门的 archetype。这个专门生成 Web 工程骨架的 archetype 可以参照官网看到它的用法：

![./images](maven_2022.assets/img014.942770a3.png)

参数 archetypeGroupId、archetypeArtifactId、archetypeVersion 用来指定现在使用的 maven-archetype-webapp 的坐标。

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_2、操作)2、操作

注意：如果在上一个工程的目录下执行 mvn archetype:generate 命令，那么 Maven 会报错：不能在一个非 pom 的工程下再创建其他工程。所以不要再刚才创建的工程里再创建新的工程，**请回到工作空间根目录**来操作。

然后运行生成工程的命令：

```bash
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-webapp -DarchetypeVersion=1.4
```

下面的操作按照提示执行：

> TIP
>
> Define value for property 'groupId': com.atguigu.maven 
>
> Define value for property 'artifactId': pro02-maven-web 
>
> Define value for property 'version' 1.0-SNAPSHOT: :【直接回车，使用默认值】
>
> Define value for property 'package' com.atguigu.maven: :【直接回车，使用默认值】 Confirm properties configuration: groupId: com.atguigu.maven artifactId: pro02-maven-web version: 1.0-SNAPSHOT package: com.atguigu.maven Y: :【直接回车，表示确认】

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_3、生成的pom-xml)3、生成的pom.xml

确认打包的方式是war包形式

```xml
<packaging>war</packaging>
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_4、生成的web工程的目录结构)4、生成的Web工程的目录结构

![./images](maven_2022.assets/imagesasdada.png)

webapp 目录下有 index.jsp

WEB-INF 目录下有 web.xml

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_5、创建-servlet)5、创建 Servlet

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_1在-main-目录下创建-java-目录)①在 main 目录下创建 java 目录

![./images](maven_2022.assets/imagedasdadassdas.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_2在-java-目录下创建-servlet-类所在的包的目录)②在 java 目录下创建 Servlet 类所在的包的目录

![./images](maven_2022.assets/imagesweqweq.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_3在包下创建-servlet-类)③在包下创建 Servlet 类

```java
package com.atguigu.maven;
	
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
	
public class HelloServlet extends HttpServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().write("hello maven web");
		
	}
	
}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_4在-web-xml-中注册-servlet)④在 web.xml 中注册 Servlet

```xml
  <servlet>
	<servlet-name>helloServlet</servlet-name>
	<servlet-class>com.atguigu.maven.HelloServlet</servlet-class>
  </servlet>
  <servlet-mapping>
	<servlet-name>helloServlet</servlet-name>
	<url-pattern>/helloServlet</url-pattern>
  </servlet-mapping>
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_6、在-index-jsp-页面编写超链接)6、在 index.jsp 页面编写超链接

```html
<html>
<body>
<h2>Hello World!</h2>
<a href="helloServlet">Access Servlet</a>
</body>
</html>
```

> TIP
>
> JSP全称是 Java Server Page，和 Thymeleaf 一样，是服务器端页面渲染技术。这里我们不必关心 JSP 语法细节，编写一个超链接标签即可。

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_7、编译)7、编译

此时直接执行 mvn compile 命令出错：

DANGER

程序包 javax.servlet.http 不存在

程序包 javax.servlet 不存在

找不到符号

符号: 类 HttpServlet

……

上面的错误信息说明：我们的 Web 工程用到了 HttpServlet 这个类，而 HttpServlet 这个类属于 servlet-api.jar 这个 jar 包。此时我们说，Web 工程需要依赖 servlet-api.jar 包。

![./images](maven_2022.assets/img018.f836f056.png)

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_8、配置对-servlet-api-jar-包的依赖)8、配置对 servlet-api.jar 包的依赖

对于不知道详细信息的依赖可以到https://mvnrepository.com/网站查询。使用关键词搜索，然后在搜索结果列表中选择适合的使用。

![./images](maven_2022.assets/img019.46741083.png)

比如，我们找到的 servlet-api 的依赖信息：

```xml
<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
</dependency>
```

这样就可以把上面的信息加入 pom.xml。重新执行 mvn compile 命令。

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_9、将-web-工程打包为-war-包)9、将 Web 工程打包为 war 包

运行 mvn package 命令，生成 war 包的位置如下图所示：

![./images](maven_2022.assets/imagefdsfss.png)

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse04.html#_10、将-war-包部署到-tomcat-上运行)10、将 war 包部署到 Tomcat 上运行

将 war 包复制到 Tomcat/webapps 目录下

![./images](maven_2022.assets/imagestrtyr.png)

启动 Tomcat：

访问：http://localhost/pro02-maven-web/

![image-20220309233533996](maven_2022.assets/image-20220309233533996.png)

## 实验五：让 Web 工程依赖 Java 工程

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse05.html#_1、观念)1、观念

明确一个意识：从来只有 Web 工程依赖 Java 工程，没有反过来 Java 工程依赖 Web 工程。本质上来说，Web 工程依赖的 Java 工程其实就是 Web 工程里导入的 jar 包。最终 Java 工程会变成 jar 包，放在 Web 工程的 WEB-INF/lib 目录下。

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse05.html#_2、操作)2、操作

在 pro02-maven-web 工程的 pom.xml 中，找到 dependencies 标签，在 dependencies 标签中做如下配置：

```xml
<!-- 配置对Java工程pro01-maven-java的依赖 -->
<!-- 具体的配置方式：在dependency标签内使用坐标实现依赖 -->
<dependency>
	<groupId>com.atguigu.maven</groupId>
	<artifactId>pro01-maven-java</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse05.html#_3、在-web-工程中-编写测试代码)3、在 Web 工程中，编写测试代码

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse05.html#_1补充创建目录)①补充创建目录

pro02-maven-web**\src\test\java\com\atguigu\maven**

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse05.html#_2确认-web-工程依赖了-junit)②确认 Web 工程依赖了 junit

```xml
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse05.html#_3创建测试类)③创建测试类

把 Java 工程的 CalculatorTest.java 类复制到 pro02-maven-wb**\src\test\java\com\atguigu\maven** 目录下

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse05.html#_4、执行maven命令)4、执行Maven命令

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse05.html#_1测试命令)①测试命令

mvn test

说明：测试操作中会提前自动执行编译操作，测试成功就说明编译也是成功的。

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse05.html#_2打包命令)②打包命令

mvn package

![./images](maven_2022.assets/img024.91b00e04.png)

通过查看 war 包内的结构，我们看到被 Web 工程依赖的 Java 工程确实是会变成 Web 工程的 WEB-INF/lib 目录下的 jar 包。

![./images](data:image/png;base64,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)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse05.html#_3查看当前-web-工程所依赖的-jar-包的列表)③查看当前 Web 工程所依赖的 jar 包的列表

`mvn dependency:list`

> TIP
>
> [INFO] The following files have been resolved:
> [INFO] org.hamcrest:hamcrest-core:jar:1.3:test
> [INFO] javax.servlet:javax.servlet-api:jar:3.1.0:provided
> [INFO] com.atguigu.maven:pro01-maven-java:jar:1.0-SNAPSHOT:compile
> [INFO] junit:junit:jar:4.12:test

说明：javax.servlet:javax.servlet-api:jar:3.1.0:provided 格式显示的是一个 jar 包的坐标信息。格式是：

> TIP
>
> groupId:artifactId:打包方式:version:依赖的范围
>

这样的格式虽然和我们 XML 配置文件中坐标的格式不同，但是本质上还是坐标信息，大家需要能够认识这样的格式，将来从 Maven 命令的日志或错误信息中看到这样格式的信息，就能够识别出来这是坐标。进而根据坐标到Maven 仓库找到对应的jar包，用这样的方式解决我们遇到的报错的情况。

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse05.html#_4以树形结构查看当前-web-工程的依赖信息)④以树形结构查看当前 Web 工程的依赖信息

`mvn dependency:tree`

> TIP
>
> [INFO] com.atguigu.maven:pro02-maven-web:war:1.0-SNAPSHOT
> [INFO] +- junit:junit:jar:4.12:test
> [INFO] | \- org.hamcrest:hamcrest-core:jar:1.3:test
> [INFO] +- javax.servlet:javax.servlet-api:jar:3.1.0:provided
> [INFO] \- com.atguigu.maven:pro01-maven-java:jar:1.0-SNAPSHOT:compile

我们在 pom.xml 中并没有依赖 hamcrest-core，但是它却被加入了我们依赖的列表。原因是：junit 依赖了hamcrest-core，然后基于依赖的传递性，hamcrest-core 被传递到我们的工程了。

## 实验六：测试依赖的范围

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse06.html#_1、依赖范围)1、依赖范围

标签的位置：dependencies/dependency/**scope**

标签的可选值：**compile**/**test**/**provided**/system/runtime/**import**

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse06.html#_1compile-和-test-对比)①compile 和 test 对比

|         | main目录（空间） | test目录（空间） | 开发过程（时间） | 部署到服务器（时间） |
| ------- | ---------------- | ---------------- | ---------------- | -------------------- |
| compile | 有效             | 有效             | 有效             | 有效                 |
| test    | 无效             | 有效             | 有效             | 无效                 |

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse06.html#_2compile-和-provided-对比)②compile 和 provided 对比

|          | main目录（空间） | test目录（空间） | 开发过程（时间） | 部署到服务器（时间） |
| -------- | ---------------- | ---------------- | ---------------- | -------------------- |
| compile  | 有效             | 有效             | 有效             | 有效                 |
| provided | 有效             | 有效             | 有效             | 无效                 |

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse06.html#_3结论)③结论

`compile`：通常使用的第三方框架的 jar 包这样在项目实际运行时真正要用到的 jar 包都是以 compile 范围进行依赖的。比如 SSM 框架所需jar包。

`test`：测试过程中使用的 jar 包，以 test 范围依赖进来。比如 junit。

`provided`：在开发过程中需要用到的“服务器上的 jar 包”通常以 provided 范围依赖进来。比如 servlet-api、jsp-api。而这个范围的 jar 包之所以不参与部署、不放进 war 包，就是避免和服务器上已有的同类 jar 包产生冲突，同时减轻服务器的负担。说白了就是：“**服务器上已经有了，你就别带啦！**”

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse06.html#_2、测试)2、测试

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse06.html#_1验证-compile-范围对-main-目录有效)①验证 compile 范围对 main 目录有效

> TIP
>
> main目录下的类：HelloServlet 使用compile范围导入的依赖：pro01-atguigu-maven
>
> 验证：使用compile范围导入的依赖对main目录下的类来说是有效的
>
> 有效：HelloServlet 能够导入使用 pro01-atguigu-maven 工程中的 Calculator 类
>
> **验证方式：在 HelloServlet 类中`import com.atguigu.maven.Calculator` 类，然后编译通过就说明有效。**

```java
// 在main空间导入scope周期的 pro01-atguigu-maven【compile】 jar包里面的Calculator
import com.atguigu.maven.Calculator
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse06.html#_2验证test范围对main目录无效)②验证test范围对main目录无效

测试方式：在主体程序中导入`org.junit.Test`这个注解，然后执行编译。

具体操作：在pro01-maven-java\src\main\java\com\atguigu\maven目录下修改Calculator.java

```java
package com.atguigu.maven;

// @Test  注解是junit包里面的，scope是test，在main空间中导入验证是否对main空间有效
// 在main空间导入scope周期的 junit【test】 jar包里面的Test
import org.junit.Test;

public class Calculator {
	
	public int sum(int i, int j){
		return i + j;
	}
	
}
```

执行Maven编译命令：

```java
[ERROR] /D:/maven-workspace/space201026/pro01-maven-java/src/main/java/com/atguigu/maven/Calculator.java:[3,17] 程序包org.junit不存在
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse06.html#_3验证test和provided范围不参与服务器部署)③验证test和provided范围不参与服务器部署

其实就是验证：**通过compile范围依赖的jar包会放入war包，通过test范围依赖的jar包不会放入war包。**

![./images](maven_2022.assets/img026.0ad36150.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse06.html#_4验证provided范围对测试程序有效)④验证provided范围对测试程序有效

测试方式是在pro02-maven-web的测试程序中加入servlet-api.jar包中的类。

修改：`pro02-maven-web\src\test\java\com\atguigu\maven\CalculatorTest.java`

```java
package com.atguigu.maven;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.junit.Test;
import com.atguigu.maven.Calculator;

// 静态导入的效果是将Assert类中的静态资源导入当前类
// 这样一来，在当前类中就可以直接使用Assert类中的静态资源，不需要写类名
import static org.junit.Assert.*;

public class CalculatorTest{
	
	@Test
	public void testSum(){
		
		// 1.创建Calculator对象
		Calculator calculator = new Calculator();
		
		// 2.调用Calculator对象的方法，获取到程序运行实际的结果
		int actualResult = calculator.sum(5, 3);
		
		// 3.声明一个变量，表示程序运行期待的结果
		int expectedResult = 8;
		
		// 4.使用断言来判断实际结果和期待结果是否一致
		// 如果一致：测试通过，不会抛出异常
		// 如果不一致：抛出异常，测试失败
		assertEquals(expectedResult, actualResult);
		
	}
	
}
```

然后运行Maven的编译命令：mvn compile

然后看到编译成功。

## 实验七：测试依赖的传递性

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse07.html#_1、依赖的传递性)1、依赖的传递性

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse07.html#_1概念)①概念

A 依赖 B，B 依赖 C，那么在 A 没有配置对 C 的依赖的情况下，A 里面能不能直接使用 C？

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse07.html#_2传递的原则)②传递的原则

在 A 依赖 B，B 依赖 C 的前提下，C 是否能够传递到 A，取决于 B 依赖 C 时使用的依赖范围。

- B 依赖 C 时使用 compile 范围：可以传递
- B 依赖 C 时使用 test 或 provided 范围：不能传递，所以需要这样的 jar 包时，就必须在需要的地方明确配置依赖才可以。

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse07.html#_2、使用-compile-范围依赖-spring-core)2、使用 compile 范围依赖 spring-core

测试方式：让 pro01-maven-java 工程依赖 spring-core

具体操作：编辑 pro01-maven-java 工程根目录下 pom.xml

```xml
<!-- https://mvnrepository.com/artifact/org.springframework/spring-core -->
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-core</artifactId>
	<version>4.0.0.RELEASE</version>
</dependency>
```

使用 mvn dependency:tree 命令查看效果：

> TIP
>
> [INFO] com.atguigu.maven:pro01-maven-java:jar:1.0-SNAPSHOT
> [INFO] +- junit:junit:jar:4.12:test
> [INFO] | \- org.hamcrest:hamcrest-core:jar:1.3:test
>
> <u>[INFO] \- org.springframework:spring-core:jar:4.0.0.RELEASE:compile
> [INFO] \- commons-logging:commons-logging:jar:1.1.1:compile</u>



（ps：需要重新将 pro01-maven-java 安装到仓库【mvn install】）



还可以在 Web 工程【pro02-maven-web】中，使用 mvn dependency:tree 命令查看效果

> TIP
>
> [INFO] com.atguigu.maven:pro02-maven-web:war:1.0-SNAPSHOT
> [INFO] +- junit:junit:jar:4.12:test
> [INFO] | \- org.hamcrest:hamcrest-core:jar:1.3:test
> [INFO] +- javax.servlet:javax.servlet-api:jar:3.1.0:provided
>
> <u>[INFO] \- com.atguigu.maven:pro01-maven-java:jar:1.0-SNAPSHOT:compile
> [INFO] \- org.springframework:spring-core:jar:4.0.0.RELEASE:compile
> [INFO] \- commons-logging:commons-logging:jar:1.1.1:compile</u>

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse07.html#_3、验证-test-和-provided-范围不能传递)3、验证 test 和 provided 范围不能传递

从上面的例子已经能够看到，pro01-maven-java 依赖了 junit，但是在 pro02-maven-web 工程中查看依赖树的时候并没有看到 junit。

要验证 provided 范围不能传递，可以在 pro01-maven-java 工程中加入 servlet-api 的依赖。

```xml
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>javax.servlet-api</artifactId>
	<version>3.1.0</version>
	<scope>provided</scope>
</dependency>
```

效果还是和之前一样：

> TIP
>
> [INFO] com.atguigu.maven:pro02-maven-web:war:1.0-SNAPSHOT
> [INFO] +- junit:junit:jar:4.12:test
> [INFO] | \- org.hamcrest:hamcrest-core:jar:1.3:test
> [INFO] +- javax.servlet:javax.servlet-api:jar:3.1.0:provided
> [INFO] \- com.atguigu.maven:pro01-maven-java:jar:1.0-SNAPSHOT:compile
> [INFO] \- org.springframework:spring-core:jar:4.0.0.RELEASE:compile
> [INFO] \- commons-logging:commons-logging:jar:1.1.1:compile

## 第八节 实验八：测试依赖的排除

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse08.html#_1、概念)1、概念

当 A 依赖 B，B 依赖 C 而且 C 可以传递到 A 的时候，A 不想要 C，需要在 A 里面把 C 排除掉。而往往这种情况都是为了避免 jar 包之间的冲突。

![./images](maven_2022.assets/img027.2faff879.png)

所以配置依赖的排除其实就是阻止某些 jar 包的传递。因为这样的 jar 包传递过来会和其他 jar 包冲突。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse08.html#_2、配置方式)2、配置方式

```xml
<dependency>
	<groupId>com.atguigu.maven</groupId>
	<artifactId>pro01-maven-java</artifactId>
	<version>1.0-SNAPSHOT</version>
	<scope>compile</scope>
	<!-- 使用excludes标签配置依赖的排除	-->
	<exclusions>
		<!-- 在exclude标签中配置一个具体的排除 -->
		<exclusion>
			<!-- 指定要排除的依赖的坐标（不需要写version） -->
			<groupId>commons-logging</groupId>
			<artifactId>commons-logging</artifactId>
		</exclusion>
	</exclusions>
</dependency>
```

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse08.html#_3、测试)3、测试

测试的方式：在 pro02-maven-web 工程中配置对 commons-logging 的排除

```xml
<dependency>
	<groupId>com.atguigu.maven</groupId>
	<artifactId>pro01-maven-java</artifactId>
	<version>1.0-SNAPSHOT</version>
	<scope>compile</scope>
	<!-- 使用excludes标签配置依赖的排除	-->
	<exclusions>
		<!-- 在exclude标签中配置一个具体的排除 -->
		<exclusion>
			<!-- 指定要排除的依赖的坐标（不需要写version） -->
			<groupId>commons-logging</groupId>
			<artifactId>commons-logging</artifactId>
		</exclusion>
	</exclusions>
</dependency>
```

运行 mvn dependency:tree 命令查看效果：

> TIP
>
> [INFO] com.atguigu.maven:pro02-maven-web:war:1.0-SNAPSHOT
> [INFO] +- junit:junit:jar:4.12:test
> [INFO] | \- org.hamcrest:hamcrest-core:jar:1.3:test
> [INFO] +- javax.servlet:javax.servlet-api:jar:3.1.0:provided
>
> <u>[INFO] \- com.atguigu.maven:pro01-maven-java:jar:1.0-SNAPSHOT:compile
> [INFO] \- org.springframework:spring-core:jar:4.0.0.RELEASE:compile</u>

发现在 spring-core 下面就没有 commons-logging 了。

## 第九节 实验九：继承

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_1、概念)1、概念

Maven工程之间，A 工程继承 B 工程

- B 工程：父工程
- A 工程：子工程

本质上是 A 工程的 pom.xml 中的配置继承了 B 工程中 pom.xml 的配置。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_2、作用)2、作用

在父工程中统一管理项目中的依赖信息，具体来说是管理依赖信息的版本。

它的背景是：

- 对一个比较大型的项目进行了模块拆分。
- 一个 project 下面，创建了很多个 module。
- 每一个 module 都需要配置自己的依赖信息。

它背后的需求是：

- 在每一个 module 中各自维护各自的依赖信息很容易发生出入，不易统一管理。
- 使用同一个框架内的不同 jar 包，它们应该是同一个版本，所以整个项目中使用的框架版本需要统一。
- 使用框架时所需要的 jar 包组合（或者说依赖信息组合）需要经过长期摸索和反复调试，最终确定一个可用组合。这个耗费很大精力总结出来的方案不应该在新的项目中重新摸索。

通过在父工程中为整个项目维护依赖信息的组合既**保证了整个项目使用规范、准确的 jar 包**；又能够将**以往的经验沉淀**下来，节约时间和精力。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_3、举例)3、举例

在一个工程中依赖多个 Spring 的 jar 包

> TIP
>
> [INFO] +- org.springframework:**spring-core**:jar:**4.0.0**.RELEASE:compile
> [INFO] | \- commons-logging:commons-logging:jar:1.1.1:compile
> [INFO] +- org.springframework:**spring-beans**:jar:**4.0.0**.RELEASE:compile
> [INFO] +- org.springframework:**spring-context**:jar:**4.0.0**.RELEASE:compile
> [INFO] +- org.springframework:**spring-expression**:jar:4.0.0.RELEASE:compile
> [INFO] +- org.springframework:**spring-aop**:jar:**4.0.0**.RELEASE:compile
> [INFO] | \- aopalliance:aopalliance:jar:1.0:compile

使用 Spring 时要求所有 Spring 自己的 jar 包版本必须一致。为了能够对这些 jar 包的版本进行统一管理，我们使用继承这个机制，将所有版本信息统一在父工程中进行管理。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_4、操作)4、操作

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_1创建父工程)①创建父工程

创建的过程和前面创建 pro01-maven-java 一样。

`mvn archetype:generate`

工程名称：pro03-maven-parent

工程创建好之后，要修改它的打包方式：

```xml
  <groupId>com.atguigu.maven</groupId>
  <artifactId>pro03-maven-parent</artifactId>
  <version>1.0-SNAPSHOT</version>

  <!-- 当前工程作为父工程，它要去管理子工程，所以打包方式必须是 pom -->
  <packaging>pom</packaging>
```

只有打包方式为 pom 的 Maven 工程能够管理其他 Maven 工程。打包方式为 pom 的 Maven 工程中不写业务代码，它是专门管理其他 Maven 工程的工程。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_2创建模块工程)②创建模块工程

模块工程类似于 IDEA 中的 module，所以需要**进入 pro03-maven-parent 工程的根目录**，然后运行 `mvn archetype:generate` 命令来创建模块工程。

假设，我们创建三个模块工程：

![./images](maven_2022.assets/imagesouuu.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_3查看被添加新内容的父工程-pom-xml)③查看被添加新内容的父工程【pro03-maven-parent】pom.xml

下面 modules 和 module 标签是聚合功能的配置

```xml
<modules>  
	<module>pro04-maven-module</module>
	<module>pro05-maven-module</module>
	<module>pro06-maven-module</module>
</modules>
```

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_4解读子工程的pom-xml)④解读子工程的pom.xml

> 如果子工程坐标中的groupId和version与父工程一致，那么可以省略

```xml
<!-- 使用parent标签指定当前工程的父工程 -->
<parent>
	<!-- 父工程的坐标 -->
	<groupId>com.atguigu.maven</groupId>
	<artifactId>pro03-maven-parent</artifactId>
	<version>1.0-SNAPSHOT</version>
</parent>

<!-- 子工程的坐标 -->
<!-- 如果子工程坐标中的groupId和version与父工程一致，那么可以省略 -->
<!-- <groupId>com.atguigu.maven</groupId> -->
<artifactId>pro04-maven-module</artifactId>
<!-- <version>1.0-SNAPSHOT</version> -->
```

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_5在父工程中配置依赖的统一管理)⑤在父工程中配置依赖的统一管理

```xml
<!-- 使用dependencyManagement标签配置对依赖的管理 -->
<!-- 被管理的依赖并没有真正被引入到工程 -->
<!-- 注意：即使在父工程配置了对依赖的管理，子工程需要使用具体哪一个依赖还是要明确配置。-->
<!--只是统一定义版本信息，如果哪个模块需要，需要单独引入-->
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>4.0.0.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-beans</artifactId>
			<version>4.0.0.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context</artifactId>
			<version>4.0.0.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-expression</artifactId>
			<version>4.0.0.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-aop</artifactId>
			<version>4.0.0.RELEASE</version>
		</dependency>
	</dependencies>
</dependencyManagement>
```

![](maven_2022.assets/image-20220315220122848.png)

> dependencyManagement标签内的依赖子模块没有引入时，不会导入依赖



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_6子工程中引用那些被父工程管理的依赖)⑥子工程中按需引用那些被父工程管理的依赖

关键点：省略版本号

子工程可以按需导入dependencyManagement管理依赖，父工程中没有管理的依赖依然可以单独引入

```xml
<!-- 子工程引用父工程中的依赖信息时，可以把版本号去掉。	-->
<!-- 把版本号去掉就表示子工程中这个依赖的版本由父工程决定。 -->
<!-- 具体来说是由父工程的dependencyManagement来决定。 -->
<dependencies>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-core</artifactId>
        <!--  对于父工程中进行了管理的依赖dependencyManagement，子工程中引用时可以不写version -->
        <!-- <version>4.0.0.RELEASE</version> -->
        <!--情况1确实省略了version标签：子工程采纳的就是父工程管理的版本-->
	    <!--情况2没有省略version标签：
			A：这里配置了version和父工程管理的版本一致，最终还是采纳这个版本。
			B：这里配置了version但是和父工程管理的版本不一致，那么这里子工程配置的版本会覆盖父工程管理的版本并最终采纳。
    -->    
        
        
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-beans</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-context</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-expression</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-aop</artifactId>
	</dependency>
</dependencies>
```

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_7在父工程中升级依赖信息的版本)⑦在父工程中升级依赖信息的版本

```xml
……
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-beans</artifactId>
				<version>4.1.4.RELEASE</version>
			</dependency>
……
```

然后在子工程中运行`mvn dependency:list`，效果如下：

> TIP
>
> [INFO] org.springframework:spring-aop:jar:4.1.4.RELEASE:compile
> [INFO] org.springframework:spring-core:jar:4.1.4.RELEASE:compile
> [INFO] org.springframework:spring-context:jar:4.1.4.RELEASE:compile
> [INFO] org.springframework:spring-beans:jar:4.1.4.RELEASE:compile
> [INFO] org.springframework:spring-expression:jar:4.1.4.RELEASE:compile

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_8在父工程中声明自定义属性)⑧在父工程中声明自定义属性

```xml
<!-- 通过自定义属性，统一指定Spring的版本 -->
<properties>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
	
	<!-- 自定义标签，维护Spring版本数据 -->
	<atguigu.spring.version>4.3.6.RELEASE</atguigu.spring.version>
</properties>
```

在需要的地方使用`${}`的形式来引用自定义的属性名：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>${atguigu.spring.version}</version>
</dependency>
```

真正实现“一处修改，处处生效”。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse09.html#_5、实际意义)5、实际意义

![./images](maven_2022.assets/img037.53c95c38.jpg)

编写一套符合要求、开发各种功能都能正常工作的依赖组合并不容易。如果公司里已经有人总结了成熟的组合方案，那么再开发新项目时，如果不使用原有的积累，而是重新摸索，会浪费大量的时间。为了提高效率，我们可以使用工程继承的机制，让成熟的依赖组合方案能够保留下来。

如上图所示，公司级的父工程中管理的就是成熟的依赖组合方案，各个新项目、子系统各取所需即可。

## 实验十：聚合

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse10.html#_1、聚合本身的含义)1、聚合本身的含义

部分组成整体

![./images](maven_2022.assets/img029.48831f65.jpg)

动画片《战神金刚》中的经典台词：“我来组成头部！我来组成手臂！”就是聚合关系最生动的体现。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse10.html#_2、maven-中的聚合)2、Maven 中的聚合

使用一个“总工程”将各个“模块工程”汇集起来，作为一个整体对应完整的项目。

- 项目：整体
- 模块：部分

> TIP
>
> 概念的对应关系：
>
> 从继承关系角度来看：
>
> - 父工程
> - 子工程
>
> 从聚合关系角度来看：
>
> - 总工程
> - 模块工程

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse10.html#_3、好处)3、好处

- 一键执行 Maven 命令：很多构建命令都可以在“总工程”中一键执行。

  以 mvn install 命令为例：Maven 要求有父工程时先安装父工程；有依赖的工程时，先安装被依赖的工程。我们自己考虑这些规则会很麻烦。但是工程聚合之后，在总工程执行 mvn install 可以一键完成安装，而且会自动按照正确的顺序执行。

- 配置聚合之后，各个模块工程会在总工程中展示一个列表，让项目中的各个模块一目了然。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse10.html#_4、聚合的配置)4、聚合的配置

在总工程中配置 modules 即可：

```xml
	<modules>  
		<module>pro04-maven-module</module>
		<module>pro05-maven-module</module>
		<module>pro06-maven-module</module>
	</modules>
```

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter03/verse10.html#_5、依赖循环问题)5、依赖循环问题

如果 A 工程依赖 B 工程，B 工程依赖 C 工程，C 工程又反过来依赖 A 工程，那么在执行构建操作时会报下面的错误：



[ERROR] [ERROR] The projects in the reactor contain a cyclic reference:

这个错误的含义是：循环引用。



# 第四章 使用Maven：IDEA环境

TIP

各个 IDEA 不同版本在具体操作方面存在一定差异，这里我们以 2019.3.3 版本为例进行演示。其它版本大家灵活变通即可。

![images](maven_2022.assets/img001.4f8b5c6b.png)

## [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse01.html#第一节-创建父工程)第一节 创建父工程

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse01.html#_1、创建-project)1、创建 Project

![./images](maven_2022.assets/imagekgggs.png)

![./images](maven_2022.assets/img030.7f885903.png)

![./images](maven_2022.assets/img032.9289c76e.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse01.html#_2、开启自动导入)2、开启自动导入

创建 Project 后，IDEA 会自动弹出下面提示，我们选择**『Enable Auto-Import』**，意思是启用自动导入。

![images](maven_2022.assets/images;lkjhg.png)

这个自动导入**一定要开启**，因为 Project、Module 新创建或 pom.xml 每次修改时都应该让 IDEA 重新加载 Maven 信息。这对 Maven 目录结构认定、Java 源程序编译、依赖 jar 包的导入都有非常关键的影响。

另外也可以通过 IDEA 的 Settings 设置来开启：

![images](maven_2022.assets/img004.e823213d.png)

## 第二节 配置Maven信息

每次创建 Project 后都需要设置 Maven 家目录位置，否则 IDEA 将使用内置的 Maven 核心程序（不稳定）并使用默认的本地仓库位置。这样一来，我们在命令行操作过程中已下载好的 jar 包就白下载了，默认的本地仓库通常在 C 盘，还影响系统运行。

配置之后，IDEA 会根据我们在这里指定的 Maven 家目录自动识别到我们在 settings.xml 配置文件中指定的本地仓库。

![./images](maven_2022.assets/img033.39a65cee.png)

## 第三节 创建Java模块工程

![./images](maven_2022.assets/img034.dcd746ce.png)

![./images](maven_2022.assets/img035.939da5d9.png)

## 第四节 创建Web模块工程

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse04.html#_1、创建模块)1、创建模块

按照前面的同样操作创建模块，**此时**这个模块其实还是一个**Java模块**。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse04.html#_2、修改打包方式)2、修改打包方式

Web 模块将来打包当然应该是 **war** 包。

```xml
<packaging>war</packaging>
```

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse04.html#_3、web-设定)3、Web 设定

首先打开项目结构菜单：

![images](maven_2022.assets/imagelkjhrs.png)

然后到 Facets 下查看 IDEA 是否已经帮我们自动生成了 Web 设定。正常来说只要我们确实设置了打包方式为 war，那么 IDEA 2019 版就会自动生成 Web 设定。

![images](maven_2022.assets/img006.969793b4.png)

另外，对于 IDEA 2018 诸版本没有自动生成 Web 设定，那么请参照下面两图，我们自己创建：

![./images](maven_2022.assets/img042.32a9d794.png)

![./images](maven_2022.assets/img043.0a913d5c.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse04.html#_4、借助idea生成web-xml)4、借助IDEA生成web.xml

需要手动指定到   `模块路径下：src\main\webapp\WEB-INF\web.xml`

![./images](maven_2022.assets/imagesgdfdgretevx.png)

![./images](maven_2022.assets/img045.dd04540f.png)

![image-20220316003830989](maven_2022.assets/image-20220316003830989.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse04.html#_5、设置-web-资源的根目录)5、设置 Web 资源的根目录

结合 Maven 的目录结构，Web 资源的根目录需要设置为 `src/main/webapp` 目录。

![./images](maven_2022.assets/img046.71c20d43.png)

![./images](maven_2022.assets/imageqrures.png)

![image-20220316003955617](maven_2022.assets/image-20220316003955617.png)

### 6、maven web工程约定目录结构 

```sh
# maven web 工程约定目录结构
pro-maven-web
├─src
│  ├─main
│  │  ├─java # java程序目录
│  │  │  └─com.atguigu.maven
│  │  │           
│  │  ├─resources  # 配置文件目录
│  │  │ 
│  │  └─webapp   # maven web工程默认目录
│  │      └─WEB-INF
│  │		└─web.xml # web配置文件
│  └─test   # 测试目录
│      └─java
└─target  # maven打包文件目录
    ├─classes
    │  └─com
    │      └─atguigu
    │          └─maven
    └─generated-sources
        └─annotations
```

### #7、添加tomcat运行，测试

创建测试类、index.jsp

![image-20220316005324729](maven_2022.assets/image-20220316005324729.png)

添加tomcat配置，部署工件，指定webContext

![image-20220316005705070](maven_2022.assets/image-20220316005705070.png)



访问首页

## 第五节 其他操作

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_1、在idea中执行maven命令)1、在IDEA中执行Maven命令

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_1直接执行)①直接双击执行

![./images](maven_2022.assets/img040.ddaaa560.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_2手动输入)②手动输入命令

![images](maven_2022.assets/imagegssds.png)

![images](maven_2022.assets/imagesdfsfsdfsdfdsfs.png)

![images](maven_2022.assets/img029.7b9c7a12.png)

![images](maven_2022.assets/imagesdfsfsfss.png)

如果有需要，还可以给命令后面附加参数：

![images](maven_2022.assets/images54fgdf.png)

```sh
## -D 表示后面要附加命令的参数，字母 D 和后面的参数是紧挨着的，中间没有任何其它字符
## maven.test.skip=true 表示在执行命令的过程中跳过测试
mvn clean install -Dmaven.test.skip=true
```

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_2、在idea中查看某个模块的依赖信息)2、在IDEA中查看某个模块的依赖信息

![./images](maven_2022.assets/img041.c804be73.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_3、工程导入)3、工程导入

Maven工程除了自己创建的，还有很多情况是别人创建的。而为了参与开发或者是参考学习，我们都需要导入到 IDEA 中。下面我们分几种不同情况来说明：

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_1来自版本控制系统)①来自版本控制系统

目前我们通常使用的都是 Git（本地库） + 码云（远程库）的版本控制系统，结合 IDEA 的相关操作方式请点[**这里** (opens new window)](http://heavy_code_industry.gitee.io/code_heavy_industry/pro008-Git/lecture/chapter05/verse03.html)查看**克隆远程库**部分。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_2来自工程目录)②来自工程目录

直接使用 IDEA 打开工程目录即可。下面咱们举个例子：

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_1-工程压缩包)[1]工程压缩包

假设别人发给我们一个 Maven 工程的 zip 压缩包：maven-rest-demo.zip。从码云或GitHub上也可以以 ZIP 压缩格式对项目代码打包下载。

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_2-解压)[2]解压

如果你的所有 IDEA 工程有一个专门的目录来存放，而不是散落各处，那么首先我们就把 ZIP 包解压到这个指定目录中。

![./images](maven_2022.assets/img008.bb31d9a2.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_3-打开)[3]打开

只要我们确认在解压目录下可以直接看到 pom.xml，那就能证明这个解压目录就是我们的工程目录。那么接下来让 IDEA 打开这个目录就可以了。

![./images](maven_2022.assets/imagesfsfdsfewwew.png)

![./images](maven_2022.assets/img010.900e648d.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_4-设置-maven-核心程序位置)[4]设置 Maven 核心程序位置

打开一个新的 Maven 工程，和新创建一个 Maven 工程是一样的，此时 IDEA 的 settings 配置中关于 Maven 仍然是默认值：

![./images](maven_2022.assets/img011.d7914ffd.png)

所以我们还是需要像新建 Maven 工程那样，指定一下 Maven 核心程序位置：

![./images](maven_2022.assets/img012.a62a48e8.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_4、模块导入)4、模块导入

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_1情景重现)①情景重现

在实际开发中，通常会忽略模块（也就是module）所在的项目（也就是project）仅仅导入某一个模块本身。这么做很可能是类似这样的情况：比如基于 Maven 学习 SSM 的时候，做练习需要导入老师发给我们的代码参考。

![./images](maven_2022.assets/img013.5f438a0d.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_2导入-java-类型模块)②导入 Java 类型模块

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_1-找到老师发的工程目录)[1]找到老师发的工程目录

![./images](maven_2022.assets/imagesvcxvsdfdss.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_2-复制我们想要导入的模块目录)[2]复制我们想要导入的模块目录

![./images](maven_2022.assets/images12321as.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_3-粘贴到我们自己工程目录下)[3]粘贴到我们自己工程目录下

这个工程（project）是我们事先在 IDEA 中创建好的。

![./images](maven_2022.assets/img016.1933c568.png)

------

![./images](maven_2022.assets/imagesfsadsadassda.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_4-在-idea-中执行导入)[4]在 IDEA 中执行导入

![./images](maven_2022.assets/imagesdfouashosaoda.png)

------

![./images](maven_2022.assets/img019.772f07d3.png)

------

![./images](maven_2022.assets/img020.bb620847.png)

------

![./images](maven_2022.assets/img021.ac677293.png)

------

![./images](maven_2022.assets/img022.ac55e275.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_5-修改-pom-xml)[5]修改 pom.xml

刚刚导入的 module 的父工程坐标还是以前的，需要改成我们自己的 project。

![./images](maven_2022.assets/img023.4ffa60b5.png)

------

![./images](maven_2022.assets/img024.7585bfaa.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_6-最终效果)[6]最终效果

![./images](maven_2022.assets/img025.9e3d577f.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse05.html#_3导入-web-类型模块)③导入 Web 类型模块

其它操作和上面演示的都一样，只是多一步：删除多余的、不正确的 web.xml 设置。如下图所示：

![./images](maven_2022.assets/img026.8dad97d2.png)

# 第五章 其他核心概念

## [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter05/#_1、生命周期)1、生命周期

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter05/#_1作用)①作用

为了让构建过程自动化完成，Maven 设定了三个生命周期，生命周期中的每一个环节对应构建过程中的一个操作。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter05/#_2三个生命周期)②三个生命周期

| 生命周期名称 | 作用         | 各个环节                                                     |
| ------------ | ------------ | ------------------------------------------------------------ |
| Clean        | 清理操作相关 | pre-clean <br/>clean <br/>post-clean                         |
| Site         | 生成站点相关 | pre-site<br/>site<br/>post-site<br/>deploy-site              |
| Default      | 主要构建过程 | validate<br/><br/>generate-sources<br/>process-sources<br/>generate-resources-<br/>process-resources 复制并处理资源文件，至目标目录，准备打包。 <br/>compile 编译项目 main 目录下的源代码。 <br/>process-classes <br/><br/>generate-test-sources <br/>process-test-sources <br/>generate-test-resources <br/>process-test-resources 复制并处理资源文件，至目标测试目录。 <br/>test-compile 编译测试源代码。 <br/>process-test-classes test 使用合适的单元测试框架运行测试。这些测试代码不会被打包或部署。 <br/>prepare-package <br/>package 接受编译好的代码，打包成可发布的格式，如JAR。 <br/>pre-integration-test <br/>integration-test <br/>post-integration-test verify <br/>install将包安装至本地仓库，以让其它项目依赖。 <br/>deploy将最终的包复制到远程的仓库，以让其它开发人员共享；或者部署到服务器上运行（需借助插件，例如：cargo）。 |

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter05/#_3特点)③特点

- 前面三个生命周期彼此是独立的。
- 在任何一个生命周期内部，执行任何一个具体环节的操作，都是从本周期最初的位置开始执行，直到指定的地方。（本节记住这句话就行了，其他的都不需要记）

Maven 之所以这么设计其实就是为了提高构建过程的自动化程度：让使用者只关心最终要干的即可，过程中的各个环节是自动执行的。

> 这就是为什么前面 `mvn clean install` ？
>
> 因为 clean 不属于default声明周期的,属于单独的clean声明周期。即默认mvn install 会执行默认  validate compile   compile-test test  package等

## [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter05/#_2、插件和目标)2、插件和目标

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter05/#_1插件)①插件

Maven 的核心程序仅仅负责宏观调度，不做具体工作。具体工作都是由 Maven 插件完成的。例如：编译就是由 maven-compiler-plugin-3.1.jar 插件来执行的。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter05/#_2目标)②目标

一个插件可以对应多个目标，而每一个目标都和生命周期中的某一个环节对应。

Default 生命周期中有 compile 和 test-compile 两个和编译相关的环节，这两个环节对应 compile 和 test-compile 两个目标，而这两个目标都是由 maven-compiler-plugin-3.1.jar 插件来执行的。

## [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter05/#_3、仓库)3、仓库

- 本地仓库：在当前电脑上，为电脑上所有 Maven 工程服务
- 远程仓库：需要联网
  - 局域网：我们自己搭建的 Maven 私服，例如使用 Nexus 技术。
  - Internet
    - 中央仓库
    - 镜像仓库：内容和中央仓库保持一致，但是能够分担中央仓库的负载，同时让用户能够就近访问提高下载速度，例如：Nexus aliyun

建议：不要中央仓库和阿里云镜像混用，否则 jar 包来源不纯，彼此冲突。

专门搜索 Maven 依赖信息的网站：https://mvnrepository.com/

#  第六章 单一架构案例

## 第一节 创建工程，引入依赖

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_1、架构)1、架构

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_1架构的概念)①架构的概念

『架构』其实就是『项目的**结构**』，只是因为架构是一个更大的词，通常用来形容比较大规模事物的结构。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_2单一架构)②单一架构

单一架构也叫『all-in-one』结构，就是所有代码、配置文件、各种资源都在同一个工程。

- 一个项目包含一个工程
- 导出一个 war 包
- 放在一个 Tomcat 上运行

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_2、创建工程)2、创建工程

![images](maven_2022.assets/imagesfsfsfsdr.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_3、引入依赖)3、引入依赖

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_1搜索依赖信息的网站)①搜索依赖信息的网站

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_1-到哪儿找)[1]到哪儿找？

https://mvnrepository.com/

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_2-怎么选择)[2]怎么选择？

- 确定技术选型：确定我们项目中要使用哪些技术
- 到 mvnrepository 网站搜索具体技术对应的具体依赖信息

![images](maven_2022.assets/img006.250bd584.png)

- 确定这个技术使用哪个版本的依赖
  - 考虑因素1：看是否有别的技术要求这里必须用某一个版本
  - 考虑因素2：如果没有硬性要求，那么选择较高版本或下载量大的版本

![images](maven_2022.assets/imagetyts.png)

![imagse](maven_2022.assets/imagsejjjj.png)

- 在实际使用中检验所有依赖信息是否都正常可用

::: TIP

确定技术选型、组建依赖列表、项目划分模块……等等这些操作其实都属于**架构设计**的范畴。

- 项目本身所属行业的基本特点
- 项目具体的功能需求
- 项目预计访问压力程度
- 项目预计将来需要扩展的功能
- 设计项目总体的体系结构

:::

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_2持久化层所需依赖)②持久化层所需依赖

- mysql:mysql-connector-java:5.1.37
- com.alibaba:druid:1.2.8
- commons-dbutils:commons-dbutils:1.6

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_3表述层所需依赖)③表述层所需依赖

- javax.servlet:javax.servlet-api:3.1.0
- org.thymeleaf:thymeleaf:3.0.11.RELEASE

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_4辅助功能所需依赖)④辅助功能所需依赖

- junit:junit:4.12
- ch.qos.logback:logback-classic:1.2.3

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_5最终完整依赖信息)⑤最终完整依赖信息

```xml
<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.37</version>
</dependency>
<!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.8</version>
</dependency>
<!-- https://mvnrepository.com/artifact/commons-dbutils/commons-dbutils -->
<dependency>
    <groupId>commons-dbutils</groupId>
    <artifactId>commons-dbutils</artifactId>
    <version>1.6</version>
</dependency>
<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
</dependency>
<!-- https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf -->
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf</artifactId>
    <version>3.0.11.RELEASE</version>
</dependency>

<!-- 如果使用jsp  导入如下依赖-->
<!--        <dependency>-->
<!--            <groupId>javax.servlet.jsp</groupId>-->
<!--            <artifactId>jsp-api</artifactId>-->
<!--            <version>2.1</version>-->
<!--            <scope>provided</scope>-->
<!--        </dependency>-->

<!-- https://mvnrepository.com/artifact/junit/junit -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>
<!-- https://mvnrepository.com/artifact/ch.qos.logback/logback-classic -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
<!-- <scope>test</scope>-->
     <scope>compile</scope>
</dependency>
```

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse01.html#_4、建包)4、建包

| package 功能          | package 名称                              |
| --------------------- | ----------------------------------------- |
| 主包                  | com.atguigu.imperial.court                |
| 子包[实体类]          | com.atguigu.imperial.court.entity         |
| 子包[Servlet基类包]   | com.atguigu.imperial.court.servlet.base   |
| 子包[Servlet模块包]   | com.atguigu.imperial.court.servlet.module |
| 子包[Service接口包]   | com.atguigu.imperial.court.service.api    |
| 子包[Service实现类包] | com.atguigu.imperial.court.service.impl   |
| 子包[Dao接口包]       | com.atguigu.imperial.court.dao.api        |
| 子包[Dao实现类包]     | com.atguigu.imperial.court.dao.impl       |
| 子包[Filter]          | com.atguigu.imperial.court.filter         |
| 子包[异常类包]        | com.atguigu.imperial.court.exception      |
| 子包[工具类]          | com.atguigu.imperial.court.util           |
| 子包[测试类]          | com.atguigu.imperial.court.test           |

## 第二节 搭建环境：持久化层

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_1、数据建模)1、数据建模

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_1物理建模)①物理建模

```sql
create database db_imperial_court;

use db_imperial_court;

create table t_emp
(
    emp_id         int primary key auto_increment,
    emp_name       char(100) not null,
    emp_position   char(100) not null,
    login_account  char(100) not null unique,
    login_password char(100) not null
);

insert into t_emp(emp_name, emp_position, login_account, login_password)
values ('爱新觉罗·玄烨', 'emperor', 'xiaoxuanzi1654', '25325C896624D444B2E241807DCAC98B'), # 16540504
       ('纳兰明珠', 'minister', 'brightball1635', 'A580D0EF93C22036C859E194C14CB777'),   # 16351119
       ('赫舍里·索额图', 'minister', 'tutu1636', 'E40FD7D49B8B7EF46F47407D583C3538'); # 17030921

create table t_memorials
(
    memorials_id          int primary key auto_increment,
    memorials_title       char(100)     not null,
    memorials_content     varchar(5000) not null,
    memorials_emp         int           not null,
    memorials_create_time char(100),
    feedback_time       char(100),
    feedback_content    varchar(1000),
    memorials_status      int           not null
);

insert into t_memorials(memorials_title,
                      memorials_content,
                      memorials_emp,
                      memorials_create_time,
                      feedback_time,
                      feedback_content,
                      memorials_status)
values ('浙江巡抚奏钱塘堤决口疏', '皇上啊，不好啦！钱塘江发大水啦！堤坝冲毁啦！您看这咋弄啊！', 2, '1690-05-07', null, null, 0),
       ('左都御史参鳌拜圈地疏', '皇上啊，鳌拜这厮不是东西呀！占老百姓的地哇！还打人呀！您看咋弄啊！', 3, '1690-04-14', null, null, 0),
       ('都察院劾吴三桂不臣疏', '皇上啊，不得了啦！吴三桂那孙子想造反呀！', 2, '1693-11-18', null, null, 0),
       ('兵部奏准噶尔犯境疏', '皇上啊，不得了啦！葛尔丹要打过来了呀！', 3, '1693-11-18', null, null, 0),
       ('朝鲜使臣朝拜事宜呈皇上御览', '皇上啊！朝鲜国的人要来啦！咱们请他们吃猪肉炖粉条子吧！', 2, '1680-06-11', null, null, 0),
       ('英吉利炮舰购买事宜疏', '皇上啊！英国的小船船咱们买多少啊？', 3, '1680-06-12', null, null, 0),
       ('劾杭州织造贪墨疏', '皇上啊！杭州织造有问题啊！', 2, '1680-06-13', null, null, 0),
       ('禀畅春园落成疏', '皇上啊！畅春园修好了哇！您啥时候过来看看呀！', 3, '1680-06-14', null, null, 0),
       ('请旨木兰秋狝疏', '皇上啊！秋天到啦，又该打猎啦！', 2, '1680-06-15', null, null, 0),
       ('核准西北军饷银两疏', '皇上啊！您看看这钱数算的对不对呀！', 3, '1680-06-16', null, null, 0),
       ('请旨裁撤三藩疏', '皇上啊！咱们不裁撤三藩就芭比Q了哇！', 2, '1680-06-17', null, null, 0),
       ('蒙古王公进京朝拜疏', '皇上啊！蒙古王公要来啦！咱们请他们吃猪肉炖粉条子吧！', 3, '1680-06-18', null, null, 0),
       ('礼部请旨九阿哥赐名疏', '皇上啊！您看九阿哥该叫什么名字呀？', 2, '1680-06-19', null, null, 0),
       ('户部尚书请旨告老还乡疏', '皇上啊！臣想回家养老啦！您看看啥时候给臣把俸禄结一下啊！', 3, '1680-06-20', null, null, 0),
       ('查江宁织造贪墨疏', '皇上啊！江宁织造有问题啊！', 2, '1680-06-21', null, null, 0)
       ;
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_2逻辑建模)②逻辑建模

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_1-emp-实体类)[1] Emp 实体类

```java
public class Emp {

    private Integer empId;
    private String empName;
    private String empPosition;
    private String loginAccount;
    private String loginPassword;
```

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_2-memorials-实体类)[2] Memorials 实体类

```java
public class Memorials {

    private Integer memorialsId;
    private String memorialsTitle;
    private String memorialsContent;
    
    // 奏折摘要数据库没有，这里是为了配和页面显示
    private String memorialsContentDigest;
    private Integer memorialsEmp;

    // 员工姓名数据库没有，这里是为了配合页面显示
    private String memorialsEmpName;
    private String memorialsCreateTime;
    private String feedbackTime;
    private String feedbackContent;
    private Integer memorialsStatus;
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_2、数据库连接信息)2、数据库连接信息

说明：这是我们第一次用到 Maven 约定目录结构中的 resources 目录，这个目录存放各种配置文件。jdbc.properties

![images](maven_2022.assets/imagehhtrhrs.png)

```properties
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://192.168.198.100:3306/db_imperial_court
username=root
password=atguigu
initialSize=10
maxActive=20
maxWait=10000
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_3、获取数据库连接)3、获取数据库连接

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_1创建-jdbcutils-工具类)①创建 JDBCUtils 工具类

![images](maven_2022.assets/imageshhh.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_2创建-javax-sql-datasource-对象)②创建 javax.sql.DataSource 对象

```java
// 将数据源对象设置为静态属性，保证大对象的单一实例
private static DataSource dataSource;

static {

    // 1.创建一个用于存储外部属性文件信息的Properties对象
    Properties properties = new Properties();

    // 2.使用当前类的类加载器加载外部属性文件：jdbc.properties
    InputStream inputStream = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");

    try {

        // 3.将外部属性文件jdbc.properties中的数据加载到properties对象中
        properties.load(inputStream);

        // 4.创建数据源对象
        dataSource = DruidDataSourceFactory.createDataSource(properties);

    } catch (Exception e) {
        e.printStackTrace();
    }

}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_3创建-threadlocal-对象)③创建 ThreadLocal 对象

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_1-提出需求)[1]提出需求

###### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_1-在一个方法内控制事务)(1)在一个方法内控制事务

如果在每一个 Service 方法中都写下面代码，那么代码重复性就太高了：

```java
try{

	// 1、获取数据库连接
	// 重要：要保证参与事务的多个数据库操作（SQL 语句）使用的是同一个数据库连接
	Connection conn = JDBCUtils.getConnection();
	conn.setAutoCommit(false);
    
	// 2、核心操作
	// ...
	
	// 3、核心操作成功结束，可以提交事务
	conn.commit();

}catch(Exception e){

	// 4、核心操作抛出异常，必须回滚事务
	conn.rollBack();

}finally{

	// 5、释放数据库连接
	JDBCUtils.releaseConnection(conn);
	
}
```

###### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_2-将重复代码抽取到-filter)(2)将重复代码抽取到 Filter

所谓『当前请求覆盖的 Servlet 方法、Service 方法、Dao 方法』其实就是 chain.doFilter(request, response) **间接调用**的方法。

```java
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){

	try{

		// 1、获取数据库连接
		// 重要：要保证参与事务的多个数据库操作（SQL 语句）使用的是同一个数据库连接
		Connection conn = JDBCUtils.getConnection();
        
        // 重要操作：关闭自动提交功能
        connection.setAutoCommit(false);
		
		// 2、核心操作：通过 chain 对象放行当前请求
		// 这样就可以保证当前请求覆盖的 Servlet 方法、Service 方法、Dao 方法都在同一个事务中。
		// 同时各个请求都经过这个 Filter，所以当前事务控制的代码在这里只写一遍就行了，
		// 避免了代码的冗余。
		chain.doFilter(request, response);
		
		// 3、核心操作成功结束，可以提交事务
		conn.commit();

	}catch(Exception e){

		// 4、核心操作抛出异常，必须回滚事务
		conn.rollBack();

	}finally{

		// 5、释放数据库连接
		JDBCUtils.releaseConnection(conn);
		
	}

}
```

###### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_3-数据的跨方法传递)(3)数据的跨方法传递

通过 JDBCUtils 工具类获取到的 Connection 对象需要传递给 Dao 方法，让事务涉及到的所有 Dao 方法用的都是同一个 Connection 对象。

但是 Connection 对象无法通过 chain.doFilter() 方法以参数的形式传递过去。

所以从获取到 Connection 对象到使用 Connection 对象中间隔着很多不是我们自己声明的方法——我们无法决定它们的参数。

![images](maven_2022.assets/img011.ee4b484a.png)

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_2-threadlocal-对象的功能)[2] ThreadLocal 对象的功能

![images](maven_2022.assets/img012.7aef6f35.png)

- 全类名：`java.lang.ThreadLocal<T>`
- 泛型 T：要绑定到当前线程的数据的类型
- 具体三个主要的方法：

| 方法名       | 功能                       |
| ------------ | -------------------------- |
| set(T value) | 将数据绑定到当前线程       |
| get()        | 从当前线程获取已绑定的数据 |
| remove()     | 将数据从当前线程移除       |

```java
	ThreadLocal<T>  内部源码分析

	// 绑定线程私有变量
    public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);//通过当前Tread对象获取内部绑定的属性ThreadLocal.ThreadLocalMap threadLocals = null;
        
        if (map != null)
            map.set(this, value);//将当前ThreadLocal当做key，存入的值当做value
        else
            createMap(t, value);
    }
    
    
    
    ThreadLocal.ThreadLocalMap内部结构  Entry数组
    
     static class Entry extends WeakReference<ThreadLocal<?>> {
            /** The value associated with this ThreadLocal. */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }

        private Entry[] table;

强：A a=new A(); 此时引用a强引用对象A；不会被GC
软：SoftReference.java，在内存不够时引用对象会被GC；
弱：WeakReference.java，每次GC都会被回收；
虚：PhantomReference.java，每次GC都会被回收；
```



##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_3-java-代码)[3] Java 代码

```java
// 由于 ThreadLocal 对象需要作为绑定数据时 k-v 对中的 key，所以要保证唯一性
// 加 static 声明为静态资源即可保证唯一性
private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_4声明方法-获取数据库连接)④声明方法：获取数据库连接

```java
/**
 * 工具方法：获取数据库连接并返回
 * @return
 */
public static Connection getConnection() {

    Connection connection = null;

    try {
        // 1、尝试从当前线程检查是否存在已经绑定的 Connection 对象
        connection = threadLocal.get();

        // 2、检查 Connection 对象是否为 null
        if (connection == null) {
            
            // 3、如果为 null，则从数据源获取数据库连接
            connection = dataSource.getConnection();

            // 4、获取到数据库连接后绑定到当前线程
            threadLocal.set(connection);
            
        }
    } catch (SQLException e) {
        e.printStackTrace();
        
        // 为了调用工具方法方便，编译时异常不往外抛
        // 为了不掩盖问题，捕获到的编译时异常封装为运行时异常抛出
        throw new RuntimeException(e);
    }

    return connection;
}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_5声明方法-释放数据库连接)⑤声明方法：释放数据库连接

```java
/**
 * 释放数据库连接
 */
public static void releaseConnection(Connection connection) {

    if (connection != null) {

        try {
            // 在数据库连接池中将当前连接对象标记为空闲
            connection.close();

            // 将当前数据库连接从当前线程上移除
            threadLocal.remove();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_6初步测试)⑥初步测试

![images](maven_2022.assets/images456.png)

```java
public class ImperialCourtTest {

    @Test
    public void testGetConnection() {

        Connection connection = JDBCUtils.getConnection();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM t_emp");
        while (resultSet.next()) {
            Integer id = resultSet.getInt("emp_id");
            String empName = resultSet.getString("emp_name");
            String empPosition = resultSet.getString("emp_position");
            String loginAccount = resultSet.getString("login_account");
            String loginPassword = resultSet.getString("login_password");

            Emp emp = new Emp(id, empName, empPosition, loginAccount, loginPassword);
            System.out.println("emp = " + emp);
        }

        JDBCUtils.releaseConnection(connection);

    }

}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_7完整代码)⑦完整代码

[传送门](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/jdbc-utils.html)

```java
/**
 * 功能1：从数据源获取数据库连接
 * 功能2：从数据库获取到数据库连接后，绑定到本地线程（借助 ThreadLocal）
 * 功能3：释放线程时和本地线程解除绑定
 */
public class JDBCUtils {

    // 数据源成员变量设置为静态资源，保证大对象的单例性；同时保证静态方法中可以访问
    private static DataSource dataSource;

    // 由于 ThreadLocal 对象需要作为绑定数据时 k-v 对中的 key，所以要保证唯一性
    // 加 static 声明为静态资源即可保证唯一性
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    // 在静态代码块中初始化数据源
    static {

        try {
            // 操作思路分析：
            // 从 jdbc.properties 文件中读取连接数据库的信息
            // 为了保证程序代码的可移植性，需要基于一个确定的基准来读取这个文件
            // 确定的基准：类路径的根目录。resources 目录下的内容经过构建操作中的打包操作后会确定放在 WEB-INF/classes 目录下。
            // WEB-INF/classes 目录存放编译好的 *.class 字节码文件，所以这个目录我们就称之为类路径。
            // 类路径无论在本地运行还是在服务器端运行都是一个确定的基准。
            // 操作具体代码：
            // 1、获取当前类的类加载器
            ClassLoader classLoader = JDBCUtils.class.getClassLoader();

            // 2、通过类加载器对象从类路径根目录下读取文件
            InputStream stream = classLoader.getResourceAsStream("jdbc.properties");

            // 3、使用 Properties 类封装属性文件中的数据
            Properties properties = new Properties();
            properties.load(stream);

            // 4、根据 Properties 对象（已封装了数据库连接信息）来创建数据源对象
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();

            // 为了避免在真正抛出异常后，catch 块捕获到异常从而掩盖问题，
            // 这里将所捕获到的异常封装为运行时异常继续抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 工具方法：获取数据库连接并返回
     * @return
     */
    public static Connection getConnection() {

        Connection connection = null;

        try {
            // 1、尝试从当前线程检查是否存在已经绑定的 Connection 对象
            connection = threadLocal.get();

            // 2、检查 Connection 对象是否为 null
            if (connection == null) {

                // 3、如果为 null，则从数据源获取数据库连接
                connection = dataSource.getConnection();

                // 4、获取到数据库连接后绑定到当前线程
                threadLocal.set(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();

            // 为了调用工具方法方便，编译时异常不往外抛
            // 为了不掩盖问题，捕获到的编译时异常封装为运行时异常抛出
            throw new RuntimeException(e);
        }

        return connection;
    }

    /**
     * 释放数据库连接
     */
    public static void releaseConnection(Connection connection) {

        if (connection != null) {

            try {
                // 在数据库连接池中将当前连接对象标记为空闲
                connection.close();

                // 将当前数据库连接从当前线程上移除
                threadLocal.remove();

            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }

    }

}
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_4、basedao)4、BaseDao

![images](maven_2022.assets/images343.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_1泛型的说明)①泛型的说明

![images](maven_2022.assets/img015.685c2745.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_2创建-queryrunner-对象)②创建 QueryRunner 对象

```java
    // DBUtils 工具包提供的数据库操作对象
    private QueryRunner runner = new QueryRunner();
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_3通用增删改方法)③通用增删改方法

**特别说明**：在 BaseDao 方法中获取数据库连接但是不做释放，因为我们要在<u>控制事务的 Filter 中统一释放。</u>

```java
/**
 * 通用的增删改方法，insert、delete、update 操作都可以用这个方法
 * @param sql 执行操作的 SQL 语句
 * @param parameters SQL 语句的参数
 * @return 受影响的行数
 */
public int update(String sql, Object ... parameters) {

    try {
        Connection connection = JDBCUtils.getConnection();

        int affectedRowNumbers = runner.update(connection, sql, parameters);
        
        return affectedRowNumbers;
    } catch (SQLException e) {
        e.printStackTrace();

        // 如果真的抛出异常，则将编译时异常封装为运行时异常抛出
        new RuntimeException(e);
        
        return 0;
    }

}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_4查询单个对象)④查询单个对象

```java
/**
 * 查询单个对象
 * @param sql 执行查询的 SQL 语句
 * @param entityClass 实体类对应的 Class 对象
 * @param parameters 传给 SQL 语句的参数
 * @return 查询到的实体类对象
 */
public T getSingleBean(String sql, Class<T> entityClass, Object ... parameters) {

    try {
        // 获取数据库连接
        Connection connection = JDBCUtils.getConnection();

        return runner.query(connection, sql, new BeanHandler<>(entityClass), parameters);

    } catch (SQLException e) {
        e.printStackTrace();

        // 如果真的抛出异常，则将编译时异常封装为运行时异常抛出
        new RuntimeException(e);
    }

    return null;
}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_5查询多个对象)⑤查询多个对象

```java
/**
 * 查询返回多个对象的方法
 * @param sql 执行查询操作的 SQL 语句
 * @param entityClass 实体类的 Class 对象
 * @param parameters SQL 语句的参数
 * @return 查询结果
 */
public List<T> getBeanList(String sql, Class<T> entityClass, Object ... parameters) {
    try {
        // 获取数据库连接
        Connection connection = JDBCUtils.getConnection();

        return runner.query(connection, sql, new BeanListHandler<>(entityClass), parameters);

    } catch (SQLException e) {
        e.printStackTrace();

        // 如果真的抛出异常，则将编译时异常封装为运行时异常抛出
        new RuntimeException(e);
    }

    return null;
}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_6测试)⑥测试

![images](maven_2022.assets/images123.png)

```java
private BaseDao<Emp> baseDao = new BaseDao<>();

@Test
public void testGetSingleBean() {
	// 返回的结果集字段名称和实体类名称需要对应，使用字段别名
    String sql = "select emp_id empId,emp_name empName,emp_position empPosition,login_account loginAccount,login_password loginPassword from t_emp where emp_id=?";

    Emp emp = baseDao.getSingleBean(sql, Emp.class, 1);

    System.out.println("emp = " + emp);

}

@Test
public void testGetBeanList() {

    String sql = "select emp_id empId,emp_name empName,emp_position empPosition,login_account loginAccount,login_password loginPassword from t_emp";

    List<Emp> empList = baseDao.getBeanList(sql, Emp.class);

    for (Emp emp : empList) {
        System.out.println("emp = " + emp);
    }

}

@Test
public void testUpdate() {

    String sql = "update t_emp set emp_position=? where emp_id=?";

    String empPosition = "minister";
    String empId = "3";

    int affectedRowNumber = baseDao.update(sql, empPosition, empId);

    System.out.println("affectedRowNumber = " + affectedRowNumber);

}
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse02.html#_5、子类-dao)5、子类 Dao

创建接口和实现类如下：

![images](maven_2022.assets/images4532.png)

## 第三节 搭建环境：事务控制

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse03.html#_1、总体思路)1、总体思路

![images](maven_2022.assets/img016.934d82c3.png)

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse03.html#_2、transactionfilter)2、TransactionFilter

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse03.html#_1创建-filter-类)①创建 Filter 类

![images](maven_2022.assets/images65343443.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse03.html#_2transactionfilter-完整代码)②TransactionFilter 完整代码

```java
public class TransactionFilter implements Filter {

    // 声明集合保存静态资源扩展名
    private static Set<String> staticResourceExtNameSet;

    static {
        staticResourceExtNameSet = new HashSet<>();
        staticResourceExtNameSet.add(".png");
        staticResourceExtNameSet.add(".jpg");
        staticResourceExtNameSet.add(".css");
        staticResourceExtNameSet.add(".js");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 前置操作：排除静态资源
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String servletPath = request.getServletPath();
        if (servletPath.contains(".")) {
            String extName = servletPath.substring(servletPath.lastIndexOf("."));

            if (staticResourceExtNameSet.contains(extName)) {

                // 如果检测到当前请求确实是静态资源，则直接放行，不做事务操作
                filterChain.doFilter(servletRequest, servletResponse);

                // 当前方法立即返回
                return ;
            }

        }

        Connection connection = null;

        try{

            // 1、获取数据库连接
            connection = JDBCUtils.getConnection();

            // 重要操作：关闭自动提交功能
            connection.setAutoCommit(false);

            // 2、核心操作
            filterChain.doFilter(servletRequest, servletResponse);

            // 3、提交事务
            connection.commit();

        }catch (Exception e) {

            try {
                // 4、回滚事务
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // 页面显示：将这里捕获到的异常发送到指定页面显示
            // 获取异常信息
            String message = e.getMessage();

            // 将异常信息存入请求域
            request.setAttribute("systemMessage", message);

            // 将请求转发到指定页面
            request.getRequestDispatcher("/").forward(request, servletResponse);

        }finally {

            // 5、释放数据库连接
            JDBCUtils.releaseConnection(connection);

        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    @Override
    public void destroy() {}
}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse03.html#_3配置-web-xml)③配置 web.xml

**注意**：需要首先将当前工程改成 Web 工程。

![images](maven_2022.assets/images5645232.png)

```xml
<filter>
    <filter-name>txFilter</filter-name>
    <filter-class>com.atguigu.imperial.court.filter.TransactionFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>txFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse03.html#_4注意点)④注意点

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse03.html#_1-确保异常回滚)[1]确保异常回滚

在程序执行的过程中，必须让所有 catch 块都把编译时异常转换为运行时异常抛出；如果不这么做，在 TransactionFilter 中 catch 就无法捕获到底层抛出的异常，那么该回滚的时候就无法回滚。

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse03.html#_2-谨防数据库连接提前释放)[2]谨防数据库连接提前释放

由于诸多操作都是在使用同一个数据库连接，那么中间任何一个环节释放数据库连接都会导致后续操作无法正常完成。

## 第四节 搭建环境：表述层

### 视图层拓展
#### 后端工程师与前端工程师交互

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro000-dev-story/chapter05/content.html#服务器端渲染)**服务器端渲染**

除了我们熟悉的JSP，还有Velocity、Freemarker、Thymeleaf等视图模板技术。虽然具体语法各不相同，但是它们都有一个共通的特点，就是在固定内容中可以穿插表达式等形式的动态内容。将视图模板中的动态内容转换为对应的Java代码并执行，然后使用计算得到的具体数据替换原来的动态部分。这样整个文件的动态内容就可以作为确定的响应结果返回给浏览器。在这种模式下，前端工程师将前端页面全部开发完成，交给后端程序员加入到项目中。此时不可避免的需要后端程序员根据需要对前端代码进行补充和调整。

![images](maven_2022.assets/009.81a1c9af.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro000-dev-story/chapter05/content.html#前后端分离)**前后端分离**

前后端分离模式下，前端程序和后端程序使用JSON格式进行交互，所以项目启动时前端工程和后端工程师需要坐在一起开会，商量确定JSON格式的具体细节。然后分头开发。后端工程师在把后端的代码发布到测试服务器前，前端工程师无法调用后端程序拿到真实数据，所以使用Mock.js生成假数据。直到后端工程师开发完成，后端程序发布到了测试服务器上，前端工程师再从Mock.js切换到实际后端代码。

![images](maven_2022.assets/010.d36c34ee.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro000-dev-story/chapter05/content.html#开发工程师与运维工程师交互)开发工程师与运维工程师交互

开发工程师有项目需要部署的时候，如果需要交给运维工程操作，那么除了要部署的项目本身，还要编写可以自动化部署项目的Shell脚本，一起交给运维工程师去部署项目。

另外还有一种情况，程序员开发具体模块时需要设计创建本模块的数据库表，本地建表调试确定后，把建表语句发送给运维工程师，在正式环境服务器上创建数据库表。因为正式服务器上通常是不给开发工程师操作权限的。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro000-dev-story/chapter05/content.html#开发工程师与产品经理交互)开发工程师与产品经理交互

你以为完成组长交给的任务就可以轻松优雅的下班？图样图森破！产品经理会直接找到你，让你改需求！你90%的精力其实都用来忍住揍他的冲动。

![images](maven_2022.assets/011.3ac71f72.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro000-dev-story/chapter05/content.html#开发工程师与测试工程师)开发工程师与测试工程师

开发工程师将代码提交到版本控制服务器（SVN或Git的代码托管中心），然后根据版本控制服务器上的代码部署到测试服务器上，测试工程师访问测试服务器上的应用进行测试。发现Bug通过Bug管理软件通知开发工程师。

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_1、视图模板技术-thymeleaf)1、视图模板技术 Thymeleaf

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_1服务器端渲染)①服务器端渲染

[参考资料(opens new window)](http://heavy_code_industry.gitee.io/code_heavy_industry/pro000-dev-story/chapter05/content.html)

![images](maven_2022.assets/img019.81a1c9af.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_2thymeleaf-简要工作机制)②Thymeleaf 简要工作机制

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_1-初始化阶段)[1]初始化阶段

- 目标：创建 TemplateEngine 对象
- 封装：因为对每一个请求来说，TemplateEngine 对象使用的都是同一个，所以在初始化阶段准备好

![images](maven_2022.assets/images456321.png)

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_2-请求处理阶段)[2]请求处理阶段

![images](maven_2022.assets/img021.353c9b83.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_3逻辑视图与物理视图)③逻辑视图与物理视图

假设有下列页面地址：

> /WEB-INF/pages/apple.html
> /WEB-INF/pages/banana.html
> /WEB-INF/pages/orange.html
> /WEB-INF/pages/grape.html
> /WEB-INF/pages/egg.html

这样的地址可以**直接访问**到页面本身，我们称之为：**物理视图**。而将物理视图中前面、后面的固定内容抽取出来，让每次请求指定中间变化部分即可，那么**中间变化**部分就叫：**逻辑视图**。

![images](maven_2022.assets/img022.0761b2c1.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_4viewbaseservlet-完整代码)④ViewBaseServlet 完整代码

为了简化视图页面处理过程，我们将 Thymeleaf 模板引擎的初始化和请求处理过程封装到一个 Servlet 基类中：ViewBaseServlet。以后负责具体模块业务功能的 Servlet 继承该基类即可直接使用。

[传送门](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04-extend01.html)

```java
public class ViewBaseServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {

        // 1.获取ServletContext对象
        ServletContext servletContext = this.getServletContext();

        // 2.创建Thymeleaf解析器对象
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);

        // 3.给解析器对象设置参数
        // ①HTML是默认模式，明确设置是为了代码更容易理解
        templateResolver.setTemplateMode(TemplateMode.HTML);

        // ②设置前缀
        String viewPrefix = servletContext.getInitParameter("view-prefix");

        templateResolver.setPrefix(viewPrefix);

        // ③设置后缀
        String viewSuffix = servletContext.getInitParameter("view-suffix");

        templateResolver.setSuffix(viewSuffix);

        // ④设置缓存过期时间（毫秒）
        templateResolver.setCacheTTLMs(60000L);

        // ⑤设置是否缓存
        templateResolver.setCacheable(true);

        // ⑥设置服务器端编码方式
        templateResolver.setCharacterEncoding("utf-8");

        // 4.创建模板引擎对象
        templateEngine = new TemplateEngine();

        // 5.给模板引擎对象设置模板解析器
        templateEngine.setTemplateResolver(templateResolver);

    }

    protected void processTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 1.设置响应体内容类型和字符集
        resp.setContentType("text/html;charset=UTF-8");

        // 2.创建WebContext对象
        WebContext webContext = new WebContext(req, resp, getServletContext());

        // 3.处理模板数据
        templateEngine.process(templateName, webContext, resp.getWriter());
    }
}
```

**特别提醒**：这个类**不需要掌握**，因为以后都被框架封装了，我们现在只是暂时用一下。

![images](maven_2022.assets/imag121es.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_5声明初始化参数)⑤声明初始化参数

![images](maven_2022.assets/images23fre.png)

```xml
<!-- 配置 Web 应用初始化参数指定视图前缀、后缀 -->
<!-- 
    物理视图举例：/WEB-INF/pages/index.html
    对应逻辑视图：index
-->
<context-param>
    <param-name>view-prefix</param-name>
    <param-value>/WEB-INF/pages/</param-value>
</context-param>
<context-param>
    <param-name>view-suffix</param-name>
    <param-value>.html</param-value>
</context-param>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_6thymeleaf-的页面语法)⑥Thymeleaf 的页面语法

[传送门(opens new window)](http://heavy_code_industry.gitee.io/code_heavy_industry/pro001-javaweb/lecture/chapter08)

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_2、modelbaseservlet)2、ModelBaseServlet

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_1提出问题)①提出问题

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_1-我们的需求)[1]我们的需求

![images](maven_2022.assets/images32gh.png)

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_2-httpservlet-的局限)[2]HttpServlet 的局限

- doGet() 方法：处理 GET 请求
- doPost() 方法：处理 POST 请求

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_2解决方案)②解决方案

- 每个请求附带一个**请求参数**，表明自己要调用的目标方法
- Servlet 根据目标方法名通过**反射**调用目标方法

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_3modelbaseservlet-完整代码)③ModelBaseServlet 完整代码

![iamges](maven_2022.assets/iamges456ff.png)

**特别提醒**：为了配合 TransactionFilter 实现事务控制，捕获的异常必须抛出。

[传送门](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04-extend02.html)

```java
public class ModelBaseServlet extends ViewBaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 在doGet()方法中调用doPost()方法，这样就可以在doPost()方法中集中处理所有请求
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1.在所有request.getParameter()前面设置解析请求体的字符集
        request.setCharacterEncoding("UTF-8");

        // 2.从请求参数中获取method对应的数据
        String method = request.getParameter("method");

        // 3.通过反射调用method对应的方法
        // ①获取Class对象
        Class<? extends ModelBaseServlet> clazz = this.getClass();

        try {
            // ②获取method对应的Method对象  ，约定所有方法都需要传入HttpServletRequest  HttpServletResponse
            Method methodObject = clazz.getDeclaredMethod(method, HttpServletRequest.class, HttpServletResponse.class);

            // ③打开访问权限
            methodObject.setAccessible(true);

            // ④通过Method对象调用目标方法
            methodObject.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();

            // 重要提醒：为了配合 TransactionFilter 实现事务控制，捕获的异常必须抛出。
            throw new RuntimeException(e);
        }
    }

}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse04.html#_4继承关系)④继承关系

![images](maven_2022.assets/img024.d0ec152a.png)

## 第五节 搭建环境：辅助功能

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse05.html#_1、常量类)1、常量类

![images](maven_2022.assets/images345ggg.png)

```java
public class ImperialCourtConst {
    public static final String LOGIN_FAILED_MESSAGE = "账号、密码错误，不可进宫！";
    public static final String ACCESS_DENIED_MESSAGE = "宫闱禁地，不得擅入！";
}
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse05.html#_2、md5-加密工具方法)2、MD5 加密工具方法

![images](maven_2022.assets/imagestyyyy.png)

```java
public class MD5Util {

    /**
     * 针对明文字符串执行MD5加密
     * @param source
     * @return
     */
    public static String encode(String source) {

        // 1.判断明文字符串是否有效
        if (source == null || "".equals(source)) {
            throw new RuntimeException("用于加密的明文不可为空");
        }

        // 2.声明算法名称
        String algorithm = "md5";

        // 3.获取MessageDigest对象
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // 4.获取明文字符串对应的字节数组
        byte[] input = source.getBytes();

        // 5.执行加密
        byte[] output = messageDigest.digest(input);

        // 6.创建BigInteger对象
        int signum = 1;
        BigInteger bigInteger = new BigInteger(signum, output);

        // 7.按照16进制将bigInteger的值转换为字符串
        int radix = 16;
        String encoded = bigInteger.toString(radix).toUpperCase();

        return encoded;
    }

}
```


### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse05.html#_3、日志配置文件)3、日志配置文件

![images](maven_2022.assets/images567yt.png)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">
    <!-- 指定日志输出的位置 -->
    <appender name="STDOUT"
              class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <!-- 日志输出的格式 -->
            <!-- 按照顺序分别是：时间、日志级别、线程名称、打印日志的类、日志主体内容、换行 -->
            <pattern>[%d{HH:mm:ss.SSS}] [%-5level] [%thread] [%logger] [%msg]%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!-- 设置全局日志级别。日志级别按顺序分别是：DEBUG、INFO、WARN、ERROR -->
    <!-- 指定任何一个日志级别都只打印当前级别和后面级别的日志。 -->
    <root level="INFO">
        <!-- 指定打印日志的appender，这里通过“STDOUT”引用了前面配置的appender -->
        <appender-ref ref="STDOUT" />
    </root>

    <!-- 专门给某一个包指定日志级别 -->
    <logger name="com.atguigu" level="DEBUG" additivity="false">
        <appender-ref ref="STDOUT" />
    </logger>

</configuration>
```

## 第六节 业务功能：登录

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_1、显示首页)1、显示首页

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_1流程图)①流程图

![images](maven_2022.assets/img028.d8341124.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_2创建-portalservlet)②创建 PortalServlet

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_1-创建-java-类)[1]创建 Java 类

![images](maven_2022.assets/images122.png)

```java
public class PortalServlet extends ViewBaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 声明要访问的首页的逻辑视图
        String templateName = "index";
        
        // 调用父类的方法根据逻辑视图名称渲染视图
        processTemplate(templateName, req, resp);
    }
}
```

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_2-注册)[2]注册

![images](maven_2022.assets/imagesrttt.png)

```xml
<servlet>
    <servlet-name>portalServlet</servlet-name>
    <servlet-class>com.atguigu.imperial.court.servlet.module.PortalServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>portalServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_3在-index-html-中编写登录表单)③在 index.html 中编写登录表单

![iamgs](maven_2022.assets/iamgsvf.png)

```html
<!DOCTYPE html>
<html lang="en" xml:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<!-- @{/auth} 解析后：/demo/auth   添加contextPath路径 getServletContext().getContextPath()   request.getContextPath()-->
<form th:action="@{/auth}" method="post">
    <!-- 传递 method 请求参数，目的是为了让当前请求调用 AuthServlet 中的 login() 方法 -->
    <input type="hidden" name="method" value="login" />

    <!-- th:text 解析表达式后会替换标签体 -->
    <!-- ${attrName} 从请求域获取属性名为 attrName 的属性值 -->
    <p th:text="${message}"></p>
    <p th:text="${systemMessage}"></p>
    
    账号：<input type="text" name="loginAccount"/><br/>
    密码：<input type="password" name="loginPassword"><br/>
    <button type="submit">进宫</button>
</form>

</body>
</html>
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_2、登录操作)2、登录操作

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_1流程图-2)①流程图

![images](maven_2022.assets/img031.1ee5f5f7.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_2创建-empservice)②创建 EmpService

![images](maven_2022.assets/imagehhhhs.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_3创建登录失败异常)③创建登录失败异常

![images](maven_2022.assets/imagesffff.png)

```java
public class LoginFailedException extends RuntimeException {

    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    public LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_4增加常量声明)④增加常量声明

![images](maven_2022.assets/imagessdsds.png)

```java
public class ImperialCourtConst {

    public static final String LOGIN_FAILED_MESSAGE = "账号、密码错误，不可进宫！";
    public static final String ACCESS_DENIED_MESSAGE = "宫闱禁地，不得擅入！";
    public static final String LOGIN_EMP_ATTR_NAME = "loginInfo";

}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_5创建-authservlet)⑤创建 AuthServlet

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_1-创建-java-类-2)[1]创建 Java 类

![images](maven_2022.assets/images545454.png)

```java
public class AuthServlet extends ModelBaseServlet {

    private EmpService empService = new EmpServiceImpl();

    protected void login(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1、获取请求参数
            String loginAccount = request.getParameter("loginAccount");
            String loginPassword = request.getParameter("loginPassword");

            // 2、调用 EmpService 方法执行登录逻辑
            Emp emp = empService.getEmpByLoginAccount(loginAccount, loginPassword);

            // 3、通过 request 获取 HttpSession 对象
            HttpSession session = request.getSession();

            // 4、将查询到的 Emp 对象存入 Session 域
            session.setAttribute(ImperialCourtConst.LOGIN_EMP_ATTR_NAME, emp);

            // 5、前往指定页面视图
            String templateName = "temp";
            processTemplate(templateName, request, response);

        } catch (Exception e) {
            e.printStackTrace();

            // 6、判断此处捕获到的异常是否是登录失败异常
            if (e instanceof LoginFailedException) {
                // 7、如果是登录失败异常则跳转回登录页面
                // ①将异常信息存入请求域
                request.setAttribute("message", e.getMessage());

                // ②处理视图：index
                processTemplate("index", request, response);

            }else {
                // 8、如果不是登录异常则封装为运行时异常继续抛出
                throw new RuntimeException(e);

            }

        }

    }
}
```

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_2-注册-2)[2]注册

![images](maven_2022.assets/images34555.png)

```xml
<servlet>
    <servlet-name>authServlet</servlet-name>
    <servlet-class>com.atguigu.imperial.court.servlet.module.AuthServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>authServlet</servlet-name>
    <url-pattern>/auth</url-pattern>
</servlet-mapping>
```


#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_6empservice-方法)⑥EmpService 方法

![images](maven_2022.assets/images12121.png)

```java
public class EmpServiceImpl implements EmpService {

    private EmpDao empDao = new EmpDaoImpl();

    @Override
    public Emp getEmpByLoginAccount(String loginAccount, String loginPassword) {

        // 1、对密码执行加密
        String encodedLoginPassword = MD5Util.encode(loginPassword);

        // 2、根据账户和加密密码查询数据库
        Emp emp = empDao.selectEmpByLoginAccount(loginAccount, encodedLoginPassword);

        // 3、检查 Emp 对象是否为 null
        if (emp != null) {
            //	①不为 null：返回 Emp
            return emp;
        } else {
            //	②为 null：抛登录失败异常
            throw new LoginFailedException(ImperialCourtConst.LOGIN_FAILED_MESSAGE);
        }
    }
}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_7empdao-方法)⑦EmpDao 方法

![images](maven_2022.assets/images121vvv21.png)

```java
public class EmpDaoImpl extends BaseDao<Emp> implements EmpDao {
    @Override
    public Emp selectEmpByLoginAccount(String loginAccount, String encodedLoginPassword) {

        // 1、编写 SQL 语句
        String sql = "select emp_id empId," +
                "emp_name empName," +
                "emp_position empPosition," +
                "login_account loginAccount," +
                "login_password loginPassword " +
                "from t_emp where login_account=? and login_password=?";

        // 2、调用父类方法查询单个对象
        return super.getSingleBean(sql, Emp.class, loginAccount, encodedLoginPassword);
    }
}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_8临时页面)⑧临时页面

![images](maven_2022.assets/imageccccs.png)

```html
<!DOCTYPE html>
<html lang="en" xml:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>临时</title>
</head>
<body>

    <p th:text="${session.loginInfo}"></p>

</body>
</html>
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_3、退出登录)3、退出登录

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_1在临时页面编写超链接)①在临时页面编写超链接

![images](maven_2022.assets/imageslkjbh.png)

```html
<a th:href="@{/auth?method=logout}">退朝</a>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse06.html#_2在-authservlet-编写退出逻辑)②在 AuthServlet 编写退出逻辑

![images](maven_2022.assets/imageaaaaas.png)

```java
protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // 1、通过 request 对象获取 HttpSession 对象
    HttpSession session = request.getSession();

    // 2、将 HttpSession 对象强制失效
    session.invalidate();

    // 3、回到首页
    String templateName = "index";
    processTemplate(templateName, request, response);
}
```

## 第七节 业务功能：显示奏折列表

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_1、流程图)1、流程图

![iamge](maven_2022.assets/img040.39773cf4.png)

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_2、创建组件)2、创建组件

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_1创建-workservlet)①创建 WorkServlet

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_1-创建-java-类)[1]创建 Java 类

刚开始是空的，还没有写方法：

![iamges](maven_2022.assets/iamgesasasa.png)

```java
public class WorkServlet extends ModelBaseServlet {
    
    private MemorialsService memorialsService = new MemorialsServiceImpl();
    
}
```

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_2-注册)[2]注册

```xml
<servlet>
    <servlet-name>workServlet</servlet-name>
    <servlet-class>com.atguigu.imperial.court.servlet.module.WorkServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>workServlet</servlet-name>
    <url-pattern>/work</url-pattern>
</servlet-mapping>
```



#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_2创建-memorialsservice)②创建 MemorialsService

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_1-接口)[1]接口

![images](maven_2022.assets/imagessav.png)

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_2-实现类)[2]实现类

![images](maven_2022.assets/imagesbbbbb.png)

```java
public class MemorialsServiceImpl implements MemorialsService {

    private MemorialsDao memorialsDao = new MemorialsDaoImpl();

}
```



### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_3、workservlet-方法)3、WorkServlet 方法

![iamges](maven_2022.assets/iamgaasxxxes.png)

```java
protected void showMemorialsDigestList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // 1、调用 Service 方法查询数据
    List<Memorials> memorialsList = memorialsService.getAllMemorialsDigest();

    // 2、将查询得到的数据存入请求域
    request.setAttribute("memorialsList", memorialsList);

    // 3、渲染视图
    String templateName = "memorials-list";
    processTemplate(templateName, request, response);
}
```



### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_4、memorialsservice-方法)4、MemorialsService 方法

![images](maven_2022.assets/imagesbnmv.png)

```java
@Override
public List<Memorials> getAllMemorialsDigest() {
    return memorialsDao.selectAllMemorialsDigest();
}
```



### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_5、memorialsdao-方法)5、MemorialsDao 方法

![images](maven_2022.assets/imageszxccc.png)

```java
@Override
public List<Memorials> selectAllMemorialsDigest() {

    String sql = "select memorials_id memorialsId,\n" +
            "       memorials_title memorialsTitle,\n" +
            "       concat(left(memorials_content, 10), \"...\") memorialsContentDigest,\n" +
            "       emp_name memorialsEmpName,\n" +
            "       memorials_create_time memorialsCreateTime,\n" +
            "       memorials_status memorialsStatus\n" +
            "from t_memorials m left join  t_emp e on m.memorials_emp=e.emp_id;";

    return getBeanList(sql, Memorials.class);
}
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_6、页面显示)6、页面显示

![images](maven_2022.assets/imageszxvbbbb.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_1页面上的样式声明)①页面上的样式声明

```html
<style type="text/css">
    table {
        border-collapse: collapse;
        margin: 0px auto 0px auto;
    }

    table th, td {
        border: 1px solid black;
        text-align: center;
    }

    div {
        text-align: right;
    }
</style>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_2用户登录信息部分)②用户登录信息部分

```html
<!-- 登录信息部分 -->
<div>
    <span th:if="${session.loginInfo.empPosition == 'emperor'}">恭请皇上圣安</span>
    <span th:if="${session.loginInfo.empPosition == 'minister'}">
        给
        <span th:text="${session.loginInfo.empName}">XXX</span>
        大人请安
    </span>
    <a th:href="@{/auth?method=logout}">退朝</a>
</div>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_3数据展示信息部分)③数据展示信息部分

>  Thymeleaf  :if   switch case   遍历

```html
<!-- 数据显示部分 -->
<table>
    <thead>
        <tr>
            <th>奏折标题</th>
            <th>内容摘要</th>
            <th>上疏大臣</th>
            <th>上疏时间</th>
            <th>奏折状态</th>
            <th>奏折详情</th>
        </tr>
    </thead>
    <tbody th:if="${#lists.isEmpty(memorialsList)}">
        <tr>
            <td colspan="6">没有人上过折子</td>
        </tr>
    </tbody>
    <tbody th:if="${not #lists.isEmpty(memorialsList)}">
        <tr th:each="memorials : ${memorialsList}">
            <td th:switch="${memorials.memorialsStatus}">
                <span th:text="${memorials.memorialsTitle}" th:case="0" style="color: red;">奏折标题</span>
                <span th:text="${memorials.memorialsTitle}" th:case="1" style="color: blue;">奏折标题</span>
                <span th:text="${memorials.memorialsTitle}" th:case="2">奏折标题</span>
            </td>
            <td th:switch="${memorials.memorialsStatus}">
                <span th:text="${memorials.memorialsContentDigest}" th:case="0" style="color: red;">内容摘要</span>
                <span th:text="${memorials.memorialsContentDigest}" th:case="1" style="color: blue;">内容摘要</span>
                <span th:text="${memorials.memorialsContentDigest}" th:case="2">内容摘要</span>
            </td>
            <td th:switch="${memorials.memorialsStatus}">
                <span th:text="${memorials.memorialsEmpName}" th:case="0" style="color: red;">上疏大臣</span>
                <span th:text="${memorials.memorialsEmpName}" th:case="1" style="color: blue;">上疏大臣</span>
                <span th:text="${memorials.memorialsEmpName}" th:case="2">上疏大臣</span>
            </td>
            <td th:switch="${memorials.memorialsStatus}">
                <span th:text="${memorials.memorialsCreateTime}" th:case="0" style="color: red;">上疏时间</span>
                <span th:text="${memorials.memorialsCreateTime}" th:case="1" style="color: blue;">上疏时间</span>
                <span th:text="${memorials.memorialsCreateTime}" th:case="2">上疏时间</span>
            </td>
            <td th:switch="${memorials.memorialsStatus}">
                <span th:case="0" style="color: red;">未读</span>
                <span th:case="1" style="color: blue;">已读</span>
                <span th:case="2">已批示</span>
            </td>

            <td>
                <a th:href="@{/work?method=detail}">奏折详情</a>
            </td>
        </tr>
    </tbody>
</table>
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse07.html#_7、和登录成功对接)7、和登录成功对接 【重定向到列表页面】

![images](maven_2022.assets/imagenhs.png)

```java
protected void login(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    try {
        // 1、获取请求参数
        String loginAccount = request.getParameter("loginAccount");
        String loginPassword = request.getParameter("loginPassword");

        // 2、调用 EmpService 方法执行登录逻辑
        Emp emp = empService.getEmpByLoginAccount(loginAccount, loginPassword);

        // 3、通过 request 获取 HttpSession 对象
        HttpSession session = request.getSession();

        // 4、将查询到的 Emp 对象存入 Session 域
        session.setAttribute(ImperialCourtConst.LOGIN_EMP_ATTR_NAME, emp);

        // 5、前往指定页面视图
        // 前往临时页面
        // String templateName = "temp";
        // processTemplate(templateName, request, response);

        // 前往正式的目标地址
        response.sendRedirect(request.getContextPath() + "/work?method=showMemorialsDigestList");

    } catch (Exception e) {
        e.printStackTrace();

        // 6、判断此处捕获到的异常是否是登录失败异常
        if (e instanceof LoginFailedException) {
            // 7、如果是登录失败异常则跳转回登录页面
            // ①将异常信息存入请求域
            request.setAttribute("message", e.getMessage());

            // ②处理视图：index
            processTemplate("index", request, response);

        }else {
            // 8、如果不是登录异常则封装为运行时异常继续抛出
            throw new RuntimeException(e);

        }

    }

}
```

## 第八节 业务功能：显示奏折详情

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse08.html#_1、流程图)1、流程图

![images](maven_2022.assets/img046.51eebd72.png)

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse08.html#_2、调整奏折列表页面的超链接)2、调整奏折列表页面的超链接

```html
<a th:href="@{/work(method='showMemorialsDetail',memorialsId=${memorials.memorialsId})}">奏折详情</a>
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse08.html#_3、workservlet-方法)3、WorkServlet 方法

![iamges](maven_2022.assets/iamgeppps.png)

```java
protected void showMemorialsDetail(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    // 1、从请求参数读取 memorialsId
    String memorialsId = request.getParameter("memorialsId");

    // 2、根据 memorialsId 从 Service 中查询 Memorials 对象
    Memorials memorials = memorialsService.getMemorialsDetailById(memorialsId);

    // 3、将 Memorials 对象存入请求域
    request.setAttribute("memorials", memorials);

    // 4、解析渲染视图
    String templateName = "memorials_detail";
    processTemplate(templateName, request, response);

}
```



### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse08.html#_4、memorialsservice-方法)4、MemorialsService 方法

![images](maven_2022.assets/image5555s.png)

```java
    @Override
    public Memorials getMemorialsDetailById(String memorialsId) {

        return memorialsDao.selectMemorialsById(memorialsId);

    }
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse08.html#_5、memorialsdao-方法)5、MemorialsDao 方法

![images](maven_2022.assets/images45555.png)

```java
@Override
public Memorials selectMemorialsById(String memorialsId) {

    String sql = "select memorials_id memorialsId,\n" +
            "       memorials_title memorialsTitle,\n" +
            "       memorials_content memorialsContent,\n" +
            "       emp_name memorialsEmpName,\n" +
            "       memorials_create_time memorialsCreateTime,\n" +
            "       memorials_status memorialsStatus,\n" +
            "       feedback_time feedbackTime,\n" +
            "       feedback_content feedbackContent\n" +
            "from t_memorials m left join  t_emp e on m.memorials_emp=e.emp_id " +
            "where memorials_id=?;";

    return getSingleBean(sql, Memorials.class, memorialsId);
}
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse08.html#_6、详情页)6、详情页

![images](maven_2022.assets/imageddsds.png)

```html
<!-- 登录信息部分 -->
<div>
    <span th:if="${session.loginInfo.empPosition == 'emperor'}">恭请皇上圣安</span>
    <span th:if="${session.loginInfo.empPosition == 'minister'}">给<span th:text="${session.loginInfo.empName}">XXX</span>大人请安</span>
    <a th:href="@{/auth?method=logout}">退朝</a>
</div>

<table>
    <tr>
        <td>奏折标题</td>
        <td th:text="${memorials.memorialsTitle}"></td>
    </tr>
    <tr>
        <td>上疏大臣</td>
        <td th:text="${memorials.memorialsEmpName}"></td>
    </tr>
    <tr>
        <td>上疏时间</td>
        <td th:text="${memorials.memorialsCreateTime}"></td>
    </tr>
    <tr>
        <td>奏折内容</td>
        <td th:text="${memorials.memorialsContent}"></td>
    </tr>
    <tr th:if="${memorials.memorialsStatus == 2}">
        <td>批复时间</td>
        <td th:text="${memorials.feedbackTime}"></td>
    </tr>
    <tr th:if="${memorials.memorialsStatus == 2}">
        <td>批复时间</td>
        <td th:text="${memorials.feedbackContent}"></td>
    </tr>
</table>

<div th:if="${memorials.memorialsStatus != 2}">
    <form th:action="@{/work}" method="post">

        <input type="hidden" name="method" value="feedBack" />
        <input type="hidden" name="memorialsId" th:value="${memorials.memorialsId}"/>

        <textarea name="feedbackContent"></textarea>

        <button type="submit">御批</button>

    </form>
</div>

<a th:href="@{/work?method=showMemorialsDigestList}">返回列表</a>
```



### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse08.html#_7、更新状态)7、更新状态

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse08.html#_1业务逻辑规则)①业务逻辑规则

一份**未读**奏折，点击查看后，需要从未读变成已读。

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse08.html#_2workservlet-方法)②WorkServlet 方法

![iamges](maven_2022.assets/iamgesdddd.png)

增加判断：

```java
protected void showMemorialsDetail(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    // 1、从请求参数读取 memorialsId
    String memorialsId = request.getParameter("memorialsId");

    // 2、根据 memorialsId 从 Service 中查询 Memorials 对象
    Memorials memorials = memorialsService.getMemorialsDetailById(memorialsId);

    // **********************补充功能**********************
    // 获取当前奏折对象的状态
    Integer memorialsStatus = memorials.getMemorialsStatus();
    
    // 判断奏折状态
    if (memorialsStatus == 0) {
        // 更新奏折状态：数据库修改
        memorialsService.updateMemorialsStatusToRead(memorialsId);

        // 更新奏折状态：当前对象修改
        memorials.setMemorialsStatus(1);
    }
    // **********************补充功能**********************

    // 3、将 Memorials 对象存入请求域
    request.setAttribute("memorials", memorials);

    // 4、解析渲染视图
    String templateName = "memorials_detail";
    processTemplate(templateName, request, response);

}
```



#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse08.html#_3memorialsservice-方法)③MemorialsService 方法

![images](maven_2022.assets/imagesbbvcvcv.png)

```java
@Override
public void updateMemorialsStatusToRead(String memorialsId) {
    memorialsDao.updateMemorialsStatusToRead(memorialsId);
}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse08.html#_4memorialsdao-方法)④MemorialsDao 方法

![images](maven_2022.assets/imagesvvvc.png)

```java
@Override
public void updateMemorialsStatusToRead(String memorialsId) {
    String sql = "update t_memorials set memorials_status=1 where memorials_id=?";
    update(sql, memorialsId);
}
```

## 第九节 业务功能：批复奏折

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse09.html#_1、本质)1、本质

提交表单，更新数据。

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse09.html#_2、workservlet-方法)2、WorkServlet 方法

```java
protected void feedBack(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // 获取表单提交的请求参数
    String memorialsId = request.getParameter("memorialsId");
    String feedbackContent = request.getParameter("feedbackContent");

    // 执行更新
    memorialsService.updateMemorialsFeedBack(memorialsId, feedbackContent);

    // 重定向回显示奏折列表页面
    response.sendRedirect(request.getContextPath() + "/work?method=showMemorialsDigestList");
}
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse09.html#_3、memorialsservice-方法)3、MemorialsService 方法

```java
@Override
public void updateMemorialsFeedBack(String memorialsId, String feedbackContent) {
    memorialsDao.updateMemorialsFeedBack(memorialsId, feedbackContent);
}
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse09.html#_4、memorialsdao-方法)4、MemorialsDao 方法

```java
@Override
public void updateMemorialsFeedBack(String memorialsId, String feedbackContent) {

    String feedbackTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    String sql = "update t_memorials set memorials_status=2,feedback_content=?,feedback_time=? where memorials_id=?";
    
    update(sql, feedbackContent, feedbackTime, memorialsId);
}
```

## 第十节 业务功能：登录检查

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse10.html#_1、流程图)1、流程图

![images](maven_2022.assets/img048.158e78c1.png)

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse10.html#_2、创建-loginfilter)2、创建 LoginFilter

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse10.html#_1创建-java-类)①创建 Java 类

![images](maven_2022.assets/imagesffffffff.png)

```java
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 1、获取 HttpSession 对象
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        HttpSession session = request.getSession();
        
        // 2、尝试从 Session 域获取已登录的对象
        Object loginEmp = session.getAttribute(ImperialCourtConst.LOGIN_EMP_ATTR_NAME);
        
        // 3、判断 loginEmp 是否为空
        if (loginEmp != null) {
            
            // 4、若不为空则说明当前请求已登录，直接放行
            filterChain.doFilter(request, servletResponse);
            
            return ;
        }
        
        // 5、若为空说明尚未登录，则回到登录页面
        request.setAttribute("systemMessage", ImperialCourtConst.ACCESS_DENIED_MESSAGE);
        request.getRequestDispatcher("/").forward(request, servletResponse);
    }

    @Override
    public void destroy() {}
}
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse10.html#_2注册)②注册

![images](maven_2022.assets/imagesczxzcz.png)

把 LoginFilter 放在 TransactionFilter 前面声明，原因是：如果登录检查失败不放行，直接跳转到页面，此时将不必执行 TransactionFilter 中的事务操作，可以节约性能。

```xml
<filter>
    <filter-name>loginFilter</filter-name>
    <filter-class>com.atguigu.imperial.court.filter.LoginFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>loginFilter</filter-name>
    <url-pattern>/work</url-pattern>
</filter-mapping>
```

## 第十一节 打包部署

友情提示：[Linux教程目录 | 代码重工 (gitee.io)(opens new window)](http://heavy_code_industry.gitee.io/code_heavy_industry/pro006-Linux/lecture/)

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse11.html#_1、适配部署环境)1、适配部署环境

MySQL 连接信息中，IP 地址部分需要改成 localhost。

![images](data:image/png;base64,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)

```properties
url=jdbc:mysql://localhost:3306/db_imperial_court
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse11.html#_2、跳过测试打包)2、跳过测试打包

```sh
mvn clean package -Dmaven.test.skip=true
```

可以人为指定最终 war 包名称：

```xml
    <!-- 对构建过程进行自己的定制 -->
    <build>
        <!-- 当前工程在构建过程中使用的最终名称 -->
        <finalName>demo-me</finalName>
    </build>
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse11.html#_3、部署执行)3、部署执行

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse11.html#_1上传-war-包)①上传 war 包

[略]

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse11.html#_2启动-tomcat)②启动 Tomcat

```sh
/opt/apache-tomcat-8.5.75/bin/startup.sh
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter06/verse11.html#_3访问测试)③访问测试

![images](maven_2022.assets/img051.6f66d0fc.png)

# 第七章 SSM 整合伪分布式案例

## 第一节 创建工程，引入依赖

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse01.html#_1、创建工程)1、创建工程

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse01.html#_1工程清单)①工程清单

![images](maven_2022.assets/imagesert.png)

| 工程名                       | 地位   | 说明                 |
| ---------------------------- | ------ | -------------------- |
| demo-imperial-court-ssm-show | 父工程 | 总体管理各个子工程   |
| demo-module01-web            | 子工程 | 唯一的 war 包工程    |
| demo-module02-component      | 子工程 | 管理项目中的各种组件 |
| demo-module03-entity         | 子工程 | 管理项目中的实体类   |
| demo-module04-util           | 子工程 | 管理项目中的工具类   |
| demo-module05-environment    | 子工程 | 框架环境所需依赖     |
| demo-module06-generate       | 子工程 | Mybatis 逆向工程     |

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse01.html#_2工程间关系)②工程间关系

![images](maven_2022.assets/img002.5c441704.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse01.html#_2、各工程-pom-配置)2、各工程 POM 配置

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse01.html#_1父工程)①父工程

POM 位置如下：

![images](maven_2022.assets/images5644.png)

各子工程创建好之后就会有下面配置，不需要手动编辑：

```xml
<groupId>com.atguigu</groupId>
<artifactId>demo-imperial-court-ssm-show</artifactId>
<packaging>pom</packaging>
<version>1.0-SNAPSHOT</version>
<modules>
    <module>demo-module01-web</module>
    <module>demo-module02-component</module>
    <module>demo-module03-entity</module>
    <module>demo-module04-util</module>
    <module>demo-module05-environment</module>
    <module>demo-module06-generate</module>
</modules>
```

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse01.html#_2mybatis-逆向工程)②Mybatis 逆向工程

POM 位置如下：

![images](maven_2022.assets/images45612121.png)

```xml
<!-- 依赖MyBatis核心包 -->
<dependencies>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.7</version>
    </dependency>
</dependencies>

<!-- 控制Maven在构建过程中相关配置 -->
<build>

    <!-- 构建过程中用到的插件 -->
    <plugins>

        <!-- 具体插件，逆向工程的操作是以构建过程中插件形式出现的 -->
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.0</version>

            <!-- 插件的依赖 -->
            <dependencies>

                <!-- 逆向工程的核心依赖 -->
                <dependency>
                    <groupId>org.mybatis.generator</groupId>
                    <artifactId>mybatis-generator-core</artifactId>
                    <version>1.3.2</version>
                </dependency>

                <!-- 数据库连接池 -->
                <dependency>
                    <groupId>com.mchange</groupId>
                    <artifactId>c3p0</artifactId>
                    <version>0.9.2</version>
                </dependency>

                <!-- MySQL驱动 -->
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>5.1.8</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse01.html#_3环境依赖工程)③环境依赖工程

POM 位置如下：

![images](maven_2022.assets/imagessdaa.png)

```xml
<!-- SpringMVC -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.3.1</version>
</dependency>

<!-- Spring 持久化层所需依赖 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-orm</artifactId>
    <version>5.3.1</version>
</dependency>

<!-- 日志 -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
</dependency>

<!-- Spring5和Thymeleaf整合包 -->
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf-spring5</artifactId>
    <version>3.0.12.RELEASE</version>
</dependency>

<!-- Mybatis 和 Spring 的整合包 -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>2.0.6</version>
</dependency>

<!-- Mybatis核心 -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.7</version>
</dependency>

<!-- MySQL驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.3</version>
</dependency>

<!-- 数据源 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.0.31</version>
</dependency>
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse01.html#_4工具类工程)④工具类工程

无配置。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse01.html#_5实体类工程)⑤实体类工程

无配置。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse01.html#_6组件工程)⑥组件工程

POM 位置如下：

![images](maven_2022.assets/imagesww.png)

```xml
<dependency>
    <groupId>com.atguigu</groupId>
    <artifactId>demo-module03-entity</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.atguigu</groupId>
    <artifactId>demo-module04-util</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.atguigu</groupId>
    <artifactId>demo-module05-environment</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<!-- ServletAPI -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
</dependency>
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse01.html#_7web-工程)⑦Web 工程

POM 位置如下：

![images](maven_2022.assets/images453.png)

```xml
<dependency>
    <groupId>com.atguigu</groupId>
    <artifactId>demo-module02-component</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<!-- junit5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.7.0</version>
    <scope>test</scope>
</dependency>

<!-- Spring 的测试功能 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.3.1</version>
    <scope>test</scope>
</dependency>
```

## 第二节 搭建环境：持久化层

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_1、物理建模)1、物理建模

我们仍然继续使用《第六章 单一架构案例》中创建的数据库和表。

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_2、mybatis-逆向工程)2、Mybatis 逆向工程

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_1generatorconfig-xml)①generatorConfig.xml

![images](maven_2022.assets/images123444.png)

[查看详细配置信息](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/generatorConfig.html)



```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!--
            targetRuntime: 执行生成的逆向工程的版本
                    MyBatis3Simple: 生成基本的CRUD（清新简洁版）
                    MyBatis3: 生成带条件的CRUD（奢华尊享版）
     -->
    <context id="DB2Tables" targetRuntime="MyBatis3">
        <!-- 数据库的连接信息 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://192.168.198.100:3306/db_imperial_court"
                        userId="root"
                        password="atguigu">
        </jdbcConnection>
        <!-- javaBean的生成策略-->
        <javaModelGenerator targetPackage="com.atguigu.imperial.court.entity" targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- SQL映射文件的生成策略 -->
        <sqlMapGenerator targetPackage="com.atguigu.imperial.court.mapper"  targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>
        <!-- Mapper接口的生成策略 -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.atguigu.imperial.court.mapper"  targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- 逆向分析的表 -->
        <!-- tableName设置为*号，可以对应所有表，此时不写domainObjectName -->
        <!-- domainObjectName属性指定生成出来的实体类的类名 -->
        <table tableName="t_emp" domainObjectName="Emp"/>
        <table tableName="t_memorials" domainObjectName="Memorials"/>
    </context>
</generatorConfiguration>
```



#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_2执行逆向生成)②执行逆向生成

![images](maven_2022.assets/img009.785f4ea0.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_3资源归位)③资源归位

下面罗列各种资源应该存放的位置，排名不分先后：

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_1-mapper-配置文件)[1]Mapper 配置文件

![images](maven_2022.assets/imagesdssdss.png)

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_2-mapper-接口)[2]Mapper 接口

![images](maven_2022.assets/images46565.png)

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_3-实体类)[3]实体类

Mybatis 逆向工程生成的实体类只有字段和 get、set 方法，我们可以自己添加无参构造器、有参构造器、toString() 方法。

![images](maven_2022.assets/images55555.png)

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_3、建立数据库连接)3、建立数据库连接

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_1数据库连接信息)①数据库连接信息

![images](maven_2022.assets/imagesfssfff.png)

```properties
dev.driverClassName=com.mysql.jdbc.Driver
dev.url=jdbc:mysql://192.168.198.100:3306/db_imperial_court
dev.username=root
dev.password=atguigu
dev.initialSize=10
dev.maxActive=20
dev.maxWait=10000
```



#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_2配置数据源)②配置数据源

![images](data:image/png;base64,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)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 加载外部属性文件 -->
    <context:property-placeholder location="classpath:jdbc.properties"/>

    <!-- 配置数据源 -->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="username" value="${dev.username}"/>
        <property name="password" value="${dev.password}"/>
        <property name="url" value="${dev.url}"/>
        <property name="driverClassName" value="${dev.driverClassName}"/>
        <property name="initialSize" value="${dev.initialSize}"/>
        <property name="maxActive" value="${dev.maxActive}"/>
        <property name="maxWait" value="${dev.maxWait}"/>
    </bean>
    
</beans>
```



#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_3测试)③测试

![images](data:image/png;base64,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)

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(value = {"classpath:spring-persist.xml"})
public class ImperialCourtTest {

    @Autowired
    private DataSource dataSource;

    private Logger logger = LoggerFactory.getLogger(ImperialCourtTest.class);

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        logger.debug(connection.toString());
    }

}
```



TIP

配置文件为什么要放到 Web 工程里面？

- Web 工程将来生成 war 包。
- war 包直接部署到 Tomcat 运行。
- Tomcat 从 war 包（解压目录）查找配置文件最直接。
- 如果不是把配置文件放在 Web 工程，而是放在 Java 工程，那就等于将配置文件放在了 war 包内的 jar 包中。
- 配置文件在 jar 包中读取相对困难。

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_4、spring-整合-mybatis)4、Spring 整合 Mybatis

![images](maven_2022.assets/imagesvvbcxxc.png)

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_1配置-sqlsessionfactorybean)①配置 SqlSessionFactoryBean

**目的**1：装配数据源

**目的**2：指定 Mapper 配置文件的位置

```xml
<!-- 配置 SqlSessionFactoryBean -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">

    <!-- 装配数据源 -->
    <property name="dataSource" ref="druidDataSource"/>

    <!-- 指定 Mapper 配置文件的位置 -->
    <property name="mapperLocations" value="classpath:mapper/*Mapper.xml"/>
</bean>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_2扫描-mapper-接口)②扫描 Mapper 接口

```xml
<mybatis:scan base-package="com.atguigu.imperial.court.mapper"/>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse02.html#_3测试-2)③测试

![images](data:image/png;base64,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)

```java
@Autowired
private EmpMapper empMapper;

@Test
public void testEmpMapper() {
    List<Emp> empList = empMapper.selectByExample(new EmpExample());
    for (Emp emp : empList) {
        System.out.println("emp = " + emp);
    }
}
```

## 第三节 搭建环境：事务控制

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse03.html#_1、声明式事务配置)1、声明式事务配置

![images](maven_2022.assets/imageskskkk.png)

```xml
<!-- 配置事务管理器 -->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <!-- 装配数据源 -->
    <property name="dataSource" ref="dataSource"/>
</bean>

<!-- 配置事务的注解驱动，开启基于注解的声明式事务功能 -->
<tx:annotation-driven transaction-manager="transactionManager"/>
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse03.html#_2、注解写法)2、注解写法

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse03.html#_1查询操作)①查询操作

```java
@Transactional(readOnly = true)
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse03.html#_2增删改操作)②增删改操作

```java
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
```

TIP

在具体代码开发中可能会将相同设置的 @Transactional 注解提取到 Service 类上。

## 第四节 搭建环境：表述层

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_1、设定-web-工程)1、设定 Web 工程

[传送门(opens new window)](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter04/verse04.html)

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_2、web-xml-配置)2、web.xml 配置

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_1配置-contextloaderlistener)①配置 ContextLoaderListener

```xml
<!-- 第一部分：加载 spring-persist.xml 配置文件 -->
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:spring-persist.xml</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_2配置-dispatcherservlet)②配置 DispatcherServlet

```xml
<!-- 第二部分：加载 spring-mvc.xml 配置文件 -->
<servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring-mvc.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_3配置-characterencodingfilter)③配置 CharacterEncodingFilter

```xml
<!-- 第三部分：设置字符集的 Filter -->
<filter>
    <filter-name>characterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
        <param-name>forceRequestEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
    <init-param>
        <param-name>forceResponseEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_4配置-hiddenhttpmethodfilter)④配置 HiddenHttpMethodFilter

```xml
<!-- 第四部分：按照 RESTFul 风格负责转换请求方式的 Filter -->
<filter>
    <filter-name>hiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>hiddenHttpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_3、显示首页)3、显示首页

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_1配置-springmvc)①配置 SpringMVC

![images](maven_2022.assets/imagesssss.png)

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_1-标配)[1]标配

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvn="http://www.springframework.org/schema/mvc"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
    
    <!-- 开启 SpringMVC 的注解驱动功能 -->
    <mvn:annotation-driven />

    <!-- 让 SpringMVC 对没有 @RequestMapping 的请求直接放行 -->
    <mvc:default-servlet-handler />

</beans>
```

##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_2-配置视图解析相关)[2]配置视图解析相关

```xml
<!-- 配置视图解析器 -->
<bean id="thymeleafViewResolver" class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
	<property name="order" value="1"/>
	<property name="characterEncoding" value="UTF-8"/>
	<property name="templateEngine">
		<bean class="org.thymeleaf.spring5.SpringTemplateEngine">
			<property name="templateResolver">
				<bean class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
					<property name="prefix" value="/WEB-INF/templates/"/>
					<property name="suffix" value=".html"/>
					<property name="characterEncoding" value="UTF-8"/>
					<property name="templateMode" value="HTML5"/>
				</bean>
			</property>
		</bean>
	</property>
</bean>
```

**注意**：需要我们自己手动创建 templates 目录。



##### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_3-配置自动扫描的包)[3]配置自动扫描的包

```xml
<!-- 配置自动扫描的包 -->
<context:component-scan base-package="com.atguigu.imperial.court.controller"/>
<!-- 配置对 Service 所在包的自动扫描 -->
<context:component-scan base-package="com.atguigu.imperial.court.service"/>
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_2配置-view-controller-访问首页)②配置 view-controller 访问首页

```xml
<!-- 配置 view-controller -->
<mvc:view-controller path="/" view-name="index" />
```

#### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse04.html#_3创建首页模板文件)③创建首页模板文件

![imags](maven_2022.assets/imaghfhfgfs.png)

```html
<!DOCTYPE html>
<html lang="en" xml:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>

<!-- @{/auth} 解析后：/demo/auth -->
<form th:action="@{/auth/login}" method="post">

    <!-- th:text 解析表达式后会替换标签体 -->
    <!-- ${attrName} 从请求域获取属性名为 attrName 的属性值 -->
    <p style="color: red;font-weight: bold;" th:text="${message}"></p>
    <p style="color: red;font-weight: bold;" th:text="${systemMessage}"></p>

    账号：<input type="text" name="loginAccount"/><br/>
    密码：<input type="password" name="loginPassword"><br/>
    <button type="submit">进宫</button>
</form>

</body>
</html>
```

## 第五节 搭建环境：辅助功能

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse05.html#_1、登录失败异常)1、登录失败异常

![images](maven_2022.assets/img013.e816d73b.png)

```java
public class LoginFailedException extends RuntimeException {

    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    public LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
```



### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse05.html#_2、常量类)2、常量类

![images](maven_2022.assets/img014.2c7da5fa.png)

```java
public class ImperialCourtConst {

    public static final String LOGIN_FAILED_MESSAGE = "账号、密码错误，不可进宫！";
    public static final String ACCESS_DENIED_MESSAGE = "宫闱禁地，不得擅入！";
    public static final String LOGIN_EMP_ATTR_NAME = "loginInfo";

}
```



### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse05.html#_3、md5-工具)3、MD5 工具

![images](maven_2022.assets/img015.f2d6236f.png)

```java
public class MD5Util {

    /**
     * 针对明文字符串执行MD5加密
     * @param source
     * @return
     */
    public static String encode(String source) {

        // 1.判断明文字符串是否有效
        if (source == null || "".equals(source)) {
            throw new RuntimeException("用于加密的明文不可为空");
        }

        // 2.声明算法名称
        String algorithm = "md5";

        // 3.获取MessageDigest对象
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // 4.获取明文字符串对应的字节数组
        byte[] input = source.getBytes();

        // 5.执行加密
        byte[] output = messageDigest.digest(input);

        // 6.创建BigInteger对象
        int signum = 1;
        BigInteger bigInteger = new BigInteger(signum, output);

        // 7.按照16进制将bigInteger的值转换为字符串
        int radix = 16;
        String encoded = bigInteger.toString(radix).toUpperCase();

        return encoded;
    }

}
```



### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse05.html#_4、日志配置文件)4、日志配置文件

![images](maven_2022.assets/img016.29effed9.png)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">
    <!-- 指定日志输出的位置 -->
    <appender name="STDOUT"
              class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <!-- 日志输出的格式 -->
            <!-- 按照顺序分别是：时间、日志级别、线程名称、打印日志的类、日志主体内容、换行 -->
            <pattern>[%d{HH:mm:ss.SSS}] [%-5level] [%thread] [%logger] [%msg]%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!-- 设置全局日志级别。日志级别按顺序分别是：DEBUG、INFO、WARN、ERROR -->
    <!-- 指定任何一个日志级别都只打印当前级别和后面级别的日志。 -->
    <root level="INFO">
        <!-- 指定打印日志的appender，这里通过“STDOUT”引用了前面配置的appender -->
        <appender-ref ref="STDOUT" />
    </root>

    <!-- 专门给某一个包指定日志级别 -->
    <logger name="com.atguigu" level="DEBUG" additivity="false">
        <appender-ref ref="STDOUT" />
    </logger>

</configuration>
```

## 第六节 业务功能：登录

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse06.html#_1、authcontroller)1、AuthController

![images](maven_2022.assets/imagesdss.png)

```java
@Controller
public class AuthController {

    @Autowired
    private EmpService empService;

    @RequestMapping("/auth/login")
    public String doLogin(
            @RequestParam("loginAccount") String loginAccount,
            @RequestParam("loginPassword") String loginPassword,
            HttpSession session,
            Model model
    ) {

        // 1、尝试查询登录信息
        Emp emp = empService.getEmpByLogin(loginAccount, loginPassword);

        // 2、判断登录是否成功
        if (emp == null) {

            // 3、如果登录失败则回到登录页面显示提示消息
            model.addAttribute("message", ImperialCourtConst.LOGIN_FAILED_MESSAGE);

            return "index";

        } else {

            // 4、如果登录成功则将登录信息存入 Session 域
            session.setAttribute("loginInfo", emp);

            return "target";
        }
    }

}
```

### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse06.html#_2、empservice)2、EmpService

![images](maven_2022.assets/imagesasass.png)

```java
@Service
@Transactional(readOnly = true)
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;


    @Override
    public Emp getEmpByLogin(String loginAccount, String loginPassword) {

        // 1、密码加密
        String encodedLoginPassword = MD5Util.encode(loginPassword);

        // 2、通过 QBC 查询方式封装查询条件
        EmpExample example = new EmpExample();

        EmpExample.Criteria criteria = example.createCriteria();

        criteria.andLoginAccountEqualTo(loginAccount).andLoginPasswordEqualTo(encodedLoginPassword);

        List<Emp> empList = empMapper.selectByExample(example);

        if (empList != null && empList.size() > 0) {

            // 3、返回查询结果
            return empList.get(0);
        }

        return null;
    }
}
```

---

**xml形式：**

`com.atguigu.imperial.court.mapper.EmpMapper`

```java
public interface EmpMapper {
    //...
    Emp login(String loginAccount, String newPwd);
}
```

`src/main/resources/mapper/EmpMapper.xml`

```java
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.atguigu.imperial.court.mapper.EmpMapper">
    <resultMap id="BaseResultMap" type="com.atguigu.imperial.court.entity.Emp">
        <id column="emp_id" property="empId" jdbcType="INTEGER"/>
        <result column="emp_name" property="empName" jdbcType="CHAR"/>
        <result column="emp_position" property="empPosition" jdbcType="CHAR"/>
        <result column="login_account" property="loginAccount" jdbcType="CHAR"/>
        <result column="login_password" property="loginPassword" jdbcType="CHAR"/>
    </resultMap>
  
    <select id="login" resultMap="BaseResultMap">
        SELECT *
        FROM t_emp
        WHERE login_account = #{arg0}
          AND login_password = #{arg1}
    </select>
</mapper>
```



### [#](https://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter07/verse06.html#_3、target-html)3、target.html

![images](maven_2022.assets/imagesggg.png)

```html
<!DOCTYPE html>
<html lang="en" xml:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

    <p th:text="${session.loginInfo}"></p>

</body>
</html>
```

# 第八章 微服务架构案例

## 第一节 创建工程

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse01.html#_1、创建工程)1、创建工程

![images](maven_2022.assets/imageshhhvcz.png)

| 工程名                          | 地位   | 说明               |
| ------------------------------- | ------ | ------------------ |
| **demo-imperial-court-ms-show** | 父工程 | 总体管理各个子工程 |
| demo01-imperial-court-gateway   | 子工程 | 网关               |
| **demo02-user-auth-center**     | 子工程 | 用户中心           |
| demo03-emp-manager-center       | 子工程 | 员工数据维护中心   |
| demo04-memorials-manager-center | 子工程 | 奏折数据维护中心   |
| demo05-working-manager-center   | 子工程 | 批阅奏折工作中心   |
| **demo06-mysql-data-provider**  | 子工程 | MySQL 数据提供者   |
| demo07-redis-data-provider      | 子工程 | Redis 数据提供者   |
| **demo08-base-api**             | 子工程 | 声明 Feign 接口    |
| **demo09-base-entity**          | 子工程 | 实体类             |
| **demo10-base-util**            | 子工程 | 工具类             |

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse01.html#_2、建立工程间依赖关系)2、建立工程间依赖关系

![images](maven_2022.assets/img002.a402cf04.png)

## 第二节 父工程管理依赖

![images](maven_2022.assets/img003.223b16ed.png)

```xml
<dependencyManagement>
    <dependencies>

        <!-- SpringCloud 依赖导入 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>Hoxton.SR9</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <!-- SpringCloud Alibaba 依赖导入 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>2.2.6.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <!-- SpringBoot 依赖导入 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>2.3.6.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <!-- 通用 Mapper 依赖 -->
        <dependency>
            <groupId>tk.mybatis</groupId>
            <artifactId>mapper-spring-boot-starter</artifactId>
            <version>2.1.5</version>
        </dependency>

        <!-- Druid 数据源依赖 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>

        <!-- JPA 依赖 -->
        <dependency>
            <groupId>javax.persistence</groupId>
            <artifactId>persistence-api</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## 第三节 打基础

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse03.html#_1、demo10-base-util)1、demo10-base-util

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse03.html#_1imperialcourtconst-常量类)①ImperialCourtConst 常量类

```java
public class ImperialCourtConst {

    public static final String LOGIN_FAILED_MESSAGE = "账号、密码错误，不可进宫！";
    public static final String ACCESS_DENIED_MESSAGE = "宫闱禁地，不得擅入！";

}
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse03.html#_2字符串加密工具类)②字符串加密工具类

```java
public class MD5Util {

    /**
     * 针对明文字符串执行MD5加密
     * @param source
     * @return
     */
    public static String encode(String source) {

        // 1.判断明文字符串是否有效
        if (source == null || "".equals(source)) {
            throw new RuntimeException("用于加密的明文不可为空");
        }

        // 2.声明算法名称
        String algorithm = "md5";

        // 3.获取MessageDigest对象
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // 4.获取明文字符串对应的字节数组
        byte[] input = source.getBytes();

        // 5.执行加密
        byte[] output = messageDigest.digest(input);

        // 6.创建BigInteger对象
        int signum = 1;
        BigInteger bigInteger = new BigInteger(signum, output);

        // 7.按照16进制将bigInteger的值转换为字符串
        int radix = 16;
        String encoded = bigInteger.toString(radix).toUpperCase();

        return encoded;
    }

}
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse03.html#_3登录失败异常)③登录失败异常

```java
public class LoginFailedException extends RuntimeException {

    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    public LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse03.html#_4远程方法调用统一返回结果)④远程方法调用统一返回结果

```java
/**
 * 统一整个项目中远程方法调用返回的结果
 * @author Lenovo
 *
 * @param <T>
 */
public class ResultEntity<T> {
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
	
	// 用来封装当前请求处理的结果是成功还是失败
	private String result;
	
	// 请求处理失败时返回的错误消息
	private String message;
	
	// 要返回的数据
	private T data;
	
	/**
	 * 请求处理成功且不需要返回数据时使用的工具方法
	 * @return
	 */
	public static <Type> ResultEntity<Type> successWithoutData() {
		return new ResultEntity<Type>(SUCCESS, null, null);
	}
	
	/**
	 * 请求处理成功且需要返回数据时使用的工具方法
	 * @param data 要返回的数据
	 * @return
	 */
	public static <Type> ResultEntity<Type> successWithData(Type data) {
		return new ResultEntity<Type>(SUCCESS, null, data);
	}
	
	/**
	 * 请求处理失败后使用的工具方法
	 * @param message 失败的错误消息
	 * @return
	 */
	public static <Type> ResultEntity<Type> failed(String message) {
		return new ResultEntity<Type>(FAILED, message, null);
	}
	
	public ResultEntity() {
		
	}

	public ResultEntity(String result, String message, T data) {
		super();
		this.result = result;
		this.message = message;
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResultEntity [result=" + result + ", message=" + message + ", data=" + data + "]";
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
```



### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse03.html#_2、demo09-base-entity)2、demo09-base-entity

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse03.html#_1引入依赖)①引入依赖

```xml
<dependency>
    <groupId>javax.persistence</groupId>
    <artifactId>persistence-api</artifactId>
</dependency>
```

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse03.html#_2创建实体类)②创建实体类

在 MySQL 数据提供服务中用到的通用 Mapper 技术需要借助 **@Table 注解**将实体类和数据库表关联起来。

```java
@Table(name = "t_emp")
public class Emp implements Serializable {

    private Integer empId;

    private String empName;

    private String empPosition;

    private String loginAccount;

    private String loginPassword;

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName == null ? null : empName.trim();
    }

    public String getEmpPosition() {
        return empPosition;
    }

    public void setEmpPosition(String empPosition) {
        this.empPosition = empPosition == null ? null : empPosition.trim();
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount == null ? null : loginAccount.trim();
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword == null ? null : loginPassword.trim();
    }
}
```

## 第四节 用户登录认证服务：提供端

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_1、总体分析)1、总体分析

![images](maven_2022.assets/img004.e6803319.png)



### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_2、注册中心)2、注册中心

在本地启动 Nacos 注册中心：

```sh
d:\software\nacos\bin>startup.cmd -m standalone
```



### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_3、声明接口-暴露服务)3、声明接口，暴露服务

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_1接口文档)①接口文档

[点此查看接口文档](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/api.html)



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_2-feign-接口代码)② Feign 接口代码

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_1-接口位置)[1]接口位置

![images](maven_2022.assets/img005.4502365c.png)



##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_2-引入依赖)[2]引入依赖

```xml
<!-- OpenFeign 专用依赖 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!-- 提供 Emp 实体类使用 -->
<dependency>
    <groupId>com.atguigu.demo</groupId>
    <artifactId>demo09-base-entity</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<!-- 提供 ResultEntity 工具类使用 -->
<dependency>
    <groupId>com.atguigu.demo</groupId>
    <artifactId>demo10-base-util</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```



##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_3-接口代码)[3]接口代码

**注意**：@FeignClient 注解中指定的是提供服务的微服务名称，要和注册中心注册的一致

```java
// @FeignClient 注解将当前接口标记为服务暴露接口
// name 属性：指定被暴露服务的微服务名称
@FeignClient(name = "demo06-mysql-data-provider")
public interface MySQLProvider {

    @RequestMapping("/remote/get/emp/by/login/info")
    ResultEntity<Emp> getEmpByLoginInfo(

            // @RequestParam 无论如何不能省略
            @RequestParam("loginAccount") String loginAccount,
            @RequestParam("loginPassword") String loginPassword);
}
```



### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_4、实现接口)4、实现接口

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_1所在工程)①所在工程

![images](maven_2022.assets/img006.4bee7822.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_2引入依赖)②引入依赖

```xml
<!-- Nacos 服务注册发现启动器 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>

<!--通用mapper启动器依赖-->
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper-spring-boot-starter</artifactId>
</dependency>

<!--mysql驱动-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>

<!--druid启动器依赖-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
</dependency>

<!--web启动器依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!--编码工具包-->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
</dependency>

<!--单元测试启动器-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!--热部署 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>com.atguigu.demo</groupId>
    <artifactId>demo09-base-entity</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>com.atguigu.demo</groupId>
    <artifactId>demo10-base-util</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_3java-代码)③Java 代码

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_1-总体结构)[1]总体结构

![images](data:image/png;base64,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)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_2-empmapper)[2]EmpMapper

继承 tk.mybatis.mapper.common.Mapper 后就可以使用通用 Mapper 提供的常规代码实现。除非有非常规需求，否则我们自己什么都不用写。

![images](data:image/png;base64,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)

```java
public interface EmpMapper extends Mapper<Emp> {
}
```



##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_3-service-接口)[3]Service 接口

![images](data:image/png;base64,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)

```java
public interface EmpService {
    Emp getEmpByLoginInfo(String loginAccount, String loginPassword);
}
```

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_4-service-实现)[4]Service 实现

```java
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Override
    public Emp getEmpByLoginInfo(String loginAccount, String loginPassword) {

        String encodedLoginPassword = MD5Util.encode(loginPassword);

        Example example = new Example(Emp.class);

        example
                .createCriteria()
                .andEqualTo("loginAccount", loginAccount)
                .andEqualTo("loginPassword", encodedLoginPassword);

        List<Emp> empList = empMapper.selectByExample(example);

        if (empList == null || empList.size() == 0) {
            throw new LoginFailedException(ImperialCourtConst.LOGIN_FAILED_MESSAGE);
        }

        return empList.get(0);
    }
}
```



##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_5-empcontroller)[5]EmpController

![images](data:image/png;base64,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)

```java
@RestController
public class EmpController {

    @Autowired
    private EmpService empService;

    @RequestMapping("remote/get/emp/by/login/info")
    ResultEntity<Emp> getEmpByLoginInfo(
            @RequestParam("loginAccount") String loginAccount,
            @RequestParam("loginPassword") String loginPassword) {

        try {
            Emp emp = empService.getEmpByLoginInfo(loginAccount, loginPassword);

            return ResultEntity.successWithData(emp);

        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            return ResultEntity.failed(message);
        }

    }

}
```



##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_6-主启动类)[6]主启动类

![images](maven_2022.assets/img019.e387b0f7.png)

```java
// 为了让当前微服务对接（注册或发现服务）注册中心
@EnableDiscoveryClient

// SpringBoot 标配注解
@SpringBootApplication

// 扫描通用 Mapper 的 Mapper 接口所在包
// 这个注解全类名：tk.mybatis.spring.annotation.MapperScan
// basePackage 属性：指定要扫描的 Mapper 接口所在的包
@MapperScan(basePackages = "com.atguigu.imperial.court.mapper")
public class MainType {

    public static void main(String[] args) {
        SpringApplication.run(MainType.class, args);
    }

}
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse04.html#_4yaml-配置文件)④YAML 配置文件

![images](data:image/png;base64,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)

```yaml
server:
  port: 10001
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://192.168.198.100:3306/db_imperial_court
    username: root
    password: atguigu
    type: com.alibaba.druid.pool.DruidDataSource
  application:
    name: demo06-mysql-data-provider
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

## 第五节 用户登录认证服务：消费端

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_1、所在工程)1、所在工程

![images](data:image/png;base64,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)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_2、引入依赖)2、引入依赖

```xml
<!-- Nacos 服务注册发现启动器 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>

<!-- web启动器依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- 视图模板技术 thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<dependency>
    <groupId>com.atguigu.demo</groupId>
    <artifactId>demo08-base-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```



### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_3、yaml-配置文件)3、YAML 配置文件

![images](data:image/png;base64,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)

```yaml
server:
  port: 10002
spring:
  application:
    name: demo02-user-auth-center
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```



TIP

就 Thymeleaf 而言，有两个常用属性，但我们全部都使用的是默认值，所以可以省略。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_4、显示首页)4、显示首页

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_1配置-view-controller)①配置 view-controller

![images](data:image/png;base64,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)

```java
@SpringBootConfiguration
public class DemoConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("portal");
    }
}
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_2thymeleaf-视图模板页面)②Thymeleaf 视图模板页面

![images](data:image/png;base64,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)

```html
<!DOCTYPE html>
<html lang="en" xml:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>

<form th:action="@{/consumer/do/login}" method="post">
    <p style="color: red;font-weight: bold;" th:if="${not #strings.isEmpty(authMessage)}" th:text="${authMessage}">
        这里根据条件显示登录失败消息</p>
    <p style="color: red;font-weight: bold;" th:if="${not #strings.isEmpty(systemMessage)}" th:text="${systemMessage}">
        这里根据条件显示系统消息</p>
    账号：<input type="text" name="loginAccount"/><br/>
    密码：<input type="password" name="loginPassword"><br/>
    <button type="submit">进宫</button>
</form>
</body>
</html>
```



### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_5、登录验证)5、登录验证

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_1流程图)①流程图

![images](maven_2022.assets/img020.c5cd1fc4.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_2主启动类)②主启动类

**注意**：一定要标记 @EnableFeignClients 注解。

![images](data:image/png;base64,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)

```java
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MainType {

    public static void main(String[] args) {
        SpringApplication.run(MainType.class, args);
    }

}
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_3authcontroller)③AuthController

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_1-装配远程接口分析)[1]装配远程接口分析

![images](data:image/png;base64,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)

```java
@Controller
public class AuthController {

    // 1、本地使用 @Autowired 注解装配远程接口类型即可实现方法的远程调用，
    // 看起来就像是调用本地方法一样，我们管这种特性叫方法的声明式远程调用。
    // 2、凭啥通过 @Autowired 注解就能够导入远程接口对应的 bean
    //      ①当前环境包含 Feign 相关 jar 包。
    //      ②当前微服务的主启动类上标记 @EnableFeignClients
    //      ③符合 SpringBoot 自动扫描包的约定规则：默认情况下主启动类所在的包、以及主启动类所在包的子包都会被自动扫描
    //          主启动类所在包：     com.atguigu.imperial.court
    //          被扫描的接口所在的包：com.atguigu.imperial.court.api
    @Autowired
    private MySQLProvider mySQLProvider;

}
```



##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse05.html#_2-执行登录验证的方法)[2]执行登录验证的方法

```java
@RequestMapping("/consumer/do/login")
public String doLogin(@RequestParam("loginAccount") String loginAccount,
                      @RequestParam("loginPassword") String loginPassword, HttpSession session, Model model) {

    // 1、调用远程接口根据登录账号、密码查询 Emp 对象
    ResultEntity<Emp> resultEntity = mySQLProvider.getEmpByLoginInfo(loginAccount, loginPassword);

    // 2、验证远程接口调用是否成功
    String result = resultEntity.getResult();
    
    if ("SUCCESS".equals(result)) {

        // 3、从 ResultEntity 中获取查询得到的 Emp 对象
        Emp emp = resultEntity.getData();
        
        // 4、将 Emp 对象存入 Session 域
        session.setAttribute("loginInfo", emp);

        // 5、前往 target 页面
        return "target";
    } else {
        
        // 6、获取失败消息
        String message = resultEntity.getMessage();
        
        // 7、将失败消息存入模型
        model.addAttribute("message", message);
        
        // 8、回到登录页面
        return "index";

    }
}
```

## 第六节 部署运行

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_1、最终目标)1、最终目标

![images](data:image/png;base64,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)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_2、微服务打包)2、微服务打包

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_1修改-mysql-连接信息)①修改 MySQL 连接信息

![images](data:image/png;base64,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)

```yaml
server:
  port: 10001
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    
    # 当前微服务和 MySQL 位于同一个服务器上，需要把访问地址改成 localhost
    url: jdbc:mysql://localhost:3306/db_imperial_court
    username: root
    password: atguigu
    type: com.alibaba.druid.pool.DruidDataSource
  application:
    name: demo06-mysql-data-provider
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_2在父工程执行-install-命令)②在父工程执行 install 命令

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_1-why-parent-工程间关系梳理)[1]Why parent？工程间关系梳理

正确的安装顺序：

- ①父工程：pro07-demo-imperial-court-micro-service
- ②被依赖的 module：demo10-base-util 或 demo09-base-entity
- ③当前 module：demo06-mysql-data-provider

![images](data:image/png;base64,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)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_2-执行命令)[2]执行命令

![images](data:image/png;base64,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)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_3生成微服务可运行-jar-包)③生成微服务可运行 jar 包

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_1-应用微服务打包插件)[1]应用微服务打包插件

可以以 SpringBoot 微服务形式直接运行的 jar 包包括：

- 当前微服务本身代码
- 当前微服务所依赖的 jar 包
- 内置 Tomcat（Servlet 容器）
- 与 jar 包可以通过 java -jar 方式直接启动相关的配置

要加入额外的资源、相关配置等等，仅靠 Maven 自身的构建能力是不够的，所以要通过 build 标签引入下面的插件。

```xml
<!-- build 标签：用来配置对构建过程的定制 -->
<build>
    <!-- plugins 标签：定制化构建过程中所使用到的插件 -->
	<plugins>
        <!-- plugin 标签：一个具体插件 -->
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins>
</build>
```



加入这个插件后的效果：

![images](maven_2022.assets/img026.51697e69.png)

提示：IDEA 对于我们这里 build 标签里加入的 plugin 的配置没有能够很好的识别到插件的版本。如果我们能够保证其它操作都正常执行完成，准备工作都准备好了，那么这里我们判定是 IDEA 识别能力不足导致。一切以实际执行的结果为准：**运行结果是最高权威**

请对 demo02-user-auth-center 和 demo06-mysql-data-provider 都添加上面的 build 配置。

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_2-执行插件目标)[2]执行插件目标

请对 demo02-user-auth-center 和 demo06-mysql-data-provider 都执行下面的命令：

- clean 子命令：清理之前构建的结果
- package 子命令：我们真正要调用的 spring-boot:repackage 要求必须将当前微服务本身的 jar 包提前准备好，所以必须在它之前执行 package 子命令。
- spring-boot:repackage 子命令：调用 spring-boot 插件的 repackage 目标
- -Dmaven.test.skip=true 参数：跳过测试

```sh
mvn clean package spring-boot:repackage -Dmaven.test.skip=true
```

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_3、执行部署)3、执行部署

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_1启动-nacos)①启动 Nacos

```sh
sh /opt/nacos/bin/startup.sh -m standalone
```

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_2上传微服务-jar-包)②上传微服务 jar 包

![images](maven_2022.assets/img027.03f0c407.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter08/verse06.html#_2启动微服务)②启动微服务

```sh
nohup java -jar demo06-mysql-data-provider-1.0-SNAPSHOT.jar>demo06.log 2>&1 &
nohup java -jar demo02-user-auth-center-1.0-SNAPSHOT.jar>demo02.log 2>&1 &
```

# 第九章 POM 深入与强化

## 第一节 重新认识Maven

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse01.html#_1、maven-的完整功能)1、Maven 的完整功能

在入门的时候我们介绍说 Maven 是一款『**构建**管理』和『**依赖**管理』的工具。但事实上这只是 Maven 的一部分功能。Maven 本身的产品定位是一款『**项目**管理工具』。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse01.html#_2、项目管理功能的具体体现)2、项目管理功能的具体体现

下面是 spring-boot-starter 的 POM 文件，可以看到：除了我们熟悉的坐标标签、dependencies 标签，还有 description、url、organization、licenses、developers、scm、issueManagement 等这些描述项目信息的标签。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <!-- This module was also published with a richer model, Gradle metadata,  -->
  <!-- which should be used instead. Do not delete the following line which  -->
  <!-- is to indicate to Gradle or any Gradle module metadata file consumer  -->
  <!-- that they should prefer consuming it instead. -->
  <!-- do_not_remove: published-with-gradle-metadata -->
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter</artifactId>
  <version>2.5.6</version>
  <name>spring-boot-starter</name>
  <description>Core starter, including auto-configuration support, logging and YAML</description>
  <url>https://spring.io/projects/spring-boot</url>
  <organization>
    <name>Pivotal Software, Inc.</name>
    <url>https://spring.io</url>
  </organization>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>https://www.apache.org/licenses/LICENSE-2.0</url>
    </license>
  </licenses>
  <developers>
    <developer>
      <name>Pivotal</name>
      <email>info@pivotal.io</email>
      <organization>Pivotal Software, Inc.</organization>
      <organizationUrl>https://www.spring.io</organizationUrl>
    </developer>
  </developers>
  <scm>
    <connection>scm:git:git://github.com/spring-projects/spring-boot.git</connection>
    <developerConnection>scm:git:ssh://git@github.com/spring-projects/spring-boot.git</developerConnection>
    <url>https://github.com/spring-projects/spring-boot</url>
  </scm>
  <issueManagement>
    <system>GitHub</system>
    <url>https://github.com/spring-projects/spring-boot/issues</url>
  </issueManagement>

  <dependencies>
    <dependency>
      ……
    </dependency>
  </dependencies>
</project>
```



所以从『项目管理』的角度来看，Maven 提供了如下这些功能：

- 项目对象模型（POM）：将整个项目本身抽象、封装为应用程序中的一个对象，以便于管理和操作。
- 全局性构建逻辑重用：Maven 对整个构建过程进行封装之后，程序员只需要指定配置信息即可完成构建。让构建过程从 Ant 的『编程式』升级到了 Maven 的『声明式』。
- 构件的标准集合：在 Maven 提供的标准框架体系内，所有的构件都可以按照统一的规范生成和使用。
- 构件关系定义：Maven 定义了构件之间的三种基本关系，让大型应用系统可以使用 Maven 来进行管理
  - 继承关系：通过从上到下的继承关系，将各个子构件中的重复信息提取到父构件中统一管理
  - 聚合关系：将多个构件聚合为一个整体，便于统一操作
  - 依赖关系：Maven 定义了依赖的范围、依赖的传递、依赖的排除、版本仲裁机制等一系列规范和标准，让大型项目可以有序容纳数百甚至更多依赖
- 插件目标系统：Maven 核心程序定义抽象的生命周期，然后将插件的目标绑定到生命周期中的特定阶段，实现了标准和具体实现解耦合，让 Maven 程序极具扩展性
- 项目描述信息的维护：我们不仅可以在 POM 中声明项目描述信息，更可以将整个项目相关信息收集起来生成 HTML 页面组成的一个可以直接访问的站点。这些项目描述信息包括：
  - 公司或组织信息
  - 项目许可证
  - 开发成员信息
  - issue 管理信息
  - SCM 信息

## 第二节 POM 的四个层次

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse02.html#_1、超级-pom)1、超级 POM

经过我们前面的学习，我们看到 Maven 在构建过程中有很多默认的设定。例如：源文件存放的目录、测试源文件存放的目录、构建输出的目录……等等。但是其实这些要素也都是被 Maven 定义过的。定义的位置就是：**超级 POM**。

关于超级 POM，Maven 官网是这样介绍的：

> The Super POM is Maven's default POM. All POMs extend the Super POM unless explicitly set, meaning the configuration specified in the Super POM is inherited by the POMs you created for your projects.
>
> 译文：Super POM 是 Maven 的默认 POM。除非明确设置，否则所有 POM 都扩展 Super POM，这意味着 Super POM 中指定的配置由您为项目创建的 POM 继承。

所以我们自己的 POM 即使没有明确指定一个父工程（父 POM），其实也默认继承了超级 POM。就好比一个 Java 类默认继承了 Object 类。

那么超级 POM 中定义了哪些东西呢？点击[这里](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/super-pom-content.html)查看。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse02.html#_2、父-pom)2、父 POM

和 Java 类一样，POM 之间其实也是**单继承**的。如果我们给一个 POM 指定了父 POM，那么继承关系如下图所示：

![images](data:image/png;base64,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)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse02.html#_3、有效-pom)3、有效 POM

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse02.html#_1概念)①概念

有效 POM 英文翻译为 effective POM，它的概念是这样的——在 POM 的继承关系中，子 POM 可以覆盖父 POM 中的配置；如果子 POM 没有覆盖，那么父 POM 中的配置将会被继承。按照这个规则，继承关系中的所有 POM 叠加到一起，就得到了一个最终生效的 POM。显然 Maven 实际运行过程中，执行构建操作就是按照这个最终生效的 POM 来运行的。这个最终生效的 POM 就是**有效 POM**，英文叫**effective POM**。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse02.html#_2查看有效-pom)②查看有效 POM

> mvn help:effective-pom

运行效果[点击这里](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/effective-pom-content)查看。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse02.html#_4、小结)4、小结

综上所述，平时我们使用和配置的 POM 其实大致是由四个层次组成的：

- 超级 POM：所有 POM 默认继承，只是有直接和间接之分。
- 父 POM：这一层可能没有，可能有一层，也可能有很多层。
- 当前 pom.xml 配置的 POM：我们最多关注和最多使用的一层。
- 有效 POM：隐含的一层，但是实际上真正生效的一层。

## 第三节 属性的声明与引用

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_1、help-插件的各个目标)1、help 插件的各个目标

官网说明地址：https://maven.apache.org/plugins/maven-help-plugin

| 目标                    | 说明                                              |
| ----------------------- | ------------------------------------------------- |
| help:active-profiles    | 列出当前已激活的 profile                          |
| help:all-profiles       | 列出当前工程所有可用 profile                      |
| help:describe           | 描述一个插件和/或 Mojo 的属性                     |
| help:effective-pom      | 以 XML 格式展示有效 POM                           |
| help:effective-settings | 为当前工程以 XML 格式展示计算得到的 settings 配置 |
| **help:evaluate**       | 计算用户在交互模式下给出的 Maven 表达式           |
| help:system             | 显示平台详细信息列表，如系统属性和环境变量        |

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_2、使用-help-evaluate-查看属性值)2、使用 help:evaluate 查看属性值

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_1定义属性)①定义属性

```xml
<properties>
    <com.atguigu.hello>good morning maven</com.atguigu.hello>
</properties>
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_2运行命令)②运行命令

![images](data:image/png;base64,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)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_3运行结果)③运行结果

![images](maven_2022.assets/img003.4bc5a83a.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_3、通过-maven-访问系统属性)3、通过 Maven 访问系统属性

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_1-java-系统属性一览)① Java 系统属性一览

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_1-java-代码)[1] Java 代码

```java
Properties properties = System.getProperties();
Set<Object> propNameSet = properties.keySet();
for (Object propName : propNameSet) {
    String propValue = properties.getProperty((String) propName);
    System.out.println(propName + " = " + propValue);
}
```



##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_2-运行结果)[2]运行结果

> java.runtime.name = Java(TM) SE Runtime Environment
> sun.boot.library.path = D:\software\Java\jre\bin
> java.vm.version = 25.141-b15
> java.vm.vendor = Oracle Corporation
> java.vendor.url = http://java.oracle.com/
> path.separator = ;
> java.vm.name = Java HotSpot(TM) 64-Bit Server VM
> file.encoding.pkg = sun.io
> user.country = CN
> user.script =
> sun.java.launcher = SUN_STANDARD
> sun.os.patch.level =
> java.vm.specification.name = Java Virtual Machine Specification
> user.dir = D:\idea2019workspace\atguigu-maven-test-prepare
> java.runtime.version = 1.8.0_141-b15
> java.awt.graphicsenv = sun.awt.Win32GraphicsEnvironment
> java.endorsed.dirs = D:\software\Java\jre\lib\endorsed
> os.arch = amd64
> java.io.tmpdir = C:\Users\ADMINI~1\AppData\Local\Temp\
> line.separator =
> java.vm.specification.vendor = Oracle Corporation
> user.variant =
> os.name = Windows 10
> sun.jnu.encoding = GBK
> java.library.path = D:\software\Java\bin;C:\WINDOWS\Sun\Java\bin;C:\WIN……
> java.specification.name = Java Platform API Specification
> java.class.version = 52.0
> sun.management.compiler = HotSpot 64-Bit Tiered Compilers
> os.version = 10.0
> user.home = C:\Users\Administrator
> user.timezone =
> java.awt.printerjob = sun.awt.windows.WPrinterJob
> file.encoding = UTF-8
> java.specification.version = 1.8
> java.class.path = D:\software\Java\jre\lib\charsets.jar;D:\softw……
> user.name = Administrator
> java.vm.specification.version = 1.8
> sun.java.command = com.atguigu.maven.MyTest
> java.home = D:\software\Java\jre
> sun.arch.data.model = 64
> user.language = zh
> java.specification.vendor = Oracle Corporation
> awt.toolkit = sun.awt.windows.WToolkit
> java.vm.info = mixed mode
> java.version = 1.8.0_141
> java.ext.dirs = D:\software\Java\jre\lib\ext;C:\WINDOWS\Sun\Java\lib\ext
> sun.boot.class.path = D:\software\Java\jre\lib\resources.jar;D:\sof……
> java.vendor = Oracle Corporation
> file.separator = \
> java.vendor.url.bug = http://bugreport.sun.com/bugreport/
> sun.io.unicode.encoding = UnicodeLittle
> sun.cpu.endian = little
> sun.desktop = windows
> sun.cpu.isalist = amd64

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_2使用-maven-访问系统属性)②使用 Maven 访问系统属性

![images](maven_2022.assets/img004.f6eca629.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_4、访问系统环境变量)4、访问系统环境变量

${env.系统环境变量名}

![images](maven_2022.assets/img009.4a2e24e8.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_5、访问-project-属性)5、访问 project 属性

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_1含义)①含义

使用表达式 ${project.xxx} 可以访问当前 POM 中的元素值。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_2访问一级标签)②访问一级标签

${project.标签名}

![images](maven_2022.assets/img005.fba68730.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_3访问子标签)③访问子标签

${project.标签名.子标签名}

![images](maven_2022.assets/img006.265f7fd8.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_4访问列表标签)④访问列表标签

${project.标签名[下标]}

![images](maven_2022.assets/img007.e3598dca.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_6、访问-settings-全局配置)6、访问 settings 全局配置

${settings.标签名} 可以访问 settings.xml 中配置的元素值。

![images](maven_2022.assets/img008.5ad4fbe3.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse03.html#_7、用途)7、用途

- 在当前 pom.xml 文件中引用属性
- 资源过滤功能：在非 Maven 配置文件中引用属性，由 Maven 在处理资源时将引用属性的表达式替换为属性值

## 第四节 build 标签详解

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_1、一睹真容)1、一睹真容

在实际使用 Maven 的过程中，我们会发现 build 标签有时候有，有时候没，这是怎么回事呢？其实通过有效 POM 我们能够看到，build 标签的相关配置其实一直都在，只是在我们需要定制构建过程的时候才会通过配置 build 标签覆盖默认值或补充配置。这一点我们可以通过打印有效 POM 来看到。

[点我查看 build 标签的完整示例](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/whole-build.html)

所以**本质**上来说：我们配置的 build 标签都是对**超级 POM 配置**的**叠加**。那我们又为什么要在默认配置的基础上叠加呢？很简单，在默认配置无法满足需求的时候**定制构建过程**。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_2、build-标签组成)2、build 标签组成

从完整示例中我们能够看到，build 标签的子标签大致包含三个主体部分：

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_1定义约定的目录结构)①定义约定的目录结构

参考示例中的如下部分：

```xml
<sourceDirectory>D:\idea2019workspace\atguigu-maven-test-prepare\src\main\java</sourceDirectory>
<scriptSourceDirectory>D:\idea2019workspace\atguigu-maven-test-prepare\src\main\scripts</scriptSourceDirectory>
<testSourceDirectory>D:\idea2019workspace\atguigu-maven-test-prepare\src\test\java</testSourceDirectory>
<outputDirectory>D:\idea2019workspace\atguigu-maven-test-prepare\target\classes</outputDirectory>
<testOutputDirectory>D:\idea2019workspace\atguigu-maven-test-prepare\target\test-classes</testOutputDirectory>
<resources>
    <resource>
        <directory>D:\idea2019workspace\atguigu-maven-test-prepare\src\main\resources</directory>
    </resource>
</resources>
<testResources>
    <testResource>
        <directory>D:\idea2019workspace\atguigu-maven-test-prepare\src\test\resources</directory>
    </testResource>
</testResources>
<directory>D:\idea2019workspace\atguigu-maven-test-prepare\target</directory>
```



我们能看到各个目录的作用如下：

| 目录名                | 作用                       |
| --------------------- | -------------------------- |
| sourceDirectory       | 主体源程序存放目录         |
| scriptSourceDirectory | 脚本源程序存放目录         |
| testSourceDirectory   | 测试源程序存放目录         |
| outputDirectory       | 主体源程序编译结果输出目录 |
| testOutputDirectory   | 测试源程序编译结果输出目录 |
| resources             | 主体资源文件存放目录       |
| testResources         | 测试资源文件存放目录       |
| directory             | 构建结果输出目录           |

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_2备用插件管理)②备用插件管理

pluginManagement 标签存放着几个极少用到的插件：

- maven-antrun-plugin
- maven-assembly-plugin
- maven-dependency-plugin
- maven-release-plugin

通过 pluginManagement 标签管理起来的插件就像 dependencyManagement 一样，子工程使用时可以省略版本号，起到在父工程中统一管理版本的效果。情看下面例子：

- 被 spring-boot-dependencies 管理的插件信息：

```xml
<plugin>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-maven-plugin</artifactId>
	<version>2.6.2</version>
</plugin>
```



- 子工程使用的插件信息：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_3生命周期插件)③生命周期插件

plugins 标签存放的是默认生命周期中实际会用到的插件，这些插件想必大家都不陌生，所以抛开插件本身不谈，我们来看看 plugin 标签的结构：

```xml
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.1</version>
    <executions>
        <execution>
            <id>default-compile</id>
            <phase>compile</phase>
            <goals>
                <goal>compile</goal>
            </goals>
        </execution>
        <execution>
            <id>default-testCompile</id>
            <phase>test-compile</phase>
            <goals>
                <goal>testCompile</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```



##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_1-坐标部分)[1]坐标部分

artifactId 和 version 标签定义了插件的坐标，作为 Maven 的自带插件这里省略了 groupId。

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_2-执行部分)[2]执行部分

executions 标签内可以配置多个 execution 标签，execution 标签内：

- id：指定唯一标识
- phase：关联的生命周期阶段
- goals/goal：关联指定生命周期的目标
  - goals 标签中可以配置多个 goal 标签，表示一个生命周期环节可以对应当前插件的多个目标。

另外，插件目标的执行过程可以进行配置，例如 maven-site-plugin 插件的 site 目标：

```xml
<execution>
    <id>default-site</id>
    <phase>site</phase>
    <goals>
        <goal>site</goal>
    </goals>
    <configuration>
        <outputDirectory>D:\idea2019workspace\atguigu-maven-test-prepare\target\site</outputDirectory>
        <reportPlugins>
            <reportPlugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-project-info-reports-plugin</artifactId>
            </reportPlugin>
        </reportPlugins>
    </configuration>
</execution>
```



configuration 标签内进行配置时使用的标签是插件本身定义的。就以 maven-site-plugin 插件为例，它的核心类是 org.apache.maven.plugins.site.render.SiteMojo，在这个类中我们看到了 outputDirectory 属性：

![images](data:image/png;base64,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)

SiteMojo 的父类是：AbstractSiteRenderingMojo，在父类中我们看到 reportPlugins 属性：

![images](data:image/png;base64,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)

**结论**：每个插件能够做哪些设置都是各个插件自己规定的，无法一概而论。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_3、典型应用-指定-jdk-版本)3、典型应用：指定 JDK 版本

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_1提出问题)①提出问题

前面我们在 settings.xml 中配置了 JDK 版本，那么将来把 Maven 工程部署都服务器上，脱离了 settings.xml 配置，如何保证程序正常运行呢？思路就是我们直接把 JDK 版本信息告诉负责编译操作的 maven-compiler-plugin 插件，让它在构建过程中，按照我们指定的信息工作。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_2暂时取消-settings-xml-配置)②暂时取消 settings.xml 配置

为了测试对 maven-compiler-plugin 插件进行配置的效果，我们暂时取消 settings.xml 中的 profile 配置。

```xml
<!-- 配置Maven工程的默认JDK版本 -->
<!-- <profile>
  <id>jdk-1.8</id>
  <activation>
	<activeByDefault>true</activeByDefault>
	<jdk>1.8</jdk>
  </activation>
  <properties>
	<maven.compiler.source>1.8</maven.compiler.source>
	<maven.compiler.target>1.8</maven.compiler.target>
	<maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
  </properties>
</profile> -->
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_3编写源文件代码)③编写源文件代码

很明显这里用到了 Lambda 表达式，这是 JDK 1.8 才支持的语法。

```java
package com.atguigu.maven;

public class Hello {

    public void hello() {
        new Thread(()->{
            System.out.println("thread ...");
        }).start();
    }

}
```



此时我们执行编译命令：

![images](maven_2022.assets/img116.a4961940.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_4配置构建过程)④配置构建过程

```xml
<!-- build 标签：意思是告诉 Maven，你的构建行为，我要开始定制了！ -->
<build>
    <!-- plugins 标签：Maven 你给我听好了，你给我构建的时候要用到这些插件！ -->
    <plugins>
        <!-- plugin 标签：这是我要指定的一个具体的插件 -->
        <plugin>
            <!-- 插件的坐标。此处引用的 maven-compiler-plugin 插件不是第三方的，是一个 Maven 自带的插件。 -->
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.1</version>
            
            <!-- configuration 标签：配置 maven-compiler-plugin 插件 -->
            <configuration>
                <!-- 具体配置信息会因为插件不同、需求不同而有所差异 -->
                <source>1.8</source>
                <target>1.8</target>
                <encoding>UTF-8</encoding>
            </configuration>
        </plugin>
    </plugins>
</build>
```



#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_5再次执行编译命令)⑤再次执行编译命令

![images](maven_2022.assets/img117.567b90ed.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_6两种配置方式比较)⑥两种配置方式比较

- settings.xml 中配置：仅在本地生效，如果脱离当前 settings.xml 能够覆盖的范围，则无法生效。
- 在当前 Maven 工程 pom.xml 中配置：无论在哪个环境执行编译等构建操作都有效。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_7补充说明)⑦补充说明

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_1-source-标签含义)[1]source 标签含义

查看 [Maven 官网页面 (opens new window)](http://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.html)，我们找到 source 标签的介绍：

![images](data:image/png;base64,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)

翻译过来就是：调用 Java 编译器命令时传入的 -source 参数。那对编译器来说，-source 参数是啥意思呢？

![images](maven_2022.assets/img109.6a115e94.png)



『提供与指定发行版的源兼容性』这句话我的理解是：

- 我们写代码是按 JDK 1.8 写的——这就是『源兼容性』里的『源』。
- 指定发行版就是我们指定的 JDK 1.8。
- 『兼容性』是谁和谁兼容呢？现在源代码是既定的，所以就是要求编译器使用指定的 JDK 版本来兼容我们的源代码。



另外我们还看到：

![images](data:image/png;base64,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)

这个功能还可以通过在 properties 标签中配置 maven.compiler.source 属性来实现。所以我们也经常会看到类似这样的配置：

```xml
<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

1
2
3
4
5

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_2-target-标签含义)[2]target 标签含义

![images](data:image/png;base64,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)

翻译过来就是：调用 Java 编译器命令时传入的 -target 参数。那对编译器来说，-target 参数是啥意思呢？

![images](maven_2022.assets/img112.f7844c6b.png)

『生成特定 VM 版本的类文件』这句话我的理解是：

- VM 指 JVM
- 类文件指 *.class 字节码文件
- 整体意思就是源文件编译后，生成的 *.class 字节码文件要符合指定的 JVM 版本

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_4、典型应用-springboot-定制化打包)4、典型应用：SpringBoot 定制化打包

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_1需求)①需求

很显然 spring-boot-maven-plugin 并不是 Maven 自带的插件，而是 SpringBoot 提供的，用来改变 Maven 默认的构建行为。具体来说是改变打包的行为。默认情况下 Maven 调用 maven-jar-plugin 插件的 jar 目标，生成普通的 jar 包。

普通 jar 包没法使用 java -jar xxx.jar 这样的命令来启动、运行，但是 SpringBoot 的设计理念就是每一个『微服务』导出为一个 jar 包，这个 jar 包可以使用 java -jar xxx.jar 这样的命令直接启动运行。

这样一来，打包的方式肯定要进行调整。所以 SpringBoot 提供了 spring-boot-maven-plugin 这个插件来定制打包行为。

![images](maven_2022.assets/img118.48c7b12c.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_2示例代码)②示例代码

所有的一切已经都被 SpringBoot 封装好了，所以配置非常简单，提供插件坐标即可。

```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
			<version>2.5.5</version>
		</plugin>
	</plugins>
</build>
```

1
2
3
4
5
6
7
8
9

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_3插件的七个目标)③插件的七个目标

![images](maven_2022.assets/img120.3be3c586.png)

| 目标名称                | 作用                                                         |
| ----------------------- | ------------------------------------------------------------ |
| spring-boot:build-image | Package an application into a OCI image using a buildpack.   |
| spring-boot:build-info  | Generate a build-info.properties file based on the content of the current MavenProject. |
| spring-boot:help        | Display help information on spring-boot-maven-plugin. Call mvn spring-boot:help -Ddetail=true -Dgoal=<goal-name> to display parameter details. |
| spring-boot:repackage   | Repackage existing JAR and WAR archives so that they can be executed from the command line using java -jar. With layout=NONE can also be used simply to package a JAR with nested dependencies (and no main class, so not executable). |
| spring-boot:run         | Run an application in place.                                 |
| spring-boot:start       | Start a spring application. Contrary to the run goal, this does not block and allows other goals to operate on the application. This goal is typically used in integration test scenario where the application is started before a test suite and stopped after. |
| spring-boot:stop        | Stop an application that has been started by the 'start' goal. Typically invoked once a test suite has completed. |

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_5、典型应用-mybatis-逆向工程)5、典型应用：Mybatis 逆向工程

使用 Mybatis 的逆向工程需要使用如下配置，MBG 插件的特点是需要提供插件所需的依赖：

```xml
<!-- 控制 Maven 在构建过程中相关配置 -->
<build>
		
	<!-- 构建过程中用到的插件 -->
	<plugins>
		
		<!-- 具体插件，逆向工程的操作是以构建过程中插件形式出现的 -->
		<plugin>
			<groupId>org.mybatis.generator</groupId>
			<artifactId>mybatis-generator-maven-plugin</artifactId>
			<version>1.3.0</version>
	
			<!-- 插件的依赖 -->
			<dependencies>
				
				<!-- 逆向工程的核心依赖 -->
				<dependency>
					<groupId>org.mybatis.generator</groupId>
					<artifactId>mybatis-generator-core</artifactId>
					<version>1.3.2</version>
				</dependency>
					
				<!-- 数据库连接池 -->
				<dependency>
					<groupId>com.mchange</groupId>
					<artifactId>c3p0</artifactId>
					<version>0.9.2</version>
				</dependency>
					
				<!-- MySQL驱动 -->
				<dependency>
					<groupId>mysql</groupId>
					<artifactId>mysql-connector-java</artifactId>
					<version>5.1.8</version>
				</dependency>
			</dependencies>
		</plugin>
	</plugins>
</build>
```

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse04.html#_6、小结)6、小结

不知大家有没有发现，通常需要用到 build 标签的时候底层都会帮我们封装好，需要我们配置的地方不多。即使有些地方需要我们配置，也不会真的我们自己去写，把现成的案例复制过来就行。

所以对 build 标签来说，我们的掌握要求就是：**能大致看懂**就行。

## 第五节 依赖配置补充

TIP

[Maven 官网介绍依赖机制(opens new window)](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse05.html#_1、依赖范围)1、依赖范围

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse05.html#_1import)①import

管理依赖最基本的办法是继承父工程，但是和 Java 类一样，Maven 也是单继承的。如果不同体系的依赖信息封装在不同 POM 中了，没办法继承多个父工程怎么办？这时就可以使用 import 依赖范围。

典型案例当然是在项目中引入 SpringBoot、SpringCloud 依赖：

```xml
<dependencyManagement>
    <dependencies>

        <!-- SpringCloud 依赖导入 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>Hoxton.SR9</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <!-- SpringCloud Alibaba 依赖导入 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>2.2.6.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <!-- SpringBoot 依赖导入 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>2.3.6.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31

import 依赖范围使用要求：

- 打包类型必须是 pom
- 必须放在 dependencyManagement 中

> 官网说明如下：
>
> This scope is only supported on a dependency of type `pom` in the `<dependencyManagement>` section. It indicates the dependency is to be replaced with the effective list of dependencies in the specified POM's `<dependencyManagement>` section. Since they are replaced, dependencies with a scope of `import` do not actually participate in limiting the transitivity of a dependency.

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse05.html#_2system)②system

以 Windows 系统环境下开发为例，假设现在 D:\tempare\atguigu-maven-test-aaa-1.0-SNAPSHOT.jar 想要引入到我们的项目中，此时我们就可以将依赖配置为 system 范围：

```xml
<dependency>
    <groupId>com.atguigu.maven</groupId>
    <artifactId>atguigu-maven-test-aaa</artifactId>
    <version>1.0-SNAPSHOT</version>
    <systemPath>D:\tempare\atguigu-maven-test-aaa-1.0-SNAPSHOT.jar</systemPath>
    <scope>system</scope>
</dependency>
```

1
2
3
4
5
6
7

但是很明显：这样引入依赖完全不具有可移植性，所以**不要使用**。如果需要引入体系外 jar 包我们后面会讲专门的办法。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse05.html#_3runtime)③runtime

专门用于编译时不需要，但是运行时需要的 jar 包。比如：编译时我们根据接口调用方法，但是实际运行时需要的是接口的实现类。典型案例是：

```xml
<!--热部署 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

1
2
3
4
5
6
7

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse05.html#_2、可选依赖)2、可选依赖

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse05.html#_1配置举例)①配置举例

```xml
<!--热部署 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

1
2
3
4
5
6
7

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse05.html#_2本质含义)②本质含义

可选其实就是『可有可无』。官网的解释是：

![images](maven_2022.assets/img013.b6ada9b0.png)

其核心含义是：Project X 依赖 Project A，A 中一部分 X 用不到的代码依赖了 B，那么对 X 来说 B 就是『可有可无』的。

![images](maven_2022.assets/img012.b802c22a.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse05.html#_3、版本仲裁)3、版本仲裁

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse05.html#_1最短路径优先)①最短路径优先

在下图的例子中，对模块 pro25-module-a 来说，Maven 会采纳 1.2.12 版本。

![images](data:image/png;base64,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)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse05.html#_2路径相同时先声明者优先)②路径相同时先声明者优先

![images](maven_2022.assets/img205.1dbd09c7.png)

此时 Maven 采纳哪个版本，取决于在 pro29-module-x 中，对 pro30-module-y 和 pro31-module-z 两个模块的依赖哪一个先声明。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse05.html#_3小结)③小结

其实 Maven 的版本仲裁机制只是在没有人为干预的情况下，自主决定 jar 包版本的一个办法。而实际上我们要使用具体的哪一个版本，还要取决于项目中的实际情况。所以在项目正常运行的情况下，jar 包版本可以由 Maven 仲裁，不必我们操心；而发生冲突时 Maven 仲裁决定的版本无法满足要求，此时就应该由程序员明确指定 jar 包版本。

## 第六节 Maven 自定义插件

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_1、本节定位)1、本节定位

其实实际开发中几乎没有什么场景需要我们开发自定义 Maven 插件，所以本节只是通过这个角度帮助我们更好的理解插件的目标和生命周期阶段之间的关系。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_2、插件开发)2、插件开发

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_1创建工程)①创建工程

[略]

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_2设定打包方式)②设定打包方式

```xml
<packaging>maven-plugin</packaging>
```

1

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_3引入依赖)③引入依赖

下面两种方式二选一：

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_1-将来在文档注释中使用注解)[1]将来在文档注释中使用注解

```xml
<dependency>
    <groupId>org.apache.maven</groupId>
    <artifactId>maven-plugin-api</artifactId>
    <version>3.5.2</version>
</dependency>
```

1
2
3
4
5

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_2-将来直接使用注解)[2]将来直接使用注解

```xml
<dependency>
    <groupId>org.apache.maven.plugin-tools</groupId>
    <artifactId>maven-plugin-annotations</artifactId>
    <version>3.5.2</version>
</dependency>
```

1
2
3
4
5

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_4创建-mojo-类)④创建 Mojo 类

Mojo 类是一个 Maven 插件的核心类。

Mojo 这个单词的意思是：Maven Old Java Object，其实 mojo 这个单词本身包含魔力;符咒(袋);护身符;(人的)魅力的含义，Maven 用 Mojo 是因为它是对 POJO 开的一个小玩笑。

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_1-mojo-接口)[1] Mojo 接口

每一个 Mojo 都需要实现 org.apache.maven.plugin.Mojo 接口。

![images](data:image/png;base64,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)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_2-abstractmojo-抽象类)[2] AbstractMojo 抽象类

我们实现 Mojo 接口比较困难，幸好可以继承 AbstractMojo，此时我们只要实现 execute() 这一个方法即可。

```java
public class MyHelloPlugin extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("---> This is my first maven plugin. <---");
    }
}
```

1
2
3
4
5
6

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_3、插件配置)3、插件配置

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_1mojo-类中的配置)①Mojo 类中的配置

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_1-文档注释中用注解)[1]文档注释中用注解

对应的 pom.xml 中的依赖： maven-plugin-api

![images](maven_2022.assets/img015.ca57432a.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_2-直接在类上标记注解)[2]直接在类上标记注解

对应 pom.xml 中的依赖：maven-plugin-annotations

```java
// name 属性：指定目标名称
@Mojo(name = "firstBlood")
public class MyPluginOfFistBlood extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("---> first blood <---");
    }
}
```

1
2
3
4
5
6
7
8

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_2安装插件)②安装插件

要在后续使用插件，就必须至少将插件安装到本地仓库。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_3注册插件)③注册插件

我们需要将插件坐标中的 groupId 部分注册到 **settings.xml** 中。

```xml
<pluginGroups>
	<!-- pluginGroup
	 | Specifies a further group identifier to use for plugin lookup.
	<pluginGroup>com.your.plugins</pluginGroup>
	-->
	<pluginGroup>com.atguigu.maven</pluginGroup>
</pluginGroups>
```

1
2
3
4
5
6
7

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_4、使用插件)4、使用插件

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_1识别插件前缀)①识别插件前缀

Maven 根据插件的 artifactId 来识别插件前缀。例如下面两种情况：

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_1-前置匹配)[1]前置匹配

- 匹配规则：${prefix}-maven-plugin
- artifactId：hello-maven-plugin
- 前缀：hello

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_2-中间匹配)[2]中间匹配

- 匹配规则：maven-${prefix}-plugin
- artifactId：maven-good-plugin
- 前缀：good

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_2在命令行直接用)②在命令行直接用

- 命令：

```sh
mvn hello:sayHello
```

1

- 效果：

![images](maven_2022.assets/img016.faea8444.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_3配置到-build-标签里)③配置到 build 标签里

这里找一个和插件无关的 Maven 工程配置才有说服力。

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_1-配置)[1]配置

```xml
<build>
	<plugins>
		<plugin>
			<groupId>com.atguigu.maven</groupId>
			<artifactId>hello-maven-plugin</artifactId>
			<version>1.0-SNAPSHOT</version>
			<executions>
				<execution>
                    <id>hello</id>
                    <!-- 指定和目标关联的生命周期阶段 -->
					<phase>clean</phase>
					<goals>
						<goal>sayHello</goal>
					</goals>
				</execution>
                <execution>
                    <id>blood</id>
                    <phase>validate</phase>
                    <goals>
                        <goal>firstBlood</goal>
                    </goals>
                </execution>
			</executions>
		</plugin>
	</plugins>
</build>
```

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_2-效果)[2]效果

![images](data:image/png;base64,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)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_3-图形化界面使用)[3]图形化界面使用

![images](maven_2022.assets/img018.35b9f044.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse06.html#_4-命令行使用)[4]命令行使用

执行已和插件目标绑定的生命周期：

![images](maven_2022.assets/img019.6771554e.png)

## 第七节 profile 详解

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_1、profile-概述)1、profile 概述

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_1单词释义)①单词释义

![images](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAUsAAACFCAIAAABdQE25AAAYQElEQVR42u2db3AURd7HG/xbWOhxXMkdqUIWUtZhKVJUVCqJVLmSClCPh/IqLzYRE0oRKIIiXp1mI24if8QXIY+liMlqQUp4wRuiIqmLq8clETVehS3rXiC6eZDA8/Dwp7QsUPDP9cz09HT39Mz0zG6yk+H3eZHa7J+Znv71t/vX/3494bfffkMAAESUCaBwAIgwoHAAiDKgcACIMqBwAIgyoHAAiDKgcACIMqBwAIgyoHAAiDJFU3i2vax+N3ld0dzTMFyt/Vue7GlfNpV+av4LAEAwiqTwobaylV30v0THYPxwGSgcAApOcRVekTy4Y9mtks9B4QBQENwVnm0rq+9ivWgNVpb0C+l4b33LAPcp64ezWuXe10ikB9ejdq82/MyBxqUt/cxP5hY77wAg/Cgp3A72q9fPk32BaFL6QyL+IArnvXr2agAAuKCqcFHSopI5vZkapm8Kv5J46Vk3hZs/r0sPrsMt97kD66o1f4H8CwCAI2oKZ7XEiVP2Bca33/GXqbJf+VS4vdNO3gFfHQA8UO2HW1o1+8N6q+75BSR/04/CUXdjdapfljxw1AHAA1A4AESZwnjpnMLHwEsHAECNwoy08Qof7ZE2mC0HAFUKM1smKNzhh8zAmD+Fo3MyR53rBQAAIEO1H96MUqbGpCteBIXrnzmseNHwqXANbsULeOwAoIT/kTYAAMYPoHAAiDKgcACIMqBwAIgyEOMFAKIMKBwAogwoHACiDCgcAKIMKBwAogwoHACiDCgcAKIMKBwAogwoHACiDCgcAKIMKBwAogwofPxx5syZPXv2bNiwodgJAYqAX+uDwgvGr7/+unfvXmyAp556qlDX/Omnn/r6+oaGhk6dOvXDDz9Mnjx5+vTpd911V3d399atW2+66aZiPzRACK31QeGF4ccff3zzzTcvX7587NixN954Q/h08+bNa9euvfnmm31d89NPP92/f39paek999wzc+bMSZMmXbx4MZfLff7559jqq1evnjsXosWHgjBbHxReAC5cuPDqq6/OmjXr4Ycffvrpp+02/vDDD7/99tsVK1aoX/O99947cuTIypUrsXXtn+JKfdq0addcc02xHx0Iu/XHXOFnDrQdqVwfod3mR48efeeddxYtWlRVVYVdtSeffNJu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)

这里我们可以对接 profile 这个单词中『侧面』这个含义：项目的每一个运行环境，相当于是项目整体的一个侧面。

![images](data:image/png;base64,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)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_2项目的不同运行环境)②项目的不同运行环境

![images](maven_2022.assets/img020.a8144d62.png)

通常情况下，我们至少有三种运行环境：

- 开发环境：供不同开发工程师开发的各个模块之间互相调用、访问；内部使用
- 测试环境：供测试工程师对项目的各个模块进行功能测试；内部使用
- 生产环境：供最终用户访问——所以这是正式的运行环境，对外提供服务

而我们这里的『环境』仍然只是一个笼统的说法，实际工作中一整套运行环境会包含很多种不同服务器：

- MySQL
- Redis
- ElasticSearch
- RabbitMQ
- FastDFS
- Nginx
- Tomcat
- ……

就拿其中的 MySQL 来说，不同环境下的访问参数肯定完全不同：

| 开发环境                                                     | 测试环境                                                     | 生产环境                                                     |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| dev.driver=com.mysql.jdbc.Driver dev.url=jdbc:mysql://124.71.36.17:3306/db-sys dev.username=root dev.password=atguigu | test.driver=com.mysql.jdbc.Driver test.url=jdbc:mysql://124.71.36.89:3306/db-sys test.username=dev-team test.password=atguigu | product.driver=com.mysql.jdbc.Driver product.url=jdbc:mysql://39.107.88.164:3306/prod-db-sys product.username=root product.password=atguigu |

可是代码只有一套。如果在 jdbc.properties 里面来回改，那就太麻烦了，而且很容易遗漏或写错，增加调试的难度和工作量。所以最好的办法就是把适用于各种不同环境的配置信息分别准备好，部署哪个环境就激活哪个配置。

在 Maven 中，使用 profile 机制来管理不同环境下的配置信息。但是解决同类问题的类似机制在其他框架中也有，而且从模块划分的角度来说，持久化层的信息放在构建工具中配置也违反了『高内聚，低耦合』的原则。

所以 Maven 的 profile 我们了解一下即可，不必深究。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_3profile-声明和使用的基本逻辑)③profile 声明和使用的基本逻辑

- 首先为每一个环境声明一个 profile
  - 环境 A：profile A
  - 环境 B：profile B
  - 环境 C：profile C
  - ……
- 然后激活某一个 profile

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_4默认-profile)④默认 profile

其实即使我们在 pom.xml 中不配置 profile 标签，也已经用到 profile了。为什么呢？因为根标签 project 下所有标签相当于都是在设定默认的 profile。这样一来我们也就很容易理解下面这句话：project 标签下除了 modelVersion 和坐标标签之外，其它标签都可以配置到 profile 中。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_2、profile-配置)2、profile 配置

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_1外部视角-配置文件)①外部视角：配置文件

从外部视角来看，profile 可以在下面两种配置文件中配置：

- settings.xml：全局生效。其中我们最熟悉的就是配置 JDK 1.8。
- pom.xml：当前 POM 生效

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_2内部实现-具体标签)②内部实现：具体标签

从内部视角来看，配置 profile 有如下语法要求：

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_1-profiles-profile-标签)[1] profiles/profile 标签

- 由于 profile 天然代表众多可选配置中的一个所以由复数形式的 profiles 标签统一管理。
- 由于 profile 标签覆盖了 pom.xml 中的默认配置，所以 profiles 标签通常是 pom.xml 中的最后一个标签。

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_2-id-标签)[2]id 标签

每个 profile 都必须有一个 id 标签，指定该 profile 的唯一标识。这个 id 标签的值会在命令行调用 profile 时被用到。这个命令格式是：-D<profile id>。

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_3-其它允许出现的标签)[3]其它允许出现的标签

一个 profile 可以覆盖项目的最终名称、项目依赖、插件配置等各个方面以影响构建行为。

- build
  - defaultGoal
  - finalName
  - resources
  - testResources
  - plugins
- reporting
- modules
- dependencies
- dependencyManagement
- repositories
- pluginRepositories
- properties

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_3、激活-profile)3、激活 profile

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_1默认配置默认被激活)①默认配置默认被激活

前面提到了，POM 中没有在 profile 标签里的就是默认的 profile，当然默认被激活。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_2基于环境信息激活)②基于环境信息激活

环境信息包含：JDK 版本、操作系统参数、文件、属性等各个方面。一个 profile 一旦被激活，那么它定义的所有配置都会覆盖原来 POM 中对应层次的元素。大家可以参考下面的标签结构：

```xml
<profile>
	<id>dev</id>
    <activation>
        <!-- 配置是否默认激活 -->
    	<activeByDefault>false</activeByDefault>
        <jdk>1.5</jdk>
        <os>
        	<name>Windows XP</name>
            <family>Windows</family>
            <arch>x86</arch>
            <version>5.1.2600</version>
        </os>
        <property>
        	<name>mavenVersion</name>
            <value>2.0.5</value>
        </property>
        <file>
        	<exists>file2.properties</exists>
            <missing>file1.properties</missing>
        </file>
    </activation>
</profile>
```

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22

这里有个问题是：多个激活条件之间是什么关系呢？

- Maven **3.2.2 之前**：遇到第一个满足的条件即可激活——**或**的关系。
- Maven **3.2.2 开始**：各条件均需满足——**且**的关系。

下面我们来看一个具体例子。假设有如下 profile 配置，在 JDK 版本为 1.6 时被激活：

```xml
<profiles>
	<profile>
    	<id>JDK1.6</id>
        <activation>
            <!-- 指定激活条件为：JDK 1.6 -->
        	<jdk>1.6</jdk>
        </activation>
        ……
    </profile>
</profiles>
```

1
2
3
4
5
6
7
8
9
10

这里需要指出的是：Maven 会自动检测当前环境安装的 JDK 版本，只要 JDK 版本是以 1.6 开头都算符合条件。下面几个例子都符合：

- 1.6.0_03
- 1.6.0_02
- ……

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_3命令行激活)③命令行激活

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_1-列出活动的-profile)[1]列出活动的 profile

```sh
## 列出所有激活的 profile，以及它们在哪里定义
mvn help:active-profiles
```

1
2

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_2-指定某个具体-profile)[2]指定某个具体 profile

```xml
mvn compile -P<profile id>
```

1

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_4、操作举例)4、操作举例

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_1编写-lambda-表达式代码)①编写 Lambda 表达式代码

Lambda 表达式代码要求 JDK 版本必须是 1.8，我们可以以此来判断某个指定更低 JDK 版本的 profile 是否被激活生效。

```java
@Test
public void test() {
    new Thread(()->{
        System.out.println(Thread.currentThread().getName() + " is working");
    }).start();
}
```

1
2
3
4
5
6

以目前配置运行这个测试方法：

![images](data:image/png;base64,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)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_2配置-profile)②配置 profile

```xml
<profiles>
    <profile>
        <id>myJDKProfile</id>
        <!-- build 标签：意思是告诉 Maven，你的构建行为，我要开始定制了！ -->
        <build>
            <!-- plugins 标签：Maven 你给我听好了，你给我构建的时候要用到这些插件！ -->
            <plugins>
                <!-- plugin 标签：这是我要指定的一个具体的插件 -->
                <plugin>
                    <!-- 插件的坐标。此处引用的 maven-compiler-plugin 插件不是第三方的，是一个 Maven 自带的插件。 -->
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.1</version>

                    <!-- configuration 标签：配置 maven-compiler-plugin 插件 -->
                    <configuration>
                        <!-- 具体配置信息会因为插件不同、需求不同而有所差异 -->
                        <source>1.6</source>
                        <target>1.6</target>
                        <encoding>UTF-8</encoding>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_3执行构建命令)③执行构建命令

```sh
mvn clean test -PmyJDKProfile
```

1

![images](maven_2022.assets/img024.4eeaea9b.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_5、资源属性过滤)5、资源属性过滤

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_1简介)①简介

Maven 为了能够通过 profile 实现各不同运行环境切换，提供了一种『资源属性过滤』的机制。通过属性替换实现不同环境使用不同的参数。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_2操作演示)②操作演示

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_1-配置-profile)[1]配置 profile

```xml
<profiles>
    <profile>
        <id>devJDBCProfile</id>
        <properties>
            <dev.jdbc.user>root</dev.jdbc.user>
            <dev.jdbc.password>atguigu</dev.jdbc.password>
            <dev.jdbc.url>http://localhost:3306/db_good</dev.jdbc.url>
            <dev.jdbc.driver>com.mysql.jdbc.Driver</dev.jdbc.driver>
        </properties>
        <build>
            <resources>
                <resource>
                    <!-- 表示为这里指定的目录开启资源过滤功能 -->
                    <directory>src/main/resources</directory>

                    <!-- 将资源过滤功能打开 -->
                    <filtering>true</filtering>
                </resource>
            </resources>
        </build>
    </profile>
</profiles>
```

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_2-创建待处理的资源文件)[2]创建待处理的资源文件

```properties
dev.user=${dev.jdbc.user}
dev.password=${dev.jdbc.password}
dev.url=${dev.jdbc.url}
dev.driver=${dev.jdbc.driver}
```

1
2
3
4

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_3-执行处理资源命令)[3]执行处理资源命令

```sh
mvn clean resources:resources -PdevJDBCProfile
```

1

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_4-找到处理得到的资源文件)[4]找到处理得到的资源文件

![images](maven_2022.assets/img025.97fa02d4.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter09/verse07.html#_5-延伸)[5]延伸

我们时不时会在 resource 标签下看到 includes 和 excludes 标签。它们的作用是：

- includes：指定执行 resource 阶段时要包含到目标位置的资源
- excludes：指定执行 resource 阶段时要排除的资源

情看下面的例子：

```xml
<build>
    <resources>
        <resource>
            <!-- 表示为这里指定的目录开启资源过滤功能 -->
            <directory>src/main/resources</directory>

            <!-- 将资源过滤功能打开 -->
            <filtering>true</filtering>

            <includes>
                <include>*.properties</include>
            </includes>

            <excludes>
                <exclude>happy.properties</exclude>
            </excludes>
        </resource>
    </resources>
</build>
```

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19

执行处理资源命令：

```sh
mvn clean resources:resources -PdevJDBCProfile
```

1

执行效果如下：

![images](data:image/png;base64,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)

当然我们这里只是以 properties 文件为例，并不是只能处理 properties 文件。

#  第十章 生产实践

## 第一节 搭建 Maven 私服：Nexus

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_1、nexus-安装)1、Nexus 安装

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_1下载地址)①下载地址

小诀窍：使用迅雷下载比直接用浏览器下载快很多

https://download.sonatype.com/nexus/3/latest-unix.tar.gz

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_2上传、解压)②上传、解压

上传到 Linux 系统，解压后即可使用，不需要安装。但是需要**注意**：必须提前安装 JDK。

> [root@x nexus-3.37.0-01]# ll
> 总用量 96
> drwxr-xr-x. 3 root root 4096 2月 13 17:33 bin
> drwxr-xr-x. 2 root root 4096 2月 13 17:33 deploy
> drwxr-xr-x. 7 root root 4096 2月 13 17:33 etc
> drwxr-xr-x. 5 root root 4096 2月 13 17:33 lib
> -rw-r--r--. 1 root root 651 11月 20 01:40 NOTICE.txt
> -rw-r--r--. 1 root root 17321 11月 20 01:40 OSS-LICENSE.txt
> -rw-r--r--. 1 root root 41954 11月 20 01:40 PRO-LICENSE.txt
> drwxr-xr-x. 3 root root 4096 2月 13 17:33 public
> drwxr-xr-x. 3 root root 4096 2月 13 17:33 replicator
> drwxr-xr-x. 23 root root 4096 2月 13 17:33 system

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_3启动-nexus)③启动 Nexus

> [root@x ~]# /opt/nexus-3.37.0-01/bin/**nexus start**
> WARNING: ************************************************************
> WARNING: Detected execution as "root" user. This is NOT recommended!
> WARNING: ************************************************************
> Starting nexus
> [root@x ~]# /opt/nexus-3.37.0-01/bin/**nexus status**
> WARNING: ************************************************************
> WARNING: Detected execution as "root" user. This is NOT recommended!
> WARNING: ************************************************************
> nexus is running.

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_4查看端口占用情况)④查看端口占用情况

> [root@x ~]# netstat -anp | grep java
> tcp 0 0 127.0.0.1:**45614** 0.0.0.0:* LISTEN 9872/java
> tcp 0 0 0.0.0.0:**8081** 0.0.0.0:* LISTEN 9872/java

上面 45614 这个每次都不一样，不用管它。我们要访问的是 8081 这个端口。但是需要**注意**：8081 端口的这个进程要在启动 /opt/nexus-3.37.0-01/bin/nexus 这个主体程序**一、两分钟**后才会启动，请耐心等待。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_5访问-nexus-首页)⑤访问 Nexus 首页

首页地址：http://[Linux 服务器地址]:8081/

初始化界面还是很酷的：

![images](maven_2022.assets/img001.612496a3.png)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_2、初始设置)2、初始设置

![images](maven_2022.assets/img002.e1ac8197.png)

![images](maven_2022.assets/img003.97a620db.png)

这里参考提示：

- 用户名：admin
- 密码：查看 /opt/sonatype-work/nexus3/admin.password 文件

> [root@hello ~]# cat /opt/sonatype-work/nexus3/admin.password
> ed5e96a8-67aa-4dca-9ee8-1930b1dd5415

所以登录信息输入如下：

![images](maven_2022.assets/img004.266b8a05.png)

继续执行初始化：

![images](maven_2022.assets/img005.4b81e5ab.png)

给 admin 用户指定新密码：

![images](maven_2022.assets/img006.43ebb0ac.png)

匿名登录，启用还是禁用？由于启用匿名登录后，后续操作比较简单，这里我们演示禁用匿名登录的操作方式：

![images](maven_2022.assets/img007.9291087d.png)

完成：

![images](data:image/png;base64,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)

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_3、对接-nexus)3、对接 Nexus

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_1通过-nexus-下载-jar-包)①通过 Nexus 下载 jar 包

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_1-了解-nexus-上的各种仓库)[1]了解 Nexus 上的各种仓库

![images](maven_2022.assets/img009.7f737ed7.png)

| 仓库类型 | 说明                                           |
| -------- | ---------------------------------------------- |
| proxy    | 某个远程仓库的代理                             |
| group    | 存放：通过 Nexus 获取的第三方 jar 包           |
| hosted   | 存放：本团队其他开发人员部署到 Nexus 的 jar 包 |

| 仓库名称        | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| maven-central   | Nexus 对 Maven 中央仓库的代理                                |
| maven-public    | Nexus 默认创建，供开发人员下载使用的组仓库                   |
| maven-releasse  | Nexus 默认创建，供开发人员部署自己 jar 包的宿主仓库 要求 releasse 版本 |
| maven-snapshots | Nexus 默认创建，供开发人员部署自己 jar 包的宿主仓库 要求 snapshots 版本 |

初始状态下，这几个仓库都没有内容：

![images](maven_2022.assets/img010.e3573d0b.png)

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_2-使用空的本地仓库)[2]使用空的本地仓库

![images](data:image/png;base64,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)

```xml
  <!-- 配置一个新的 Maven 本地仓库 -->
  <localRepository>D:/maven-repository-new</localRepository>
```

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_3-指定-nexus-服务器地址)[3]指定 Nexus 服务器地址

把我们原来配置阿里云仓库地址的 mirror 标签改成下面这样：

```xml
<mirror>
	<id>nexus-mine</id>
	<mirrorOf>central</mirrorOf>
	<name>Nexus mine</name>
	<url>http://192.168.198.100:8081/repository/maven-public/</url>
</mirror>
```

这里的 url 标签是这么来的：

![images](maven_2022.assets/img012.5a3b1f11.png)



![images](maven_2022.assets/img013.959ab72e.png)

把上图中看到的地址复制出来即可。如果我们在前面允许了匿名访问，到这里就够了。但如果我们禁用了匿名访问，那么接下来我们还要继续配置 settings.xml：

```xml
<server>
  <id>nexus-mine</id>
  <username>admin</username>
  <password>atguigu</password>
</server>
```

这里需要**格外注意**：server 标签内的 id 标签值必须和 mirror 标签中的 id 值一样。

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_4-效果)[4]效果

找一个用到框架的 Maven 工程，执行命令：

```sh
mvn clean compile
```

1

下载过程日志：

> Downloading from nexus-mine: http://192.168.198.100:8081/repository/maven-public/com/jayway/jsonpath/json-path/2.4.0/json-path-2.4.0.pom
> Downloaded from nexus-mine: http://192.168.198.100:8081/repository/maven-public/com/jayway/jsonpath/json-path/2.4.0/json-path-2.4.0.pom (2.6 kB at 110 kB/s)
> Downloading from nexus-mine: http://192.168.198.100:8081/repository/maven-public/net/minidev/json-smart/2.3/json-smart-2.3.pom
> Downloaded from nexus-mine: http://192.168.198.100:8081/repository/maven-public/net/minidev/json-smart/2.3/json-smart-2.3.pom (9.0 kB at 376 kB/s)
> Downloading from nexus-mine: http://192.168.198.100:8081/repository/maven-public/net/minidev/minidev-parent/2.3/minidev-parent-2.3.pom
> Downloaded from nexus-mine: http://192.168.198.100:8081/repository/maven-public/net/minidev/minidev-parent/2.3/minidev-parent-2.3.pom (8.5 kB at 404 kB/s)
> Downloading from nexus-mine: http://192.168.198.100:8081/repository/maven-public/net/minidev/accessors-smart/1.2/accessors-smart-1.2.pom
> Downloaded from nexus-mine: http://192.168.198.100:8081/repository/maven-public/net/minidev/accessors-smart/1.2/accessors-smart-1.2.pom (12 kB at 463 kB/s)

下载后，Nexus 服务器上就有了 jar 包：

![images](maven_2022.assets/img014.cc0e87c3.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_2将-jar-包部署到-nexus)②将 jar 包部署到 Nexus

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_1-配置-maven-工程)[1]配置 Maven 工程

```xml
<distributionManagement>
    <snapshotRepository>
        <id>nexus-mine</id>
        <name>Nexus Snapshot</name>
        <url>http://192.168.198.100:8081/repository/maven-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

1
2
3
4
5
6
7

这里 snapshotRepository 的 id 标签也必须和 settings.xml 中指定的 mirror 标签的 id 属性一致。

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_2-执行部署命令)[2]执行部署命令

```sh
mvn deploy
```

1

> Uploading to nexus-mine: http://192.168.198.100:8081/repository/maven-snapshots/com/atguigu/demo/demo07-redis-data-provider/1.0-SNAPSHOT/maven-metadata.xml
> Uploaded to nexus-mine: http://192.168.198.100:8081/repository/maven-snapshots/com/atguigu/demo/demo07-redis-data-provider/1.0-SNAPSHOT/maven-metadata.xml (786 B at 19 kB/s)
> Uploading to nexus-mine: http://192.168.198.100:8081/repository/maven-snapshots/com/atguigu/demo/demo07-redis-data-provider/maven-metadata.xml
> Uploaded to nexus-mine: http://192.168.198.100:8081/repository/maven-snapshots/com/atguigu/demo/demo07-redis-data-provider/maven-metadata.xml (300 B at 6.5 kB/s)
> [INFO] ------------------------------------------------------------------------
> [INFO] Reactor Summary:
> [INFO]
> [INFO] demo-imperial-court-ms-show 1.0-SNAPSHOT ........... SUCCESS [ 1.875 s]
> [INFO] demo09-base-entity ................................. SUCCESS [ 21.883 s]
> [INFO] demo10-base-util ................................... SUCCESS [ 0.324 s]
> [INFO] demo08-base-api .................................... SUCCESS [ 1.171 s]
> [INFO] demo01-imperial-court-gateway ...................... SUCCESS [ 0.403 s]
> [INFO] demo02-user-auth-center ............................ SUCCESS [ 2.932 s]
> [INFO] demo03-emp-manager-center .......................... SUCCESS [ 0.312 s]
> [INFO] demo04-memorials-manager-center .................... SUCCESS [ 0.362 s]
> [INFO] demo05-working-manager-center ...................... SUCCESS [ 0.371 s]
> [INFO] demo06-mysql-data-provider ......................... SUCCESS [ 6.779 s]
> [INFO] demo07-redis-data-provider 1.0-SNAPSHOT ............ SUCCESS [ 0.273 s]

![images](maven_2022.assets/img015.b413af9d.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_3引用别人部署的-jar-包)③引用别人部署的 jar 包

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_1-提出问题)[1]提出问题

- 默认访问的 Nexus 仓库：maven-public
- 存放别人部署 jar 包的仓库：maven-snapshots

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_2-配置-maven-工程)[2]配置 Maven 工程

```xml
<repositories>
    <repository>
        <id>nexus-mine</id>
        <name>nexus-mine</name>
        <url>http://192.168.198.100:8081/repository/maven-snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
        <releases>
            <enabled>true</enabled>
        </releases>
    </repository>
</repositories>
```



### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse01.html#_4、修改仓库配置)4、修改仓库配置

举例：修改 maven-central 仓库代理的远程库地址

![images](maven_2022.assets/img128.714c1100.png)

![images](maven_2022.assets/img129.c33151a5.png)

![images](data:image/png;base64,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)

## 第二节 jar包冲突问题

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_1、谁需要面对-jar-包冲突)1、谁需要面对 jar 包冲突？

先给结论：编订依赖列表的程序员。初次设定一组依赖，因为尚未经过验证，所以确实有可能存在各种问题，需要做有针对性的调整。那么谁来做这件事呢？我们最不希望看到的就是：团队中每个程序员都需要自己去找依赖，即使是做同一个项目，每个模块也各加各的依赖，没有统一管理。那前人踩过的坑，后人还要再踩一遍。而且大家用的依赖有很多细节都不一样，版本更是五花八门，这就让事情变得更加复杂。

所以虽然初期需要根据项目开发和实际运行情况对依赖配置不断调整，最终确定一个各方面都 OK 的版本。但是一旦确定下来，放在父工程中做依赖管理，各个子模块各取所需，这样基本上就能很好的避免问题的扩散。

即使开发中遇到了新问题，也可以回到源头检查、调整 dependencyManagement 配置的列表——而不是每个模块都要改。所以学完这一节你应该就会对前面讲过的『继承』有了更深的理解。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_2、表现形式)2、表现形式

由于实际开发时我们往往都会整合使用很多大型框架，所以一个项目中哪怕只是一个模块也会涉及到大量 jar 包。数以百计的 jar 包要彼此协调、精密配合才能保证程序正常运行。而规模如此庞大的 jar 包组合在一起难免会有磕磕碰碰。最关键的是由于 jar 包冲突所导致的问题非常诡异，这里我们只能罗列较为典型的问题，而没法保证穷举。

但是我们仍然能够指出一点：一般来说，由于我们自己编写代码、配置文件写错所导致的问题通常能够在异常信息中看到我们自己类的全类名或配置文件的所在路径。如果整个错误信息中完全没有我们负责的部分，全部是框架、第三方工具包里面的类报错，这往往就是 jar 包的问题所引起的。

而具体的表现形式中，主要体现为找不到类或找不到方法。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_1抛异常-找不到类)①抛异常：找不到类

此时抛出的常见的异常类型：

- java.lang.**ClassNotFoundException**：编译过程中找不到类
- java.lang.**NoClassDefFoundError**：运行过程中找不到类
- java.lang.**LinkageError**：不同类加载器分别加载的多个类有相同的全限定名

我们来举个例子：

```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.x.x</version>
</dependency>
```



httpclient 这个 jar 包中有一个类：org.apache.http.conn.ssl.NoopHostnameVerifier。这个类在较低版本中没有，但在较高版本存在。比如：

| jar 包版本 | 是否存在 |
| ---------- | -------- |
| 4.3.6      | 否       |
| 4.4        | 是       |

那当我们确实需要用到 NoopHostnameVerifier 这个类，我们看到 Maven 通过依赖传递机制引入了这个 jar 包，所以没有明确地显式声明对这个 jar 包的依赖。可是 Maven 传递过来的 jar 包是 4.3.6 版本，里面没有包含我们需要的类，就会抛出异常。

而『冲突』体现在：4.3.6 和 4.4 这两个版本的 jar 包都被框架所依赖的 jar 包给传递进来了，但是假设 Maven 根据**『版本仲裁』**规则实际采纳的是 4.3.6。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_2抛异常-找不到方法)②抛异常：找不到方法

程序找不到符合预期的方法。这种情况多见于通过反射调用方法，所以经常会导致：java.lang.NoSuchMethodError。比如 antlr:antlr:x.x.x 这个包中有一个接口：antlr.collections.AST

| 版本  | getLine()方法 |
| ----- | ------------- |
| 2.7.2 | 无            |
| 2.7.6 | 有            |

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_3没报错但结果不对)③没报错但结果不对

发生这种情况比较典型的原因是：两个 jar 包中的类分别实现了同一个接口，这本来是很正常的。但是问题在于：由于没有注意命名规范，两个不同实现类恰巧是同一个名字。

![images](data:image/png;base64,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)

具体例子是有的同学在实际工作中遇到过：项目中部分模块使用 log4j 打印日志；其它模块使用 logback，编译运行都不会冲突，但是会引起日志服务降级，让你的 log 配置文件失效。比如：你指定了 error 级别输出，但是冲突就会导致 info、debug 都在输出。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_3、本质)3、本质

以上表现形式归根到底是**两种基本情况**导致的：

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_1同一jar包的不同版本)①同一jar包的不同版本

![images](maven_2022.assets/img101.0a16f272.png)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_3不同jar包中包含同名类)③不同jar包中包含同名类

这里我们拿 netty 来举个例子，netty 是一个类似 Tomcat 的 Servlet 容器。通常我们不会直接依赖它，所以基本上都是框架传递进来的。那么当我们用到的框架很多时，就会有不同的框架用不同的坐标导入 netty。大家可以参照下表对比一下两组坐标：

| 截止到3.2.10.Final版本以前的坐标形式：                       | 从3.3.0.Final版本开始以后的坐标形式：                        |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| <dependency> <groupId>**org.jboss.netty**</groupId> <artifactId>**netty**</artifactId> <version>**3.2.10.Final**</version> </dependency> | <dependency> <groupId>**io.netty**</groupId> <artifactId>**netty**</artifactId> <version>**3.9.2.Final**</version> </dependency> |

但是偏偏这两个**『不同的包』**里面又有很多**『全限定名相同』**的类。例如：

> org.jboss.netty.channel.socket.ServerSocketChannelConfig.class org.jboss.netty.channel.socket.nio.NioSocketChannelConfig.class org.jboss.netty.util.internal.jzlib.Deflate.class org.jboss.netty.handler.codec.serialization.ObjectDecoder.class org.jboss.netty.util.internal.ConcurrentHashMap$HashIterator.class org.jboss.netty.util.internal.jzlib.Tree.class org.jboss.netty.util.internal.ConcurrentIdentityWeakKeyHashMap$Segment.class org.jboss.netty.handler.logging.LoggingHandler.class org.jboss.netty.channel.ChannelHandlerLifeCycleException.class org.jboss.netty.util.internal.ConcurrentIdentityHashMap$ValueIterator.class org.jboss.netty.util.internal.ConcurrentIdentityWeakKeyHashMap$Values.class org.jboss.netty.util.internal.UnterminatableExecutor.class org.jboss.netty.handler.codec.compression.ZlibDecoder.class org.jboss.netty.handler.codec.rtsp.RtspHeaders$Values.class org.jboss.netty.handler.codec.replay.ReplayError.class org.jboss.netty.buffer.HeapChannelBufferFactory.class
>
> ……

其实还有很多，这里列出的只是冰山一角。

当然，如果全限定名相同，类中的代码也完全相同，那么用着也行。问题是如果**『全限定名相同』**，但是**『代码不同』**，那可太坑了。我们随便找一个来看看：

| 坐标信息：org.jboss.netty:netty:jar:3.2.10.Final             |
| ------------------------------------------------------------ |
| **代码截图：** ![images](maven_2022.assets/img102.7c201094.png) |

| 坐标信息：io.netty:netty:jar:3.9.2.Final                     |
| ------------------------------------------------------------ |
| **代码截图：** ![images](maven_2022.assets/img103.9c32611b.png) |

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_4、解决办法)4、解决办法

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_1概述)①概述

很多情况下常用框架之间的整合容易出现的冲突问题都有人总结过了，拿抛出的异常搜索一下基本上就可以直接找到对应的 jar 包。我们接下来要说的是通用方法。

不管具体使用的是什么工具，基本思路无非是这么两步：

- 第一步：把彼此冲突的 jar 包找到
- 第二步：在冲突的 jar 包中选定一个。具体做法无非是通过 exclusions 排除依赖，或是明确声明依赖。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_2idea-的-maven-helper-插件)②IDEA 的 Maven Helper 插件

这个插件是 IDEA 中安装的插件，不是 Maven 插件。它能够给我们罗列出来同一个 jar 包的不同版本，以及它们的来源。但是对不同 jar 包中同名的类没有办法。

- [在 IDEA 中安装 Maven helper 插件](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02-part01.html)。
- [基于 pom.xml 的依赖冲突分析](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02-part02.html)。

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_3maven-的-enforcer-插件)③Maven 的 enforcer 插件

使用 Maven 的 enforcer 插件既可以检测同一个 jar 包的不同版本，又可以检测不同 jar 包中同名的类。

##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_1-引入-netty-依赖)[1]引入 netty 依赖

这里我们引入两个对 netty 的依赖，展示不同 jar 包中有同名类的情况。

```xml
<dependencies>
    <dependency>
        <groupId>org.jboss.netty</groupId>
        <artifactId>netty</artifactId>
        <version>3.2.10.Final</version>
    </dependency>

    <dependency>
        <groupId>io.netty</groupId>
        <artifactId>netty</artifactId>
        <version>3.9.2.Final</version>
    </dependency>
</dependencies>
```



##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_2-配置-enforcer-插件)[2]配置 enforcer 插件

```xml
<build>
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-enforcer-plugin</artifactId>
                <version>1.4.1</version>
                <executions>
                    <execution>
                        <id>enforce-dependencies</id>
                        <phase>validate</phase>
                        <goals>
                            <goal>display-info</goal>
                            <goal>enforce</goal>
                        </goals>
                    </execution>
                </executions>
                <dependencies>
                    <dependency>
                        <groupId>org.codehaus.mojo</groupId>
                        <artifactId>extra-enforcer-rules</artifactId>
                        <version>1.0-beta-4</version>
                    </dependency>
                </dependencies>
                <configuration>
                    <rules>
                        <banDuplicateClasses>
                            <findAllDuplicates>true</findAllDuplicates>
                        </banDuplicateClasses>
                    </rules>
                </configuration>
            </plugin>
        </plugins>
    </pluginManagement>
</build>
```



##### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse02.html#_3-测试)[3]测试

执行如下 Maven 命令：

```sh
mvn clean package enforcer:enforce
```



部分运行结果：

> [INFO] --- maven-enforcer-plugin:1.4.1:enforce (default-cli) @ pro32-duplicate-class ---
> [WARNING] Rule 0: org.apache.maven.plugins.enforcer.BanDuplicateClasses failed with message:
> Duplicate classes found:
>
> Found in:
> io.netty:netty:jar:3.9.2.Final:compile
> org.jboss.netty:netty:jar:3.2.10.Final:compile
> Duplicate classes:
> org/jboss/netty/channel/socket/ServerSocketChannelConfig.class
> org/jboss/netty/channel/socket/nio/NioSocketChannelConfig.class
> org/jboss/netty/util/internal/jzlib/Deflate.class
> org/jboss/netty/handler/codec/serialization/ObjectDecoder.class
> org/jboss/netty/util/internal/ConcurrentHashMap$HashIterator.class
>
> ……

TIP

最后，问你一个问题：解决 jar 包冲突问题这么麻烦，是不是不该用 Maven？

## 第三节 体系外 jar 包引入

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse03.html#_1、提出问题)1、提出问题

『体系外 jar 包』这个名字是我起的，来源是这样——目前来说我们在 Maven 工程中用到的 jar 包都是通过 Maven 本身的机制导入进来的。

而实际开发中确实有可能用到一些 jar 包并非是用 Maven 的方式发布，那自然也没法通过 Maven 导入。

此时如果我们能够拿到该 jar 包的源码那还可以自己建一个 Maven 工程，自己打包。可是如果连源码都没有呢？

这方面的例子包括一些人脸识别用的 jar 包、海康视频监控 jar 包等等。

### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse03.html#_2、解决办法)2、解决办法

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse03.html#_1准备一个体系外-jar-包)①准备一个体系外 jar 包

我们通过学 Maven 以前的方式创建一个 Java 工程，然后导出 jar 包即可用来测试。

![images](maven_2022.assets/img016.4e398cba.png)

![images](data:image/png;base64,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)

![images](maven_2022.assets/img018.b2e5347f.png)

![images](maven_2022.assets/img019.5b1e9d05.png)

![images](data:image/png;base64,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)

![images](data:image/png;base64,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)

![images](data:image/png;base64,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)

![images](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAARMAAAEkCAIAAACDtEL6AAAhcElEQVR42u2dfXgV1Z3HTwgCSSwSwKCVt0BCIhJRoJSgPJhSWRPTqlsDylq11U3cx6ctia3ts49oq3361K4mtm6fNql2F+pqIW7VNk26VIhuESQLWksfDSQkKPWF8KptwPKWnbd75syZM3Nnzp1770zu9/MHzJ3MmTlz7/nc83Ln/E7W9u3bCQDAJ1mKORdOmZnubAAQMWAOADLAHABkgDkA+Obg+2/DHAB8A3MAkAHmgChx4sTx13Z07e3ZfezYUeXluHH5M4tL5i1YmJOTm+KcxDdn62t/9HiuxfMuS3HuQUaxZ/dbv2v/9enTpydPnvrO2/3KnqnTCv/yl3dGjhx5TdXnZ5Vc7PeEH3147JdPr1P+pXvGnjfuplW3Kv/GTRtSc4b6fvqPV75w7Zb2O2dkBXVOEGkUbX7z/LOfvGjKNdd+Pj9//CPff1DZ+fVv3X/06JHf/fbX7727/3PX35igPN61IUk1Z2io78nrFj64w3h581MHH/6MVw1gDmA5fvz4E82Pn3/+pBU3fzE7O1vZQ81R/j1z5syGZ35x8OCBO+u+kpvru9mmy6NseNeGeDTHS2ViP0wz527ymFr6k22CbmnPah9yggjxyh9e2v7qK1+681+U2kbfw5qjoNQ8//HETz696IorllwlcX5a53hP4tWcn+//2OUsX54yxt0c5eVL35j4w+Ku52pnJOOdhTnDm3U/b87Jzau56RaXY1p/+dSJ44O3frkuNVlKtTm/+meiFfH15JaVzyz47ssv1E0nm7910cpnjES3rnv30auy9Dpqz9f07aFN9ID53zbcY3de/sBjpd9ZbZxBO2dhlsCfY0ePPv3Uz48PDnL7c/PyVt3y5XH5+al5x4EEP2r8/smTJ+lLWtWQWOWjM2rUqK82fMvLCdlUTrBXsZMic2hr7Y7Cfq3zEzNEE2AP9WFzw7RbiPKnpf3NujlLVa8eKdaaebRi+X7FZjYV8VznHPjg/fVPr2U/A+W9XrnqtkkXXCj1gYIUkYHm0BGCT91vLf16EVdVeWwWrSVMN6bHzOm8Z9ot69jTKtXOr4r+nU1F/LTW3t7X998bnjl79oyyPWJE9hdW3DxtelIakCBAMrq1xux0N0dNcgdhzLFKYk9FfPZz3nrzz+2/eU7ZqPrcDRfPnpOaNxokQkaPEDA7GXPsrTVNiemW1tpKEhvRfukbDeQHlp3KGZ78WeEdRvfJ6wjBjq5tyr8LFpbLfpQgpUR4VDpJ5hBrX188QqBu37dT+zP9UYjZaSTpb6lc+u3/cxkhAJEmkr+Eyv2ekwisOUGdE0QdPH0TH7UCabsOtQfgiNITnylGG5heR9tg6c4OAGJCZw4AkQDmACADzAFAEpgDgAwwBwAZshAdFwAJVHMWLlyY7mwAEDFgDgAywBwAZIA5AMgAcwCQITBzntv4Mt3OHTO6ZMa06ZPTMku5oy6rqoXUtg81V6bj8hnN0Z3PbugvXHHjfC6sQ/+m5p3j7LsjiHqHx+bXLStMjjk6s4sLSwqnSpyqp2nxrAbSuGdrfbFEapijkth7qKOWkp1H6EyqmcvVAsNiEyJYc/o3tfx+r3bp8QvMxMq5Nu5Vg1iwO439R9ldamZ2HBEcmQgpMceJG5YvdT9AK/vliX3qmU4Q76FZSlS0cjzz6lrWHuWITWRZ/FIpY07/pmePzNOTqFIQzVtGTevOHYeLiop6j5oXMf8caI3nYM769evz8vIqKiqUf/U9g4ODnZ2dyr8rV650P6WUOdo34zZ92/iY9SrDQK856FG17e2kyigQRNtpVC09zAtrneMvrRX9L0qKNd2z1DxpaXuN/MVKpeMtWE5vu7lY8lgJNy5BnAs7+8ZY7852I8T+HtovHTtce1uEVbTVHNEOr+okWnZj6QmbAT43lotYXngV3AMO5rS1tSmSUHmoNsp2dXW1+ymlzLFoon/Itk+dWI8hxkfvyZwOn2mtGIW+vHzbtpgazLZRAgW3UMl+4ZvbvVxmmEPZ0woLsf1GtMO8mkMEly5i786bOXTPOPrVbz3G8spoVukph8bPZwq1sX/8/BqzbGstK2WTq9ZiKbT6Q9nom0EzxPnoao6arRl9dJ+wRjIzxjRN9bTLycaNvRMW1Nw4o19oDquKsrOrq4sVyd2HBFtr4sIW+1K2fpt7Nsd3WpE5bBm1FFi+uDHZ5nRhxGFqUWLu1VNZqieBOPa3pLrN6UYE7yF3aT2tSyXnyRyt5NHSbKZgiqalC8KWaHr00Z2b+mYs4xpm7DV35NNmGZMhN3OYVLSdOe+Imbtndx4lhWo1FEt1zNJNsjUFl3N3SDbx/Rwqj/7Sozay5rBNHWJt7VjaFPTjNf/kwZwiybTEUjWJmmDMC+Et0HN+vTumCLEep0JrA77pxlZlpiD0Rsz3xJs5PcJLa1nb5jaS4rFxxqhDUzgV6linn0KrHSVp684jxDoUoRVbsoDtvHiscwgzwDB+/oL8fjLPbOwRtQuluKL0o2b06Xd0jEvMWG15E7SX+UW9vaIRArbm8aiNizku4wHs+I9TnWP9erfVG4JizZvjJ60gf+7mmGaIsl3b2LiroaGM6azZvuE7HMyx5sSaR4fvBnsOxd8+lrvzZY5Dd8Xc7cEcwRlYP5hr2qsfYRtM3M/hM6gLp5vPODO/sH8nWSbIGP2OEJizg0yYcNhhbE3RRmmqKfs9auNuTkXH29zOzspphNi/C239aPrVGDtAa5ULOxiE2Myx1gde0lrxao7gFoxvBfWSZbEzi3pEHs0h7jcruBHLe8j3c+x9JCH82JrZnOFGnwXFjC32rBlcK05JZtHMbGTZKzwuSzZRnMzhGoib+snhfKM2Ubb1Nhs/qC34MrBmYNzOpI9Ku5hjfr7ljY1lDQ0ttvEqa/Eob2yvaa0yvz1j+2vb95Q+5DC25i+tb3NifSnbLRBLx8hyQhlziHhsjTjfCPce2i/tzRzz9xyzi2//3SamDjv2xf4asyB/Rz/bu+EGA8wLjZ85k/QSvdbi2nXGweavNPzvS1ZzmHPScQhi7f/wd+EyQmAzJwW/57ia4xOjlEj9yplIWhAPcQ0xrEnFc2uJmcM3SPz8vJdIWuALa+c9A4iYOT6rjETSAuBG+M0BIIxglgEAMsAcAGSAOQDIAHMAkAHmACADzAFAhlCYk1AMA/UpktYa4VOSD5Xih0+QJII0xz6l1CMJxTCAOSAdBGkON6XUe8IAYxiYwByQTII0R25iD4E5IIIE3M8JdlacHYE5amute41wDoz5gKc9dIblmX880gb8EvwIwcDAgCKPslFQUKDI4yVJQOaoehB2HtkuOrWG1j706J6murbqZjrNmsAd4IthVOdwzTP60jZv0jb7jKDWAX6Jfj/Hizn2Dg8bA8HS3gPAE5EdW6OD0UTYWuOjgzCtuDrSbNFMO7QMdQ7wRUh/z3HC1RzrQEBjWUNrqXOzzNxVXltLWgjqHOCL0D1D4I6PUWkAkkkozAEgcsAcAGSAOQDIAHMAkAHmACCDbs5F6c4GABED5gAgA8wBQAaYA4AMoTDn46f+zcxQ3tjsOYtGFs9N8xsDgCuhM0dn5NwlI8sWpTdXALgQUnOcGHPLN6Su0N+0eEn3mv3NlVnpvVMwbAjMnPXrf52Xl1tRsVj5V98zOHi8s3Or8u/KlZ93TwtzQOQIzJy2thcVSag8VBtlu7r6s+5pZc3ZXJd1a2y256r2oYcrSRbp+dniWT1rmO37hq55gR5W/sCerXcWE/gDEiUwc1hVFi68rKvrj6xI7mmlzFG12dX4h6312jJhHfdqs9cerux5gjNH296HOgcES5D9HCqP/tKjNkTOHEWVh4qZCiTWHiuCOSAVBDxCwNY8HrUhgZmzmqx9vp7AHJAKgh9bU7RRmmpKg82jNiSo1pouktpaa6vZ83x9cVZP03WzGkraYQ5IApEelRaNEKgRBhRhdhI1wMAqLcCAut/YiRECEBCRNgeAtBEKcwCIHDAHABlgDgAywBwAZIA5AMgAcwCQAeYAIAPMAUAGmAOADJE3JxUxDNQnR41n4fSHshu2kdp2PAWX0Qwrc3SCj2HAmKM+/9b9taHmz6T7vkGaGYbmOOHzmTfxBOyOuskPlcYe0E7gPCDqhMKcUMYwgDnAjVCYk4YYBmoD7Dux9Q/1Yq0X8XWk6taWRatWvPr0Bv3I8gd2rx26fVbvmqGHSd2U2CrXq2Izt7mTEHbuw6JHf3DpPfeyIRBI0/X6DAh2WgSIIqEwJ+UxDPqb6l6sbtYm6tAABtrst4ZttEAzdQUTFYSpc4Qn6bTMt9OvJTpPet9wkDihMIekOIaBBp0AF/v65+aNxjVHdJKOb1rneFvPY1RH8xuNYToQYcJiDkllDAOtqUb0msExVkE8c4QniWOOuadhG/yJNiEyh6QshgET/cM5VkE8c8QnUVtrxOjzbG5qKqxXV503z9PUe2d9ZRaGDYYB4TJHAsl+jvZrJrHEKuCjfNDQBXSEgO/nCE6SxYw9GF0mNgRCLx1jqF2HH4UiTWaaA0CiRN4cANICzAFABpgDgAwwBwAZYA4AMsAcAGSAOQDIAHMAkAHmACBDRpuTihgG7lgiHIAoAXMsBB/DwB2YE1lgjifiPfOGZ5+9M0zeq8ibE44YBsOkNKSEYfJeRd6cEMQw6DQXXTRWU6R75jc2ljS00gVMbUv/sjvNAAbxUvGTsZWEPyxtr26tUnOlZol8M6vqaTU7dF43n2d1SkVrjfFXdR5Ea7U15zStdnIlPw3WE/KIVp5kM6xt3zd0zQuO7xU9ORMQIsSrU0benPDEMGCnTBMmKkgDecCDOZ0+UgnMiRWyDs0ZffIPO93Vnmdz+irNfKcqCROQMbbz1hbzhMJemW21Y/0SPXHXCd8suiIXECKkRN4cErYYBtxy85ZFs53N4aZhu6cS1jlG+XPatuc55glh8qDVVBStEui3Fu7VZK3NHO6WqQNF8cxRHbNfkUSiOTcczCGhimEQQnNUN+x5Nua3riWrbyOPcfPDHU7u3RztMOLBHMEVo9ERGibmkBDFMHBprTEhdvWEnlprtlR+zen9pijPWmm+raeM7C5dS4+neSAddfeSZqZqcjHH3loztbdn3um9olfcB3PCTnAxDCzBBorNCIZMX58ewybkqxRvqXy31vaJQybo91LGREQQBHD0UOcYebCNEDBNRA/vFRcyEuaEmFTEMBA3SJKTCqSQjDYnOShfmT8p3WoONO9q9BKHWi4VSBswJwkwbZ5y7wJ4TcW2i1RSvpJP2jMQCmAOADLAHABkgDkAyKCac0PbqXRnA4CIAXMAkAHmACADzEmI2hnH9I2WvnHZo7KnlRX87eiJ68k+ujPdGQTJAuYkBDXneTL93Pyct3cNnDl5htUpeZcemjRly90TNv749YcOZNxvKX5JxnsFcxIijXUOzPEOzAkdidc5Q0M5939l3syXttz+ZwgQh1C9VzAnIRKvc0JVGkJOqN4rmJMQfuscrdkwdYa2/fsNW27bNXHtQ6VX638beKfy8XfeIHTP4E9fPnHXxcfVnRdM3XJ37o/WdLdmZWlnULc3sDuHJnhMpWxb8qMmnLJ3w+HlK9RcqVkipe+tmKj8qe/l15ZsOiHKc65SfJe/Zfz10mWXdVx82JpzI61xciU/Sy0n5N8TM/MKh1bbMqxv//C+g9XfvdjhvaJX1NXqJitKr9aO+ZN2v7azmbejSOiUyh2YkxC+6hz1E7p54gvPqB/M0JyS91YQpZRsILn0e1QvQ4T5OO8i78Q1Z4NWgDymEpljFJc3yjRn3uy+6JeHzaso2bPnWTnyKk3OWOa1r4Ape7WOBK0ZjO8F84SCnoaegZnUUnoJ4S3z75X9iuoBdxUY+plXcb8dIk7lDsxJCIl+jvolvTRP2zzEm6N8llqJNL4pYy/jmBMrx55SsXXaj19/8AO2xE8QbovybKR6kDB50GoqilIJXPlirrVwl5BW/YpMBs63Zp46cDCeOWq5t1+RCJtz3BeHy0fg/aOHOQnhr87R2glEb1c4lYYkmyNurTmbo7nB51k5Sc1NV3z14Gt3k5Ifk93qn6w5F53cMIevc7hbpoIRD+YIrpjjbo72prl9BN4/epiTEL7qHPbD1r72TvhprRlNHTOhp9Yan8q3OUydwJ5ELXY1uXtJzt5WeryRB+W0NTeVkGfMqsnNHHtrzdTedssO7xVzRYsDtIloqRtdPwLvHz3MSQjf/Ry1Pa1u9715iMwm+neq0XgYoMVd77+aff0/0WOYhLaegKdUvs35IFeYZ+NeDql9GONUgp53/DqHOIwQEKZN5eG94vr6zuYQwe3AnDSQ1GcIhA2SJKUa3jg1VhMB5iREsM8QaF+ZU7sfNweaZzqM5CaeKqOgQ+cBfpvAnIQIvM5h2zx9ngWQS5UJxIbg/I04ewHmACADzAFABpgDgAyI4AGADDAHABlgDgAywBwAZIi8Oex6BFl5Y7PnLBpZPDelOUhk3QGs6h5ZwmKOfYlpj9hX8hg5d8nIskWpyzrMyUjCYg63xLT3hKlYA8cd3+ZEY2Ul4E5YzJFb6JPAHJAmwmIOSeUquUbZXUeqjHVgrOvsafv1tfi05Tvti/jxa8iUu6/Abh686NEfXHrPvS2xVLvXDt0+q9d+GHMtbbHBxpKGBnUNZx+r8YAkEyJzFAYGDinyKBsFBRMVebwkSWCtz1jpVNcWJ3TxV3O/feFY47BO67qwk6t2uZjTaVuAjalznA6zXqulVlvKE52iMBEic1Je59D2En1pXRVZsFi59lfiZwV2brl27uqOh9FjvCwNDdJAWMxJbT9H2hyt4PamxhxdEpgTUsJiTmrH1iyrmWuLjJfQ1hpjlK21ZhRutrWmnYrQ1prRmmLOyR68uampsL6eeGqtmdeCOWEkLOYE+HuOE3ydU7aqpeVp7SXt2NhHvTaLRwjUfoiedn6j0n1vNaoLTZidyt7y2lWkhTD1j750rnEG4zDvIwQwJ3yExRxpgmitAeCbyJsjBcwBiQJzAJAhM80BIFFgDgAywBwAZNDNWZjubAAQMWAOADLAHABkgDkAyBAKcx7982G6/YlzRiw6P+fS8WPS/MYA4ErozNG5YlKu4k96cwWACyE1x4l75kxIb1YB0AnMnPXr1+fl5VVUVCj/6nsGBwc7OzuVf1euXOmeFuaAyBGYOW1tbYokVB6qjbJdXV3tnlbOnJ6mxbMatIf3SW37UHOltqN7TTupqmopb9yztb6YdNRlVenP7Rs7AAiIwMxhVVFO2NXVxYrknlbGHF0TVRiKrpJuESGaNrsgDEgOQfZzqDz6S4/aEMk6R69P2LrEKpPy94dK4Q1IEgGPELA1j0dtSEL9HL2e0f2BOSB1BD+2pmijNNWUc3rUhsi21pp66+srCVPVcA04tVIiRtOto6mpqB4SgeCI8Ki02f03uja2ro85hkA7PwAEQ4TNASCNhMIcACIHzAFABpgDgAwwBwAZYA4AMsAcAGSAOQDIAHMAkAHmACBD5M15buPLdDt3zOiSGdOmT74w3ZlKGPW5odYaPK8aYoaVOTqziwtLCqemO1+JAXNCzzA0x4kbli9Nee4E8+/CRyQyGTpCYU4iMQxgTmZkMnSEwpxEYhhQc7JHjJhy4SRlY//7B86cPWs/0mqOPUSBuYeZlaDNj2uvaa1SZyuoe4lxlCCwAR8YgTkhHxehvLGxrKG1lJ+PJ55q5DmVJTei27Glum/o+hccM0nv0R7gAYTDnERiGFBzPn3ZJZ8smKhsvHfg4PY33rQfyZhjD1Fg3WNOitOKkV5c9BLFBgvhoiDUtVU3a+nN5Oxx7Ew7TTLS6MEcP6nMzDjcjjgVl0k6k9aSGybAA1AJhTkkgRgG1JzPLbtyZHa2snH6zJnfbNpiP9I0xz7Rmt9DCw37B6dtE4dwPFqJ41LQl+7m+EoV93aK4pnDVlQa2pcGQXPOTljMIbIxDMw6Z+7sT046n3ipczyZcxtZq1c03sxh6gPHQplOc7TbIR7McfhCgDkcITKHSMUwoOaMUPs5BUTt5wycjdPPsYco6OWbN0YBimcOiQ0f95p/1CQq89NaMwagzYSeWmvCVPrODvHtCFM5ZlJ5UUeaxW1TEC5zJJAcWxOEKHAeIfBiTrF5xvLaWtJC1tDYCMpeMziPcYjZ12fyYibkqxTvqeivQMLbEaVyzKRwYAEYZKo5aUcuqBVCYYWGyJsTHZRv7kdKt/qNPyqXCiQdmJNCmIaQj99F5FKBJANzAJAB5gAgA8wBQAaYA4AMMAcAGWAOADLAHJUTZ4ZeO3yi76NTR0+eUV7mj8qeMfaceRNycrKz0p01EFJgDtnz4cn/efdvJ88OcftHjcj6h4vOnXXeqHRnEISRTDdH0abtL38dGhL/NSuLVE/+hFUedpU49lhmJo/L9ZifNfHDZqTJaHOOnz775J5j9tqGRal57pg1LnfkiNgO7bFKUk5q1rKl3vLQpNO5uKdnmMeVHcCjluElo815ZeD4qwMn9O2zZ8/s/d+N+1/ffnLwo/FTZ15SdWPexEn6nxYV5FxRkBtLpJXmmsZdDWyR1ibA1JgPMouwPMDvso8B5oSXjDbnF70fDnx8Wt/evfm3fxt4/5Jrbxw5esyhvj2KPKNyjTlCBWNGfrHovFgiozRf/zzz1LL+CPOa7lmWXdbZ/OLHnNnZCogQECUy2pzH3zxCm2qbmx5YfEf9mLHj7IcpDbavzB4fexWrB4gt2AARzoez/5XF8zxnRAgIGTCHNWf1mLH59sPE5tB5ZKWPGEWalm3hbH56mOXcfuY5I0JAmMhoc6yttba/fvDunOoV2eeMPrB717kTCsZNKdT/JGyt0Vn75jRN1hxxw8y5nxM3Kg0iBISMjDaHGyHoeanj3T92nfr4xMQZs2ZXfiHnPKOeEYwQMLP2W+ikY0v0AvtsfkuQj9iZYmNriBAQNTLaHOlRaVpY+YiClp4IN5ufphb/noMIAdEio80hMr+EAqCS6eYQPH0DpIA5KnjiE/gF5gAgA8wBQAaYA4AMMAcAGWAOADLAHABkCKM5b+3dd/HM6Vyo9XDFUwcZT+jMUbTp3vu24klKzcEq6t7x8V511GU9f/0wfTYoXObo2hDNE5gTUmCORojModoQeXPwBKR3UvBewZx0AHOSDMxJiBCZ4776msAc/ql7ZtokP3GfWScwzirqhHhPxWdIm2bQXtNapeZKzRIxzmROJ+DzbGn6MC/sIQdi0+garCcU5MG2yGFPoEEOLGuYut8vzEkJPs3paaprq26mS2ISPyvaupjjJxWfRybkGjvnjZ3jac+zOatHuCA1u7OqxTyhsKdhW1jX44RTX0EOrO+V6/3CnJTgu84RzB9L8SrqTO3Ez6J2XALelmdb+BtxyIFea+Gmi84zGei1L+buLTyIryAHXJ3jdr8wJyXYzXHr27BTk71M3E+KORxeFrK259n481pym25DnPhSFnOcr0+Iv/Ag3oMcwByNsJtTNL2Q29m7r1/9j/mkPEzc59pdthn/nlpr9jgBHPFKUq8wz3oJ7y4ju0rXbuVan8QMOeDBHHtrzdQ+4SAH9CQE5qhE1hym1eNh4j7T1xfO+OerFG+p+DuIW5LEeRbEyRWEHPBiDhGPEAQS5ADmWImuOT4RN0iSkwpkAMPYHOVL8pHSreZA8y5P4WPlUoGMIwLmbHr5Jf3lsqVXEV91DtMC8RF1WS4VyDBCZI6QwFprAARK2M0BIJzAHABkgDkAyABzAJAB5gAgQxLN0UeZET8ADEsy3Bxvj4eYz30FNxsM87cjDm/O+vXr8/LyKioqlH99nYh6ckf7Z5WNJ6tepHsqOtQ50p2V02yJLKvJcCvNpAShOTY9YA6wwZvT1tY2ODgoIY+0OR4KYvLm/XozJxU5ARGDN0fRprOzU0IeL+bY2m8wB0QVQT9HTp6gzGEfuFefuBx6sPZn9/uaMb97a+mjwin74kf3PbbW2Nlg2oUs57FN2Xd8aD+WybXkdkHkAzO3TsslgpAgHiEYGBhQ5FE2CgoKFHm8nEjaHLOfw81EMZdB9ztjXjhlXxi3QMYc80LW+AdMBhxjJJjHiGdWiuY/g1ASujpHQ/8Otnz/+pkx7zgJTBQDQKLOoX8Sx84x/+YUI4FN6hh1gH0HQOgIZz/H1Zz4M+ZF5ohjACTHnLgxEojVHMfJc7p98CeMpHRszaM5NKIFO3nex4x5oTniGABSrbUyNkxBmaA+iR8jgQ+gw99RT1NTb3091m0PMSn9PSdOP0erZJTeTWwepjXAhvcZ8+LWmjAGgKM5Zq4svXntQmW1LS3WAQJRXWS7lpM54tEAsxGHAYJQEtgzBFLmABBV8MQnADLAHABkgDkAyABzAJAB5gAgA8wBQAaYA4AMMAcAGQL+JdQJ/PoJhhlBmrNw7uy+d947dPSY/a/pNifdaxKk+/qxTAzbNTlST5DPrU3MH6fI0/XGm3Z5nM1JzfNZ6S65btdnphkk9xE1mBMkAT8rrchTfvmcra/96fCxj/T9Y0aPPjc3Z8mn5ooScQuk0eU3JHB/pjis5jBz8PSXi7u/nrRcwpwgCXh+TlnJzIn5572yc9fJU6cmjBs7u7hQcckxjcvSY76JojmW+QUpyQTMCYwg54Sy2ky76ILLLyl5deBE676Puo/9XTmgdNzomuljywtymERORYebl0/izfJnGjziWAXa7JxYZALCfsn7DQDgtA6hPewBN3/VPj/NYxPOMvNAcCH3OQrWndQcRD5IlCDjEEy5cNKBQ0f02mbJwsuf3HPsv/Z+yB32TzPPu3MWUwsZnyD7UdlXSvM6y5+d/WY9gzAygUQAAKdgBsKTs/PVRIu/OZpjWyjX5UKCpdddkuvmIPJBAAQch0Bnyacu6zk16l93Dugvr5yUq/y75cBx/eX35hdYa57Ytx0NDcCXJ5+z/AVncJglKhUAQBTMwG0KKn99Yabc9gsnXtOWbgefYbfkmjmIfBAEAfdziDokMKpyaXlD14HXD3+s77m9WK1k/rPHGHC7fMKYxoWTbCeIfTeSeObEneXv3Ry/AQDEl5Y1x0kdQdGnkjgtT81kuNclecwcRD5ImMDG1jiu/f3+46fP6tucObkjR/z26inqlmU4jV0KnLZzOpqaiuq5Fkn8Wf72M/Q6fVv7CwDQ4RTMwP3krHAW+D8YY2u9fHPLOL04Moktwx3OyWlrDZEPEiWw33M4WHOWXpB7V2l+w/YD7584TVhzrFP+zb6OuVcUb0k8yz+2lx9CcG5QFUsEAHAKZiA6OdsJV7r1rQ5f88LOvMsIge1CogzHGyFA5IOESdZza2xrTeG6qZ9YWTj2S1ve+/uZIYfWGgBRIlnmbBs4QUcIdKbknbN/8BQRjRAAEDmS+Kz0E15GpQGIJsmdZbAtzi+hAEQVzM8BQAaYA4AMMAcAGWAOADLAHABk+H8MDQ7oBzo9nwAAAABJRU5ErkJggg==)

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse03.html#_2将该-jar-包安装到-maven-仓库)②将该 jar 包安装到 Maven 仓库

这里我们使用 install 插件的 install-file 目标：

```sh
mvn install:install-file -Dfile=[体系外 jar 包路径] \
-DgroupId=[给体系外 jar 包强行设定坐标] \
-DartifactId=[给体系外 jar 包强行设定坐标] \
-Dversion=1 \
-Dpackage=jar
```



例如（Windows 系统下使用 ^ 符号换行；Linux 系统用 \）：

```sh
mvn install:install-file -Dfile=D:\idea2019workspace\atguigu-maven-outer\out\artifacts\atguigu_maven_outer\atguigu-maven-outer.jar ^
-DgroupId=com.atguigu.maven ^
-DartifactId=atguigu-maven-outer ^
-Dversion=1 ^
-Dpackaging=jar
```



执行结果：

![images](maven_2022.assets/img024.5a4875e8.png)

再看本地仓库中确实有：

![imags](data:image/png;base64,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)

我们打开 POM 文件看看：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.atguigu.maven</groupId>
  <artifactId>atguigu-maven-outer</artifactId>
  <version>1</version>
  <description>POM was created from install:install-file</description>
</project>
```

#### [#](http://heavy_code_industry.gitee.io/code_heavy_industry/pro002-maven/chapter10/verse03.html#_3测试)③测试

在其它地方依赖这个 jar 包：

```xml
<dependency>
    <groupId>com.atguigu.maven</groupId>
    <artifactId>atguigu-maven-outer</artifactId>
    <version>1</version>
</dependency>
```

创建对象、调用方法：

![images](maven_2022.assets/img026.87955209.png)