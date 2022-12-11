package com.atguigu.distributedlock.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_stock")
public class Stock {

    @TableId
    private Long id;

    private String productCode;

    private String warehouse;

    private Integer count;
    private Long version;
}