package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-09-26
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> findChapterVideo(String courseId);

    /**
     * 删除章节  前提 下没有关联小节信息
     * @param id
     * @return
     */
    boolean deleteChapter(String id);
}
