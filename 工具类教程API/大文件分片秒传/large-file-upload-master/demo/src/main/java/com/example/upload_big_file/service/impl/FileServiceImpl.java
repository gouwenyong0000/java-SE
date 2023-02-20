package com.example.upload_big_file.service.impl;

import com.example.upload_big_file.callable.FileCallable;
import com.example.upload_big_file.constant.FileConstant;
import com.example.upload_big_file.enu.FileCheckMd5Status;
import com.example.upload_big_file.enu.UploadModeEnum;
import com.example.upload_big_file.po.FileUpload;
import com.example.upload_big_file.po.FileUploadRequest;
import com.example.upload_big_file.service.FileService;
import com.example.upload_big_file.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private FilePathUtil filePathUtil;


    private AtomicInteger atomicInteger = new AtomicInteger(0);


    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Integer.valueOf(YmlUtil.getValue("upload.thread.maxSize").toString()), (r) -> {
                String threadName = "uploadPool-" + atomicInteger.getAndIncrement();
                Thread thread = new Thread(r);
                thread.setName(threadName);
                return thread;
            });

    private final CompletionService<FileUpload> completionService = new ExecutorCompletionService<>(executorService,
            new LinkedBlockingDeque<>(Integer.valueOf(YmlUtil.getValue("upload.queue.maxSize").toString())));


    @Override
    public FileUpload upload(FileUploadRequest param) throws IOException {

        if (Objects.isNull(param.getFile())) {
            throw new RuntimeException("file can not be empty");
        }
        param.setPath(FileUtil.withoutHeadAndTailDiagonal(param.getPath()));
        String md5 = FileMD5Util.getFileMD5(param.getFile());
        param.setMd5(md5);

        String filePath = filePathUtil.getPath(param);
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        String path = filePath + FileConstant.FILE_SEPARATORCHAR + param.getFile().getOriginalFilename();
        FileOutputStream out = new FileOutputStream(path);
        out.write(param.getFile().getBytes());
        out.flush();
        FileUtil.close(out);

        redisUtil.hset(FileConstant.FILE_UPLOAD_STATUS, md5, "true");

        return FileUpload.builder().path(path).mtime(DateUtil.getCurrentTimeStamp()).uploadComplete(true).build();
    }

    @Override
    public FileUpload sliceUpload(FileUploadRequest fileUploadRequestDTO) {

        try {
            completionService.submit(new FileCallable(UploadModeEnum.RANDOM_ACCESS, fileUploadRequestDTO));

            FileUpload fileUploadDTO = completionService.take().get();
            return fileUploadDTO;
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FileUpload checkFileMd5(FileUploadRequest param) throws IOException {
        Object uploadProgressObj = redisUtil.hget(FileConstant.FILE_UPLOAD_STATUS, param.getMd5());
        if (uploadProgressObj == null) {
            FileUpload fileMd5DTO = FileUpload.builder()
                    .code(FileCheckMd5Status.FILE_NO_UPLOAD.getValue()).build();
            return fileMd5DTO;
        }
        String processingStr = uploadProgressObj.toString();
        boolean processing = Boolean.parseBoolean(processingStr);
        String value = String.valueOf(redisUtil.get(FileConstant.FILE_MD5_KEY + param.getMd5()));
        return fillFileUploadDTO(param, processing, value);
    }

    /**
     * 填充返回文件内容信息
     */
    private FileUpload fillFileUploadDTO(FileUploadRequest param, boolean processing,
                                         String value) throws IOException {

        if (processing) {
            param.setPath(FileUtil.withoutHeadAndTailDiagonal(param.getPath()));
            String path = filePathUtil.getPath(param);
            return FileUpload.builder().code(FileCheckMd5Status.FILE_UPLOADED.getValue())
                    .path(path).build();
        } else {
            File confFile = new File(value);
            byte[] completeList = FileUtils.readFileToByteArray(confFile);
            List<Integer> missChunkList = new LinkedList<>();
            for (int i = 0; i < completeList.length; i++) {
                if (completeList[i] != Byte.MAX_VALUE) {
                    missChunkList.add(i);
                }
            }
            return FileUpload.builder().code(FileCheckMd5Status.FILE_UPLOAD_SOME.getValue())
                    .missChunks(missChunkList).build();
        }
    }


}
