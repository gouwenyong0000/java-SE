package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/eduservice/chapter")
//@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId) {

        List<ChapterVo> chapterVideo = eduChapterService.findChapterVideo(courseId);
        return R.ok().data("data", chapterVideo);
    }

    @PostMapping("add")
    public R add(@RequestBody EduChapter eduChapter) {
        boolean save = eduChapterService.save(eduChapter);
        return R.ok();
    }

    /**
     * @param id
     * @return
     */
    @PostMapping("delete/{id}")
    public R deleteById(@PathVariable String id) {
        //todo 关联删除

        boolean flag = eduChapterService.deleteChapter(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("getChapter/{id}")
    public R getChapter(@PathVariable String id) {
        EduChapter byId = eduChapterService.getById(id);
        return R.ok().data("data", byId);
    }


    @PostMapping("update")
    public R update(@RequestBody EduChapter eduChapter) {
        boolean save = eduChapterService.updateById(eduChapter);
        return R.ok();
    }

}

