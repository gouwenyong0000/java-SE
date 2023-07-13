package com.shf.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shf.security.entity.SecurityUser;
import com.shf.security.entity.User;
import com.shf.security.security.TokenManager;
import com.shf.utils.utils.R;
import com.shf.utils.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 认证Filter
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate<String,Object> redisTemplate;
    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManage, TokenManager tokenManager, RedisTemplate<String, Object> redisTemplate) {
        this.authenticationManager = authenticationManage;
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        this.setPostOnly(true);
        this.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/admin/acl/login","POST")
        );
    }

    /**
     * 1. 获取表单提交用户名和密码
     * @param request from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     * redirect as part of a multi-stage authentication process (such as OpenID).
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
//        获取表单提交的数据
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            new ArrayList<>())

            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 2.认证成功给个调用的方法
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     * method.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        认证成功，得到认证成功之后的用户信息
        SecurityUser user = (SecurityUser) authResult.getPrincipal();
//        根据用户名生成token
        String token = tokenManager.createToken(user.getCurrentUserInfo().getUsername());
//        把用户名称和用户权限列表放到redis
        redisTemplate.opsForValue().set(
                user.getCurrentUserInfo().getUsername(),
                user.getPermissionValueList());
//        返回token
        ResponseUtil.out(response, R.ok().data("token", token));
    }

    /**
     * 3.认证失败调用的方法
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.out(response, R.error());
    }
}
