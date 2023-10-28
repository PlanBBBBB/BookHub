package com.itheima.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPageDto {
    private String title;
    private String author;
    private String ISBN;
    private LocalDateTime publicationDate;
}
