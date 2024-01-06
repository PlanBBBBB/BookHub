package com.planb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.planb.entity.Book;
import com.planb.utils.Result;
import com.planb.dto.UserPageDto;
import com.planb.vo.AddBookVo;
import com.planb.vo.UserGetPageVo;


public interface IBookService {
    /**
     * 新增图书
     *
     * @param book
     * @return
     */
    boolean addBook(AddBookVo book);

    /**
     * 修改图书
     *
     * @param book
     * @return
     */
    boolean modify(Book book);

    /**
     * 删除图书
     *
     * @param id
     * @return
     */
    boolean delete(Integer id);

    /**
     * 图书管理员分页查询图书
     *
     * @param userGetPageVo
     * @return
     */
    IPage<Book> getAdminPage(UserGetPageVo userGetPageVo);

    /**
     * 借阅图书
     *
     * @param bookId
     * @return
     */
    Result borrow(Integer bookId);

    /**
     * 归还图书
     *
     * @param bookId
     * @return
     */
    Result restore(Integer bookId);

    /**
     * 预订图书
     *
     * @param bookId
     * @return
     */
    Result booking(Integer bookId);

    /**
     * 用户分页查询图书
     *
     * @param userGetPageVo
     * @return
     */
    IPage<UserPageDto> getUserPage(UserGetPageVo userGetPageVo);

    /**
     * 根据id查询图书
     *
     * @param bookId
     * @return
     */
    Result findBookById(Integer bookId);

}
