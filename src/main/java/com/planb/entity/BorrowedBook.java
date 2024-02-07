package com.planb.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 借阅记录表
 */
@Data
@Table("borrowedbook")
@ApiModel("借阅记录表")
public class BorrowedBook {
    @Id(keyType = KeyType.Auto)
    @ApiModelProperty("借阅id")
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