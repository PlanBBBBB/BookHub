package com.planb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书预订表
 */
@Data
@TableName("PreOrderBook")
@ApiModel("图书预订表")
public class PreOrderBook {
    /**
     * 预订记录ID
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
     * 预订日期
     */
    @ApiModelProperty("预订日期")
    private LocalDateTime bookingDate;
}