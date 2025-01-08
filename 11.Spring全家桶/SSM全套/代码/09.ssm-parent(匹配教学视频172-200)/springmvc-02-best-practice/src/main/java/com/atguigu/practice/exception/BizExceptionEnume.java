package com.atguigu.practice.exception;

import lombok.Getter;

public enum BizExceptionEnume {


    // ORDER_xxx：订单模块相关异常
    // PRODUCT_xxx：商品模块相关异常

    // 动态扩充.....

    ORDER_CLOSED(10001, "订单已关闭"),
    ORDER_NOT_EXIST(10002, "订单不存在"),
    ORDER_TIMEOUT(10003, "订单超时"),
    PRODUCT_STOCK_NOT_ENOUGH(20003, "库存不足"),
    PRODUCT_HAS_SOLD(20002, "商品已售完"),
    PRODUCT_HAS_CLOSED(20001, "商品已下架");


    @Getter
    private Integer code;
    @Getter
    private String msg;


    private BizExceptionEnume(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
