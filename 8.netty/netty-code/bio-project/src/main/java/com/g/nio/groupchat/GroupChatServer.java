package com.g.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义相关属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private final static int PORT = 6667;

    //构造器，完成初始化工作
    GroupChatServer() {

        try {
            //得到选择器
            selector = Selector.open();
            //ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将该listenchannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //监听代码
    public void listen() {
        try {
            //循环处理
            while (true) {
                int count = selector.select(2000);
                if (count > 0) {//有事件处理
                    //遍历得到selectionkey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        //取出selectionkey
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if (key.isAcceptable()){
                            SocketChannel socketChannel = listenChannel.accept();
                            //设置非阻塞
                            socketChannel.configureBlocking(false);
                            //将该sc注册到seletor
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress()+"上线了");
                        }else if (key.isReadable()){//通道发送read事件，即通道是可读的状态
                            //处理读【专门写方法】
                            readData(key);
                        }

                       //手动从集合中移动当前的selectionkey，防止重复操作
                       iterator.remove();
                    }
                } else {
                    System.out.println("wait........");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取客户端消息
    private void  readData(SelectionKey key){
        SocketChannel channel=null;
        try {
            //取到关联的channe
            channel = (SocketChannel) key.channel();
            //创建缓冲
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int readCount = channel.read(buffer);
            //根据count的值做处理
            if (readCount > 0 ){
                //把缓存区的数据转成字符串
                String msg = new String(buffer.array(), 0, buffer.position());
                //输出消息
                System.out.println("from 客户端： "  +msg);
                //向其他客户端转发消息(去掉自己)【专门写一个方法处理】
                sendInfoToOther(msg,channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress()+"离线了");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }finally {
                //取消注册
                try {
                    channel.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

        }

    }

    //转发消息给其它客户（通道）
    private void sendInfoToOther(String msg, SocketChannel self) throws IOException {

        System.out.println("服务器转发消息中");

        //遍历所有注册到selector上的socketChannel，并排除self
        int sendCount = 0;
        for (SelectionKey key : selector.keys()) {
            //通过key取出通道SocketChannel
           Channel targetChannel = key.channel();
           if (targetChannel instanceof SocketChannel && targetChannel != self){
               //转型
               SocketChannel destChannel = (SocketChannel) targetChannel;
               //将msg存储到buff
               ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
               //将buffer的数据写入通道
               destChannel.write(buffer);
               sendCount++;
           }
        }
        System.out.println("sendCount = " + sendCount);
    }


    public static void main(String[] args) {
        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

}
