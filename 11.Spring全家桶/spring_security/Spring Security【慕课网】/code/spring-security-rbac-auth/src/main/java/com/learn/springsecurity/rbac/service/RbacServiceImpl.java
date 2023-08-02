package com.learn.springsecurity.rbac.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * autor:liman
 * createtime:2021/9/5
 * comment: Rbac服务类的实现类
 */
@Component("rbacService")
public class RbacServiceImpl implements RbacService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 判断是否有权限
     * @param request
     * @param authentication
     * @return
     */
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        boolean hasPermission =false;
        if(principal instanceof UserDetails){
            String username = ((UserDetails) principal).getUsername();

            //TODO:从数据库读取当前用户所拥有权限的url
            Set<String> urls = new HashSet<>();

            //判断当前请求url，是否在拥有权限的url列表中（这里用antPathMatcher）
            for(String url:urls){
                if(antPathMatcher.match(url,request.getRequestURI())){
                    hasPermission = true;
                    break;
                }
            }
        }
        return hasPermission;
    }
}
