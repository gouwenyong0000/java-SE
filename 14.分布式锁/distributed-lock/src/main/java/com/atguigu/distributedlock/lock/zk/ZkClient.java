package com.atguigu.distributedlock.lock.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class ZkClient {

    private static final String connectString = "192.168.64.3:2181";
    private static final String ROOT_PATH = "/locks";
    private ZooKeeper zooKeeper;

    @PostConstruct
    public void init() {
        //项目容器启动时  获取连接
        try {
            // 连接zookeeper服务器
            CountDownLatch countDownLatch = new CountDownLatch(1);
            this.zooKeeper = new ZooKeeper(connectString, 30000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("获取链接成功！！");
                    countDownLatch.countDown();

                }
            });
            countDownLatch.await();//等待连接OK

            // 创建分布式锁的根节点
            if (this.zooKeeper.exists(ROOT_PATH, false) == null) {
                this.zooKeeper.create(ROOT_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("创建根节点ok   【{}】", ROOT_PATH);
            }
        } catch (Exception e) {
            System.out.println("获取链接失败！");
            e.printStackTrace();
        }

    }

    //容器关闭时释放连接
    @PreDestroy
    public void destroy() {
        try {
            if (zooKeeper != null) {
                zooKeeper.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化zk分布式锁对象方法
     *
     * @param lockName
     * @return
     */
    public ZkDistributedLock getZkDistributedLock(String lockName) {
        return new ZkDistributedLock(zooKeeper, ROOT_PATH, lockName);
    }
}