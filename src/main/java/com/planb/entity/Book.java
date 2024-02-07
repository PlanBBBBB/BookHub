package com.planb.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书表
 */
@Data
@Table("book")
@ApiModel("图书表")
public class Book {
    @Id(keyType = KeyType.Auto)
    @ApiModelProperty("图书id")
    private Integer bookId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("作者")
    private String author;

    @Column(value = "ISBN")
    @ApiModelProperty("ISBN号")
    private String ISBN;

    @ApiModelProperty("出版日期")
    private LocalDateTime publicationDate;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("预约计数")
    private Integer reservationCount;

    @ApiModelProperty("库存")
    private Integer stock;

    @ApiModelProperty("图书图片")
    private String image;

    @ApiModelProperty("是否已删除")
    private String isDeleted;
}