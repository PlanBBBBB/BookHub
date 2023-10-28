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
 * 图书管理员操作记录表
 */
@Data
@TableName("adminactions")
@ApiModel("图书管理员操作记录")
public class AdminAction {
    @TableId(type = IdType.AUTO)
    private Integer recordId;

    @ApiModelProperty("操作类型")
    private String actionType;

    @ApiModelProperty("管理员用户ID")
    private Integer adminUserId;

    @ApiModelProperty("图书ID")
    private Integer bookId;

    @ApiModelProperty("操作日期")
    private LocalDateTime actionDate;
}