package com.planb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.planb.entity.AdminAction;
import org.apache.ibatis.annotations.Mapper;


public interface AdminActionMapper {
    /**
     * 新增管理员操作记录
     *
     * @param adminAction
     */
    void insert(AdminAction adminAction);
}
