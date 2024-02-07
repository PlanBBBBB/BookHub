package com.planb.dao;

import com.mybatisflex.core.BaseMapper;
import com.planb.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
