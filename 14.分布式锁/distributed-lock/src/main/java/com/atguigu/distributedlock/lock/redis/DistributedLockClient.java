package com.atguigu.distributedlock.lock.redis;

import com.atguigu.distributedlock.lock.redis.DistributedRedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 锁工厂
 * 单例模式
 */
@Component
public class DistributedLockClient {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String uuid;

    public DistributedLockClient() {
        this.uuid = UUID.randomUUID().toString(); // 框架初始化时 赋值唯一服务标识
    }

    /**
     * 获取redis 分布式锁
     * @param lockName
     * @return
     */
    public DistributedRedisLock getRedisLock(String lockName) {
        return new DistributedRedisLock(redisTemplate, lockName, uuid );
    }
}