package com.itheima.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书表
 */
@Data
@TableName("book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Integer bookId;
    private String title;
    private String author;
    private String ISBN;
    private LocalDateTime publicationDate;
    private String status;
    private LocalDateTime borrowedDate;
    private Integer reservationCount;
    private Integer stock;
}