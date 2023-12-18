package com.itheima.controller.common;

import com.itheima.constant.UserConstants;
import com.itheima.entity.User;
import com.itheima.service.IUserService;
import com.itheima.utils.Result;
import com.itheima.vo.UserLoginVo;
import com.itheima.vo.UserRegisterVo;
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
    public Result register(@RequestBody UserRegisterVo userRegisterVo) {
        return userService.register(userRegisterVo);
    }

    @GetMapping("/login")
    @ApiOperation("登录")
    public Result login(@RequestBody UserLoginVo userLoginVo) {
        String jwt = userService.login(userLoginVo);
        return Result.ok(jwt);
    }


    @GetMapping("/logout")
    @ApiOperation("登出")
    public Result logout() {
        userService.logout();
        return Result.ok(UserConstants.LOGOUT_SUCCESS);
    }

}
