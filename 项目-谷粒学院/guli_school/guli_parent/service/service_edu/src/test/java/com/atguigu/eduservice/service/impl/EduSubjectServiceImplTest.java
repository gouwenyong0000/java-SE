package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.SubjectTree;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import junit.framework.TestCase;


import org.junit.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class EduSubjectServiceImplTest  {

    @Autowired
    EduSubjectService mapper;


    @Test
    public void testTree() {

        List<SubjectTree> treeList = mapper.tree();

        System.out.println(treeList);
    }


}