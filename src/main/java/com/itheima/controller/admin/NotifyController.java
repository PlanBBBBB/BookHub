package com.itheima.controller.admin;

import com.itheima.service.INotifyService;
import com.itheima.utils.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public Result bookingNotice() {
        Boolean flag = notifyService.bookingNotice();
        if (flag) return Result.ok("发送预订通知成功");
        else return Result.fail("发送预订通知失败");
    }


    /**
     * 归还通知
     *
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result returnNotice() {
        Boolean flag = notifyService.returnNotice();
        if (flag) return Result.ok("发送归还通知成功");
        else return Result.fail("发送归还通知失败");
    }


    /**
     * 逾期通知
     *
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result overdueNotice() {
        Boolean flag = notifyService.overdueNotice();
        if (flag) return Result.ok("发送逾期通知成功");
        else return Result.fail("发送逾期通知失败");
    }
}
