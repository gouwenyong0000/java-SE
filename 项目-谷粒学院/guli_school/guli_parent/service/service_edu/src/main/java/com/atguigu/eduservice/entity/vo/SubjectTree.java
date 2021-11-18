package com.atguigu.eduservice.entity.vo;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class SubjectTree {
    private String id;
    private String label;

    /**
     * 子类 为null时不输出
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SubjectTree> children;
}
