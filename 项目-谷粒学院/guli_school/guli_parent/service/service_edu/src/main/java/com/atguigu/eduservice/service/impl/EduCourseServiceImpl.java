package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.*;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.front.vo.CourseQueryVo;
import com.atguigu.eduservice.front.vo.CourseWebVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.config.exception.define.GuliException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-09-26
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {


    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;
    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VodClient vodClient;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {

        //课程表中添加基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse);

        if (insert <= 0) {
            throw new GuliException(20001, "添加课程信息失败");
        }

        //添加课程简介
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescription.setId(eduCourse.getId());
        boolean save = eduCourseDescriptionService.save(eduCourseDescription);

        return eduCourse.getId();
    }

    @Override
    public CourseInfoVo info(String id) {
        EduCourse course = baseMapper.selectById(id);
        EduCourseDescription description = eduCourseDescriptionService.getById(id);

        CourseInfoVo courseInfoVo = new CourseInfoVo();

        BeanUtils.copyProperties(course, courseInfoVo);
        BeanUtils.copyProperties(description, courseInfoVo);

        return courseInfoVo;
    }

    @Override
    public void updateCourse(CourseInfoVo courseInfoVo) {

        EduCourse course = new EduCourse();
        EduCourseDescription description = new EduCourseDescription();

        BeanUtils.copyProperties(courseInfoVo, course);
        BeanUtils.copyProperties(courseInfoVo, description);

        int i = baseMapper.updateById(course);

        if (i == 0) {
            throw new GuliException(20001, "修改课程信息失败");
        }
        boolean b = eduCourseDescriptionService.updateById(description);
        if (!b) {
            throw new GuliException(20001, "修改课程描述信息失败");
        }
    }

    @Override
    public CoursePublishVo getPublishInfo(String id) {
        return baseMapper.getPublish(id);
    }

    @Override
    public Page<EduCourse> pageList(Long pageNo, Long pageSize, CourseQuery courseQuery) {
        Page<EduCourse> pageParam = new Page<>(pageNo, pageSize);

        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        if (courseQuery == null) {
            baseMapper.selectPage(pageParam, queryWrapper);
            return pageParam;
        }
        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            queryWrapper.eq("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.ge("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.ge("subject_id", subjectId);
        }
        baseMapper.selectPage(pageParam, queryWrapper);
        return pageParam;

    }

    @Override
    public boolean removeCourse(String id) {

        //todo 删除小节时删除对应的视频文件
        List<String> ids = new ArrayList<>();
        //根据课程id 查询所有 视频id
        QueryWrapper<EduVideo> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("course_id", id);
        deleteWrapper.select("video_source_id");//返回字段名称
        List<EduVideo> list = eduVideoService.list(deleteWrapper);
        ids = list.stream().map(it -> {
            return it.getVideoSourceId();
        }).distinct().filter(it -> {
            return it != null;
        }).collect(Collectors.toList());

        R result = vodClient.deleteList(ids);

        if (!result.getCode().equals(20000)) {
            throw new GuliException(20001, "调用失败");
        }

        //1 根据课程id删除小节
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id", id);
        eduVideoService.remove(eduVideoQueryWrapper);


        //2根据课程计d删除章节
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id", id);
        eduChapterService.remove(eduChapterQueryWrapper);

        //3根据课程id删除描述
        eduCourseDescriptionService.removeById(id);
        //4根据课程id删除课程本身
        baseMapper.deleteById(id);

        return true;
    }


    @Override
    public List<EduCourse> selectByTeacherId(String teacherId) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<EduCourse>();
        queryWrapper.eq("teacher_id", teacherId);
        //按照最后更新时间倒序排列
        queryWrapper.orderByDesc("gmt_modified");
        List<EduCourse> courses = baseMapper.selectList(queryWrapper);
        return courses;
    }

    @Override
    public Map<String, Object> pageListWeb(Page<EduCourse> pageParam, CourseQueryVo courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        //根据一级二级分类构建查询条件
        if (!StringUtils.isEmpty(courseQuery.getSubjectParentId())) {
            queryWrapper.eq("subject_parent_id", courseQuery.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseQuery.getSubjectId())) {
            queryWrapper.eq("subject_id", courseQuery.getSubjectId());
        }
        //根据特定字段排序【倒序】
        if (!StringUtils.isEmpty(courseQuery.getBuyCountSort())) {
            queryWrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseQuery.getGmtCreateSort())) {
            queryWrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(courseQuery.getPriceSort())) {
            queryWrapper.orderByDesc("price");
        }
        baseMapper.selectPage(pageParam, queryWrapper);
        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    @Override
    public CourseWebVo getCourseInfoById(String id) {
        CourseWebVo vo =  baseMapper.getCourseInfoById(id);
        return vo;
    }



}
