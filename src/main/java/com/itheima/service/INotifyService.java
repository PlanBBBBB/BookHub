package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Notification;

public interface INotifyService extends IService<Notification> {

    Boolean returnNotice();

    Boolean overdueNotice();

    Boolean bookingNotice();

}
