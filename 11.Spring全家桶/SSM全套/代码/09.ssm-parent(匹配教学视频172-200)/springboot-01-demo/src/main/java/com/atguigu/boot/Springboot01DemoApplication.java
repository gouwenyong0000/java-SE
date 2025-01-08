package com.atguigu.boot;

import com.atguigu.boot.properties.DogProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * 环境隔离：
 * 1、定义环境： dev、test、prod；
 * 2、定义这个环境下生效哪些组件或者哪些配置？
 *      1）、生效哪些组件： 给组件 @Profile("dev")
 *      2）、生效哪些配置： application-{环境标识}.properties
 * 3、激活这个环境：这些组件和配置就会生效
 *      1）、application.properties:  配置项：spring.profiles.active=dev
 *      2）、命令行：java -jar xxx.jar --spring.profiles.active=dev
 *
 * 注意：激活的配置优先级高于默认配置
 * 生效的配置 = 默认配置 + 激活的配置(profiles.active) +  包含的配置(profiles.include)
 *
 */
@EnableAsync //开启基于注解的自动异步
@Slf4j
@EnableConfigurationProperties(DogProperties.class)
//自动配置
@SpringBootApplication
public class Springboot01DemoApplication {


    //1、以前： war 包； webapps
    public static void main(String[] args) {
        //应用启动
//        SpringApplication.run(Springboot01DemoApplication.class, args);

        SpringApplicationBuilder builder = new SpringApplicationBuilder();

        //链式调用
        builder
                .sources(Springboot01DemoApplication.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .environment(null)
//                .listeners(null)
                .run(args);

        //1、创建SpringApplication对象
//        SpringApplication application = new SpringApplication(Springboot01DemoApplication.class);

        //3、关闭banner
//        application.setBannerMode(Banner.Mode.OFF);
//        //4、设置监听器
//        application.setListeners();
//        //5、设置环境
//        application.setEnvironment();

        //2、启动
//        application.run(args);

    }


    @Bean
    CommandLineRunner commandLineRunner(){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                log.info("CommandLineRunner...run...");
                //项目启动后的一次性任务

            }
        };
    }

}
