package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.ExcelSubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.config.exception.define.GuliException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.poi.ss.formula.functions.T;

public class SubjectExcelLister extends AnalysisEventListener<ExcelSubjectData> {
    /*
        因为SubjectExcelLister不能交给spring进行管理，需要自己new，不能注入其他对数据库的操作
     */
    private EduSubjectService eduSubjectService;

    public SubjectExcelLister() {
    }

    public SubjectExcelLister(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    /**
     * 一行一行的读取数据
     *
     * @param excelSubjectData
     * @param analysisContext
     */
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        if (excelSubjectData == null) {
            throw new GuliException(20001, "文件数据为空");
        }
        //判断一级分类是否存在
        EduSubject eduSubject = existOneSubject(excelSubjectData.getOneSubjectName());
        if (eduSubject == null) {//没有相同的已经分类进行添加
            eduSubject = new EduSubject();

            eduSubject.setParentId("0");
            eduSubject.setTitle(excelSubjectData.getOneSubjectName());

            eduSubjectService.save(eduSubject);
        }

        String pid = eduSubject.getId();

        if (existTwoSubject(excelSubjectData.getTwoSubjectName(), pid) == null) {
            //保存二分类
            EduSubject eduSubjectTwo = new EduSubject();
            eduSubjectTwo.setParentId(pid);
            eduSubjectTwo.setTitle(excelSubjectData.getTwoSubjectName());
            eduSubjectService.save(eduSubjectTwo);
        }
    }

    /**
     * 判断一级分类不能重复添加
     *
     * @param
     */
    private EduSubject existOneSubject(String name) {

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");

        return eduSubjectService.getOne(wrapper);

    }

    /**
     * 判断二级分类不能重复添加
     *
     * @param
     */
    private EduSubject existTwoSubject(String name, String pid) {

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);

        return eduSubjectService.getOne(wrapper);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
