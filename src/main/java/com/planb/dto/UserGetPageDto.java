package com.planb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户获取图书分页列表")
public class UserGetPageDto {
    @ApiModelProperty(value = "书名")
    private String title;
    @ApiModelProperty(value = "作者")
    private String author;
    @ApiModelProperty(value = "ISBN")
    private String ISBN;
    @ApiModelProperty(value = "当前页")
    private int currentPage;
    @ApiModelProperty(value = "每页显示数量")
    private int pageSize;
}
