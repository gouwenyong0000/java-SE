package com.g.qqclent.service;

import java.util.HashMap;

/**
 * 管理客户端连接到服务器端的线程的类
 * <p>
 * 每一个线程持有一个socket
 */
public class ManageClientConnetServerThread {
    //我们把多个线程放入一个HashMap集合，key就是用户id，value就是线和
    private static HashMap<String, ClientConnectServerThread> map = new HashMap<>();

    /**
     * 将某个线程加入到集合
     */
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread thread) {
        map.put(userId, thread);
    }

    /**
     * 通过userid可以得到对应线程
     */
    public static ClientConnectServerThread getClientConnectServerThread(String userId) {
        return map.get(userId);
    }


}
