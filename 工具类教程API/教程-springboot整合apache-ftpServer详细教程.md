> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.cnblogs.com](https://www.cnblogs.com/baixianlong/p/12192425.html)

**原创不易，如需转载，请注明出处 [https://www.cnblogs.com/baixianlong/p/12192425.html](https://www.cnblogs.com/baixianlong/p/12192425.html)，否则将追究法律责任！！！**

一、Apache ftpserver 相关简介
=======================

　　Apache FtpServer 是 100％纯 Java FTP 服务器。它被设计为基于当前可用的开放协议的完整且可移植的 FTP 服务器引擎解决方案。FtpServer 可以作为 Windows 服务或 Unix / Linux 守护程序独立运行，也可以嵌入 Java 应用程序中。我们还提供对 Spring 应用程序内集成的支持，并以 OSGi 捆绑软件的形式提供我们的发行版。默认的网络支持基于高性能异步 IO 库 Apache MINA。使用 MINA，FtpServer 可以扩展到大量并发用户。

二、Apache ftpserver 相关特性
=======================

*   100％纯 Java，免费的开源可恢复 FTP 服务器
*   多平台支持和多线程设计。
*   用户虚拟目录，写入权限，空闲超时和上载 / 下载带宽限制支持。
*   匿名登录支持。
*   上传和下载文件都是可恢复的。
*   处理 ASCII 和二进制数据传输。
*   支持 IP 限制以禁止 IP。
*   数据库和文件可用于存储用户数据。
*   所有 FTP 消息都是可定制的。
*   隐式 / 显式 SSL / TLS 支持。
*   MDTM 支持 - 您的用户可以更改文件的日期时间戳。
*   “模式 Z” 支持更快地上传 / 下载数据。
*   可以轻松添加自定义用户管理器，IP 限制器，记录器。
*   可以添加用户事件通知（Ftplet）。

三、Apache ftpserver 简单部署使用（基于 windows 下，linux 大同小异）
==================================================

*   1、根据需要下载对应版本的部署包：[https://mina.apache.org/ftpserver-project/downloads.html](https://mina.apache.org/ftpserver-project/downloads.html "下载")
*   2、解压部署包并调整.\res\conf\users.properties 和.\res\conf\ftpd-typical.xml 配置文件![ftp_01.png](https://i.loli.net/2020/01/14/QSF5JKXBaGiOdhg.png) ![ftp_02.png](https://i.loli.net/2020/01/14/OkUIQmJuhRAenNz.png)

users.properties 文件配置
---------------------

```properties
例如配置一个bxl用户：
	#密码 配置新的用户
	ftpserver.user.bxl.userpassword=123456
	#主目录，这里可以自定义自己的主目录
	ftpserver.user.bxl.homedirectory=./res/bxl-home
	#当前用户可用
	ftpserver.user.bxl.enableflag=true
	#具有上传权限
	ftpserver.user.bxl.writepermission=true
	#最大登陆用户数为20
	ftpserver.user.bxl.maxloginnumber=20
	#同IP登陆用户数为2
	ftpserver.user.bxl.maxloginperip=2
	#空闲时间为300秒
	ftpserver.user.bxl.idletime=300
	#上传速率限制为480000字节每秒
	ftpserver.user.bxl.uploadrate=48000000
	#下载速率限制为480000字节每秒
	ftpserver.user.bxl.downloadrate=48000000
```

ftpd-typical.xml 文件配置
---------------------

```xml
<server xmlns="http://mina.apache.org/ftpserver/spring/v1"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://mina.apache.org/ftpserver/spring/v1 http://mina.apache.org/ftpserver/ftpserver-1.0.xsd" id="myServer">
		<listeners>
			<nio-listener >
			    <ssl>
	                <keystore file="./res/ftpserver.jks" password="password" />
	            </ssl>
				<!--注意:如果要支持外网连接，需要使用被动模式passive，默认开启主动模式-->
				<data-connection idle-timeout="60">
					<active enabled="true" ip-check="true" />
					<!-- <passive ports="2000-2222" address="0.0.0.0" external-address="xxx.xxx.xxx.xxx" /> -->
				</data-connection>
				<!--添加ip黑名单-->
				<blacklist>127.0.0.1</blacklist>
			</nio-listener>
		</listeners>
		
		<!--这里添加encrypt-passwords="clear"，去掉密码加密-->
		<file-user-manager file="./res/conf/users.properties" encrypt-passwords="clear" />
	</server>
```

*   3、启动并访问
    *   首先启动服务，打开 cmd 并 cd 到 bin 路径执行.\ftpd.bat res/conf/ftpd-typical.xml, 看到如下状态说明启动成功
        
        ![](https://i.loli.net/2020/01/14/nyehjA8HaKPwcro.png)
        
    *   测试访问，打开浏览器输入：ftp://localhost:2121 / 就会看到你的文件目录了，如果没有配置匿名用户，则会要求你输入用户名密码，正是你在 user.properties 中配置的
        
        ![](https://i.loli.net/2020/01/14/PjKTs8ZnYhl9bJL.png)
        



# 四、代码内嵌入ftp服务器

## pom依赖

```xml
//这些只是apache ftpserver相关的依赖，springboot项目本身的依赖大家自己添加即可
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
  <version>1.7.25 </version>
</dependency>
<dependency>
  <groupId>org.apache.ftpserver</groupId>
  <artifactId>ftpserver-core</artifactId>
  <version>1.1.1</version>
</dependency>
<dependency>
  <groupId>org.apache.ftpserver</groupId>
  <artifactId>ftplet-api</artifactId>
  <version>1.1.1</version>
</dependency>

<dependency>
  <groupId>org.apache.mina</groupId>
  <artifactId>mina-core</artifactId>
  <version>2.0.16</version>
</dependency>
```

最简单的 FTP 服务器
----------------------------------------

```java
public static void main(String[] args) throws FtpException {
	
	FtpServerFactory serverFactory = new FtpServerFactory();
	FtpServer server = serverFactory.createServer();
	server.start();
	
}
```

这是最简单的 FTP 服务器。运行程序，启动 FTP 服务器后，在地址栏中输入 [ftp://localhost](ftp://localhost/)，可以看到以下界面，要求输入用户名密码。当然这个 FTP 是进不去的，因为它是最简单的 FTP 服务器，简单到没有用户。

设置匿名用户及对应的服务器文件夹
--------------------------------------------------------

```java
public static void main(String[] args) throws FtpException {
	
	FtpServerFactory serverFactory = new FtpServerFactory();

	BaseUser user = new BaseUser();
	user.setName("anonymous");
	user.setHomeDirectory("D:/test");
	
	serverFactory.getUserManager().save(user);
	
	FtpServer server = serverFactory.createServer();
	server.start();
	
}
```

添加一个匿名用户 anonymous，并设置它对应的文件夹是 D:/test。再次进入 [ftp://localhost](ftp://localhost/)，可以看到 D:/test 中的文件。但是此时的 FTP 权限是只读的，也就是只能查看文件，但是不能增删改。

用户可写的权限设置
-----------------------------------

```java
public static void main(String[] args) throws FtpException {
	
	FtpServerFactory serverFactory = new FtpServerFactory();

	BaseUser user = new BaseUser();
	user.setName("anonymous");
	user.setHomeDirectory("D:/test");
	
	List<Authority> authorities = new ArrayList<Authority>();
	authorities.add(new WritePermission());
	user.setAuthorities(authorities);
	
	serverFactory.getUserManager().save(user);
	
	FtpServer server = serverFactory.createServer();
	server.start();
	
}
```

加入可写的权限，此时就能对 FTP 服务器上的文件进行增删改了。

用户登录
--------------------

```java
public static void main(String[] args) throws FtpException {
	
	FtpServerFactory serverFactory = new FtpServerFactory();

	BaseUser user = new BaseUser();
	user.setName("test");
	user.setPassword("123456");
	user.setHomeDirectory("D:/test");
	
	serverFactory.getUserManager().save(user);
	
	FtpServer server = serverFactory.createServer();
	server.start();
	
}
```

添加用户 test，密码是 123456，此时客户端要想进入 ftp，必须输入正确的用户名密码。

配置文件设置用户
--------------------------------

```java
public static void main(String[] args) throws FtpException {
	
	FtpServerFactory serverFactory = new FtpServerFactory();

	PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
	userManagerFactory.setFile(new File("users.properties"));
	
	serverFactory.setUserManager(userManagerFactory.createUserManager());
	
	FtpServer server = serverFactory.createServer();
	server.start();
	
}
```

配置文件 users.properties：  

```properties
ftpserver.user.admin.userpassword=21232F297A57A5A743894A0E4A801FC3
ftpserver.user.admin.homedirectory=D:/test
ftpserver.user.admin.enableflag=true
ftpserver.user.admin.writepermission=true
ftpserver.user.admin.maxloginnumber=0
ftpserver.user.admin.maxloginperip=0
ftpserver.user.admin.idletime=0
ftpserver.user.admin.uploadrate=0
ftpserver.user.admin.downloadrate=0

ftpserver.user.anonymous.userpassword=
ftpserver.user.anonymous.homedirectory=D:/test
ftpserver.user.anonymous.enableflag=true
ftpserver.user.anonymous.writepermission=false
ftpserver.user.anonymous.maxloginnumber=20
ftpserver.user.anonymous.maxloginperip=2
ftpserver.user.anonymous.idletime=300
ftpserver.user.anonymous.uploadrate=4800
ftpserver.user.anonymous.downloadrate=4800
```

这是通过配置文件 users.properties 设置用户。配置文件中包含两个用户：匿名用户 anonymous 和 admin。anonymous 只有只读权限，admin 有可写权限。其中 userpassword 配置项是 MD5 加密的。其他配置项也很好理解。

四、Springboot 整合 Apache ftpserver（重点）
====================================

方式一：独立部署 ftpserver 服务
---------------------

　　这种方式比较简单，只要把服务部署好即可，然后通过 FtpClien 来完成相关操作，同 jedis 访问 redis 服务一个道理，没啥可说的。主要注意一下 ftpserver 的访问模式，如果要支持外网连接，需要使用被动模式 passive。

方式二：将 ftpserver 服务内嵌到 springboot 服务中
------------------------------------

　　这种方式需要和 springboot 整合在一起，相对比较复杂，但这种方式下 ftpserver 会 随着 springboot 服务启动或关闭而开启或销毁。具体使用哪种方式就看自己的业务需求了。

　　简单说一下我的实现的方案，ftpserver 支持配置文件和 db 两种方式来保存账号信息和其它相关配置，如果我们的业务系统需要将用户信息和 ftp 的账号信息打通，并且还有相关的业务统计，比如统计系统中每个人上传文件的时间、个数等等，那么使用数据库来保存 ftp 账号信息还是比较方便灵活的。我这里就选择使用 mysql 了。



### 开始整合

*   1、项目添加依赖
    
    ```xml
    //这些只是apache ftpserver相关的依赖，springboot项目本身的依赖大家自己添加即可
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
      <version>1.7.25 </version>
    </dependency>
    <dependency>
      <groupId>org.apache.ftpserver</groupId>
      <artifactId>ftpserver-core</artifactId>
      <version>1.1.1</version>
    </dependency>
    <dependency>
      <groupId>org.apache.ftpserver</groupId>
      <artifactId>ftplet-api</artifactId>
      <version>1.1.1</version>
    </dependency>
    
    <dependency>
      <groupId>org.apache.mina</groupId>
      <artifactId>mina-core</artifactId>
      <version>2.0.16</version>
    </dependency>
    ```
    
*   2、数据库建表用来保存相关的账户信息（大家可以手动添加几条用来测试），具体字段意思参考 users.properties 文件配置（可以想象一下以后我们的系统每注册一个用户都可以为其添加一条 ftp_user 信息，用来指定保存用户的上传数据等等）
    
    ```sql
    CREATE TABLE FTP_USER (      
       userid VARCHAR(64) NOT NULL PRIMARY KEY,       
       userpassword VARCHAR(64),      
       homedirectory VARCHAR(128) NOT NULL,             
       enableflag BOOLEAN DEFAULT TRUE,    
       writepermission BOOLEAN DEFAULT FALSE,       
       idletime INT DEFAULT 0,             
       uploadrate INT DEFAULT 0,             
       downloadrate INT DEFAULT 0,
       maxloginnumber INT DEFAULT 0,
       maxloginperip INT DEFAULT 0
    );
    ```
    
*   3、配置 ftpserver, 提供 ftpserver 的 init()、start()、stop() 方法
    
    ```java
    import com.mysql.cj.jdbc.MysqlDataSource;
    import com.talkingdata.tds.ftpserver.plets.MyFtpPlet;
    import org.apache.commons.io.IOUtils;
    import org.apache.ftpserver.DataConnectionConfigurationFactory;
    import org.apache.ftpserver.FtpServer;
    import org.apache.ftpserver.FtpServerFactory;
    import org.apache.ftpserver.ftplet.FtpException;
    import org.apache.ftpserver.ftplet.Ftplet;
    import org.apache.ftpserver.listener.Listener;
    import org.apache.ftpserver.listener.ListenerFactory;
    import org.apache.ftpserver.ssl.SslConfigurationFactory;
    import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
    import org.apache.ftpserver.usermanager.DbUserManagerFactory;
    import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.io.ClassPathResource;
    import org.springframework.stereotype.Component;
    
    
    import javax.sql.DataSource;
    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.util.HashMap;
    import java.util.Map;
    
    /**
     * 注意：被@Configuration标记的类会被加入ioc容器中，而且类中所有带 @Bean注解的方法都会被动态代理，因此调用该方法返回的都是同一个实例。
     * ftp服务访问地址：
     *      ftp://localhost:3131/
     */
    @Configuration("MyFtp")
    public class MyFtpServer {
    
        private static final Logger logger = LoggerFactory.getLogger(MyFtpServer.class);
    
    	//springboot配置好数据源直接注入即可
        @Autowired
        private DataSource dataSource;
        protected FtpServer server;
    
        //我们这里利用spring加载@Configuration的特性来完成ftp server的初始化
        public MyFtpServer(DataSource dataSource) {
            this.dataSource = dataSource;
            initFtp();
            logger.info("Apache ftp server is already instantiation complete!");
        }
    
        /**
         * ftp server init
         * @throws IOException
         */
        public void initFtp() {
            FtpServerFactory serverFactory = new FtpServerFactory();
            ListenerFactory listenerFactory = new ListenerFactory();
            //1、设置服务端口
            listenerFactory.setPort(3131);
            //2、设置被动模式数据上传的接口范围,云服务器需要开放对应区间的端口给客户端
            DataConnectionConfigurationFactory dataConnectionConfFactory = new DataConnectionConfigurationFactory();
            dataConnectionConfFactory.setPassivePorts("10000-10500");
            listenerFactory.setDataConnectionConfiguration(dataConnectionConfFactory.createDataConnectionConfiguration());
            //3、增加SSL安全配置
    //        SslConfigurationFactory ssl = new SslConfigurationFactory();
    //        ssl.setKeystoreFile(new File("src/main/resources/ftpserver.jks"));
    //        ssl.setKeystorePassword("password");
            //ssl.setSslProtocol("SSL");
            // set the SSL configuration for the listener
    //        listenerFactory.setSslConfiguration(ssl.createSslConfiguration());
    //        listenerFactory.setImplicitSsl(true);
            //4、替换默认的监听器
            Listener listener = listenerFactory.createListener();
            serverFactory.addListener("default", listener);
            //5、配置自定义用户事件
            Map<String, Ftplet> ftpLets = new HashMap();
            ftpLets.put("ftpService", new MyFtpPlet());
            serverFactory.setFtplets(ftpLets);
            //6、读取用户的配置信息
            //注意：配置文件位于resources目录下，如果项目使用内置容器打成jar包发布，FTPServer无法直接直接读取Jar包中的配置文件。
            //解决办法：将文件复制到指定目录(本文指定到根目录)下然后FTPServer才能读取到。
    //        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
    //        String tempPath = System.getProperty("java.io.tmpdir") + System.currentTimeMillis() + ".properties";
    //        File tempConfig = new File(tempPath);
    //        ClassPathResource resource = new ClassPathResource("users.properties");
    //        IOUtils.copy(resource.getInputStream(), new FileOutputStream(tempConfig));
    //        userManagerFactory.setFile(tempConfig);
    //        userManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());  //密码以明文的方式
    //        serverFactory.setUserManager(userManagerFactory.createUserManager());
            //6.2、基于数据库来存储用户实例
            DbUserManagerFactory dbUserManagerFactory = new DbUserManagerFactory();
            //todo....
            dbUserManagerFactory.setDataSource(dataSource);
            dbUserManagerFactory.setAdminName("admin");
            dbUserManagerFactory.setSqlUserAdmin("SELECT userid FROM FTP_USER WHERE userid='{userid}' AND userid='admin'");
            dbUserManagerFactory.setSqlUserInsert("INSERT INTO FTP_USER (userid, userpassword, homedirectory, " +
                    "enableflag, writepermission, idletime, uploadrate, downloadrate) VALUES " +
                    "('{userid}', '{userpassword}', '{homedirectory}', {enableflag}, " +
                    "{writepermission}, {idletime}, uploadrate}, {downloadrate})");
            dbUserManagerFactory.setSqlUserDelete("DELETE FROM FTP_USER WHERE userid = '{userid}'");
            dbUserManagerFactory.setSqlUserUpdate("UPDATE FTP_USER SET userpassword='{userpassword}',homedirectory='{homedirectory}',enableflag={enableflag},writepermission={writepermission},idletime={idletime},uploadrate={uploadrate},downloadrate={downloadrate},maxloginnumber={maxloginnumber}, maxloginperip={maxloginperip} WHERE userid='{userid}'");
            dbUserManagerFactory.setSqlUserSelect("SELECT * FROM FTP_USER WHERE userid = '{userid}'");
            dbUserManagerFactory.setSqlUserSelectAll("SELECT userid FROM FTP_USER ORDER BY userid");
            dbUserManagerFactory.setSqlUserAuthenticate("SELECT userid, userpassword FROM FTP_USER WHERE userid='{userid}'");
            dbUserManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
            serverFactory.setUserManager(dbUserManagerFactory.createUserManager());
            //7、实例化FTP Server
            server = serverFactory.createServer();
        }
    
    
        /**
         * ftp server start
         */
        public void start(){
            try {
                server.start();
                logger.info("Apache Ftp server is starting!");
            }catch(FtpException e) {
                e.printStackTrace();
            }
        }
    
    
        /**
         * ftp server stop
         */
        public void stop() {
            server.stop();
            logger.info("Apache Ftp server is stoping!");
        }
    
    }
    ```
    
*   4、配置监听器，使 spring 容器启动时启动 ftpserver，在 spring 容器销毁时停止 ftpserver
    
    ```java
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.context.WebApplicationContext;
    import org.springframework.web.context.support.WebApplicationContextUtils;
    
    import javax.servlet.ServletContextEvent;
    import javax.servlet.ServletContextListener;
    import javax.servlet.annotation.WebListener;
    
    @WebListener
    public class FtpServerListener implements ServletContextListener {
    
        private static final Logger logger = LoggerFactory.getLogger(MyFtpServer.class);
        private static final String SERVER_;
    
        @Autowired
        private MyFtpServer server;
    
        //容器关闭时调用方法stop ftpServer
        public void contextDestroyed(ServletContextEvent sce) {
    //        WebApplicationContext ctx= WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
    //        MyFtpServer server=(MyFtpServer)ctx.getServletContext().getAttribute(SERVER_NAME);
            server.stop();
            sce.getServletContext().removeAttribute(SERVER_NAME);
            logger.info("Apache Ftp server is stoped!");
        }
    
        //容器初始化调用方法start ftpServer
        public void contextInitialized(ServletContextEvent sce) {
    //        WebApplicationContext ctx= WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
    //        MyFtpServer server=(MyFtpServer) ctx.getBean("MyFtp");
            sce.getServletContext().setAttribute(SERVER_NAME,server);
            try {
                //项目启动时已经加载好了
                server.start();
                logger.info("Apache Ftp server is started!");
            } catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException("Apache Ftp server start failed!", e);
            }
        }
    
    }
    ```
    
*   5、通过继承 DefaultFtplet 抽象类来实现一些自定义用户事件（我这里只是举例）
    
    ```java
    import org.apache.ftpserver.ftplet.*;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    
    import java.io.IOException;
    
    public class MyFtpPlet extends DefaultFtplet {
    
        private static final Logger logger = LoggerFactory.getLogger(MyFtpPlet.class);
    
        @Override
        public FtpletResult onUploadStart(FtpSession session, FtpRequest request)
                throws FtpException, IOException {
            //获取上传文件的上传路径
            String path = session.getUser().getHomeDirectory();
            //获取上传用户
            String name = session.getUser().getName();
            //获取上传文件名
            String filename = request.getArgument();
            logger.info("用户:'{}'，上传文件到目录：'{}'，文件名称为：'{}'，状态：开始上传~", name, path, filename);
            return super.onUploadStart(session, request);
        }
    
    
        @Override
        public FtpletResult onUploadEnd(FtpSession session, FtpRequest request)
                throws FtpException, IOException {
            //获取上传文件的上传路径
            String path = session.getUser().getHomeDirectory();
            //获取上传用户
            String name = session.getUser().getName();
            //获取上传文件名
            String filename = request.getArgument();
            logger.info("用户:'{}'，上传文件到目录：'{}'，文件名称为：'{}，状态：成功！'", name, path, filename);
            return super.onUploadEnd(session, request);
        }
    
        @Override
        public FtpletResult onDownloadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
            //todo servies...
            return super.onDownloadStart(session, request);
        }
    
        @Override
        public FtpletResult onDownloadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
            //todo servies...
            return super.onDownloadEnd(session, request);
        }
    
    }
    ```
    
*   6、 配置 springboot 静态资源的访问
    
    ```java
    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
    
    
    @Configuration
    public class FtpConfig implements WebMvcConfigurer {
    
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            //可以通过os来判断
            String os = System.getProperty("os.name");
            //linux设置
    //        registry.addResourceHandler("/ftp/**").addResourceLocations("file:/home/pic/");
            //windows设置
            //第一个方法设置访问路径前缀，第二个方法设置资源路径，既可以指定项目classpath路径，也可以指定其它非项目路径
            registry.addResourceHandler("/ftp/**").addResourceLocations("file:D:\\apache-ftpserver-1.1.1\\res\\bxl-home\\");
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        }
    
    }
    ```
    
*   7、以上 6 步已经完成 ftpserver 的配置，随着 springboot 项目的启动就会开启 ftpserver 服务，下面在给大家贴一下客户端的访问的 util, 大家可以自行封装一下即可。
    
    ```java
    import org.apache.commons.net.ftp.FTPClient;
    import org.apache.commons.net.ftp.FTPFile;
    import org.apache.commons.net.ftp.FTPReply;
    import org.apache.commons.net.ftp.FTPSClient;
    
    import java.io.*;
    
    public class FtpClientUtil {
    
        // ftp服务器ip地址
        private static String FTP_ADDRESS = "localhost";
        // 端口号
        private static int FTP_PORT = 3131;
        // 用户名
        private static String FTP_USERNAME = "bxl";
        // 密码
        private static String FTP_PASSWORD = "123456";
        // 相对路径
        private static String FTP_BASEPATH = "";
    
        public static boolean uploadFile(String remoteFileName, InputStream input) {
            boolean flag = false;
            FTPClient ftp = new FTPClient();
            ftp.setControlEncoding("UTF-8");
            try {
                int reply;
                ftp.connect(FTP_ADDRESS, FTP_PORT);// 连接FTP服务器
                ftp.login(FTP_USERNAME, FTP_PASSWORD);// 登录
                reply = ftp.getReplyCode();
                System.out.println("登录ftp服务返回状态码为：" + reply);
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    return flag;
                }
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                //设置为被动模式
                ftp.enterLocalPassiveMode();
                ftp.makeDirectory(FTP_BASEPATH);
                ftp.changeWorkingDirectory(FTP_BASEPATH);
                //originFilePath就是上传文件的文件名，建议使用生成的唯一命名，中文命名最好做转码
                boolean a = ftp.storeFile(remoteFileName, input);
    //            boolean a = ftp.storeFile(new String(remoteFileName.getBytes(),"iso-8859-1"),input);
                System.out.println("要上传的原始文件名为：" + remoteFileName + ", 上传结果：" + a);
                input.close();
                ftp.logout();
                flag = true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException ioe) {
                    }
                }
            }
            return flag;
        }
    
    //    public static Boolean uploadFile(String remoteFileName, InputStream inputStream, String ftpAddress, int ftpPort,
    //                                     String ftpName, String ftpPassWord, String ftpBasePath) {
    //        FTP_ADDRESS = ftpAddress;
    //        FTP_PORT = ftpPort;
    //        FTP_USERNAME = ftpName;
    //        FTP_PASSWORD = ftpPassWord;
    //        FTP_BASEPATH = ftpBasePath;
    //        uploadFile(remoteFileName,inputStream);
    //        return true;
    //    }
    
        public static boolean deleteFile(String filename) {
            boolean flag = false;
            FTPClient ftpClient = new FTPClient();
            try {
                // 连接FTP服务器
                ftpClient.connect(FTP_ADDRESS, FTP_PORT);
                // 登录FTP服务器
                ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
                // 验证FTP服务器是否登录成功
                int replyCode = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    return flag;
                }
                // 切换FTP目录
                ftpClient.changeWorkingDirectory(FTP_BASEPATH);
                ftpClient.dele(filename);
                ftpClient.logout();
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.logout();
                    } catch (IOException e) {
    
                    }
                }
            }
            return flag;
        }
    
        public static boolean downloadFile(String filename, String localPath) {
            boolean flag = false;
    //        FTPSClient ftpClient = new FTPSClient("TLS", true);
            FTPClient ftpClient = new FTPClient();
            try {
                // 连接FTP服务器
                ftpClient.connect(FTP_ADDRESS, FTP_PORT);
                // 登录FTP服务器
                ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
                // 验证FTP服务器是否登录成功
                int replyCode = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    return flag;
                }
                // 切换FTP目录
                ftpClient.changeWorkingDirectory(FTP_BASEPATH);
                //此处为demo方法，正常应该到数据库中查询fileName
                FTPFile[] ftpFiles = ftpClient.listFiles();
                for (FTPFile file : ftpFiles) {
                    if (filename.equalsIgnoreCase(file.getName())) {
                        File localFile = new File(localPath + "/" + file.getName());
                        OutputStream os = new FileOutputStream(localFile);
                        ftpClient.retrieveFile(file.getName(), os);
                        os.close();
                    }
                }
                ftpClient.logout();
                flag = true;
                System.out.println("文件下载完成！！！");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.logout();
                    } catch (IOException e) {
    
                    }
                }
            }
            return flag;
        }
    }
    ```
    

五、总结
====

到此，所有的配置已经完成，我们的业务系统也同时也承担了一个角色，那就是 ftp 服务器，整个配置是没有加入 SSL/TLS 安全机制的，大家如果感兴趣可以自行研究下。我代码中注释那那部分，只是注意下通过客户端访问时，需要使用 FtpsCliet, 而非 FtpCliet。当然还需要配置你自己的 ftpserver.jks 文件，也就是 java key store。百度下一下如何生成，很简单哦！

