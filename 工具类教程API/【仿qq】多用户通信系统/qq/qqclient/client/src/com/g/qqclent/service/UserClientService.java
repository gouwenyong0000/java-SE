package com.g.qqclent.service;

import com.g.common.Message;
import com.g.common.MessageType;
import com.g.common.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 该类完成用户登陆验证和注册等功能
 */
public class UserClientService {
    //因为可能在其他地方使用user信息。因此做成成员属性
    private User user = new User();

    private Socket socket;


    /**
     * 根据userId和pwd到服务器验证该用户是否合法
     *
     * @param userId
     * @param pwd
     * @return
     */
    public boolean checkUser(String userId, String pwd) {

        user.setUserId(userId);
        user.setPasswd(pwd);
        try {
            //连接到服务器
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            OutputStream out = socket.getOutputStream();

            //发送user对象
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(user);

            //读取服务器回复的MessAge对象
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Message msg = (Message) objectInputStream.readObject();

            if (MessageType.MESSAGE_LOGIN_SUCCEED.equals(msg.getMsgType())) {
                //创建一个和服务器端保持通信的线程 ->创建一个类ClientConnectServerThread  保存socket和循环读取服务器返回信息
                ClientConnectServerThread ccst = new ClientConnectServerThread(socket);
                ccst.start();//启动客户端的线程

                //这里为了后面客户端的扩展，我们将线程放入到集合管理
                ManageClientConnetServerThread.addClientConnectServerThread(userId, ccst);
                return true;

            } else {
                //如果登陆失败，我们就不能启动和服务器通信的线程，关闭socket
                socket.close();
                return false;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 向服务器端请求在线用户列表----发送
     */
    public void getOnlineUserList() {
        //发送一个Message，类型MESSAGE_GET-ONLINE-FRIEND
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserId());

        try {
            //从管理线程的集合中，通过userId，获取到缓存的的连接服务器的线程对象，该对象持有socket
            Socket socket = ManageClientConnetServerThread.getClientConnectServerThread(user.getUserId()).getSocket();

            //发送给服务器
            //应该得到当前线程的Socket对应的Objectoutputstream对象
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);

            /** 接受返回的消息在{@link ClientConnectServerThread#getOnlineUserList(com.g.common.Message)}处理，
             * 即发送请求和处理返回值异步处理
             * */

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 退出客户端，并给服务端发送一个退出系统的message对象
     */
    public void exit() {

        //发送一个Message，类型MESSAGE_GET-ONLINE-FRIEND
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserId());

        try {
            //从管理线程的集合中，通过userId，获取到缓存的的连接服务器的线程对象，该对象持有socket
            Socket socket = ManageClientConnetServerThread.getClientConnectServerThread(user.getUserId()).getSocket();

            //发送给服务器
            //应该得到当前线程的Socket对应的Objectoutputstream对象
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);

            System.out.println(user.getUserId() + "退出系统");

            //结束进程
            System.exit(0);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
