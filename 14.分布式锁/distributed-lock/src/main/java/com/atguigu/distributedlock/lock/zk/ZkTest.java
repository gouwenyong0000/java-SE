package com.atguigu.distributedlock.lock.zk;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZkTest {

    public static void main(String[] args) {
        ZooKeeper zooKeeper = null;
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            zooKeeper = new ZooKeeper("192.168.64.3:2181", 30000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {

                    if (Event.KeeperState.SyncConnected.equals(event.getState())
                            && Event.EventType.None.equals(event.getType())) {
                        System.out.println("获取链接成功。。。。。。" + event);
                        countDownLatch.countDown();
                    }
//                    else if (Event.KeeperState.Closed.equals(event.getState())) {
//                        System.out.println("关闭连接");
//                    }
                }
            });
            countDownLatch.await();//等待初始化完成
            System.out.println("进入命令操作");

           // String s = zooKeeper.create("/test", "haha~~".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//临时节点

            String nodeName = zooKeeper.create("/test/leave1", "haha~~".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);//临时节点
            System.out.println("nodeName = " + nodeName);

            List<String> children = zooKeeper.getChildren("/locks", false);
            System.out.println("children = " + children);


            Stat stat = zooKeeper.exists("/test", false);//false  表示不监听
            if (stat != null) {
                System.out.println("当前节点存在！" + stat.getVersion());
            } else {
                System.out.println("当前节点不存在！");
            }


            // 获取一个节点的数据  ==get
            byte[] data = zooKeeper.getData("/test", false, null);
            System.out.println(new String(data));

            System.out.println("---------");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

            try {
                if (zooKeeper != null)
                    zooKeeper.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
