package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.dao.NotifyMapper;
import com.itheima.domain.Notification;
import com.itheima.service.INotifyService;
import org.springframework.stereotype.Service;

@Service
public class NotifyServiceImpl extends ServiceImpl<NotifyMapper, Notification> implements INotifyService {
    @Override
    public Boolean notice() {
        return null;
    }
}
