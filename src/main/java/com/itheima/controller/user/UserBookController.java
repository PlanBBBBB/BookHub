package com.itheima.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.service.IBookService;
import com.itheima.utils.Result;
import com.itheima.dto.UserPageDto;
import com.itheima.vo.UserGetPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/book")
@Api(tags = "用户图书相关接口")
@PreAuthorize("hasAuthority('USER')")
public class UserBookController {

    @Resource
    private IBookService bookService;

    @PostMapping("/borrow/{bookId}")
    @ApiOperation("借阅图书")
    public Result borrow(@PathVariable Integer bookId) {
        return bookService.borrow(bookId);
    }

    @PutMapping("/restore/{bookId}")
    @ApiOperation("归还图书")
    public Result restore(@PathVariable Integer bookId) {
        return bookService.restore(bookId);
    }

    @PostMapping("/booking/{bookId}")
    @ApiOperation("预订图书")
    public Result booking(@PathVariable Integer bookId) {
        return bookService.booking(bookId);
    }


    @GetMapping("/finding")
    @ApiOperation("普通用户分页模糊查询")
    public Result findBook(@RequestBody UserGetPageVo userGetPageVo) {
        int currentPage = userGetPageVo.getCurrentPage();
        IPage<UserPageDto> page = bookService.getUserPage(userGetPageVo);
        //如果当前页码值大于总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            userGetPageVo.setCurrentPage((int) page.getPages());
            page = bookService.getUserPage(userGetPageVo);
        }
        return Result.ok(page);
    }
}
