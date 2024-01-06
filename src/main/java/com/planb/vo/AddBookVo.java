package com.planb.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddBookVo {
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
     * 库存
     */
    private Integer stock;
    /**
     * 标题
     */
    private String title;
    /**
     * 图书图片
     */
    private String image;
}
