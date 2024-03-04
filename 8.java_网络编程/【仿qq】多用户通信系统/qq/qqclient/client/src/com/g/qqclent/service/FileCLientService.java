package com.g.qqclent.service;

import com.g.common.Message;
import com.g.common.MessageType;

import java.io.*;
import java.net.Socket;

/**
 * 该类/对象完成文件传输服务
 */
public class FileCLientService {

    public void sendFileToOne(String dest, String src, String senderID, String getterID) {
        //读取src文件--> message
        Message message = new Message();
        message.setSender(senderID);
        message.setGetter(getterID);
        message.setMsgType(MessageType.MESSAGE_FILE_MSG);
        message.setDest(dest);
        message.setSrc(src);

        //读取文件
        File srcFile = new File(src);
        byte[] fileBytes = new byte[(int) srcFile.length()];

         FileInputStream fileInputStream = null;

        try {
            fileInputStream=new FileInputStream(srcFile);
            fileInputStream.read(fileBytes);
            message.setFileBytes(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关流
            if (fileInputStream!=null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //提示信息
        System.out.println("\n"  +senderID + "  给  " +getterID + " 发送文件 ：" + src + "-->" + dest);

        //发送
        Socket socket = ManageClientConnetServerThread.getClientConnectServerThread(senderID).getSocket();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
