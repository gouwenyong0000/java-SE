package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduOss/file")
//@CrossOrigin
public class OSSController {
    @Autowired
    public OssService ossService;

    @PostMapping("/avatar")
    public R uploadOssFile(MultipartFile file) {

        String url = ossService.uploadAvatar(file);
        return R.ok().data("url",url);
    }
}
