package com.atguigu.eduorder.service.impl;
import java.math.BigDecimal;
import java.util.Date;

import com.atguigu.commonutils.vo.CourseWebVoOrder;
import com.atguigu.commonutils.vo.UcenterMemberPay;
import com.atguigu.eduorder.client.EduServiceClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-10-21
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduServiceClient eduServiceClient;

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public String saveOrder(String courseId, String memberIdByJwtToken) {

        //通过远程调用获取到用户信息
        UcenterMemberPay info = ucenterClient.getInfo(memberIdByJwtToken);

        //通过远程调用获取课程信息
        CourseWebVoOrder courseInfoDto = eduServiceClient.getCourseInfoDto(courseId);

        Order order =new Order();

        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoDto.getTitle());
        order.setCourseCover(courseInfoDto.getCover());
        order.setTeacherName(courseInfoDto.getTeacherName());
        order.setMemberId(memberIdByJwtToken);
        order.setNickname(info.getNickname());
        order.setMobile(info.getMobile());
        order.setTotalFee(courseInfoDto.getPrice());
        order.setPayType(1);//支付类型  1：维系
        order.setStatus(0);//支付状态  0：未支付

        int insert = baseMapper.insert(order);

        return order.getOrderNo();
    }
}


