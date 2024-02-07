package com.planb.dao;

import com.mybatisflex.core.BaseMapper;
import com.planb.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotifyMapper extends BaseMapper<Notification> {
}
