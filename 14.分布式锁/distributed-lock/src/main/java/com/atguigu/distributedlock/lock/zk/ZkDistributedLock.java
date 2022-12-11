package com.atguigu.distributedlock.lock.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Slf4j
public class ZkDistributedLock {
    private ZooKeeper zooKeeper;
    private String lockName;
    private String ROOT_PATH;
    private String nodepath;//临时序列化节点名称。防止误删除

    private static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();//重入标记

    public ZkDistributedLock(ZooKeeper zooKeeper, String rootPath, String lockName) {
        this.zooKeeper = zooKeeper;
        this.ROOT_PATH = rootPath;
        this.lockName = lockName + "-";
    }


    public void lock() {
        try {
            //先判断THREAD_LOCAL是否有锁
            Integer flag = THREAD_LOCAL.get();
            if (flag != null && flag > 0) { //直接重入
                THREAD_LOCAL.set(flag + 1);
                return;
            }

            //创建临时序列化节点
            nodepath = ROOT_PATH + "/" + lockName;//   临时节点名： /locks/lock-
            nodepath = zooKeeper.create(nodepath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("当前创建节点{}", nodepath);//   当前创建节点/locks/lock-0000019514

            //查看是否有序列号比当前序列号小1的节点
            String preNode = getPreNode(nodepath);//lock-0000014876
            log.info("{} ==> {}", preNode, nodepath);//lock-0000019513 ==> /locks/lock-0000019514

            if (StringUtils.isEmpty(preNode)) { //不存在前驱结点，直接获取锁成功
                THREAD_LOCAL.set(1);//设置重入次数
                return;
            } else {
                //阻塞重点代码： getPreNode不具有原子性，需要再次判断是否存在   原子：【判断+监听】
                CountDownLatch downLatch = new CountDownLatch(1);
                Stat preNodeExists = zooKeeper.exists(ROOT_PATH + "/" + preNode, new Watcher() {//
                    @Override
                    public void process(WatchedEvent event) {
                        //节点路径：/locks/lock-0000019506  事件类型NodeDeleted 节点状态SyncConnected  回调
                        log.info("节点路径：{}  事件类型{} 节点状态{}  回调", event.getPath(), event.getType(), event.getState());
                        downLatch.countDown();
                    }
                });
                if (preNodeExists == null) {
                    THREAD_LOCAL.set(1);//设置重入次数
                    return;
                } else {
                    downLatch.await(); //阻塞
                    log.info("获取锁{}", nodepath);
                    THREAD_LOCAL.set(1);//设置重入次数
                    return;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    /**
     * 获取指定节点的前节点
     *
     * @param nodePath
     * @return
     */
    private String getPreNode(String nodePath) {

        try {
            //获取跟目录下所有子节点
            List<String> children = zooKeeper.getChildren(ROOT_PATH, false);
            if (CollectionUtils.isEmpty(children)) {
                throw new IllegalMonitorStateException("非法操作");
            }


            //获取当前节点同一资源的锁 ,根据锁名称过滤
            // [locktest-0000014879, locktest-0000014878, lock-0000014880, lock-0000014883, lock-0000014882, lock-0000014881]
            // [ lock-0000014880, lock-0000014883, lock-0000014882, lock-0000014881]
            List<String> nodes = children.stream()
                    .filter(s -> StringUtils.startsWithIgnoreCase(s, lockName))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(nodes)) {
                throw new IllegalMonitorStateException("非法操作");
            }

            //排序
            Collections.sort(nodes);
            //二分查找到当前节点下标  /locks/lock-0000000005   ==> lock-0000000005
            int indexCur = Collections.binarySearch(nodes, nodePath.substring(nodePath.lastIndexOf("/") + 1));

            // 负数表示没找到
            if (indexCur < 0) {
                throw new IllegalMonitorStateException("非法操作");
            } else if (indexCur > 0) {
                return nodes.get(indexCur - 1);
            }
            //如果当前节点是第一个节点返回null
            return null;


        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }


    public void unlock() {

        try {
            THREAD_LOCAL.set(THREAD_LOCAL.get() - 1);
            if (THREAD_LOCAL.get() == 0) {
                zooKeeper.delete(nodepath, -1);
                log.info("释放锁{}", nodepath);
            }

        } catch (InterruptedException | KeeperException e) {
            throw new RuntimeException(e);
        }
    }


}
