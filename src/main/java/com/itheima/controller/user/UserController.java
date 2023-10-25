package com.itheima.controller.user;

import com.itheima.domain.User;
import com.itheima.service.IUserService;
import com.itheima.utils.R;
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
    public R check() {
        User user = userService.check();
        return new R(true, user);
    }

    /**
     * 修改个人资料
     *
     * @param user
     * @return
     */
    @PostMapping
    public R update(@RequestBody User user) {
        userService.update(user);
        return new R(true);
    }


}
