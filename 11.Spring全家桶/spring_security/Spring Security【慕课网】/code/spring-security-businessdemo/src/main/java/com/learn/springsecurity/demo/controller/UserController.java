package com.learn.springsecurity.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.learn.springsecurity.core.social.AppSignUpUtils;
import com.learn.springsecurity.demo.dto.User;
import com.learn.springsecurity.demo.dto.UserQueryCondition;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * autor:liman
 * createtime:2021/6/29
 * comment:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;
    @Autowired
    private AppSignUpUtils appSignUpUtils;

    /**
     * Pageable 用于分页的对象，如果用的是spring-data 这个会很方便
     *
     * @param userQueryCondition
     * @param pageable
     * @return
     */
    @GetMapping(value = "")
    @JsonView(User.userSimpleView.class)
    public List<User> query(UserQueryCondition userQueryCondition, @PageableDefault(page = 1, size = 17) Pageable pageable) {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        return userList;
    }

    @GetMapping(value = "/{id:\\d+}")
    @JsonView(User.userDetailView.class)
    public User getUserById(@PathVariable(name = "id") String id) {
        User user = new User();
        user.setUsername("tom");
        return user;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().stream().forEach(e->{System.out.println(e.getDefaultMessage());});
        }
        log.info("前端上送的用户信息为:{}",user);
        user.setId("1");
        return user;
    }

    @PutMapping("/{id:\\d+}")
    public User update(@Valid @RequestBody User user, BindingResult errors) {

        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());

        user.setId("1");
        return user;
    }

    @PostMapping("/register")
    public void userRegister(@RequestBody User user, HttpServletRequest request) {
        //TODO:这里要完成用户注册，同时将用户信息存入到我们自己的数据表
        //TODO:还需要将用户信息与第三方用户信息做一个关联
        //TODO:如果是绑定，请求也会走到这里，也可以完成上述操作。

        //利用providerSignInUtils，将注册之后的用户信息，关联到会话中
//        providerSignInUtils.doPostSignUp(user.getId(),new ServletWebRequest(request));
        appSignUpUtils.doPostSignUp(new ServletWebRequest(request),user.getId());

    }

    //@GetMapping("/me")
    //public Object getCurrentUser(@AuthenticationPrincipal UserDetails userDetails){
    //    log.info("user me test");
    //    return userDetails;
    //}

    //Oauth获取用户信息的方法
    @GetMapping("/me")
    public Object getCurrentUser(Authentication user,HttpServletRequest request) throws Exception {
        log.info("user me test");

        log.info("开始解析JWT中的用户信息");
        //从header中截取token（JWT）
        String authorization = request.getHeader("Authorization");
        String token = StringUtils.substringAfter(authorization, "bearer ");
        Claims playload = Jwts.parser().setSigningKey("self-security-jwt-key".getBytes("UTF-8")).parseClaimsJws(token).getBody();

        String extentionInfo = (String) playload.get("self_extention_info");
        log.info("extention_info,{}",extentionInfo);

        return user;
    }

    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id) {
        System.out.println(id);
    }

}
