package com.learn.springsecurity.demo.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.learn.springsecurity.demo.validate.SelfValidator;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * autor:liman
 * createtime:2021/6/29
 * comment:
 */
@ToString
public class User {

    public interface userSimpleView {
    }

    ;

    public interface userDetailView extends userSimpleView {
    }

    ;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @SelfValidator(message = "自定义校验未通过")
    private String password;

    private String id;

    private Date birthday;

    @JsonView(userSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView(userDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
