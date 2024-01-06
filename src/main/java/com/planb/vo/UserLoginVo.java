package com.planb.vo;

import lombok.Data;

@Data
public class UserLoginVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
