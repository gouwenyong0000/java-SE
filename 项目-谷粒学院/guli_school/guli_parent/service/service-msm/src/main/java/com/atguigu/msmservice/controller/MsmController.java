package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/msm/api")
//@CrossOrigin //跨域
public class MsmController {
    @Autowired
    private MsmService msmService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/send/{phone}")
    public R sendPhone(@PathVariable String phone) {

        //1.从redis获取验证码，如果获取到直接返回
        String msg  = redisTemplate.opsForValue().get(phone);

        //如果获取到直接返回，否则发送
        if (!StringUtils.isEmpty(msg)) return R.ok();

        String code = RandomUtil.getFourBitRandom();
        Map<String, Object> params = new HashMap<>();

        params.put("code", code);

        //调用service短信方法发送
        boolean send = msmService.send(phone, params);

        if (send) {
            //发送成功，把验证码存入redis中并设置过期时间5min
            redisTemplate.opsForValue().set(phone,code ,5 , TimeUnit.MINUTES );
            return R.ok();

        } else {
            return R.error().message("");
        }
    }
}


