package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.config.exception.define.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author gouwenyong
 * @since 2021-09-26
 */
@RestController
@RequestMapping("/eduservice/video")
//@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VodClient vodClient;

    @PostMapping("add")
    public R add(@RequestBody EduVideo eduVideo) {
        boolean save = eduVideoService.save(eduVideo);
        return R.ok();
    }



    /**
     * 删除小节删除阿里云对应视频的id
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public R delete(@PathVariable String id) {

        EduVideo video = eduVideoService.getById(id);//根据id查询
        String videoSourceId = video.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)) {
            R result = vodClient.deleteVideoById(videoSourceId);
            if (!result.getCode().equals(20000)){
                throw new GuliException(20001, "调用失败");
            }
        }

        eduVideoService.removeById(id);
        return R.ok();
    }

    @GetMapping("/{id}")
    public R getVideo(@PathVariable String id) {

        EduVideo eduVideo = eduVideoService.getById(id);
        return R.ok().data("data", eduVideo);
    }

    @PutMapping("update")
    public R update(@RequestBody EduVideo eduVideo) {
        boolean b = eduVideoService.updateById(eduVideo);
        return R.ok();
    }

}

