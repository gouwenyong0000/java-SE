package com.shf.springsecurityweb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shf.springsecurityweb.entity.Users;
import com.shf.springsecurityweb.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserDetailsService")
public class MyDBUserDetailService implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("username", username);
        Users users = usersMapper.selectOne(usersQueryWrapper);

        if (users == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        List<GrantedAuthority> role = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_sale");

        String name = users.getUsername();
        String pwd = new BCryptPasswordEncoder().encode(users.getPassword());
        User user = new User(name, pwd, role);
        return user;
    }
}
