package com.planb.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiOperation("用户页面图书信息")
public class UserPageVo {
    @ApiModelProperty(value = "图书ID")
    private Integer bookId;
    @ApiModelProperty(value = "图书名称")
    private String title;
    @ApiModelProperty(value = "作者")
    private String author;
    @ApiModelProperty(value = "ISBN")
    private String ISBN;
    @ApiModelProperty(value = "出版日期")
    private LocalDateTime publicationDate;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "封面图片")
    private String image;
    @ApiModelProperty(value = "借阅状态")
    private String isBorrowed; // 1为已借出，0为未借出
}
