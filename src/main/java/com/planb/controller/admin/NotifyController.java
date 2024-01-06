package com.planb.controller.admin;

import com.planb.constant.NotificationMessages;
import com.planb.service.INotifyService;
import com.planb.utils.Result;
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

    /**
     * 预订通知
     *
     * @return
     */
    @PostMapping("/bookingNotice")
    @ApiOperation("预订通知")
    public Result bookingNotice() {
        Boolean flag = notifyService.bookingNotice();
        if (flag) return Result.ok(NotificationMessages.RESERVATION_NOTIFICATION_SUCCESS);
        else return Result.fail(NotificationMessages.RESERVATION_NOTIFICATION_FAILED);
    }

    /**
     * 归还通知
     *
     * @return
     */
    @PostMapping("/returnNotice")
    @ApiOperation("归还通知")
    public Result returnNotice() {
        Boolean flag = notifyService.returnNotice();
        if (flag) return Result.ok(NotificationMessages.RETURN_NOTIFICATION_SUCCESS);
        else return Result.fail(NotificationMessages.RETURN_NOTIFICATION_FAILED);
    }

    /**
     * 逾期通知
     *
     * @return
     */
    @PostMapping("/overdueNotice")
    @ApiOperation("逾期通知")
    public Result overdueNotice() {
        Boolean flag = notifyService.overdueNotice();
        if (flag) return Result.ok(NotificationMessages.OVERDUE_NOTIFICATION_SUCCESS);
        else return Result.fail(NotificationMessages.OVERDUE_NOTIFICATION_FAILED);
    }
}
