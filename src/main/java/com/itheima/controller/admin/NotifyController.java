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
     * 通知
     *
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public R notice() {
        //TODO 通知
        return new R(notifyService.notice());
    }
}
