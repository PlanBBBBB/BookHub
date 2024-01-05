package com.planb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.planb.entity.Book;
import com.planb.utils.Result;
import com.planb.dto.UserPageDto;
import com.planb.vo.AddBookVo;
import com.planb.vo.UserGetPageVo;


public interface IBookService {

    boolean addBook(AddBookVo book);

    boolean modify(Book book);

    boolean delete(Integer id);

    IPage<Book> getAdminPage(UserGetPageVo userGetPageVo);

    Result borrow(Integer bookId);

    Result restore(Integer bookId);

    Result booking(Integer bookId);

    IPage<UserPageDto> getUserPage(UserGetPageVo userGetPageVo);

    Result findBookById(Integer bookId);

}
