package com.learn.springsecurity.sso.server;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * autor:liman
 * createtime:2021/8/17
 * comment:
 */
@Component
public class SsoUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username,"123456",
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
