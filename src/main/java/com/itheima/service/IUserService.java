package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.User;
import com.itheima.utils.Result;
import com.itheima.vo.UserUpdateVo;

public interface IUserService extends IService<User> {
    Result register(User user);

    void authorize(Integer userId);

    String login(User user);

    void logout();

    User check();

    void update(UserUpdateVo user);

}
