package com.planb.service;

import com.mybatisflex.core.paginate.Page;
import com.planb.dto.UserLoginDto;
import com.planb.dto.UserRegisterDto;
import com.planb.dto.UserUpdateDto;
import com.planb.entity.User;
import com.planb.vo.Result;
import com.planb.dto.AdminGetPageDto;

public interface IUserService {
    Result register(UserRegisterDto userRegisterVo);

    boolean authorize(Integer userId);

    String login(UserLoginDto userLoginVo);

    void logout();

    User check();

    void update(UserUpdateDto user);

    Page<User> getUserPage(AdminGetPageDto adminGetPageVo);
}
