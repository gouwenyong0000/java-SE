package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.UcenterMemberPay;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.config.exception.define.GuliException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author gouwenyong
 * @since 2021-10-10
 */
@RestController
@RequestMapping("/educenter/member")
//@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    //登陆
    @PostMapping("login")
    public R login(@RequestBody UcenterMember ucenterMember, HttpServletRequest request) {
        String token = ucenterMemberService.login(ucenterMember);
        return R.ok().data("token", token);
    }

    //注册
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo) {
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("auth/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request, @RequestHeader("token") String token) {
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            UcenterMember loginInfoVo = ucenterMemberService.getLoginInfo(memberId);
            return R.ok().data("item", loginInfoVo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "error");
        }
    }

    //根据token字符串获取用户信息
    @GetMapping("getInfoUc/{id}")
    public UcenterMemberPay getInfo(@PathVariable String id) {
        //根据用户id获取用户信息
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        UcenterMemberPay memeber = new UcenterMemberPay();
        BeanUtils.copyProperties(ucenterMember, memeber);
        return memeber;
    }

    /**
     * 查询某一天的注册人数
     */
    @GetMapping("countregister/{day}")
    public R countRegister(@PathVariable String day) {
        int num = ucenterMemberService.countRegister(day);

        return R.ok().data("num",num);
    }


}

