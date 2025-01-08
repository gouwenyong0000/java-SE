package com.atguigu.spring.tx.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface UserService {

    /**
     * 用户结账
     * @param username  用户名
     * @param bookId    图书id
     * @param buyNum    购买数量
     */
    void checkout(String username, Integer bookId, Integer buyNum) throws InterruptedException, IOException;
}
