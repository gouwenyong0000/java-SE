package com.atguigu.eduservice.entity.subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubjectVo {
    private String id;
    @JsonProperty("label")
    private String title;
}
