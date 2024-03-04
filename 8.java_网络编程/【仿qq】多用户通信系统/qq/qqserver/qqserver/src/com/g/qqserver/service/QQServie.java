package com.g.qqserver.service;

import com.g.common.Message;
import com.g.common.MessageType;
import com.g.common.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这是服务端，监听9999，等待客户端的连接，并保持通讯
 */
public class QQServie {

    private ServerSocket serverSocket;
    ////创建一个集合，存放多个用户，如果是这些用户登录，就认为是合法
    // 这里我们也可以使用ConcurrentHashMap,可以处理并发的集合，没有线程安全
    //HashMap没有处理线程安全，因此在多线程情况下是不安全
//ConcurrentHashMap处理的线程安全，即线程同步处理，在多线程情况下是安全
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();

    static {//在静态代码块，初始化validUsers
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("至尊宝", new User("至尊室", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("菩提老祖", new User("菩提老祖", "123456"));

    }

    public QQServie() {

        try {
            //注意：端口可以写在配置文件
            System.out.println("服务器在9999端口监听");
            serverSocket = new ServerSocket(9999);

            //开启新闻推送线程
            new Thread(new SendNewsToALLService()).start();


            while (true) { //当和某个客户端连接后，会继续监听，因此while

                Socket socket = serverSocket.accept();//如果没有客户端连接，就会阻塞在这里

                //得到socket关联的对象输入流
                InputStream ins = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(ins);
                User client = (User) objectInputStream.readObject();

                //模拟数据库验证用户
                String userId = client.getUserId();
                String passwd = client.getPasswd();
                //创建一个Message对象，准备回复客户端
                Message message = new Message();
                if (checkUserValid(userId, passwd)) {
                    System.out.println("id =" + client.getUserId() + "\tpwd= " + client.getPasswd() + "登陆成功，准备开始读取数据");

                    //验证通过
                    message.setMsgType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectOutputStream outObj = new ObjectOutputStream(outputStream);
                    //将message对象回复客户端
                    outObj.writeObject(message);

                    //创建一个线程，和客户端保持通信，该线程需要持有socket对象
                    ServerConnectCLientThread lientThread = new ServerConnectCLientThread(userId, socket);
                    lientThread.start();//启动该线程

                    //把该线程对象，放入到一个集合中，进行管理.
                    ManageClientThread.addServerConnectCLientThread(userId, lientThread);

                } else {
                    //验证失败

                    message.setMsgType(MessageType.MESSAGE_LOGIN_FAIL);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectOutputStream outObj = new ObjectOutputStream(outputStream);
                    //将message对象回复客户端
                    outObj.writeObject(message);

                    socket.close();
                    System.out.println("id =" + client.getUserId() + "\tpwd= " + client.getPasswd() + "登陆失败");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //如果服务端退出了while循环，说明服务器不在监听，因此需要关闭资源serverSocket
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 验证用户是否有效的方法
     *
     * @param userId
     * @param passwd
     * @return
     */
    private boolean checkUserValid(String userId, String passwd) {
        if (validUsers.containsKey(userId)
                && validUsers.get(userId).getPasswd().equals(passwd)) {
            return true;

        }

        return false;
    }


}
