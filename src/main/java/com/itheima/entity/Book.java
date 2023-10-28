package com.itheima.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 图书表
 */
@Data
@TableName("book")
@ApiModel("图书表")
public class Book {
    @TableId(type = IdType.AUTO)
    private Integer bookId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("作者")
    private String author;

    @TableField(value = "ISBN")
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

    @ApiModelProperty("是否已删除")
    private String isDeleted;
}