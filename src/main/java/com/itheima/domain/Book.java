package com.itheima.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @TableField(value = "ISBN")
    private String ISBN;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime publicationDate;
    private String status;
    private Integer reservationCount;
    private Integer stock;
    private String isDeleted;
}