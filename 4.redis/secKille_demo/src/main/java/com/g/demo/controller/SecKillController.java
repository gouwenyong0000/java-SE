package com.g.demo.controller;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class SecKillController {
    /**
     * 返回秒杀页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        String userid = UUID.randomUUID().toString();
        request.setAttribute("userid", userid);
        System.out.println(userid);
        return "index";
    }

    /**
     * 秒杀过程
     *
     * @return
     */
    @PostMapping("/Seckill/doseckill")
    @ResponseBody
    public boolean doseckill(String prodid, String userid) {

        userid = UUID.randomUUID().toString();
        //1 uid和prodid非空判断
        if (Strings.isBlank(prodid) || Strings.isBlank(userid)) {
            return false;
        }
        //调用lua脚本的实现【使用连接池】
        try {
            return SecKill_redisByScript.doSecKill(userid, prodid);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        /*

        //2 连接redis
//        Jedis jedis = new Jedis("192.168.64.130", 6379);

        //替换为连接池
        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
        Jedis jedis = jedisPool.getResource();

        //3 拼接key
        // 3.1 库存key
        String kcKey = "sk:" + prodid + ":qt";
        // 3.2 秒杀成功用户key
        String userKey = "sk:" + prodid + ":user";

        //监视库存
        jedis.watch(kcKey);

        //4 获取库存，如果库存null，秒杀还没有开始
        if (jedis.get(kcKey) == null) {
            System.out.println("秒杀还没有开始，请等待");
            jedis.close();
            return false;
        }

        // 5 判断用户是否重复秒杀操作
        if (jedis.sismember(userKey, userid)) {
            System.out.println("已经秒杀成功了，不能重复秒杀");
            jedis.close();
            return false;
        }

        //6 判断如果商品数量，库存数量小于1，秒杀结束
        if (Integer.parseInt(jedis.get(kcKey)) < 1) {
            System.out.println("秒杀已经结束了");
            jedis.close();
            return false;
        }

        //7 秒杀过程
        //使用事务
        Transaction multi = jedis.multi();

        //组队操作
        multi.decr(kcKey);
        multi.sadd(userKey,userid);

        //执行
        List<Object> results = multi.exec();

        if(results == null || results.size()==0) {
            System.out.println("秒杀失败了....");
            jedis.close();
            return false;
        }

//        //7.1 库存-1
//        jedis.decr(kcKey);
//        //7.2 把秒杀成功用户添加清单里面
//        jedis.sadd(userKey, userid);

        jedis.close();

         */
    }

}
