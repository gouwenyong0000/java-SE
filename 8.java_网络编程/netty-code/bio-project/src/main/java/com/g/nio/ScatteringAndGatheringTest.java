package com.g.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering：将数据写入到buffer时，可以采用buffer数组，依次写入[分散]
 * Gathering：从buffer读取数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {
        //使用 ServerSocketChannel和SocketChannel网络

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] =  ByteBuffer.allocate(5);
        byteBuffers[1] =  ByteBuffer.allocate(3);

        //等待客户端连接（telnet测试）
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLen = 8;//假定从客户端接收8个字习
        //循环读取
        while (true){
            int byteRead = 0;

            while (byteRead <messageLen){
                long read = socketChannel.read(byteBuffers);
                byteRead += read;//累计读取的字节数
                System.out.println("byteRead = " + byteRead);

                //使用流打印，看看当前的buffer的position和limit   map 映射成新的字符串流，循环打印
                Arrays.asList(byteBuffers).stream().map(it->{
                    return "position = "+ it.position() +"，limit" +it.limit();
                }).forEach(System.out::println);
            }

            //将所有的buffer进行反转
            Arrays.asList(byteBuffers).forEach(it->{ it.flip();  });

            //将数据显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLen){
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;
            }
            //将所有的buffer进行clear
            Arrays.asList(byteBuffers).forEach(it->{ it.clear();  });

            System.out.print ("byteRead = " + byteRead+" byteWrite = " + byteWrite+" messageLen = " + messageLen);

        }

    }
}
