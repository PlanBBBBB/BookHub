package com.planb.dao;

import com.mybatisflex.core.BaseMapper;
import com.planb.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
