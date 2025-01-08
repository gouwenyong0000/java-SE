package com.atguigu.spring.tx.service.impl;


import com.atguigu.spring.tx.bean.Book;
import com.atguigu.spring.tx.dao.AccountDao;
import com.atguigu.spring.tx.dao.BookDao;
import com.atguigu.spring.tx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    BookDao bookDao;
    @Autowired
    AccountDao accountDao;


    /**
     * 事务细节：
     * 1、transactionManager：事务管理器; 控制事务的获取、提交、回滚。
     *     底层默认使用哪个事务管理器？默认使用 JdbcTransactionManager；
     *     原理：
     *     1、事务管理器：TransactionManager； 控制提交和回滚
     *     2、事务拦截器：TransactionInterceptor： 控制何时提交和回滚
     *              completeTransactionAfterThrowing(txInfo, ex);  在这个时候回滚
     *              commitTransactionAfterReturning(txInfo);  在这个时候提交
     *
     * 2、propagation：传播行为； 事务的传播行为。
     *
     * 3、isolation：隔离级别
     *
     * 4、timeout（同 timeoutString）：超时时间； 事务超时，秒为单位；
     *      一旦超过约定时间，事务就会回滚。
     *      超时时间是指：从方法开始，到最后一次数据库操作结束的时间。
     * 5、readOnly：只读优化
     * 6、rollbackFor（同rollbackForClassName）：指明哪些异常需要回滚。不是所有异常都一定引起事务回滚。
     *     异常：
     *          运行时异常（unchecked exception【非受检异常】）
     *          编译时异常（checked exception【受检异常】）
     *     【回滚的默认机制】
     *          运行时异常：回滚
     *          编译时异常：不回滚
     *
     *    【可以指定哪些异常需要回滚】；
     *    【回滚 = 运行时异常 + 指定回滚异常】
     *
     * 7、noRollbackFor（同 noRollbackForClassName）：指明哪些异常不需要回滚。
     *    【不回滚 = 编译时异常 + 指定不回滚异常】
     *
     *
     *
     *
     *
     * 场景：用户结账，炸了以后，金额扣减回滚，库存不回滚。
     * 注意：【一定关注异常的传播链】
     * 实现：
     *    checkout(){
     *        //自己的操作；
     *        扣减金额： //REQUIRED
     *        扣减库存： //REQUIRES_NEW
     *    }
     *
     *
     *  A {
     *      B(){  //REQUIRED
     *          F();//REQUIRES_NEW
     *          G();//REQUIRED
     *          H();//REQUIRES_NEW
     *      }
     *      C(){  //REQUIRES_NEW
     *         I();//REQUIRES_NEW
     *         J();//REQUIRED
     *      }
     *      D(){   //REQUIRES_NEW
     *          K();//REQUIRES_NEW
     *          L();//REQUIRES_NEW //点位2： 10/0； K,F,H,C(i,j) = ok, E整个代码走不到，剩下炸
     *      }
     *      E(){   //REQUIRED
     *          M();//REQUIRED
     *          //点位3：10/0；  F,H,C(i,j),D(K,L)= ok
     *          N();//REQUIRES_NEW
     *      }
     *
     *      int i = 10/0;  //点位1：C（I，J）,D(K，L) ，F，H,N= ok
     *  }
     *
     * @param username  用户名
     * @param bookId    图书id
     * @param buyNum    购买数量
     *
     * 传播行为：参数设置项也会传播：如果小事务和大事务公用一个事务，小事务要按照大事务的设置，小事务自己的设置失效
     */
    @Transactional(timeout = 3)
    @Override
    public void checkout(String username, Integer bookId, Integer buyNum) throws InterruptedException, IOException {
        //1、查询图书信息
        Book book = bookDao.getBookById(bookId);
        BigDecimal price = book.getPrice();
        //2、计算扣减额度
        BigDecimal total = new BigDecimal(buyNum).multiply(price);
        //3、扣减余额  //REQUIRED
        accountDao.updateBalanceByUsername(username,total);

        //4、扣减库存 //REQUIRES_NEW
        bookDao.updateBookStock(bookId,buyNum);

        //模拟超时
//        Thread.sleep(3000);
        //5、抛出异常
//        int i = 10/0;

//        FileInputStream stream = new FileInputStream("D:\\123.txt");
//        System.out.println("stream.available() = " + stream.available());

    }
}
