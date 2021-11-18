package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.CoursePublishVo;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.front.vo.CourseWebVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author gouwenyong
 * @since 2021-09-26
 */
@Mapper
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    public CoursePublishVo getPublish(String id);


    CourseWebVo getCourseInfoById(@Param("id") String id);
}
