package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.CoursePublishVo;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author gouwenyong
 * @since 2021-09-26
 */
@RestController
//@CrossOrigin
@RequestMapping("/eduservice/course")
public class EduCourseController {

    @Autowired
    EduCourseService courseService;

    /**
     * 课程列表接口
     */
    @PostMapping("query/{pageNo}/{pageSize}")
    public R listCourse(@PathVariable Long pageNo, @PathVariable Long pageSize, @RequestBody(required = false) CourseQuery courseQuery) {

        Page<EduCourse> page = courseService.pageList(pageNo, pageSize, courseQuery);

        return R.ok().data("total", page.getTotal())
                .data("rows", page.getRecords());
    }

    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("id", id);
    }

    @GetMapping("/{id}")
    public R getCourse(@PathVariable String id) {
        CourseInfoVo courseInfoVo = courseService.info(id);
        return R.ok().data("data", courseInfoVo);
    }

    @ApiOperation("更新课程信息")
    @PutMapping("/updateCourse")
    public R updateCourse(@RequestBody CourseInfoVo courseInfoVo) {
        courseService.updateCourse(courseInfoVo);
        return R.ok();
    }

    @ApiOperation("根据id 获取课程发布信息")
    @GetMapping("getPublishInfo/{id}")
    public R getPublishInfo(@PathVariable String id) {

        CoursePublishVo publishInfo = courseService.getPublishInfo(id);

        return R.ok().data("data", publishInfo);
    }

    @PostMapping("/publish/{id}")
    public R publish(@PathVariable String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");//设置课程状态为已发布
        boolean b = courseService.updateById(eduCourse);
        return R.ok();
    }

    /**
     * 删除课程时  需要删除小节  章节   课程
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public R deleteCourseById(@PathVariable String id) {

         courseService.removeCourse(id);
        return R.ok();
    }


}

