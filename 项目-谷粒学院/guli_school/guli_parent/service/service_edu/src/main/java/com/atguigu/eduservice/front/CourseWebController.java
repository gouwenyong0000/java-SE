package com.atguigu.eduservice.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.CourseWebVoOrder;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.front.vo.CourseQueryVo;
import com.atguigu.eduservice.front.vo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("eduservice/coursefront")
//@CrossOrigin
public class CourseWebController {
    @Autowired
    private EduCourseService eduCourseService;
    @Autowired
    EduChapterService eduChapterService;

    @Autowired
   private OrderClient orderClient;

    @ApiOperation(value = "分页课程列表")
    @PostMapping(value = "{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
            @RequestBody(required = false) CourseQueryVo courseQuery) {//表示查询条件可以为空
        Page<EduCourse> pageParam = new Page<EduCourse>(page, limit);

        Map<String, Object> map = eduCourseService.pageListWeb(pageParam, courseQuery);
        return R.ok().data(map);
    }

    @GetMapping("course/{id}")
    public R getCourseInfoById(@PathVariable String id, HttpServletRequest request) {

        List<ChapterVo> chapterVideo = eduChapterService.findChapterVideo(id);

        CourseWebVo courseWebVo = eduCourseService.getCourseInfoById(id);
        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);

         boolean buyCourse = orderClient.isBuyCourse(memberIdByJwtToken, id);
        return R.ok().data("course", courseWebVo).data("chapterVoList", chapterVideo).data("isbuy", buyCourse);
    }

    //根据课程id查询课程信息
    @GetMapping("getDto/{courseId}")
    public CourseWebVoOrder getCourseInfoDto(@PathVariable String courseId) {
        CourseWebVo courseInfoById = eduCourseService.getCourseInfoById(courseId);
        CourseWebVoOrder courseInfo = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfoById, courseInfo);
        return courseInfo;
    }

}
