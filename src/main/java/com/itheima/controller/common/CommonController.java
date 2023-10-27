package com.itheima.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.domain.Book;
import com.itheima.domain.User;
import com.itheima.service.IBookService;
import com.itheima.service.IUserService;
import com.itheima.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    private IUserService userService;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        String jwt = userService.login(user);
        return Result.ok(jwt);
    }


    /**
     * 登出
     *
     * @return
     */
    @GetMapping("/logout")
    public Result logout() {
        userService.logout();
        return Result.ok("登出成功");
    }

}
