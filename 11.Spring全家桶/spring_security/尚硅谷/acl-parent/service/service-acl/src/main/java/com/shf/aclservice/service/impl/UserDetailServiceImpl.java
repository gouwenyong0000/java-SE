package com.shf.aclservice.service.impl;

import com.shf.aclservice.entity.User;
import com.shf.aclservice.service.PermissionService;
import com.shf.aclservice.service.UserService;
import com.shf.security.entity.SecurityUser;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        根据用户名查询数据
        User user = userService.selectByUsername(username);
//        判断
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        com.shf.security.entity.User currentUser = new com.shf.security.entity.User();
        BeanUtils.copyProperties(user, currentUser);

//        根据用户查询用户权限列表
        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(currentUser);
        securityUser.setPermissionValueList(permissionValueList);

        return securityUser;
    }
}
