package com.planb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.planb.entity.Notification;

public interface INotifyService {

    Boolean returnNotice();

    Boolean overdueNotice();

    Boolean bookingNotice();

}
