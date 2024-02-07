package com.planb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("添加书籍的DTO")
public class AddBookDto {
    @ApiModelProperty("作者")
    private String author;
    @ApiModelProperty("ISBN")
    private String ISBN;
    @ApiModelProperty("出版日期")
    private LocalDateTime publicationDate;
    @ApiModelProperty("库存")
    private Integer stock;
    @ApiModelProperty("书名")
    private String title;
    @ApiModelProperty("封面图片URL")
    private String image;
}
