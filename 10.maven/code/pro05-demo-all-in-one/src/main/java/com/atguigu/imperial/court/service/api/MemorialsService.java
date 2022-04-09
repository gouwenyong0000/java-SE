package com.atguigu.imperial.court.service.api;

import com.atguigu.imperial.court.entity.Memorials;

import java.util.List;

public interface MemorialsService {
    List<Memorials> getAllMemorialsDigest();

    Memorials getMemorialsDetailById(String memorialsId);

    void updateMemorialsStatusToRead(String memorialsId);

    void updateMemorialsFeedBack(String memorialsId, String feedbackContent);
}
