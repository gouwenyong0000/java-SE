package com.atguigu.code.excle;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    public static void main(String[] args) throws Exception {
      EasyExcel.read("d:\\1.xlsx",ReadData.class,new ExcelListener() ).sheet().doRead();
    }




    private static void writeExcel() {
        // 写法2，方法二需要手动关闭流
        String fileName = "F:\\112.xlsx";
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = EasyExcel.write(fileName, ReadData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("写入方法二").build();
        excelWriter.write(data(), writeSheet);
        /// 千万别忘记finish 会帮忙关闭流
        excelWriter.finish();
    }


    //循环设置要添加的数据，最终封装到list集合中
    private static List<ReadData> data() {
        List<ReadData> list = new ArrayList<ReadData>();
        for (int i = 0; i < 10; i++) {
            ReadData data = new ReadData();
            data.setSno(i);
            data.setSname("张三" + i);
            list.add(data);
        }
        return list;
    }

}
