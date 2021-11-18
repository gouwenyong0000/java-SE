package com.atguigu.eduorder.client;

import com.atguigu.commonutils.vo.UcenterMemberPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Component
@FeignClient("service-ucenter")
public interface UcenterClient {

    @GetMapping("/educenter/member/getInfoUc/{id}")
    public UcenterMemberPay getInfo(@PathVariable("id") String id);
}

