package com.g.qqclent.service;

import com.g.common.Message;
import com.g.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

/**
 *
 */
public class MessageClientService {
    /**
     * 私聊，发送消息给服务器，服务器进行转发
     *
     * @param userId //发送消息id
     * @param toId
     * @param content
     */
    public void sendMessageToOne(String userId, String toId, String content) {
        //封装发送消息对象
        Message message = new Message();
        message.setSender(userId);
        message.setGetter(toId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        message.setMsgType(MessageType.MESSAGE_COMM_MES);

        //获取通道，将消息发送到服务端
        try {
            Socket socket = ManageClientConnetServerThread
                    .getClientConnectServerThread(userId)
                    .getSocket();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);

            System.out.println(userId + "\t对\t" + toId + "说 : " + content);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void sendMessageToAny(String userId, String content) {
        //封装发送消息对象
        Message message = new Message();
        message.setSender(userId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        message.setMsgType(MessageType.MESSAGE_TO_ALL);

        //获取通道，将消息发送到服务端
        try {
            Socket socket = ManageClientConnetServerThread
                    .getClientConnectServerThread(userId)
                    .getSocket();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);

            System.out.println(userId + "\t对大家说说 : " + content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
