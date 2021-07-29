package com.g.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {

    //线程池机制
//思路
//1.创建一个线程池
//2．如果有客户端连接，就创建一个线程，与之通讯（单独写一个方法）

    public static void main(String[] args) throws IOException {
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(6666);
        System.out.println("服务器启动...");
        while (true) {
            System.out.println("线程信息" + Thread.currentThread().getName() + "\t" + Thread.currentThread().getId());
            //监听等待客户端连接
            System.out.println("等待连接...");//检测accept是阻塞的
            Socket socket = server.accept();
            System.out.println("连接到一个客户端...");
            //创建一个线程，与之通讯
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    handle(socket);
                }
            });

        }
    }

    //编写一个handler方法，和客户端通讯
    public static void handle(Socket socket) {
        System.out.println("线程信息" + Thread.currentThread().getName() + "\t" + Thread.currentThread().getId());
        byte[] bytes = new byte[1024];
        //通过socket获取输入流，
        try {
            InputStream inputStream = socket.getInputStream();
            int len = 0;
            while (true) {
                System.out.println("read...");//检测read是阻塞的
                len = inputStream.read(bytes);
                if (len!= -1) {
                    String s = new String(bytes, 0, len);
                    System.out.println(s);
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("关闭client连接");
        }

    }
}
