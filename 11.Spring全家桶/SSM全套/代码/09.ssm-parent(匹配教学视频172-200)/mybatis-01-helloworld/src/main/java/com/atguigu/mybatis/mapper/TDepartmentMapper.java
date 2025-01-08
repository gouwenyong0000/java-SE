package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.bean.TDepartment;

/**
* @author lfy
* @description 针对表【t_department(部门)】的数据库操作Mapper
* @createDate 2024-09-07 15:43:44
* @Entity com.atguigu.mybatis.bean.TDepartment
*/
public interface TDepartmentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TDepartment record);

    int insertSelective(TDepartment record);

    TDepartment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TDepartment record);

    int updateByPrimaryKey(TDepartment record);

}
