package com.itheima.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.itheima.entity.Book;
import com.itheima.utils.Result;
import com.itheima.dto.UserPageDto;
import com.itheima.vo.AddBookVo;
import com.itheima.vo.UserGetPageVo;


public interface IBookService extends IService<Book> {

    boolean addBook(AddBookVo book);

    boolean modify(Book book);

    boolean delete(Integer id);

    IPage<Book> getAdminPage(UserGetPageVo userGetPageVo);

    Result borrow(Integer bookId);

    Result restore(Integer bookId);

    Result booking(Integer bookId);

    IPage<UserPageDto> getUserPage(UserGetPageVo userGetPageVo);

}
