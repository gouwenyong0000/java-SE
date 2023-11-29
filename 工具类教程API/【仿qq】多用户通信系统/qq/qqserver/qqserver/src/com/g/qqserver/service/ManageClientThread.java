package com.g.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 该类用于管理和客户端通信的线程
 */
public class ManageClientThread {

    private static Map<String, ServerConnectCLientThread> map = new HashMap<>();

    /**
     * 添加线程对象到map集合
     *
     * @param userid
     * @param thread
     */
    public static void addServerConnectCLientThread(String userid, ServerConnectCLientThread thread) {
        map.put(userid, thread);
    }

    /**
     * 根据userId返回ServerConnectclientThread线程
     */
    public static ServerConnectCLientThread getServerConnectCLientThread(String userid) {
        return map.get(userid);
    }

    /**
     * 返回用户列表
     */
    public static String getOnlineUser() {
        Set<String> strings = map.keySet();
        Iterator<String> it = strings.iterator();
        String result = "";

        while (it.hasNext()) {
            String userid = it.next();
            result = result + " " + userid;
        }
        return result;
    }

    /**
     * 从集合中，移除某个线程对象
     */
    public static void removeServerConnectCLientThread(String sender) {
        map.remove(sender);
    }


    public static Map<String, ServerConnectCLientThread> getAll() {
        return map;
    }
}
