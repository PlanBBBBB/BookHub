package com.itheima.vo;

import lombok.Data;

@Data
public class UserGetPageVo {
    private String title;
    private String author;
    private String ISBN;
    private int currentPage;
    private int pageSize;
}
