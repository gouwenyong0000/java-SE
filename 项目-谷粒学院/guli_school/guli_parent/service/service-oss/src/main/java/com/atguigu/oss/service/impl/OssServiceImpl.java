package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadAvatar(MultipartFile file) {

        //获取配置参数
        String endpoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        String uploadUrl = null;
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);


            //1.文件名称里面添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String path = new DateTime().toString("yyyy/MM/dd/");
            String filename = path + uuid + file.getOriginalFilename();

            // 获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            /*
             *  调用方法实现上传
             *
             * 第一个参数  bucket名称
             * 第二个参数 上传到oss文件路径和名称
             * 第三个参数  上传文件输入流
             */
            ossClient.putObject(bucketName, filename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //返回文件上传之后的路径
            //https://guli-edu5.oss-cn-beijing.aliyuncs.com/wallhaven-0j3oo5.jpg
            uploadUrl = "https://" + bucketName + "." + endpoint + "/" + filename;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadUrl;
    }
}
