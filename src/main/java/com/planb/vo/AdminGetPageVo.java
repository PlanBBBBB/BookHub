package com.planb.vo;

import lombok.Data;

@Data
public class AdminGetPageVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 角色
     */
    private String role;
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 每页显示条数
     */
    private int pageSize;
}
