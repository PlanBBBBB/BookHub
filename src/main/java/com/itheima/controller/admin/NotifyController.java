package com.itheima.controller.admin;

import com.itheima.service.INotifyService;
import com.itheima.utils.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/notify")
public class NotifyController {

    @Resource
    private INotifyService notifyService;

    /**
     * 预订通知
     *
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public R bookingNotice() {
        return new R(notifyService.bookingNotice());
    }


    /**
     * 归还通知
     *
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public R returnNotice() {
        return new R(notifyService.returnNotice());
    }


    /**
     * 逾期通知
     *
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public R overdueNotice() {
        return new R(notifyService.overdueNotice());
    }
}
