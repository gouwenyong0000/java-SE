package com.learn.springsecurity.demo.async;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * autor:liman
 * createtime:2021/7/2
 * comment:
 */
@Component
public class DefferredResultHolder {

    /**
     * DeferredResult订单的处理结果
     */
    private Map<String,DeferredResult<String>> map = new HashMap<String,DeferredResult<String>>();

    public Map<String, DeferredResult<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, DeferredResult<String>> map) {
        this.map = map;
    }
}
