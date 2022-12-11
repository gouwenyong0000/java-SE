package com.atguigu.distributedlock.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_lock")
public class Lock {

    private Long id;
    private String lockName;
    private String className;
    private String methodName;
    private String serverName;
    private String threadName;
    private Date createTime;
    private String desc;
}