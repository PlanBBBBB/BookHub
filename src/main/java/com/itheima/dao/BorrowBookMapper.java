package com.itheima.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.BorrowedBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowBookMapper extends BaseMapper<BorrowedBook> {
}
