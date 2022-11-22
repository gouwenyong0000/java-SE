package com.g.common;

import java.io.Serializable;

public class  User implements Serializable {
    private static final long serialVersionUID = -5911115284720695676L;

    private String userId;//id
    private String  passwd;//密码

    public User(){}
    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
