package com.atguigu.spring.tx.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class AccountDao {


    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 按照username扣减账户余额
     * @param username 用户名
     * @param delta 扣减的金额
     */
    @Transactional(propagation = Propagation.REQUIRED,timeout = 5)
    public void updateBalanceByUsername(String username, BigDecimal delta) throws InterruptedException {
        String sql = "update account set balance = balance - ? where username = ?";
        // 执行SQL
//        Thread.sleep(4000);
        jdbcTemplate.update(sql, delta, username);
    }
}
