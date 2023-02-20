package com.example.upload_big_file.strategy;

import com.example.upload_big_file.po.FileUpload;
import com.example.upload_big_file.po.FileUploadRequest;

public interface SliceUploadStrategy {

    FileUpload sliceUpload(FileUploadRequest param);
}
