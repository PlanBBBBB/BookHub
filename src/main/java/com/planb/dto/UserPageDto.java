package com.planb.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPageDto {
    /**
     * 图书ID
     */
    private Integer bookId;
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
     * 出版日期
     */
    private LocalDateTime publicationDate;
    /**
     * 状态
     */
    private String status;
    /**
     * 图书图片
     */
    private String image;
    /**
     * 是否借出
     */
    private String isBorrowed; // 1为已借出，0为未借出
}
