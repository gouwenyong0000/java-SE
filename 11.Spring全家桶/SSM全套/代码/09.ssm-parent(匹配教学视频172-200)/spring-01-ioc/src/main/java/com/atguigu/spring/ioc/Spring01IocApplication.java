package com.atguigu.spring.ioc;

import ch.qos.logback.core.CoreConstants;
import com.atguigu.spring.ioc.bean.*;
import com.atguigu.spring.ioc.controller.UserController;
import com.atguigu.spring.ioc.dao.DeliveryDao;
import com.atguigu.spring.ioc.dao.UserDao;
import com.atguigu.spring.ioc.service.HahaService;
import com.atguigu.spring.ioc.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;


/**
 * 这个是主入口类，称为主程序类
 * application.properties 就是这个项目的配置文件
 */

@SpringBootApplication
public class Spring01IocApplication {


    /**
     * 生命周期
     * @param args
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);

        System.out.println("=============ioc容器创建完成=============");

        User bean = ioc.getBean(User.class);
        System.out.println("运行：" + bean);
    }


    /**
     * 原生方式创建、使用Spring容器
     * @param args
     */
    public static void test13(String[] args) {

        //1、自己创建：类路径下找配置
        ClassPathXmlApplicationContext ioc =
                new ClassPathXmlApplicationContext("classpath:ioc.xml");

        //文件系统：其他盘中找
//        new FileSystemXmlApplicationContext();

        //2、底层组件
        for (String definitionName : ioc.getBeanDefinitionNames()) {
            System.out.println("definitionName = " + definitionName);
        }
        
        //3、获取组件
        Map<String, Person> type = ioc.getBeansOfType(Person.class);
        System.out.println("type = " + type);


        //4、
        Object zhangsan = ioc.getBean("zhangsan");
    }


    public static void test12(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);
        System.out.println("=================ioc容器创建完成===================");

        DeliveryDao dao = ioc.getBean(DeliveryDao.class);

        dao.saveDelivery();
    }

    public static void test11(String[] args) throws IOException {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);
        System.out.println("=================ioc容器创建完成===================");

        Dog bean = ioc.getBean(Dog.class);
        System.out.println("bean = " + bean);

        Cat bean1 = ioc.getBean(Cat.class);
        System.out.println("cat = " + bean1);




        File file = ResourceUtils.getFile("classpath:abc.jpg");
        System.out.println("file = " + file);


        int available = new FileInputStream(file).available();
        System.out.println("available = " + available);

    }

    public static void test10(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);
        System.out.println("=================ioc容器创建完成===================");


        HahaService hahaService = ioc.getBean(HahaService.class);
        System.out.println("hahaService = " + hahaService);


        String osType = hahaService.getOsType();
        System.out.println("osType = " + osType);


        String myName = hahaService.getMyName();
        System.out.println("myName = " + myName);
    }



    public static void test09(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);
        System.out.println("=================ioc容器创建完成===================");

        UserDao bean = ioc.getBean(UserDao.class);
        System.out.println("bean = " + bean);

    }


    /**
     *
     * @param args
     */
    public static void test08(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);
        System.out.println("=================ioc容器创建完成===================");

        UserService bean = ioc.getBean(UserService.class);
        System.out.println("UserService = " + bean);

        Map<String, Dog> beansOfType = ioc.getBeansOfType(Dog.class);
        System.out.println("dogs = " + beansOfType);


    }

    /**
     * 测试自动注入: 代码在 UserController 中
     * @param args
     */
    public static void test07(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);
        System.out.println("=================ioc容器创建完成===================");

        UserController userController = ioc.getBean(UserController.class);

        System.out.println("userController = " + userController);
    }

    /**
     * 条件注册
     * @param args
     */
    public static void test06(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);

        Map<String, Person> beans = ioc.getBeansOfType(Person.class);
        System.out.println("beans = " + beans);


        //拿到环境变量
        ConfigurableEnvironment environment = ioc.getEnvironment();

        String property = environment.getProperty("OS");
        System.out.println("property = " + property);

        Map<String, Dog> beansOfType = ioc.getBeansOfType(Dog.class);
        System.out.println("dogs = " + beansOfType);

        Map<String, UserService> ofType = ioc.getBeansOfType(UserService.class);
        System.out.println("ofType = " + ofType);

    }


    // FactoryBean在容器中放的组件的类型，是接口中泛型指定的类型，组件的名字是 工厂自己的名字
    public static void test05(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);
        System.out.println("=================ioc容器创建完成===================");


        Car bean1 = ioc.getBean(Car.class);
        Car bean2 = ioc.getBean(Car.class);
        System.out.println(bean1 == bean2);

        Map<String, Car> beansOfType = ioc.getBeansOfType(Car.class);
        System.out.println("beansOfType = " + beansOfType);
    }

    /**
     * @Scope 调整组件的作用域：
     * 1、@Scope("prototype")：非单实例:
     *      容器启动的时候不会创建非单实例组件的对象。
     *      什么时候获取，什么时候创建
     * 2、@Scope("singleton")：单实例： 默认值
     *      容器启动的时候会创建单实例组件的对象。
     *      容器启动完成之前就会创建好
     *    @Lazy：懒加载
     *      容器启动完成之前不会创建懒加载组件的对象
     *      什么时候获取，什么时候创建
     * 3、@Scope("request")：同一个请求单实例
     * 4、@Scope("session")：同一次会话单实例
     *
     * @return
     */
    public static void test04(String[] args) {
        // @Scope("singleton")
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);

        System.out.println("=================ioc容器创建完成===================");
        Object zhangsan1 = ioc.getBean("zhangsan");
        System.out.println("zhangsan1 = " + zhangsan1);
        Object zhangsan2 = ioc.getBean("zhangsan");
        System.out.println("zhangsan2 = " + zhangsan2);

        //容器创建的时候（完成之前）就把所有的单实例对象创建完成
        System.out.println(zhangsan1 == zhangsan2);

        System.out.println("=========================================");
