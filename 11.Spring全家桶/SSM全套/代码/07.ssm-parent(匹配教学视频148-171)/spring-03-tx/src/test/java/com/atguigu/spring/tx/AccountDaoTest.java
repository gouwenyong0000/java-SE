package com.atguigu.spring.tx;


import com.atguigu.spring.tx.dao.AccountDao;
import com.atguigu.spring.tx.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionManager;

import java.math.BigDecimal;

@SpringBootTest
public class AccountDaoTest {

    @Autowired
    AccountDao accountDao;

    @Autowired
    UserService userService;

    @Autowired
    TransactionManager transactionManager;


    @Test
    void testTM(){
        System.out.println("transactionManager = " + transactionManager);
        System.out.println(transactionManager.getClass());
    }


    //测试结账
    @Test
    void testcheckout() throws Exception {
        userService.checkout("wangwu",3,4);
    }

    @Test
    void testUpdate() throws InterruptedException {
        accountDao.updateBalanceByUsername("zhangsan", new BigDecimal(9.9));
    }
}
