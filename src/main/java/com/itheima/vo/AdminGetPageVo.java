package com.itheima.vo;

import lombok.Data;

@Data
public class AdminGetPageVo {
    private String username;
    private String name;
    private String email;
    private String role;
    private int currentPage;
    private int pageSize;
}
