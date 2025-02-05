> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [blog.csdn.net](https://blog.csdn.net/hehuihh/article/details/107179807)

# Apache sshd 客户端（密码和公钥登录并执行命令

### 第一步：引入依赖

```xml
<dependency>
        <groupId>org.apache.sshd</groupId>
        <artifactId>sshd-core</artifactId>
        <version>2.1.0</version>
</dependency>
```

### 第二步：准备执行代码 

```java
import lombok.Getter;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.util.security.SecurityUtils;
 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
 
public class SshdExecutor {
 
    SshClient client;
    ClientSession session;
 
    @Getter
    private String result;
    @Getter
    private String error;
 
    public SshdExecutor(String ip, Integer port, String user) {
        client = SshClient.setUpDefaultClient();
        client.start();
        try {
            session = client.connect(user, ip, port).verify(10 * 1000).getSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //密码方式
    public SshdExecutor(String ip, Integer port, String user, String password) {
        this(ip, port, user);
        session.addPasswordIdentity(password);
    }
 
    //公钥方式
    public SshdExecutor(String ip, Integer port, String user, String keyName, String publicKey) {
        this(ip, port, user);
        try {
            session.addPublicKeyIdentity(SecurityUtils.loadKeyPairIdentity(keyName, new ByteArrayInputStream(publicKey.getBytes()), null));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
    
    //执行命令
    public void execute(String command) {
        try {
            if (!session.auth().verify(10 * 1000).isSuccess()) {
                throw new Exception("auth faild");
            }
 
            ClientChannel channel = session.createExecChannel(command);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream err = new ByteArrayOutputStream();
            channel.setOut(out);
            channel.setErr(err);
 
            if (!channel.open().verify(10 * 1000).isOpened()) {
                throw new Exception("open faild");
            }
            List<ClientChannelEvent> list = new ArrayList<>();
            list.add(ClientChannelEvent.CLOSED);
            channel.waitFor(list, 10 * 1000);
            channel.close();
            result = out.toString();
            error = err.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.stop();
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

参考文章：[https://www.cnblogs.com/sybblogs/p/11555279.html](https://www.cnblogs.com/sybblogs/p/11555279.html)

#  jsch 连接 ssh 服务并远程执行命令、上传、下载操作

关键依赖：jsch-0.1.54.jar

```xml
<dependency>
    <groupId>com.jcraft</groupId>
    <artifactId>jsch</artifactId>
    <version>0.1.54</version>
</dependency>
```



### 第一，使用用户名和密码连接

```java
/**
     * 使用用户名和密码连接
     */
    @Test
    public void test1() throws JSchException {
        //创建一个ssh通讯核心类
        JSch jSch = new JSch();

        //传主机、端口、用户名获得一个会话
        Session session = jSch.getSession("admin", "your host", 22);

        //不进行严格模式检查
        Properties config = new Properties();
        config.put("StrictHostKeyChecking","no");

        //设置密码
        session.setPassword("your password");
        session.setConfig(config);

        //连接会话
        session.connect();

        log.debug("是否连接：" + session.isConnected());
        log.debug("会话：" + session);

        //断开连接
        session.disconnect();

        log.debug("是否连接：" + session.isConnected());

    }
```

![](https://i-blog.csdnimg.cn/blog_migrate/a369e52ddb1c4bbc10a6f77c2c60fd36.png)

### 第二，使用用户名和密钥连接

```java
/**
     * 使用用户名和私钥连接
     * @throws JSchException
     */
    @Test
    public void test2() throws JSchException {
        //创建一个ssh通讯核心类
        JSch jSch = new JSch();

        //设置私钥路径
        jSch.addIdentity("D:\\hehui\\jsch\\admin_private.ppk");

        //传主机、端口、用户名获得一个会话
        Session session = jSch.getSession("admin", "your host", 22);

        //不进行严格模式检查
        Properties config = new Properties();
        config.put("StrictHostKeyChecking","no");

        session.setConfig(config);

        //连接会话
        session.connect();

        log.debug("是否连接：" + session.isConnected());
        log.debug("会话：" + session);

        //断开连接
        session.disconnect();

        log.debug("是否连接：" + session.isConnected());

    }
```

![](https://i-blog.csdnimg.cn/blog_migrate/289f38d39c78ec8dac98e41aaf5e0687.png)

### 第三，远程执行命令

```java
/**
     * 远程执行命令
     * @throws JSchException
     * @throws IOException
     */
    @Test
    public void test3() throws JSchException, IOException {

        log.debug("会话：\r\n" + session);

        ChannelExec channelExec = (ChannelExec)session.openChannel("exec");

        channelExec.setCommand("df -h");
        channelExec.connect();

        InputStream inputStream = channelExec.getInputStream();

        //命令执行结果
        String result = IOUtils.toString(inputStream);

        log.debug("执行结果：\r\n" + result);

    }
```

![](https://i-blog.csdnimg.cn/blog_migrate/a15b329c021b2b3babd3e4623f6b2717.png)  
![](https://i-blog.csdnimg.cn/blog_migrate/e9cc877c9d06353c0fc828d65b1869a9.png)

### 第四，文件上传

```java
/**
     * 文件上传
     */
    @Test
    public void test4() {
        try {
            //使用ssh会话开启一个sftp文件传输的通道，Channel为抽象类
            Channel channel = session.openChannel("sftp");

            //强转为ChannelSftp
            ChannelSftp channelSftp = (ChannelSftp) channel;

            //连接该通道
            channelSftp.connect();

            String filePath = "D:\\hehui\\jsch\\apache-tomcat-9.0.35.tar.gz";

            String fileName = "apache-tomcat-9.0.35.tar.gz";

            //服务文件夹路径，只支持绝对路径
            String serverDir = "/home/admin/jsch/";

            AtomicReference<String> dir = new AtomicReference<>("/");

            Stream.of(serverDir.split("\\/")).forEach(p -> {
                try {
                    if (StringUtils.isNotEmpty(p)) {
                        String existDir = dir.get();
                        if (StringUtils.equals(existDir,"/")) {
                            dir.set(existDir + p);
                        } else {
                            dir.set(existDir + "/" + p);
                        }
                        channelSftp.cd(dir.get());
                    }

                } catch (SftpException e1) {
                    log.debug("创建目录：" + p);
                    try {
                        channelSftp.mkdir(dir.get());
                        channelSftp.cd(dir.get());
                    } catch (SftpException e2) {
                        log.error("error:",e2);
                        throw new RuntimeException(e2);
                    }
                }
            });

            FileInputStream in = new FileInputStream(filePath);

            //上传
            channelSftp.put(in,fileName);

            //关闭传输通道
            channelSftp.disconnect();

        } catch (JSchException e) {
            log.error("error",e);
        } catch (FileNotFoundException e) {
            log.error("error:",e);
        } catch (SftpException e) {
            log.error("error",e);
        }
    }
```

![](https://i-blog.csdnimg.cn/blog_migrate/75e167d8e085371fb3750ceeb6b4672f.png)  
![](https://i-blog.csdnimg.cn/blog_migrate/e3a5e836cb290b8e98cb0682551d4374.png)

### 第五，文件下载

```java
/**
     * 文件下载
     */
    @Test
    public void test5(){
        try {
            //使用ssh会话开启一个sftp文件传输的通道，Channel为抽象类
            Channel channel = session.openChannel("sftp");

            //强转为ChannelSftp
            ChannelSftp channelSftp = (ChannelSftp) channel;

            //连接该通道
            channelSftp.connect();

            String serverDir = "/home/admin/jsch/";
            String serverFile = "apache-tomcat-9.0.35.tar.gz";

            FileOutputStream out = new FileOutputStream("D:\\hehui\\jsch\\apache-tomcat-9.0.35_download.tar.gz");

            channelSftp.cd(serverDir);

            channelSftp.get(serverFile,out);

            //关闭通道
            channelSftp.disconnect();

        } catch (JSchException e) {
            log.error("error:",e);
        } catch (FileNotFoundException e) {
            log.error("error:",e);
        } catch (SftpException e) {
            log.error("error:",e);
        }
    }
```

![](https://i-blog.csdnimg.cn/blog_migrate/b559505bb2f0bf7c31ae5489e39845ee.png)  
![](https://i-blog.csdnimg.cn/blog_migrate/844656c0ebd52e7cd86f2e775085fa52.png)

### **全部 Demo**

```java
package com.day0707;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * JSch(java ssh channel)java连接ssh服务示例
 * 远程执行命令、上传、下载
 * @author hehui
 * @date 2020/7/7
 */
public class JSchDemo {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Session session = null;

    @Before
    public void open() throws JSchException {
        log.debug("开启会话");
        //创建一个ssh通讯核心类
        JSch jSch = new JSch();

        //设置私钥路径
        jSch.addIdentity("D:\\hehui\\jsch\\admin_private.ppk");

        //传主机、端口、用户名获得一个会话
        Session session = jSch.getSession("admin", "your host", 22);

        //不进行严格模式检查
        Properties config = new Properties();
        config.put("StrictHostKeyChecking","no");

        session.setConfig(config);

        //连接会话
        session.connect();

        this.session = session;
    }

    @After
    public void close(){
        log.debug("关闭会话");
        //关闭连接
        this.session.disconnect();
    }

    /**
     * 使用用户名和密码连接
     */
    @Test
    public void test1() throws JSchException {
        //创建一个ssh通讯核心类
        JSch jSch = new JSch();

        //传主机、端口、用户名获得一个会话
        Session session = jSch.getSession("admin", "your host", 22);

        //不进行严格模式检查
        Properties config = new Properties();
        config.put("StrictHostKeyChecking","no");

        //设置密码
        session.setPassword("your password");
        session.setConfig(config);

        //连接会话
        session.connect();

        log.debug("是否连接：" + session.isConnected());
        log.debug("会话：" + session);

        //断开连接
        session.disconnect();

        log.debug("是否连接：" + session.isConnected());

    }

    /**
     * 使用用户名和私钥连接
     * @throws JSchException
     */
    @Test
    public void test2() throws JSchException {
        //创建一个ssh通讯核心类
        JSch jSch = new JSch();

        //设置私钥路径
        jSch.addIdentity("D:\\hehui\\jsch\\admin_private.ppk");

        //传主机、端口、用户名获得一个会话
        Session session = jSch.getSession("admin", "your host", 22);

        //不进行严格模式检查
        Properties config = new Properties();
        config.put("StrictHostKeyChecking","no");

        session.setConfig(config);

        //连接会话
        session.connect();

        log.debug("是否连接：" + session.isConnected());
        log.debug("会话：" + session);

        //断开连接
        session.disconnect();

        log.debug("是否连接：" + session.isConnected());

    }

    /**
     * 远程执行命令
     * @throws JSchException
     * @throws IOException
     */
    @Test
    public void test3() throws JSchException, IOException {

        log.debug("会话：\r\n" + session);

        ChannelExec channelExec = (ChannelExec)session.openChannel("exec");

        channelExec.setCommand("df -h");
        channelExec.connect();

        InputStream inputStream = channelExec.getInputStream();

        //命令执行结果
        String result = IOUtils.toString(inputStream);

        log.debug("执行结果：\r\n" + result);

    }

    /**
     * 文件上传
     */
    @Test
    public void test4() {
        try {
            //使用ssh会话开启一个sftp文件传输的通道，Channel为抽象类
            Channel channel = session.openChannel("sftp");

            //强转为ChannelSftp
            ChannelSftp channelSftp = (ChannelSftp) channel;

            //连接该通道
            channelSftp.connect();

            String filePath = "D:\\hehui\\jsch\\apache-tomcat-9.0.35.tar.gz";

            String fileName = "apache-tomcat-9.0.35.tar.gz";

            //服务文件夹路径，只支持绝对路径
            String serverDir = "/home/admin/jsch/";

            AtomicReference<String> dir = new AtomicReference<>("/");

            Stream.of(serverDir.split("\\/")).forEach(p -> {
                try {
                    if (StringUtils.isNotEmpty(p)) {
                        String existDir = dir.get();
                        if (StringUtils.equals(existDir,"/")) {
                            dir.set(existDir + p);
                        } else {
                            dir.set(existDir + "/" + p);
                        }
                        channelSftp.cd(dir.get());
                    }

                } catch (SftpException e1) {
                    log.debug("创建目录：" + p);
                    try {
                        channelSftp.mkdir(dir.get());
                        channelSftp.cd(dir.get());
                    } catch (SftpException e2) {
                        log.error("error:",e2);
                        throw new RuntimeException(e2);
                    }
                }
            });

            FileInputStream in = new FileInputStream(filePath);

            //上传
            channelSftp.put(in,fileName);

            //关闭传输通道
            channelSftp.disconnect();

        } catch (JSchException e) {
            log.error("error",e);
        } catch (FileNotFoundException e) {
            log.error("error:",e);
        } catch (SftpException e) {
            log.error("error",e);
        }
    }

    /**
     * 文件下载
     */
    @Test
    public void test5(){
        try {
            //使用ssh会话开启一个sftp文件传输的通道，Channel为抽象类
            Channel channel = session.openChannel("sftp");

            //强转为ChannelSftp
            ChannelSftp channelSftp = (ChannelSftp) channel;

            //连接该通道
            channelSftp.connect();

            String serverDir = "/home/admin/jsch/";
            String serverFile = "apache-tomcat-9.0.35.tar.gz";

            FileOutputStream out = new FileOutputStream("D:\\hehui\\jsch\\apache-tomcat-9.0.35_download.tar.gz");

            channelSftp.cd(serverDir);

            channelSftp.get(serverFile,out);

            //关闭通道
            channelSftp.disconnect();

        } catch (JSchException e) {
            log.error("error:",e);
        } catch (FileNotFoundException e) {
            log.error("error:",e);
        } catch (SftpException e) {
            log.error("error:",e);
        }
    }

}
```