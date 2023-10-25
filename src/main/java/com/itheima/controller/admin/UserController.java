package com.itheima.controller.admin;

import com.itheima.service.IUserService;
import com.itheima.utils.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 给用户授予管理员权限
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public R authorize(@PathVariable("userId") Integer userId) {
        userService.authorize(userId);
        return new R(true, "授权成功");
    }
}
