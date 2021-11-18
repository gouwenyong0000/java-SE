package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.ExcelSubjectData;
import com.atguigu.eduservice.entity.subject.SubjectNestedVo;
import com.atguigu.eduservice.entity.subject.SubjectVo;
import com.atguigu.eduservice.entity.vo.SubjectTree;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.Subject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-09-25
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    @Autowired
    private EduSubjectMapper mapper;

    @Override
    public void saveSubject(MultipartFile file) {

        try {
            EasyExcel.read(file.getInputStream(), ExcelSubjectData.class, new SubjectExcelLister(this))
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<SubjectTree> tree() {
        List<SubjectTree> list = mapper.getTreeList();

        return list;
    }

    @Override
    public List<SubjectNestedVo> nestedList() {
        List<SubjectNestedVo> resList = new ArrayList<>();

        //查询一级分类
        QueryWrapper<EduSubject> oneQueryWrapper = new QueryWrapper();
        oneQueryWrapper.eq("parent_id", "0");
        List<EduSubject> listOne = list(oneQueryWrapper);

        //查询二级分类
        QueryWrapper<EduSubject> twoQueryWrapper = new QueryWrapper();
        twoQueryWrapper.ne("parent_id", "0");
        List<EduSubject> listTwo = list(twoQueryWrapper);

        //封装一级分类
        for (EduSubject eduSubject : listOne) {
            SubjectNestedVo subjectNestedVo = new SubjectNestedVo();
//            subjectNestedVo.setId(eduSubject.getId());
//            subjectNestedVo.setTitle(eduSubject.getTitle());
            BeanUtils.copyProperties(eduSubject, subjectNestedVo);//对象属性拷贝

            //封装二级分类
            List<SubjectVo> children = new ArrayList<>();

            for (EduSubject subject : listTwo) {

                if (subject.getParentId().equals(eduSubject.getId())) {
                    SubjectVo subjectVo = new SubjectVo();
//                    subjectVo.setId(subject.getId());
//                    subjectVo.setTitle(subject.getTitle());
                    BeanUtils.copyProperties(subject, subjectVo);

                    children.add(subjectVo);
                }
            }
            subjectNestedVo.setChildren(children);

            resList.add(subjectNestedVo);
        }

        return resList;

    }


}



