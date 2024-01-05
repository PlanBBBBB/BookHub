package com.planb.dao;

import com.planb.entity.BorrowedBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface BorrowBookMapper {
    /**
     * 根据bookId和userId查询借阅记录
     *
     * @param paramMap
     * @return
     */
    BorrowedBook selectBorrowedBook(Map<String, Object> paramMap);

    /**
     * 根据recordId更新曾经的借阅记录
     *
     * @param paramMap
     */
    void updateBorrowedBook(Map<String, Object> paramMap);

    /**
     * 新增借阅记录
     *
     * @param borrowedBook
     */
    void insertBorrowedBook(BorrowedBook borrowedBook);

    /**
     * 根据recordId更新归还时间
     *
     * @param recordId
     */
    void updateById(HashMap<String, Object> recordId);

    /**
     * 根据条件查询借阅记录(20-30天)
     *
     * @return
     */
    List<BorrowedBook> selectReturnBorrowedBooksByDate();

    /**
     * 根据条件查询借阅记录(超过30天)
     *
     * @return
     */
    List<BorrowedBook> selectOverdueBorrowedBooksByDate();

}
