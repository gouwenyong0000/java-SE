package com.atguigu.staservice.job;

import com.atguigu.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService dailyService;

    /**
     * 每天凌晨一点去生成数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void job(){
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        System.out.println(day + "同步数据");
        dailyService.createStatisticsByDay(day);
    }

    /**
     * 测试
     * 每五秒执行一次
     */
    //@Scheduled(cron = "0/5 * * * * ?")
    public void task1() {
        System.out.println("*********++++++++++++*****执行了");
    }


}

