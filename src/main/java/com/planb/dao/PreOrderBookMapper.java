package com.planb.dao;

import com.planb.entity.PreOrderBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PreOrderBookMapper {
    /**
     * 根据bookId和userId查询预订记录
     *
     * @param map
     * @return
     */
    PreOrderBook selectPreOrderBook(Map<String, Object> map);

    /**
     * 根据recordId删除预订记录
     *
     * @param recordId
     */
    void deleteById(Integer recordId);

    /**
     * 新增预订记录
     *
     * @param preOrderBook
     */
    void insertPreOderBook(PreOrderBook preOrderBook);

    /**
     * 查询所有预定图书记录
     *
     * @return
     */
    List<PreOrderBook> selectAllPreOrderBooks();
}
