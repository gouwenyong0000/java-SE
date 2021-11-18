package com.atguigu.staservice.controller;


import com.atguigu.commonutils.R;

import com.atguigu.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author gouwenyong
 * @since 2021-10-28
 */
@RestController
@RequestMapping("/staservice/sta")
//@CrossOrigin
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService dailyService;

    @PostMapping("{day}")
    public R createStatisticsByDate(@PathVariable String day) {
        dailyService.createStatisticsByDay(day);
        return R.ok();
    }

    /**
     * 图标显示
     */
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showdata(@PathVariable String type,@PathVariable String begin, @PathVariable String end) {
     Map<String,Object> map =  dailyService.getShowData(type,begin,end);
        return R.ok().data(map);
    }



}

