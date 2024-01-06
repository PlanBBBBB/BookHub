package com.planb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.planb.entity.User;
import com.planb.utils.Result;
import com.planb.vo.AdminGetPageVo;
import com.planb.vo.UserLoginVo;
import com.planb.vo.UserRegisterVo;
import com.planb.vo.UserUpdateVo;

public interface IUserService {
    /**
     * 注册
     *
     * @param userRegisterVo
     * @return
     */
    Result register(UserRegisterVo userRegisterVo);

    /**
     * 授权
     *
     * @param userId
     * @return
     */
    boolean authorize(Integer userId);

    /**
     * 登录
     *
     * @param userLoginVo
     * @return
     */
    String login(UserLoginVo userLoginVo);

    /**
     * 登出
     */
    void logout();

    /**
     * 查看个人信息
     *
     * @return
     */
    User check();

    /**
     * 修改个人信息
     *
     * @param user
     */
    void update(UserUpdateVo user);

    /**
     * 分页查询用户信息
     *
     * @param adminGetPageVo
     * @return
     */
    IPage<User> getUserPage(AdminGetPageVo adminGetPageVo);
}
