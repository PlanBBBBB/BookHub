package com.itheima.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import com.github.pagehelper.Page;

@Mapper
public interface BookMapper extends BaseMapper<Book> {

    Page<Book> selectAvailableBooksPage();

}
