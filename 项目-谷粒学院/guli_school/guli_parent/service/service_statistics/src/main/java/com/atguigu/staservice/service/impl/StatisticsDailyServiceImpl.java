package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-10-28
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void createStatisticsByDay(String day) {
        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(dayQueryWrapper);


        //远程调用  获取数据
        R r = ucenterClient.countRegister(day);
        Integer num = (Integer) r.getData().get("num");

        //插入数据库
        StatisticsDaily item = new StatisticsDaily();

        item.setRegisterNum(num);//注册人数
        item.setDateCalculated(day);//注册日期

        item.setLoginNum(RandomUtils.nextInt(100, 200));
        item.setVideoViewNum(RandomUtils.nextInt(100, 200));
        item.setCourseNum(RandomUtils.nextInt(100, 200));
        baseMapper.insert(item);

    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {

        //根据条件查询对应的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        wrapper.select("date_calculated", type);

        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);

        //封装数据  返回日期数据  和数据数组
        Map<String, Object> map = new HashMap<>();
        List<Integer> dataList = new ArrayList<Integer>();
        List<String> dateList = new ArrayList<String>();
        map.put("dataList", dataList);
        map.put("dateList", dateList);

        for (StatisticsDaily statisticsDaily : staList) {
            dateList.add(statisticsDaily.getDateCalculated());
            switch (type) {
                case "register_num":
                    dataList.add(statisticsDaily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(statisticsDaily.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(statisticsDaily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(statisticsDaily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        return map;
    }


}

