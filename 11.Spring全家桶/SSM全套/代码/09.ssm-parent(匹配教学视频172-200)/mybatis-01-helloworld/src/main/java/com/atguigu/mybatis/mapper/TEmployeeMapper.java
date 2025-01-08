package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.bean.TEmployee;

/**
* @author lfy
* @description 针对表【t_employee(员工表)】的数据库操作Mapper
* @createDate 2024-09-07 15:43:44
* @Entity com.atguigu.mybatis.bean.TEmployee
*/
public interface TEmployeeMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TEmployee record);

    int insertSelective(TEmployee record);

    TEmployee selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TEmployee record);

    int updateByPrimaryKey(TEmployee record);

}
