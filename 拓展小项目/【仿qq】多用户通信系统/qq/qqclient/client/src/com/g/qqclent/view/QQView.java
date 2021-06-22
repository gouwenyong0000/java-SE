package com.g.qqclent.view;

import com.g.qqclent.service.FileCLientService;
import com.g.qqclent.service.MessageClientService;
import com.g.qqclent.service.UserClientService;

/**
 * 客户端菜单界面
 */
public class QQView {
    private boolean loop = true;//控制是都显示菜单
    private String key = "";//接受用户的键盘输入
    private UserClientService userClientService = new UserClientService();
    ;
    private MessageClientService messageClientService = new MessageClientService();
    private FileCLientService fileCLientService = new FileCLientService();

    //显示主菜单
    private void mainMenu() {
        while (loop) {
            System.out.println("=============欢迎登录网络通信系统==============");
            System.out.println("\t\t 1. 登录系统");
            System.out.println("\t\t 9. 退出系统");

            System.out.println("请输入你的选择");
            key = Utility.readStr(1);

            //根据用户的输入，来处理不同的逻辑
            switch (key) {
                case "1":
                    System.out.print("请输入用户号:");
                    String userId = Utility.readStr(10);
                    System.out.print("请输入密码:");
                    String passwd = Utility.readStr(10);
                    //todo  1. 登录，需要导服务端判断用户是否合法UserClientService[用户登陆注册]

                    if (userClientService.checkUser(userId, passwd)) {
                        //如果验证成功，进入二级菜单
                        System.out.println("=============欢迎用户(" + userId + ")登陆成功==============");
                        while (loop) {
                            System.out.println("\n=============网络通信二级菜单用户(" + userId + ")==============");
                            System.out.println("\t\t 1显示在线用户列表");
                            System.out.println("\t\t 2群发消息");
                            System.out.println("\t\t 3私聊消息");
                            System.out.println("\t\t 4发送文件");
                            System.out.println("\t\t 9退出系统");

                            System.out.println("请输入你的选择");
                            key = Utility.readStr(1);
                            switch (key) {
                                case "1"://获取用户列表
                                    userClientService.getOnlineUserList();
                                    break;
                                case "2"://2群发消息
                                    System.out.print("请输入群发的内容:");
                                    String content = Utility.readStr(50);
                                    messageClientService.sendMessageToAny(userId, content);
                                    break;
                                case "3"://私聊
                                    System.out.print("请输入一个在线的用户号:");
                                    String toId = Utility.readStr(10);
                                    System.out.print("请输入发送的消息:");
                                    String content1 = Utility.readStr(10);
                                    messageClientService.sendMessageToOne(userId, toId, content1);
                                    break;
                                case "4":// 4发送文件
                                    System.out.print("请输入一个在线的用户号:");
                                    String getter= Utility.readStr(10);
                                    System.out.print("请输入读取文件的目录:");
                                    String src= Utility.readStr(50);
                                    System.out.print("请输入发送到的目录:");
                                    String dest= Utility.readStr(50);
                                    fileCLientService.sendFileToOne(dest,src,userId,getter);
                                    break;
                                case "9"://无异常退出
                                    //调用方法，给服务器发送一个退出系统的mesage
                                    userClientService.exit();
                                    loop = false;
                                    break;
                            }
                        }
                    } else {//如果失败
                        System.out.println("========登录服务器失败=======");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }
        }
    }

    public static void main(String[] args) {
        QQView qqView = new QQView();
        qqView.mainMenu();
        System.out.println("退出系统========");
    }

}
