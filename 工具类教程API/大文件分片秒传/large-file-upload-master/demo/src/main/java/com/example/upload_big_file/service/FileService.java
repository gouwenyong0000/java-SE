package com.example.upload_big_file.service;



import com.example.upload_big_file.po.FileUpload;
import com.example.upload_big_file.po.FileUploadRequest;

import java.io.IOException;

public interface FileService {

  FileUpload upload(FileUploadRequest fileUploadRequestDTO)throws IOException;

  FileUpload sliceUpload(FileUploadRequest fileUploadRequestDTO);

  FileUpload checkFileMd5(FileUploadRequest fileUploadRequestDTO)throws IOException;

}
