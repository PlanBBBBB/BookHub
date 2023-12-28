package com.planb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.planb.entity.Notification;

public interface INotifyService extends IService<Notification> {

    Boolean returnNotice();

    Boolean overdueNotice();

    Boolean bookingNotice();

}
