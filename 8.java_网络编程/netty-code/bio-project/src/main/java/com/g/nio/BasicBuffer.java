package com.g.nio;

import java.nio.IntBuffer;

public class BasicBuffer {

    public static void main(String[] args) {
        //举例说明Buffer的使用（简单说明）
        //创建一个Buffer，大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //循环向buffer存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(10+i);
        }

        //如何从buffer读取数据
        intBuffer.flip(); //将buffer转换，读写切换

        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }

    }
}
