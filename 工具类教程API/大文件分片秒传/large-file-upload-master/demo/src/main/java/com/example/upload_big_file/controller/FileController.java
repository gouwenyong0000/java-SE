package com.example.upload_big_file.controller;

import com.example.upload_big_file.po.FileDownloadRequest;
import com.example.upload_big_file.po.FileUpload;
import com.example.upload_big_file.po.FileUploadRequest;
import com.example.upload_big_file.po.Result;
import com.example.upload_big_file.service.FileService;
import com.example.upload_big_file.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping(value = "/")
@Slf4j
public class FileController {


    @Autowired
    private FileService fileService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;


    @GetMapping(value = "/")
    public String gotoPage() {
        return "index";
    }

    @GetMapping(value = "/uploadFile")
    public String gotoFilePage() {
        return "upload";
    }

    @GetMapping(value = "/oss/upload")
    public String gotoOssPage() {
        return "ossUpload";
    }


    @PostMapping(value = "/upload")
    @ResponseBody
    public Result<FileUpload> upload(FileUploadRequest fileUploadRequestDTO) throws IOException {

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        FileUpload fileUploadDTO = null;
        if (isMultipart) {

            StopWatch stopWatch = new StopWatch();
            stopWatch.start("upload");
            if (fileUploadRequestDTO.getChunk() != null && fileUploadRequestDTO.getChunks() > 0) {
                fileUploadDTO = fileService.sliceUpload(fileUploadRequestDTO);
            } else {
                fileUploadDTO = fileService.upload(fileUploadRequestDTO);
            }
            stopWatch.stop();
            log.info("{}", stopWatch.prettyPrint());

            return new Result<FileUpload>().setData(fileUploadDTO);
        }

        throw new RuntimeException("上传失败");

    }

    @RequestMapping(value = "checkFileMd5", method = RequestMethod.POST)
    @ResponseBody
    public Result<FileUpload> checkFileMd5(String md5, String path) throws IOException {

        FileUploadRequest param = new FileUploadRequest().setPath(path).setMd5(md5);
        FileUpload fileUploadDTO = fileService.checkFileMd5(param);

        return new Result<FileUpload>().setData(fileUploadDTO);
    }

    @PostMapping("/download")
    public void download(FileDownloadRequest requestDTO) {

        try {
            FileUtil.downloadFile(requestDTO.getName(), requestDTO.getPath(), request, response);
        } catch (FileNotFoundException e) {
            log.error("download error:" + e.getMessage(), e);
            throw new RuntimeException("文件下载失败");
        }
    }


}
