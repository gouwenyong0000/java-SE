package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-10-21
 */
public interface OrderService extends IService<Order> {
    /**
     * 创建订单
     * @param courseId 订单id
     * @param memberIdByJwtToken  登陆用户id
     * @return
     */
    String saveOrder(String courseId, String memberIdByJwtToken);
}
