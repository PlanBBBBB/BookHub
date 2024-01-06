package com.planb.dao;

import com.planb.entity.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


public interface BookMapper {
    /**
     * 新增图书
     *
     * @param book
     * @return
     */
    int addBook(Book book);

    /**
     * 根据id更新图书
     *
     * @param book
     * @return
     */
    int update(Book book);

    /**
     * 根据id查询图书
     *
     * @param id
     * @return
     */
    Book getById(Integer id);

    /**
     * 根据条件查询图书
     *
     * @param paramMap
     * @return
     */
    List<Book> selectBooksByConditions(Map<String, Object> paramMap);

}
