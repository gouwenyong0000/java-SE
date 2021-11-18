package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.subject.SubjectNestedVo;
import com.atguigu.eduservice.entity.vo.SubjectTree;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author gouwenyong
 * @since 2021-09-25
 */
@Api(description = "课程分类管理")
@RestController
//@CrossOrigin
@RequestMapping("/eduservice/subject")
public class EduSubjectController {

    @Autowired
    EduSubjectService eduSubjectService;


    /**
     * 添加课程分类
     */
    @ApiOperation(value = "Excel批量导入")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file) {

        eduSubjectService.saveSubject(file);
        return R.ok();
    }

    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("tree")
    public R tree() {
//          resultMap 封装的数据
        List<SubjectTree> tree = eduSubjectService.tree();

        List<SubjectNestedVo> subjectNestedVoList = eduSubjectService.nestedList();
        return R.ok().data("getTreeList", subjectNestedVoList);
    }

}

