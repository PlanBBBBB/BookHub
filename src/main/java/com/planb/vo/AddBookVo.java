package com.planb.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddBookVo {
    private String author;
    private String ISBN;
    private LocalDateTime publicationDate;
    private Integer stock;
    private String title;
    private String image;
}
