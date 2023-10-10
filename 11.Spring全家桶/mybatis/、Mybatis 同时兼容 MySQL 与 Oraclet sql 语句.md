

mybatis 兼容 MySQL 与 Oraclet sql 语句
====================================================================================================

由于项目需要同时兼容 MySQL 与 Oracl，修改后有了一些体会。

## 通过 mapper.xml 文件进行适配

首先要定义好 DatabaseId，springboot 是通过这个 id 进行选择的。有两种方式可配置  

### 1.1. **通过配置文件方式** ，

```yaml
# application.properties文件
mybatis.configuration.database-id=mysql或oracle
#=============================================
# yml文件
mybatis:
  mapper-locations: classpath*:mapper/*.xml
  configuration:
    database-id: mysql或oracle
```

### 1.2. **通过注解方式**

```java
@Configuration
public class DatabaseIdBean {
  /**
   * 自动识别使用的数据库类型
   * 在mapper.xml中databaseId的值就是跟这里对应，
   * 如果没有databaseId选择则说明该sql适用所有数据库
   * */
  @Bean
  public DatabaseIdProvider getDatabaseIdProvider(){
    DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
    Properties properties = new Properties();
    properties.setProperty("Oracle","oracle");
    properties.setProperty("MySQL","mysql");
    properties.setProperty("DB2","db2");
    properties.setProperty("Derby","derby");
    properties.setProperty("H2","h2");
    properties.setProperty("HSQL","hsql");
    properties.setProperty("Informix","informix");
    properties.setProperty("MS-SQL","ms-sql");
    properties.setProperty("PostgreSQL","postgresql");
    properties.setProperty("Sybase","sybase");
    properties.setProperty("Hana","hana");
    databaseIdProvider.setProperties(properties);
    return databaseIdProvider;
  }

}
```

## mapper.xml 文件

```xml
<insert id="方法名1" databaseId="mysql">
       	<!-- 适配mysql的sql -->
    </insert>
    <insert id="方法名1" databaseId="oracle">
        <!-- 适配oracle的sql -->
    </insert>
```

## 总结

1.1 与 1.2 两种配置方式都可以，可根据项目具体情况进行选择，两种我都测试过了没问题。  
通过这样配置就可以让程序实现可根据数据库自动使用对应 sql。