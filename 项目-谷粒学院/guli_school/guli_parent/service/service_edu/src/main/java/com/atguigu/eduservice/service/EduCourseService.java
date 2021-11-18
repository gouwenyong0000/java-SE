package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.CoursePublishVo;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.front.vo.CourseQueryVo;
import com.atguigu.eduservice.front.vo.CourseWebVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-09-26
 */
public interface EduCourseService extends IService<EduCourse> {
    /**
     * @param courseInfoVo
     * @return 返回课程id  用于步骤2 、3 保存使用，形成关联
     */
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    /**
     * 根据id 查询课程信息 和 课程描述
     */
    CourseInfoVo info(String id);

    void updateCourse(CourseInfoVo courseInfoVo);

    CoursePublishVo getPublishInfo(String id);

    /**
     * 分页条件查询
     *
     * @param pageNo
     * @param pageSize
     * @param courseQuery
     * @return
     */
    Page<EduCourse> pageList(Long pageNo, Long pageSize, CourseQuery courseQuery);

    /**
     * 删除课程
     */
    boolean removeCourse(String id);

    /**
     * 根据讲师id查询当前讲师的课程列表
     *
     * @param teacherId
     * @return
     */
    List<EduCourse> selectByTeacherId(String teacherId);

    Map<String, Object> pageListWeb(Page<EduCourse> pageParam, CourseQueryVo courseQuery);

    CourseWebVo getCourseInfoById(String id);

}

