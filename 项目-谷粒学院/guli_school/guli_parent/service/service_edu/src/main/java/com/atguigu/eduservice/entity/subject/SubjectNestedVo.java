package com.atguigu.eduservice.entity.subject;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("一级分类")
public class SubjectNestedVo {
    private String id;
    @JsonProperty("label")//属性转换成json时名称
    private String title;
    private List<SubjectVo> children = new ArrayList<>();
}
