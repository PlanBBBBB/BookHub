package com.planb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("获取管理员分页列表的请求参数")
public class AdminGetPageDto {
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "角色")
    private String role;
    @ApiModelProperty(value = "当前页")
    private int currentPage;
    @ApiModelProperty(value = "每页大小")
    private int pageSize;
}
