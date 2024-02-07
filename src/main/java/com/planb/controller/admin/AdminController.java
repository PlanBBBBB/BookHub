package com.planb.controller.admin;

import com.mybatisflex.core.paginate.Page;
import com.planb.constant.UserConstants;
import com.planb.entity.User;
import com.planb.service.IUserService;
import com.planb.vo.Result;
import com.planb.dto.AdminGetPageDto;
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

    @PostMapping("/finding")
    @ApiOperation("分页查询所有用户")
    public Result selectAllUsers(@RequestBody AdminGetPageDto adminGetPageVo) {
        int currentPage = adminGetPageVo.getCurrentPage();
        Page<User> page = userService.getUserPage(adminGetPageVo);
        //如果当前页码值大于总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getTotalPage()) {
            adminGetPageVo.setCurrentPage((int) page.getTotalPage());
            page = userService.getUserPage(adminGetPageVo);
        }
        return Result.ok(page);
    }
}
