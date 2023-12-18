package com.itheima.controller.admin;

import com.itheima.constant.UserConstants;
import com.itheima.service.IUserService;
import com.itheima.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/user")
@Api(tags = "用户管理相关接口")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Resource
    private IUserService userService;

    @PutMapping("/{userId}")
    @ApiOperation("授予管理员权限")
    public Result authorize(@PathVariable("userId") Integer userId) {
        boolean flag = userService.authorize(userId);
        if (flag) return Result.ok(UserConstants.AUTH_SUCCESS);
        else return Result.fail(UserConstants.DUPLICATE_ADMIN_ROLE);
    }
}
