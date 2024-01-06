package com.planb.service;

public interface INotifyService {
    /**
     * 归还通知
     *
     * @return
     */
    Boolean returnNotice();

    /**
     * 逾期通知
     *
     * @return
     */
    Boolean overdueNotice();

    /**
     * 预订通知
     *
     * @return
     */
    Boolean bookingNotice();

}
