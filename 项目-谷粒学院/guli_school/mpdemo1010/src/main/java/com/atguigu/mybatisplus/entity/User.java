package com.atguigu.mybatisplus.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

@Data
public class User {
    @TableId(type = IdType.AUTO)//数据库自增主键
//    @TableId(type=IdType.ID_WORKER)//MP默认  雪花算法
    private Long id;
    private String name;
    private Integer age;
    private String email;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //@TableField(fill = FieldFill.UPDATE)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)//为了第一次填充默认值
    private Integer deleted;




}
