package com.itheima.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotifyMapper extends BaseMapper<Notification> {
}
