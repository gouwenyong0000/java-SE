package com.atguigu.educenter.service;


import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-10-10
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    /**
     * 登陆 [用户名  手机号  是否禁用]，返回token
     * @param ucenterMember
     * @return
     */
    String login(UcenterMember ucenterMember);

    /**
     * 注册
     * @param registerVo
     */
    void register(RegisterVo registerVo);

    UcenterMember getLoginInfo(String memberId);

    UcenterMember getByOpenid(String openid);

    int countRegister(String day);
}
