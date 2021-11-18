package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.val;
import org.aspectj.weaver.ast.Var;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {

    public static void main(String[] args) {
        List<String> ids = new ArrayList<>();

        List<EduVideo> list = getData();
        ids = list.stream().map(it -> {
            return it.getVideoSourceId();
        }).distinct().filter(it -> {
            return it != null;
        }).collect(Collectors.toList());

        System.out.println(ids);
    }

    public static List<EduVideo> getData() {


        List<EduVideo> data=new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            EduVideo video = new EduVideo();
            video.setVideoSourceId(String.valueOf(i % 10));
            data.add(video);
        }


        return data;
    }
}
