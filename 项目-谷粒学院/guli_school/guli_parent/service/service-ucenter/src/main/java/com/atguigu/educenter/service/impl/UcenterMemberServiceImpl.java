package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;

import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.config.exception.define.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-10-10
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String login(UcenterMember ucenterMember) {

        //获取登陆手机号
        String mobile = ucenterMember.getMobile();
        //密码
        String password = ucenterMember.getPassword();

        //手机号和密码非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password))
            throw new GuliException(20001, "登陆失败");

        //判断手机号是否正确
        QueryWrapper<UcenterMember> query = new QueryWrapper();
        query.eq("mobile", mobile);
        UcenterMember mobileUser = baseMapper.selectOne(query);
        if (mobileUser == null) {
            throw new GuliException(20001, "不存在该手机号用户");
        }
        //密码加密
        password = MD5.encrypt(password);

        //判断密码是否正确
        if (!password.equals(mobileUser.getPassword())) {
            throw new GuliException(20001, "密码错误");
        }

        //判断用户是否禁用
        if (mobileUser.getIsDisabled()) {
            throw new GuliException(20001, "账户被禁用");
        }

        //返回token
        String token = JwtUtils.getJwtToken(mobileUser.getId(), mobileUser.getNickname());
        return token;
    }

    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();
        //校验参数
        if (StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new GuliException(20001, "error");
        }

        //判断验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)) {
            throw new GuliException(20001, "验证码错误");
        }
        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrwpp = new QueryWrapper<>();
        wrwpp.eq("mobile", mobile);

        Integer integer = baseMapper.selectCount(wrwpp);
        if (integer >= 1) {
            throw new GuliException(20001, "手机号重复");
        }

        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        ucenterMember.setPassword(MD5.encrypt(password));//密码加密
        ucenterMember.setNickname(nickname);
        ucenterMember.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        ucenterMember.setIsDisabled(false);
        baseMapper.insert(ucenterMember);

    }

    @Override
    public UcenterMember getLoginInfo(String memberId) {

        return baseMapper.selectById(memberId);

    }

    @Override
    public UcenterMember getByOpenid(String openid) {
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        UcenterMember member = baseMapper.selectOne(queryWrapper);
        return member;
    }

    @Override
    public int countRegister(String day) {

        return baseMapper.countRegister(day);
    }



}






