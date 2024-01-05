package com.planb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.planb.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotifyMapper {
    /**
     * 新增通知记录
     *
     * @param notification
     */
    void insertNotify(Notification notification);
}
