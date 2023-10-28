package com.itheima.controller.admin;

import com.itheima.constant.NotificationMessages;
import com.itheima.service.INotifyService;
import com.itheima.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/notify")
@PreAuthorize("hasAuthority('ADMIN')")
@Api(tags = "通知管理相关接口")
public class NotifyController {

    @Resource
    private INotifyService notifyService;

    @GetMapping
    @ApiOperation("预订通知")
    public Result bookingNotice() {
        Boolean flag = notifyService.bookingNotice();
        if (flag) return Result.ok(NotificationMessages.RESERVATION_NOTIFICATION_SUCCESS);
        else return Result.fail(NotificationMessages.RESERVATION_NOTIFICATION_FAILED);
    }


    @PutMapping
    @ApiOperation("归还通知")
    public Result returnNotice() {
        Boolean flag = notifyService.returnNotice();
        if (flag) return Result.ok(NotificationMessages.RETURN_NOTIFICATION_SUCCESS);
        else return Result.fail(NotificationMessages.RETURN_NOTIFICATION_FAILED);
    }


    @PostMapping
    @ApiOperation("逾期通知")
    public Result overdueNotice() {
        Boolean flag = notifyService.overdueNotice();
        if (flag) return Result.ok(NotificationMessages.OVERDUE_NOTIFICATION_SUCCESS);
        else return Result.fail(NotificationMessages.OVERDUE_NOTIFICATION_FAILED);
    }
}
