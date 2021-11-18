package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.config.exception.define.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-09-26
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> findChapterVideo(String courseId) {

        //1.根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> queryChapter = new QueryWrapper<>();
        queryChapter.eq("course_id", courseId);
        queryChapter.orderByAsc("sort");
        List<EduChapter> list = baseMapper.selectList(queryChapter);


        //2根据课程id查询课程里面所有的小节

        QueryWrapper<EduVideo> queryVedio = new QueryWrapper<>();
        queryVedio.eq("course_id", courseId);
        List<EduVideo> listVedio = eduVideoService.list(queryVedio);
        //3遍历查询章节list集合进行封装
        List<ChapterVo> resList = new ArrayList<>();

        for (EduChapter eduChapter : list) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            resList.add(chapterVo);

            //4遍历查询小节list集合，进行封装
            List<VideoVo> videoVos = new ArrayList<>();
            for (EduVideo eduVideo : listVedio) {
                if (eduVideo.getChapterId().equals(eduChapter.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    videoVos.add(videoVo);

                }

            }
            chapterVo.setChildren(videoVos);

        }

        return resList;
    }

    @Override
    public boolean deleteChapter(String id) {

        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", id);

        int count = eduVideoService.count(wrapper);

        if (count != 0) {
            throw new GuliException(20001, "当前章节下面存在小节信息，不能删除");
        } else {
            int res = baseMapper.deleteById(id);
            return res > 0;
        }

    }
}