//        Dog bean = ioc.getBean(Dog.class);
//        System.out.println("dog = " + bean);

    }

    /**
     * 默认，分层注解能起作用的前提是：这些组件必须在主程序所在的包及其子包结构下
     * Spring 为我们提供了快速的 MVC分层注解
     *      1、@Controller 控制器
     *      2、@Service 服务层
     *      3、@Repository 持久层
     *      4、@Component 组件
     * @param args
     */
    public static void test03(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);

        System.out.println("========");

        UserController bean = ioc.getBean(UserController.class);
        System.out.println("bean = " + bean);

        UserService bean1 = ioc.getBean(UserService.class);
        System.out.println("bean1 = " + bean1);

        CoreConstants bean2 = ioc.getBean(CoreConstants.class);
        System.out.println("bean2 = " + bean2);

    }

    /**
     * 组件：框架的底层配置；
     *   配置文件：指定配置
     *   配置类：分类管理组件的配置，配置类也是容器中的一种组件。
     *
     * 创建时机：容器启动过程中就会创建组件对象
     * 单实例特性：所有组件默认是单例的，每次获取直接从容器中拿。容器提前会创建组件
     * @param args
     */
    public static void test02(String[] args) {
        //1、跑起一个Spring的应用；  ApplicationContext：Spring应用上下文对象； IoC容器
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);
        System.out.println("=================ioc容器创建完成===================");

        //2、获取组件
        Dog bean = ioc.getBean(Dog.class);
        System.out.println("bean = " + bean);

        Dog bean1 = ioc.getBean(Dog.class);
        System.out.println("bean1 = " + bean1);

        Dog bean2 = ioc.getBean(Dog.class);
        System.out.println("bean2 = " + bean2);


        Person zhangsan = (Person) ioc.getBean("zhangsan");
        System.out.println("对象 = " + zhangsan);
        System.out.println("=============================");
        for (String definitionName : ioc.getBeanDefinitionNames()) {
            System.out.println("definitionName = " + definitionName);
        }


    }


    public static void test01BeanAnnotation(String[] args) {
        //1、跑起一个Spring的应用；  ApplicationContext：Spring应用上下文对象； IoC容器
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);
        System.out.println("ioc = " + ioc);

        System.out.println("=============================");
        //2、获取到容器中所有组件的名字；容器中装了哪些组件； Spring启动会有很多默认组件
//        String[] names = ioc.getBeanDefinitionNames();
//        for (String name : names) {
//            System.out.println("name = " + name);
//        }


        //4、获取容器中的组件对象；精确获取某个组件
        // 组件的四大特性：(名字、类型)、对象、作用域
        // 组件名字全局唯一；组件名重复了，一定只会给容器中放一个最先声明的哪个。

        //小结：
        //从容器中获取组件，
        //  1）、组件不存在，抛异常：NoSuchBeanDefinitionException
        //  2）、组件不唯一，
        //      按照类型只要一个：抛异常：NoUniqueBeanDefinitionException
        //      按照名字只要一个：精确获取到指定对象
        //      按照类型获取多个：返回所有组件的集合（Map）
        //  3）、组件唯一存在，正确返回。


        //4.1、按照组件的名字获取对象
        Person zhangsan = (Person) ioc.getBean("zhangsan");
        System.out.println("对象 = " + zhangsan);

        //4.2、按照组件类型获取对象
//        Person bean = ioc.getBean(Person.class);
//        System.out.println("bean = " + bean);
        
        //4.3、按照组件类型获取这种类型的所有对象
        Map<String, Person> type = ioc.getBeansOfType(Person.class);
        System.out.println("type = " + type);

        //4.4、按照类型+名字
        Person bean = ioc.getBean("zhangsan", Person.class);
        System.out.println("bean = " + bean);


        //5、组件是单实例的....

    }

}
