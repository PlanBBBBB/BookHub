package com.itheima.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.domain.Book;
import com.itheima.domain.User;
import com.itheima.service.IBookService;
import com.itheima.service.IUserService;
import com.itheima.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    private IUserService userService;

    @Resource
    private IBookService bookService;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public R register(@RequestBody User user) {
        boolean flag = userService.register(user);
        return new R(flag, flag ? "注册成功^_^" : "注册失败-_-!");
    }

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody User user) {
        String jwt = userService.login(user);
        return new R(true, jwt);
    }


    /**
     * 登出
     *
     * @return
     */
    @GetMapping("/logout")
    public R logout() {
        userService.logout();
        return new R(true, "登出成功");
    }

    /**
     * 分页模糊查询
     *
     * @param currentPage
     * @param pageSize
     * @param book
     * @return
     */
    @GetMapping("{currentPage}/{pageSize}")
    public R findBook(@PathVariable int currentPage, @PathVariable int pageSize, Book book) {

        IPage<Book> page = bookService.getPage(currentPage, pageSize, book);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = bookService.getPage((int) page.getPages(), pageSize, book);
        }
        return new R(true, page);
    }

}
