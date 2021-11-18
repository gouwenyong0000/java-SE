package com.atguigu.msmservice.service;

import java.util.Map;

public interface MsmService {
    /**
     * 用于测试  ，默认成功
     */
    boolean send(String phone, Map<String, Object> params);

     boolean send(String PhoneNumbers, String templateCode, Map<String, Object> param);

}
