package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.User;
import com.itheima.utils.Result;
import com.itheima.vo.UserLoginVo;
import com.itheima.vo.UserRegisterVo;
import com.itheima.vo.UserUpdateVo;

public interface IUserService extends IService<User> {
    Result register(UserRegisterVo userRegisterVo);

    void authorize(Integer userId);

    String login(UserLoginVo userLoginVo);

    void logout();

    User check();

    void update(UserUpdateVo user);

}
