# pom.xml 中 build 标签详解

## **1. 分类**

​    （1）全局配置（project build）

​         针对整个项目的所有情况都有效

​    （2）配置（profile build）

​         针对不同的 profile 配置

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" 
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
           http://maven.apache.org/maven-v4_0_0.xsd"> 
 … 
  <!– "Project Build" contains more elements than just the BaseBuild set –> 
  <build>…</build> 
  <profiles> 
   <profile> 
    <!– "Profile Build" contains a subset of "Project Build"s elements –> 
    <build>…</build> 
   </profile> 
  </profiles> 
 </project> 

```

## **2. 配置说明**

​    **（1）基本元素**

```xml
<build> 
   <defaultGoal>install</defaultGoal> 
    <directory>${basedir}/target</directory> 
    <finalName>${artifactId}-${version}</finalName> 
    <filters> 
        <filter>filters/filter1.properties</filter> 
   </filters> 
    ... 
 </build> 
```

​        1）`defaultGoal`

​          执行 build 任务时，如果没有指定目标，将使用的默认值。

​          如上配置：在命令行中执行 mvn，则相当于执行 mvn install

​       2）`directory`
​           build 目标文件的存放目录，默认在 ${basedir}/target 目录

​       3）`finalName`

​           build 目标文件的名称，默认情况为 `${artifactId}-${version}`

​       4）`filter`

​           定义 `*.properties` 文件，包含一个 properties 列表，该列表会应用到支持 filter 的 resources 中。

​           **也就是说，定义在 filter 的文件中的 name=value 键值对，会在 build 时代替 ${name} 值应用到 resources 中。**

​           maven 的默认 filter 文件夹为 `${basedir}/src/main/filters`

​    **（2）Resources 配置**

​         用于包含或者排除某些资源文件

```xml
<build>  
        ...  
       <resources>  
          <resource>  
             <targetPath>META-INF/plexus</targetPath>  
             <filtering>false</filtering>  
            <directory>${basedir}/src/main/plexus</directory>  
            <includes>  
                <include>configuration.xml</include>  
            </includes>  
            <excludes>  
                <exclude>**/*.properties</exclude>  
            </excludes>  
         </resource>  
    </resources>  
    <testResources>  
        ...  
    </testResources>  
    ...  
</build>  
```

​       1）`resources`

​          一个 resources 元素的列表。每一个都描述与项目关联的文件是什么和在哪里

​       2）`targetPath`

​          指定 build 后的 resource 存放的文件夹，默认是 basedir。

​          通常被打包在 jar 中的 resources 的目标路径是 META-INF

​       3）`filtering`

​          true/false，表示为这个 resource，filter 是否激活
​       4）`directory`

​          定义 resource 文件所在的文件夹，默认为 `${basedir}/src/main/resources`

​       5）`includes`

​          指定哪些文件将被匹配，以 * 作为通配符

​       6）`excludes`

​          指定哪些文件将被忽略

​       7）`testResources`

​          定义和 resource 类似，只不过在 test 时使用

## **3.plugins 配置**



​         用于指定使用的插件

```xml
<build>  
    ...  
    <plugins>  
        <plugin>  
            <groupId>org.apache.maven.plugins</groupId>  
            <artifactId>maven-jar-plugin</artifactId>  
            <version>2.0</version>  
            <extensions>false</extensions>  
            <inherited>true</inherited>  
            <configuration>  
                <classifier>test</classifier>  
            </configuration>  
            <dependencies>...</dependencies>  
            <executions>...</executions>  
        </plugin>  
    </plugins>  
</build>  
```

​        1）GAV

​           指定插件的标准坐标

​        2）extensions

​           是否加载 plugin 的 extensions，默认为 false

​        3）inherited

​           true/false，这个 plugin 是否应用到该 pom 的孩子 pom，默认为 true

​        4）configuration

​           配置该 plugin 期望得到的 properties

​        5）dependencies

​           作为 plugin 的依赖

​        6）executions

​           plugin 可以有多个目标，每一个目标都可以有一个分开的配置，可以将一个 plugin 绑定到不同的阶段

​           假如绑定 antrun：run 目标到 verify 阶段

 

```xml
<build>  
    <plugins>  
        <plugin>  
            <artifactId>maven-antrun-plugin</artifactId>  
            <version>1.1</version>  
            <executions>  
                <execution>  
                    <id>echodir</id>  
                    <goals>  
                        <goal>run</goal>  
                    </goals>  
                    <phase>verify</phase>  
                    <inherited>false</inherited>  
                    <configuration>  
                        <tasks>  
                            <echo>Build Dir: ${project.build.directory}</echo>  
                        </tasks>  
                    </configuration>  
                </execution>  
            </executions>  
        </plugin>  
    </plugins>  
</build>  
```

 id：标识，用于和其他 execution 区分。当这个阶段执行时，它将以这个形式展示 [plugin:goal execution: id]。在这里为： [antrun:run execution: echodir]
goals：目标列表

phase：目标执行的阶段

nherit：子类 pom 是否继承

configuration：在指定目标下的配置

## **4.pluginManagement 配置**

pluginManagement 的配置和 plugins 的配置是一样的，只是用于继承，使得可以在孩子 pom 中使用。

父 pom：

```xml
<build>  
    ...  
    <pluginManagement>  
        <plugins>  
            <plugin>  
              <groupId>org.apache.maven.plugins</groupId>  
              <artifactId>maven-jar-plugin</artifactId>  
              <version>2.2</version>  
                <executions>  
                    <execution>  
                        <id>pre-process-classes</id>  
                        <phase>compile</phase>  
                        <goals>  
                            <goal>jar</goal>  
                        </goals>  
                        <configuration>  
                            <classifier>pre-process</classifier>  
                        </configuration>  
                    </execution>  
                </executions>  
            </plugin>  
        </plugins>  
    </pluginManagement>  
    ...  
</build>  
```

​         则在子 pom 中，我们只需要配置：

```xml
<build>  
    ...  
    <plugins>  
        <plugin>  
            <groupId>org.apache.maven.plugins</groupId>  
            <artifactId>maven-jar-plugin</artifactId>  
        </plugin>  
    </plugins>  
    ...  
</build>  
```