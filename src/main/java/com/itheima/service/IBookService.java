package com.itheima.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.github.pagehelper.Page;
import com.itheima.domain.Book;

import java.util.List;


public interface IBookService extends IService<Book> {

    boolean saveBook(Book book);

    boolean modify(Book book);

    boolean delete(Integer id);

    IPage<Book> getPage(int currentPage, int pageSize, Book book);

    boolean borrow(Integer bookId);

    boolean restore(Integer bookId);

    Boolean booking(Integer bookId);

}
