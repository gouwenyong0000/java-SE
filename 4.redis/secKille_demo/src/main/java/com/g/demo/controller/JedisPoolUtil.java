package com.g.demo.controller;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil extends JedisPool {
    private static volatile JedisPool jedisPool = null;

    private JedisPoolUtil() {
    }

    public static JedisPool getJedisPoolInstance() {
        if (null == jedisPool) {
            synchronized (JedisPoolUtil.class) {
                if (null == jedisPool) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(200);
                    poolConfig.setMaxIdle(32);
                    poolConfig.setMaxWaitMillis(100 * 1000);
                    poolConfig.setBlockWhenExhausted(true);
                    poolConfig.setTestOnBorrow(true);  // ping  PONG

                    jedisPool = new JedisPool(poolConfig, "192.168.64.130", 6379, 60000);
                }
            }
        }
        return jedisPool;
    }
/*
  自Jedis3.0版本后jedisPool.returnResource()遭弃用,官方重写了Jedis的close方法用以代替
  @link redis.clients.jedis.Jedis#close()
 * <pre>
 *   @Override
 *   public void close() {
 *     if (dataSource != null) {
 *       JedisPoolAbstract pool = this.dataSource;
 *       this.dataSource = null;
 *       if (client.isBroken()) {
 *         pool.returnBrokenResource(this);
 *       } else {
 *         pool.returnResource(this);
 *       }
 *     } else {
 *       super.close();
 *     }
 *   }
 * </pre>
 */
//    public static void release(JedisPool jedisPool, Jedis jedis) {
//        if (null != jedis) {
//            jedisPool.returnResource(jedis);
//        }
//    }
}
