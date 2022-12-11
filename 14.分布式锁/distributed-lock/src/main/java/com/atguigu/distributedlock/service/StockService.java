package com.atguigu.distributedlock.service;

import com.atguigu.distributedlock.pojo.Lock;
import com.atguigu.distributedlock.lock.redis.DistributedLockClient;
import com.atguigu.distributedlock.lock.redis.DistributedRedisLock;
import com.atguigu.distributedlock.mapper.LockMapper;
import com.atguigu.distributedlock.mapper.StockMapper;
import com.atguigu.distributedlock.pojo.Stock;
import com.atguigu.distributedlock.lock.zk.ZkClient;
import com.atguigu.distributedlock.lock.zk.ZkDistributedLock;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class StockService {


    @Resource
    private StockMapper stockMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 一行sql
     */
    public void deduct0() {
        stockMapper.updateCount("1001", 1);
    }

    /**
     * 悲观锁  事务 + for update查询
     * begin ;
     * select * from tb_stock where product_code="1001" for update ; --加行锁
     * update tb_stock set count = count-1 where product_code="1001";
     * commit ;
     */
    @Transactional(rollbackFor = Exception.class)
    public void deduct1() {
        //查询库存是否充足
        Stock stock1 = stockMapper.queryStockForUpdate("1001");

        if (stock1 != null && stock1.getCount() > 0) {
            stock1.setCount(stock1.getCount() - 1);
            stockMapper.updateById(stock1);
        }

    }


    /**
     * 乐观锁
     */
    public void checkAndLock() {

        // 先查询库存是否充足
        Stock stock = this.stockMapper.selectById(1L);

        // 再减库存
        if (stock != null && stock.getCount() > 0) {
            // 获取版本号
            Long version = stock.getVersion();

            stock.setCount(stock.getCount() - 1);
            // 每次更新 版本号 + 1
            stock.setVersion(stock.getVersion() + 1);
            // 更新之前先判断是否是之前查询的那个版本，如果不是重试
            if (this.stockMapper.update(stock, new UpdateWrapper<Stock>().eq("id", stock.getId()).eq("version", version)) == 0) {
                checkAndLock();
            }
        }
    }

    /**
     * redis 超卖
     */
    public void redisLock() {


        // 先查询库存
        String stock = redisTemplate.opsForValue().get("stock");
        System.out.println("stock = " + stock);
        //是否充足
        if (stock != null && stock.length() != 0) {
            int count = Integer.parseInt(stock);
            if (count > 0) {
                //再减库存
                redisTemplate.opsForValue().set("stock", String.valueOf(--count));

            }
        }
        //可以优化
        // redisTemplate.opsForValue().decrement("stock")

    }

    /**
     * redis乐观锁
     * <pre>
     * watch stock  # 监听key
     * multi
     * set stock 5000
     * exec
     * </pre>
     */
    public void redisLock1() {

        redisTemplate.execute(new SessionCallback<Object>() {

            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String key = "stock";
                redisOperations.watch(key);//监听key

                // 先查询库存
                String stock = (String) redisOperations.opsForValue().get(key);
                System.out.println("stock = " + stock);
                //是否充足
                if (stock != null && stock.length() != 0) {
                    int count = Integer.parseInt(stock);
                    if (count > 0) {
                        redisOperations.multi();//开启事务
                        //再减库存
                        redisOperations.opsForValue().set(key, String.valueOf(--count));

                        List exec = redisOperations.exec();//提交

                        if (exec == null || exec.size() == 0) {//判断结果重试
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            redisLock1();
                        }
                        return exec;
                    }
                }

                return null;
            }
        });

    }

    /**
     * Redis   分布式锁  简单版 递归版
     */
    public void deductRecursion() {
        // 加锁setnx
        Boolean lock = this.redisTemplate.opsForValue().setIfAbsent("lock", "111");
        // 重试：递归调用
        if (!lock) {
            try {
                Thread.sleep(50);
                this.deduct();//递归
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // 1. 查询库存信息
                String stock = redisTemplate.opsForValue().get("stock").toString();

                // 2. 判断库存是否充足
                if (stock != null && stock.length() != 0) {
                    Integer st = Integer.valueOf(stock);
                    if (st > 0) {
                        // 3.扣减库存
                        redisTemplate.opsForValue().set("stock", String.valueOf(--st));
                    }
                }
            } finally {
                // 解锁
                this.redisTemplate.delete("lock");
            }
        }
    }

    /**
     * Redis   分布式锁  简单版 循环版本
     * 1、服务异常挂  --> 锁超时自动失效
     * 2、A业务超时 ，锁自动失效后被其他人B获取，业务结束时，A释放B的锁
     * -->  解铃还须系铃人  UUID 表示锁持有者
     * --> 自动续期
     */
    public void deduct() {
        String uuid = UUID.randomUUID().toString();//解铃还须系铃人
        // 加锁setnx
        while (!this.redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS)) {
            // 重试：循环
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            // this.redisTemplate.expire("lock", 3, TimeUnit.SECONDS);
            // 1. 查询库存信息
            String stock = redisTemplate.opsForValue().get("stock").toString();

            // 2. 判断库存是否充足
            if (stock != null && stock.length() != 0) {
                Integer st = Integer.valueOf(stock);
                if (st > 0) {
                    // 3.扣减库存
                    redisTemplate.opsForValue().set("stock", String.valueOf(--st));
                }
            }
        } finally {
            // 先判断是否自己的锁，再解锁
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                    "then " +
                    "   return redis.call('del', KEYS[1]) " +
                    "else " +
                    "   return 0 " +
                    "end";
            this.redisTemplate.execute(
                    new DefaultRedisScript<>(script, Boolean.class),
                    Arrays.asList("lock"),
                    uuid
            );
        }
    }

    /**
     * redis 支持可重入锁
     */
    @Autowired
    DistributedLockClient lockClient;

    public void redisReentrantLock() {

        DistributedRedisLock lock = lockClient.getRedisLock("lock");
        lock.lock();
        try {
            // this.redisTemplate.expire("lock", 3, TimeUnit.SECONDS);
            // 1. 查询库存信息
            String stock = redisTemplate.opsForValue().get("stock").toString();

            // 2. 判断库存是否充足
            if (stock != null && stock.length() != 0) {
                Integer st = Integer.valueOf(stock);
                if (st > 0) {
                    // 3.扣减库存
                    redisTemplate.opsForValue().set("stock", String.valueOf(--st));
                }
            }
            this.test();//测试可重入锁
        } finally {
            lock.unlock();
        }
    }

    public void test() {
        DistributedRedisLock lock = lockClient.getRedisLock("lock");
        lock.lock();
        System.out.println("测试课重入锁");
        lock.unlock();

    }

    @Autowired
    private RedissonClient redissonClient;

    public void redissonReentrantLock() {
        RLock lock = redissonClient.getLock("lock");

        lock.lock();
        try {
            // this.redisTemplate.expire("lock", 3, TimeUnit.SECONDS);
            // 1. 查询库存信息
            String stock = redisTemplate.opsForValue().get("stock").toString();

            // 2. 判断库存是否充足
            if (stock != null && stock.length() != 0) {
                Integer st = Integer.valueOf(stock);
                if (st > 0) {
                    // 3.扣减库存
                    redisTemplate.opsForValue().set("stock", String.valueOf(--st));
                }
            }
            TimeUnit.SECONDS.sleep(1000);
            test1();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 测试Redisson 重入锁
     */
    public void test1() {
        RLock lock = redissonClient.getLock("lock");
        lock.lock();
        System.out.println("测试课重入锁");
        lock.unlock();

    }


    public void testSemaphore() {
        RSemaphore semaphore = this.redissonClient.getSemaphore("semaphore");
        semaphore.trySetPermits(3);
        try {
            semaphore.acquire();

            TimeUnit.SECONDS.sleep(5);
            System.out.println(System.currentTimeMillis());

            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Autowired
    ZkClient zkClient;

    public void zkReentrantLock() {

        ZkDistributedLock lock = zkClient.getZkDistributedLock("lock");
        lock.lock();
        try {

            // 1. 查询库存信息
            String stock = redisTemplate.opsForValue().get("stock").toString();

            // 2. 判断库存是否充足
            if (stock != null && stock.length() != 0) {
                Integer st = Integer.valueOf(stock);
                if (st > 0) {
                    // 3.扣减库存
                    redisTemplate.opsForValue().set("stock", String.valueOf(--st));
                }
            }

            test2();

        } finally {
            lock.unlock();
        }
    }

    /**
     * 测试ZK重入锁
     */
    public void test2() {
        ZkDistributedLock lock = zkClient.getZkDistributedLock("lock");
        lock.lock();
        System.out.println("测试课重入锁");
        lock.unlock();

    }

    @Autowired
    private CuratorFramework curatorFramework;

    public void checkAndLockCuratorFramework() {
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework, "/curator/lock");
        try {
            // 加锁
            mutex.acquire();

            // 先查询库存是否充足
            Stock stock = this.stockMapper.selectById(1L);
            // 再减库存
            if (stock != null && stock.getCount() > 0) {
                stock.setCount(stock.getCount() - 1);
                this.stockMapper.updateById(stock);
            }

            // this.testSub(mutex);

            // 释放锁
            mutex.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testSub(InterProcessMutex mutex) {

        try {
            mutex.acquire();
            System.out.println("测试可重入锁。。。。");
            mutex.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * mysql 分布式锁
     */
    @Autowired
    LockMapper lockMapper;

    /**
     * 数据库分布式锁
     */
    public void checkAndLockmysql() {

        // 加锁
        Lock lock = new Lock(null, "lock", this.getClass().getName(), "checkAndLockmysql()",null,null,new Date(), null);
        try {
            this.lockMapper.insert(lock);
        } catch (Exception ex) {
            // 获取锁失败，则重试
            try {
                Thread.sleep(50);
               this.checkAndLockmysql();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 先查询库存是否充足
        Stock stock = this.stockMapper.selectById(1L);

        // 再减库存
        if (stock != null && stock.getCount() > 0) {

            stock.setCount(stock.getCount() - 1);
            this.stockMapper.updateById(stock);
        }

        // 释放锁
        this.lockMapper.deleteById(lock.getId());
    }


}
