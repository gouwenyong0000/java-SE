package com.atguigu.test;

import com.atguigu.dao.BookDao;
import com.atguigu.dao.impl.BookDaoImpl;
import com.atguigu.pojo.Book;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class BookDaoImplTest {

   BookDao bookDao= new BookDaoImpl();

    @Test
    public void addBook() {
     Book book = new Book(null, "bbbbb", "Gg", new BigDecimal(50), 30, 60, null);
     int i = bookDao.addBook(book);
    }

    @Test
    public void deleteBookByID() {
     bookDao.deleteBookById(21);
    }

    @Test
    public void updateBook() {
     Book book = new Book(22, "javabbb", "Gg", new BigDecimal(50), 0, 60, null);
     bookDao.updateBook(book);
    }

    @Test
    public void queryBookById() {
     Book book = bookDao.queryBookById(1);
     System.out.println(book);
    }

    @Test
    public void queryBooks() {
     List<Book> books = bookDao.queryBooks();

     System.out.println(books);
    }
}