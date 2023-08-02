package com.learn.springsecurity.rbac.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * autor:liman
 * createtime:2021/9/5
 * comment:
 */
public interface RbacService {

    public boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
