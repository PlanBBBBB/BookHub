package com.planb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书表
 */
@Data
@TableName("book")
@ApiModel("图书表")
public class Book {
    /**
     * 图书ID
     */
    @TableId(type = IdType.AUTO)
    private Integer bookId;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 作者
     */
    @ApiModelProperty("作者")
    private String author;

    /**
     * ISBN码
     */
    @TableField(value = "ISBN")
    @ApiModelProperty("ISBN号")
    private String ISBN;

    /**
     * 出版日期
     */
    @ApiModelProperty("出版日期")
    private LocalDateTime publicationDate;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private String status;

    /**
     * 预约计数
     */
    @ApiModelProperty("预约计数")
    private Integer reservationCount;

    /**
     * 库存
     */
    @ApiModelProperty("库存")
    private Integer stock;

    /**
     * 图书图片
     */
    @ApiModelProperty("图书图片")
    private String image;

    /**
     * 是否已删除
     */
    @ApiModelProperty("是否已删除")
    private String isDeleted;
}