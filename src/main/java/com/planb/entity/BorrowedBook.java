package com.planb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    /**
     * 借阅记录ID
     */
    @TableId(type = IdType.AUTO)
    private Integer recordId;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Integer userId;

    /**
     * 图书ID
     */
    @ApiModelProperty("图书ID")
    private Integer bookId;

    /**
     * 借阅日期
     */
    @ApiModelProperty("借阅日期")
    private LocalDateTime borrowDate;

    /**
     * 归还日期
     */
    @ApiModelProperty("归还日期")
    private LocalDateTime returnDate;
}