package com.planb.controller.common;

import com.planb.constant.UserConstants;
import com.planb.service.IUserService;
import com.planb.utils.Result;
import com.planb.vo.UserLoginVo;
import com.planb.vo.UserRegisterVo;
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

    /**
     * 注册
     *
     * @param userRegisterVo
     * @return
     */
    @PostMapping("/register")
    @ApiOperation("注册")
    public Result register(@RequestBody UserRegisterVo userRegisterVo) {
        return userService.register(userRegisterVo);
    }

    /**
     * 登录
     *
     * @param userLoginVo
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public Result login(@RequestBody UserLoginVo userLoginVo) {
        String jwt = userService.login(userLoginVo);
        return Result.ok(jwt);
    }

    /**
     * 登出
     *
     * @return
     */
    @GetMapping("/logout")
    @ApiOperation("登出")
    public Result logout() {
        userService.logout();
        return Result.ok(UserConstants.LOGOUT_SUCCESS);
    }

}
