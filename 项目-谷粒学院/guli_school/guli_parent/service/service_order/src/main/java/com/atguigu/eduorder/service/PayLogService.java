package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-10-21
 */
public interface PayLogService extends IService<PayLog> {

    Map createNative(String orderNo);

    /**
     * 根据自己系统的订单号查询订单状态
     *
     * @param orderNo
     * @return
     */
    Map<String, String> queryPayStatus(String orderNo);

    /**
     * 根据查询的订单结果更新订单表中状态
     * @param map
     */
    void updateOrderStatus(Map<String, String> map);
}
