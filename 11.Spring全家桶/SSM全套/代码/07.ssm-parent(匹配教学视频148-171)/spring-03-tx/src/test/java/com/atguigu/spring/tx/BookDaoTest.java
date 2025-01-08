package com.atguigu.spring.tx;


import com.atguigu.spring.tx.dao.BookDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class BookDaoTest {

    @Autowired
    BookDao bookDao;

    @Test
    void testQuery(){
        BigDecimal bookPrice = bookDao.getBookPrice(1);
        System.out.println("bookPrice = " + bookPrice);
        BigDecimal bookPrice1 = bookDao.getBookPrice(1);
        System.out.println("bookPrice = " + bookPrice);
        BigDecimal bookPrice2 = bookDao.getBookPrice(1);
    }
}
