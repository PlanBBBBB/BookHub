package com.planb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.planb.entity.BorrowedBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowBookMapper extends BaseMapper<BorrowedBook> {
}
