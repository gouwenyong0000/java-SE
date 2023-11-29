package com.g.qqclent.service;

import com.g.common.Message;
import com.g.common.MessageType;

import java.io.*;
import java.net.Socket;

/**
 * 保存socket和循环读取服务器返回信息
 */
public class ClientConnectServerThread extends Thread {
    //该线程需要持有Socket
    private Socket socket;


    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        //因为Thread需要在后台和服务器通信，因此我们while循

        while (true) {
            System.out.println("客户端线程，等待服务器端发送的消息");
            try {
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                //如果服务器没有发送Message对象，线程会阻塞在这里
                Message msg = (Message) objectInputStream.readObject();
                //todo 处理返回消息
                //判断这个message类型，然后做相应的业务处理
                if (msg.getMsgType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    //处理服务器返回的用户列表
                    getOnlineUserList(msg);

                }else if (msg.getMsgType().equals(MessageType.MESSAGE_COMM_MES)){
                    //处理服务器转发的私聊消息,把从服务器转发的消息，显示到控制台即可
                    System.out.println(String.format("\n%s\t收到\t%s\t :%s" ,
                                    msg.getSendTime(),
                                    msg.getSender(),
                                    msg.getContent()
                            )
                    );
                }else if (msg.getMsgType().equals(MessageType.MESSAGE_TO_ALL)){
                    //处理群发的消息
                    System.out.println(String.format("\n%s\t %s\t对大家说\t :%s" ,
                            msg.getSendTime(),
                            msg.getSender(),
                            msg.getContent()
                            )
                    );
                }else if (msg.getMsgType().equals(MessageType.MESSAGE_FILE_MSG)){//处理服务器转发的私发文件
                    byte[] fileBytes = msg.getFileBytes();
                    String dest = msg.getDest();

                    File file = new File(dest);
                    if (!file.exists()){
                        file.createNewFile();
                    }

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(fileBytes);
                    fileOutputStream.close();

                    System.out.println("\n收到"  +msg.getSender() +
                            " 发送文件 ：" + msg.getSrc() + "-->" + dest);

                }

                else {
                    System.out.println("是其他类型的message，暂时不处理....");
                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 处理类型MessageType.MESSAGE_RET_ONLINE_FRIEND的消息
     * @param msg
     */
    private void getOnlineUserList(Message msg) {
        //如果是读取到的是服务端返回的在线用户列表
        String content = msg.getContent();
        String[] onlineUsers = content.split(" ");
        System.out.println("============当前在线用户列表============");
        for (String onlineUser : onlineUsers) {
            System.out.println(onlineUser + "\t\tonline ");
        }
    }

    //为了更方便的得到Socket
    public Socket getSocket() {
        return socket;
    }


}
