package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.User;

public interface IUserService extends IService<User> {
    boolean register(User user);

    void authorize(Integer userId);

    String login(User user);

    void logout();

    User check();

    void update(User user);

}
