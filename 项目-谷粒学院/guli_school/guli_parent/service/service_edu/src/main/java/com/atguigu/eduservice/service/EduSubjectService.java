package com.atguigu.eduservice.service;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.subject.SubjectNestedVo;
import com.atguigu.eduservice.entity.vo.SubjectTree;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-09-25
 */
public interface EduSubjectService extends IService<EduSubject> {
    /**
     * 添加课程分类
     * @param file
     */
    void saveSubject(MultipartFile file);

    List<SubjectTree> tree();

    List<SubjectNestedVo> nestedList();
}
