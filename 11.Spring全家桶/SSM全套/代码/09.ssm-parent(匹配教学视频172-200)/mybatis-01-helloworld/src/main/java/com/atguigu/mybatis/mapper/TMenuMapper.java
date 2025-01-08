package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.bean.TMenu;

/**
* @author lfy
* @description 针对表【t_menu(菜单表)】的数据库操作Mapper
* @createDate 2024-09-07 15:43:44
* @Entity com.atguigu.mybatis.bean.TMenu
*/
public interface TMenuMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TMenu record);

    int insertSelective(TMenu record);

    TMenu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TMenu record);

    int updateByPrimaryKey(TMenu record);

}
