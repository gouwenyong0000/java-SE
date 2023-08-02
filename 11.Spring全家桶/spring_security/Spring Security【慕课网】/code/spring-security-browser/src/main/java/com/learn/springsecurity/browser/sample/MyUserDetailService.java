//package com.learn.springsecurity.browser.sample;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.social.security.SocialUser;
//import org.springframework.social.security.SocialUserDetails;
//import org.springframework.social.security.SocialUserDetailsService;
//import org.springframework.stereotype.Component;
//
///**
// * autor:liman
// * createtime:2021/7/8
// * comment:
// */
//@Component
//@Slf4j
//public class MyUserDetailService implements UserDetailsService,SocialUserDetailsService {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//
//    /**
//     * 根据用户名查找用户信息
//     * @param username
//     * @return
//     * @throws UsernameNotFoundException
//     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("根据用户名查找用户信息:{}",username);
//        return new User(username,passwordEncoder.encode("123456"),
//                true,true,true,true,
//                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
//    }
//
//    /**
//     * 这里的参数是spring-social根据openId查出来的userId
//     * @param userId
//     * @return
//     * @throws UsernameNotFoundException
//     */
//    @Override
//    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
//        log.info("社交登录用户id:{}",userId);
//        return new SocialUser(userId,passwordEncoder.encode("123456"),
//                true,true,true,true,
//                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
//    }
//}
