package com.planb.service;

import com.mybatisflex.core.paginate.Page;
import com.planb.dto.AddBookDto;
import com.planb.dto.UserGetPageDto;
import com.planb.vo.UserPageVo;
import com.planb.entity.Book;
import com.planb.vo.Result;


public interface IBookService {

    boolean addBook(AddBookDto book);

    boolean modify(Book book);

    boolean delete(Integer id);

    Page<Book> getAdminPage(UserGetPageDto userGetPageDto);

    Result borrow(Integer bookId);

    Result restore(Integer bookId);

    Result booking(Integer bookId);

    Page<UserPageVo> getUserPage(UserGetPageDto userGetPageDto);

    Result findBookById(Long bookId);

}
