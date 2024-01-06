package com.planb.vo;

import lombok.Data;

@Data
public class UserUpdateVo {
    /**
     * 姓名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String avatar;
}
