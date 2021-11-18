package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;

import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient {

    @Override
    public R deleteVideoById(String VideoId) {

        return R.error().message("删除失败").code(20001);
    }

    @Override
    public R deleteList(List<String> ids) {
        return R.error().message("删除失败").code(20001);
    }
}


