package com.atguigu.distributedlock.lock.redis;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;


import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


public class DistributedRedisLock implements Lock {

    private String lockName;//锁名称
    private String failed;//服务id
    private StringRedisTemplate redisTemplate;//redis客户端
    private long expire = 30;//默认过期时间

    public DistributedRedisLock(StringRedisTemplate redisTemplate, String lockName, String uid) {
        this.lockName = lockName;
        this.redisTemplate = redisTemplate;
        this.failed = uid  + ":" + Thread.currentThread().getId();// 服务uid + 线程id 实现唯一标识
    }

    @Override
    public void lock() {
        this.tryLock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            this.tryLock(-1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 加锁方法
     *
     * @param time the maximum time to wait for the lock
     * @param unit the time unit of the {@code time} argument
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

        if (time != -1) {
            this.expire = unit.toSeconds(time);
        }
        String script = "if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 " +
                "then " +
                "   redis.call('hincrby', KEYS[1], ARGV[1], 1) " +
                "   redis.call('expire', KEYS[1], ARGV[2]) " +
                "   return 1 " +
                "else " +
                "   return 0 " +
                "end";
        while (!this.redisTemplate.execute(
                new DefaultRedisScript<>(script, Boolean.class),
                Arrays.asList(lockName),
                failed,
                String.valueOf(expire))) {

            Thread.sleep(50);
        }
        return true;
    }

    /**
     * 解锁
     */
    public void unlock() {
        String script = "if redis.call('hexists', KEYS[1], ARGV[1]) == 0 " +
                "then " +
                "   return nil " +
                "elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0 " +
                "then " +
                "   return redis.call('del', KEYS[1]) " +
                "else " +
                "   return 0 " +
                "end";
        Long flag = this.redisTemplate.execute(

                /*
                 * @link(org.springframework.data.redis.connection.ReturnType)
                 * 如果使用Boolean接受返回值,  nil 和 0 不能区分，此处使用LONG,
                 */
                new DefaultRedisScript<>(script, Long.class),
                Arrays.asList(lockName),
                failed
        );
        if (flag == null) {
            throw new IllegalMonitorStateException("this lock doesn't belong to you!");
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    /**
     * 给线程拼接唯一标识
     * 拼接uid 是为了防止分布式环境中，其他服务有相同的线程id
     * todo：因为自动续期DistributedRedisLock#renewExpire()  启动新线程，续期每次获取的getId都不一样需要将入口放到构造函数中
     *
     * @return
     */
//    String getId() {
//        return failed + ":" + Thread.currentThread().getId();
//    }

    //自动续期
    private void renewExpire() {
        String script = "if redis.call('hexists', KEYS[1], ARGV[1]) == 1 " +
                "then " +
                "   return redis.call('expire', KEYS[1], ARGV[2]) " +
                "else " +
                "   return 0 " +
                "end";
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Arrays.asList(lockName), failed, String.valueOf(expire))) {
                    renewExpire();
                }
            }
        }, this.expire * 1000 / 3);
    }
}
