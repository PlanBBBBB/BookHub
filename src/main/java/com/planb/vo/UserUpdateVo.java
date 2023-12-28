package com.planb.vo;

import lombok.Data;

@Data
public class UserUpdateVo {
    private String name;
    private String password;
    private String email;
    private String avatar;
}
