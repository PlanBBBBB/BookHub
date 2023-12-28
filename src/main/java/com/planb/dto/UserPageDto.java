package com.planb.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPageDto {
    private Integer bookId;
    private String title;
    private String author;
    private String ISBN;
    private LocalDateTime publicationDate;
    private String status;
    private String image;
    private String isBorrowed; // 1为已借出，0为未借出
}
