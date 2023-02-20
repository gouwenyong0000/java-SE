package com.g.nio.c2s;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws IOException {

        //创建ServerSocketChannel-> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到一个Selecor对象
        Selector selector = Selector.open();

        //绑定一个端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到selector关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true) {

            //这里我们等待1s，如果没有事件发生，返回
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }

            //如果返回的>0，就获取到相关的selectionKey集合
            //1，如果返回的>0，表示已经获取到关注的事件
            //通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历Set<Selectionkey，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                //获取到SelectionKey,根据key对应的通道发生的事件做相应处理
                SelectionKey key = keyIterator.next();

                if (key.isAcceptable()) {//如果是OP_ACCEPT，有新的客户端连接
                    //该该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将 SocketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);

                    //将socketChannel注册到selector，关注事件为OP READ，同时给socketChannel
                    //关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                } else if (key.isReadable()) {//发生OP_READ
                    //通过key反向获取到对应channel
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    //获取到该chanel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    socketChannel.read(buffer);
                    System.out.println("from 客户端： " + new String(buffer.array(),0,buffer.position()));
                }
                //手动从集合中移动当前的selectionKey，防止重复操作
                keyIterator.remove();

            }

        }

    }
}
