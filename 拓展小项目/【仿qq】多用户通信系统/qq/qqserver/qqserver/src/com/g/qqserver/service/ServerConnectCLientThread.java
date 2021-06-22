package com.g.qqserver.service;

import com.g.common.Message;
import com.g.common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 该类的一个矿象和某个客户端保持通信
 */
public class ServerConnectCLientThread  extends Thread{

    private Socket socket;
    private String userId;//连接到服务端的用户id

    public ServerConnectCLientThread( String userId,Socket socket) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {//这里线程处于run的状态，可以发送/接收消息

        while (true){

            try {
                System.out.println( "服务端和客户端"+userId+"保持通信，读取数据...");
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message msg = (Message)objectInputStream.readObject();

                // todo 后面会使用message，根据message的类型，做相应的业务处理
                if (msg.getMsgType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    //获取用户列表
                    String onlineUser = ManageClientThread.getOnlineUser();

                    //封装返回的消息并发送
                    Message message = new Message();
                    message.setContent(onlineUser);
                    message.setMsgType(MessageType.MESSAGE_RET_ONLINE_FRIEND);

                    //获取服务器与客户端连接的socket
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(message);
                }else if (msg.getMsgType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    //客户端退出
                    System.out.println(msg.getSender() + "退出系统");

                    //将这个客户端对应的线程从集合中删除
                     ManageClientThread.removeServerConnectCLientThread(msg.getSender());
                    //关闭本线程持有的socket
                    socket.close();
                    //退出读取消息的循环
                    break;
                }else if (msg.getMsgType().equals(MessageType.MESSAGE_COMM_MES)){
                    //私聊
                    String getter = msg.getGetter();

                    // 转发到getterID的客户端
                    ////根据message获取getter id，然后在得到对应先线程
                    Socket socket = ManageClientThread.getServerConnectCLientThread(getter).getSocket();
                    //得到对应socket的对象输出流，将message对象转发给指定的客户端
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    //转发，提示如果客户不在线，可以保存到数据库，这样就可以实现离线留言
                    objectOutputStream.writeObject(msg);
                }else if (msg.getMsgType().equals(MessageType.MESSAGE_TO_ALL)){
                    //群聊
                    String sender = msg.getSender();

                    //需要遍历管理线程的集合，把所有的线程的socket得到，然后把message进行转发即可
                    Map<String, ServerConnectCLientThread> all =  ManageClientThread.getAll();
                    Set<Map.Entry<String, ServerConnectCLientThread>> entrySet = all.entrySet();
                    Iterator<Map.Entry<String, ServerConnectCLientThread>> iterator = entrySet.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, ServerConnectCLientThread> next = iterator.next();
                        String key = next.getKey();

                        //不给自己发
                        if (key.equals(sender)){
                            continue;
                        }

                        //获取线程，获取持有的socket，进行消息转发
                        ServerConnectCLientThread value = next.getValue();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(value.getSocket().getOutputStream());
                        objectOutputStream.writeObject(msg);

                    }

                }else if (msg.getMsgType().equals(MessageType.MESSAGE_FILE_MSG)){
                    //私发文件
                    String getter = msg.getGetter();

                    // 转发到getterID的客户端
                    ////根据message获取getter id，然后在得到对应先线程
                    Socket socket = ManageClientThread.getServerConnectCLientThread(getter).getSocket();
                    //得到对应socket的对象输出流，将message对象转发给指定的客户端
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    //转发，提示如果客户不在线，可以保存到数据库，这样就可以实现离线留言
                    objectOutputStream.writeObject(msg);
                }

                else {
                    System.out.println(".............");
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public Socket getSocket() {
        return socket;
    }
}
