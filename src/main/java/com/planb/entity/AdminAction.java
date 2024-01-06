package com.planb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书管理员操作记录表
 */
@Data
@TableName("adminactions")
@ApiModel("图书管理员操作记录")
public class AdminAction {
    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Integer recordId;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private String actionType;

    /**
     * 管理员用户ID
     */
    @ApiModelProperty("管理员用户ID")
    private Integer adminUserId;

    /**
     * 图书ID
     */
    @ApiModelProperty("图书ID")
    private Integer bookId;

    /**
     * 操作日期
     */
    @ApiModelProperty("操作日期")
    private LocalDateTime actionDate;
}