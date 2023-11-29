package com.g.qqserver.service;

import com.g.common.Message;
import com.g.common.MessageType;
import com.g.common.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class SendNewsToALLService implements Runnable {
    @Override
    public void run() {
        //为了可以推送多次新闻，使用whiLe
        while (true) {
            System.out.println("请输入需要推送的消息[输入(exit) 退出推送服务]：");
            String ms = Utility.readStr(50);
            if ("exit".equals(ms)){
                break;
            }

            //构建一个消息，群发消息
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(ms);
            message.setSendTime(new Date().toString());
            message.setMsgType(MessageType.MESSAGE_COMM_MES);

            //遍历所有在线用户，发送消息
            Map<String, ServerConnectCLientThread> all = ManageClientThread.getAll();
            Iterator<String> iterator = all.keySet().iterator();
            while (iterator.hasNext()){
                String onlineUserId = iterator.next();
                ServerConnectCLientThread serverConnectCLientThread = all.get(onlineUserId);
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(serverConnectCLientThread.getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
