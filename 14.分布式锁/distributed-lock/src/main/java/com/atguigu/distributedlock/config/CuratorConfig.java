package com.atguigu.distributedlock.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CuratorConfig {

    @Bean
    public CuratorFramework curatorFramework() {
        // 重试策略，这里使用的是指数补偿重试策略，重试3次，初始重试间隔1000ms，每次重试之后重试间隔递增。
        RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);
        // 初始化Curator客户端：指定链接信息 及 重试策略
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.64.3:2181", retry);
        client.start(); // 开始链接，如果不调用该方法，很多方法无法工作
        return client;
    }
}