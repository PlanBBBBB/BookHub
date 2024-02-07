package com.planb.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书管理员操作记录表
 */
@Data
@Table("adminactions")
@ApiModel("图书管理员操作记录")
public class AdminAction {
    @Id(keyType = KeyType.Auto)
    @ApiModelProperty("操作id")
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