package com.planb.dao;

import com.planb.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据userId查询用户
     *
     * @param userId
     * @return
     */
    User getById(Integer userId);

    /**
     * 根据username查询用户
     *
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 新增用户
     *
     * @param user
     */
    void insertUser(User user);

    /**
     * 修改用户信息
     *
     * @param user
     */
    void update(User user);

    /**
     * 根据条件查询用户
     *
     * @param paramMap
     * @return
     */
    List<User> selectUsersByConditions(Map<String, Object> paramMap);
}
