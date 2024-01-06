package com.planb.vo;

import lombok.Data;

@Data
public class UserGetPageVo {
    /**
     * 书名
     */
    private String title;
    /**
     * 作者
     */
    private String author;
    /**
     * ISBN码
     */
    private String ISBN;
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 每页显示的条数
     */
    private int pageSize;
}
