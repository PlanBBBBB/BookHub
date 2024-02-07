package com.planb.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书预订表
 */
@Data
@Table("PreOrderBook")
@ApiModel("图书预订表")
public class PreOrderBook {
    @Id(keyType = KeyType.Auto)
    @ApiModelProperty("预订id")
    private Integer recordId;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("图书ID")
    private Integer bookId;

    @ApiModelProperty("预订日期")
    private LocalDateTime bookingDate;
}