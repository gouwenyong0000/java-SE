package com.atguigu.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 步骤：
 * 1、导入mybatis依赖
 * 2、配置数据源信息
 * 3、编写一个JavaBean对应数据库一个表模型
 * 4、以前: Dao接口 --> Dao实现 --> 标注 @Repository注解
 *    现在: Mapper接口 --> Mapper.xml实现;  --> 标注 @Mapper注解
 *      安装mybatisx插件，自动为 mapper类生成 mapper文件
 *      在mapper文件中配置方法的实现sql
 * 5、告诉MyBatis去哪里找Mapper文件；mybatis.mapper-locations=classpath:mapper/**.xml
 * 6、编写单元测试
 */
@SpringBootApplication
public class Mybatis01HelloworldApplication {

    public static void main(String[] args) {
        SpringApplication.run(Mybatis01HelloworldApplication.class, args);
    }

}
