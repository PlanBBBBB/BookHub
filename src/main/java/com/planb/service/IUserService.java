package com.planb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.planb.entity.User;
import com.planb.utils.Result;
import com.planb.vo.AdminGetPageVo;
import com.planb.vo.UserLoginVo;
import com.planb.vo.UserRegisterVo;
import com.planb.vo.UserUpdateVo;

public interface IUserService extends IService<User> {
    Result register(UserRegisterVo userRegisterVo);

    boolean authorize(Integer userId);

    String login(UserLoginVo userLoginVo);

    void logout();

    User check();

    void update(UserUpdateVo user);

    IPage<User> getUserPage(AdminGetPageVo adminGetPageVo);
}
