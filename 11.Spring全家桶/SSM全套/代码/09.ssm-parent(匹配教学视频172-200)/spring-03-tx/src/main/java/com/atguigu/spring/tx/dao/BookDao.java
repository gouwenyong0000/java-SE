package com.atguigu.spring.tx.dao;


import com.atguigu.spring.tx.bean.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class BookDao {

    @Autowired
    JdbcTemplate jdbcTemplate;


    /**
     * 按照id查询图书
     * @param id
     * @return
     */
    @Transactional
    public Book getBookById(Integer id) {

        //1、查询图书SQL
        String sql = "select * from book where id  = ?";
        //2、执行查询
        Book book = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Book.class), id);
        return book;
    }



    // REPEATABLE_READ： 可重复读。 快照读。 MySQL默认
    // READ_COMMITTED： 读已提交。 当前读。 Oracle默认
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BigDecimal getBookPrice(Integer id){
        String sql = "select price from book where id=?";
        BigDecimal decimal1 = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);

        BigDecimal decimal2 = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);

        BigDecimal decimal3 = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);

        BigDecimal decimal4 = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);

        BigDecimal decimal5 = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);

        return jdbcTemplate.queryForObject(sql,BigDecimal.class,id);
    }


    /**
     * 添加图书
     * @param book
     */
    public void addBook(Book book){
        String sql = "insert into book(bookName,price,stock) values (?,?,?)";
        jdbcTemplate.update(sql,book.getBookName(),book.getPrice(),book.getStock());
    }


    /**
     * 按照图书id修改图书库存
     * @param bookId 图书id
     * @param num  要减几个
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateBookStock(Integer bookId,Integer num){
        String sql = "update book set stock=stock-? where id=?";
        jdbcTemplate.update(sql,num,bookId);
//        int i = 10/0;
    }


    /**
     * 按照id删除图书
     * @param id
     */
    public void deleteBook(Integer id){
        String sql = "delete from book where id=?";
        jdbcTemplate.update(sql,id);
    }


}
