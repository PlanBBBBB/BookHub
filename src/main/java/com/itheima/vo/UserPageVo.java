package com.itheima.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPageVo {
    private String title;
    private String author;
    private String ISBN;
    private LocalDateTime publicationDate;
}
