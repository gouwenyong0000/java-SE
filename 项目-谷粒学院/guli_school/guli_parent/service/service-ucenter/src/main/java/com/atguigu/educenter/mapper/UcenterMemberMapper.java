package com.atguigu.educenter.mapper;


import com.atguigu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author gouwenyong
 * @since 2021-10-10
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    Integer countRegister(@Param("dayP") String day);
}
