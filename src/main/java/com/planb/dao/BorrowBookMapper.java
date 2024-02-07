package com.planb.dao;

import com.mybatisflex.core.BaseMapper;
import com.planb.entity.BorrowedBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowBookMapper extends BaseMapper<BorrowedBook> {
}
