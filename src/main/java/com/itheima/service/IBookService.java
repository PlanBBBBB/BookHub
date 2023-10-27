package com.itheima.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.github.pagehelper.Page;
import com.itheima.domain.Book;
import com.itheima.utils.Result;
import com.itheima.vo.UserPageVo;

import java.util.List;


public interface IBookService extends IService<Book> {

    boolean saveBook(Book book);

    boolean modify(Book book);

    boolean delete(Integer id);

    IPage<Book> getAdminPage(int currentPage, int pageSize, Book book);

    Result borrow(Integer bookId);

    Result restore(Integer bookId);

    Result booking(Integer bookId);

    IPage<UserPageVo> getUserPage(int currentPage, int pageSize, Book book);

}
