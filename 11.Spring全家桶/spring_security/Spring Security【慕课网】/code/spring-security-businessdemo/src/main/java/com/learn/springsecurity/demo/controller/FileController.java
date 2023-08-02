package com.learn.springsecurity.demo.controller;

import com.learn.springsecurity.demo.dto.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * autor:liman
 * createtime:2021/7/1
 * comment:文件上传的controller
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    private String folder = "F:\\2021_learn_project\\spring-springsecurity-businessdemo\\src\\main\\java\\com\\learn\\springsecurity\\demo\\controller";

    @PostMapping
    public FileInfo upload(MultipartFile file) throws IOException {
        log.info(file.getName());
        log.info(file.getOriginalFilename());
        log.info("{}",file.getSize());
        File localFile = new File(folder,new Date().getTime()+".txt");

        //这
        file.transferTo(localFile);
        return new FileInfo(localFile.getAbsolutePath());

    }

    @GetMapping("/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (InputStream inputStream = new FileInputStream(new File(folder, id + ".txt"));
             OutputStream outputStream = response.getOutputStream()) {

            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=test.txt");

            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }
    }
}
