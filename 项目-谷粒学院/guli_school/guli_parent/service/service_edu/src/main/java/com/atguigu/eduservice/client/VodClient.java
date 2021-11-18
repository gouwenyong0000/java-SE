package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)
@Component
public interface VodClient {
    /**
     * 根据视频id删除阿里云视频
     * @param VideoId
     * @return
     */
    @DeleteMapping("/eduvod/video/{VideoId}")
    public R deleteVideoById(@PathVariable("VideoId") String VideoId);

    @DeleteMapping("/eduvod/video/deleteList")
    public R deleteList(@RequestParam("ids") List<String> ids);
}
