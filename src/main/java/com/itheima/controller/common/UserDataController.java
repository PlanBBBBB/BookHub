package com.itheima.controller.common;

import com.itheima.constant.UserConstants;
import com.itheima.entity.User;
import com.itheima.service.IUserService;
import com.itheima.utils.Result;
import com.itheima.vo.UserUpdateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(tags = "用户信息模块相关接口")
public class UserDataController {

    @Resource
    private IUserService userService;


    @GetMapping("/check")
    @ApiOperation("查看个人资料")
    public Result check() {
        User user = userService.check();
        return Result.ok(user);
    }


    @PostMapping
    @ApiOperation("修改个人资料")
    public Result update(@RequestBody UserUpdateVo user) {
        userService.update(user);
        return Result.ok(UserConstants.UPDATE_PROFILE_SUCCESS);
    }

}
