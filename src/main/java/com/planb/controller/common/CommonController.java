package com.planb.controller.common;

import com.planb.constant.UserConstants;
import com.planb.dto.UserLoginDto;
import com.planb.dto.UserRegisterDto;
import com.planb.service.IUserService;
import com.planb.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common")
@Api(tags = "公共模块相关接口")
public class CommonController {

    @Resource
    private IUserService userService;

    @PostMapping("/register")
    @ApiOperation("注册")
    public Result register(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.register(userRegisterDto);
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public Result login(@RequestBody UserLoginDto userLoginDto) {
        String jwt = userService.login(userLoginDto);
        return Result.ok(jwt);
    }


    @GetMapping("/logout")
    @ApiOperation("登出")
    public Result logout() {
        userService.logout();
        return Result.ok(UserConstants.LOGOUT_SUCCESS);
    }

}
