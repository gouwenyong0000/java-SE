package com.atguigu.practice.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EmployRespVo {

    private Long id;
    private String name;
    private String email;
    private String gender;
    private String address;
    private BigDecimal salary;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birth;
}
