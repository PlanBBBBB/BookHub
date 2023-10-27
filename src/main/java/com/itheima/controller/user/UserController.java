package com.itheima.controller.user;

import com.itheima.domain.User;
import com.itheima.service.IUserService;
import com.itheima.utils.Result;
import com.itheima.vo.UserUpdateVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 查看个人资料
     *
     * @return
     */
    @GetMapping("/check")
    public Result check() {
        User user = userService.check();
        return Result.ok(user);
    }

    /**
     * 修改个人资料
     *
     * @param user
     * @return
     */
    @PostMapping
    public Result update(@RequestBody UserUpdateVo user) {
        userService.update(user);
        return Result.ok("修改个人信息成功");
    }

}
