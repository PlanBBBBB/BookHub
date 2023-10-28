package com.itheima.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 借阅记录表
 */
@Data
@TableName("borrowedbook")
@ApiModel("借阅记录表")
public class BorrowedBook {
    @TableId(type = IdType.AUTO)
    private Integer recordId;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("图书ID")
    private Integer bookId;

    @ApiModelProperty("借阅日期")
    private LocalDateTime borrowDate;

    @ApiModelProperty("归还日期")
    private LocalDateTime returnDate;
}