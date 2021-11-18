package com.atguigu.code.excle;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

//设置表头和添加的数据字段
@Data
public class ReadData {
    //设置表头名称
    @ExcelProperty(value = "学生编号",index = 0)
    private int sno;

    //设置表头名称
    @ExcelProperty(value ="学生姓名", index = 1)
    private String sname;

}



