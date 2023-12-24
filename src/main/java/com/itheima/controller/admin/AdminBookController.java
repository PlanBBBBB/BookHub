package com.itheima.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.constant.BookConstants;
import com.itheima.entity.Book;
import com.itheima.service.IBookService;
import com.itheima.utils.Result;
import com.itheima.vo.AddBookVo;
import com.itheima.vo.UserGetPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/book")
@Slf4j
@Api(tags = "管理员图书相关接口")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminBookController {

    @Resource
    private IBookService bookService;

    @PostMapping
    @ApiOperation("新增图书")
    public Result add(@RequestBody AddBookVo addBookVo) {
        boolean flag = bookService.addBook(addBookVo);
        if (flag) return Result.ok(BookConstants.BOOK_ADDED_SUCCESS);
        else return Result.fail(BookConstants.BOOK_ADDED_FAILED);
    }


    @PutMapping
    @ApiOperation("修改图书")
    public Result update(@RequestBody Book book) {
        boolean flag = bookService.modify(book);
        if (flag) return Result.ok(BookConstants.BOOK_MODIFIED_SUCCESS);
        else return Result.fail(BookConstants.BOOK_MODIFIED_FAILED);
    }


    @DeleteMapping("{id}")
    @ApiOperation("删除图书")
    public Result delete(@PathVariable Integer id) {
        boolean flag = bookService.delete(id);
        if (flag) return Result.ok(BookConstants.BOOK_DELETED_SUCCESS);
        else return Result.fail(BookConstants.BOOK_DELETED_FAILED);
    }


    @PostMapping("/finding")
    @ApiOperation("管理员分页模糊查询")
    public Result findBook(@RequestBody UserGetPageVo userGetPageVo) {
        log.info(userGetPageVo.toString());

        int currentPage = userGetPageVo.getCurrentPage();
        IPage<Book> page = bookService.getAdminPage(userGetPageVo);
        //如果当前页码值大于总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            userGetPageVo.setCurrentPage((int) page.getPages());
            page = bookService.getAdminPage(userGetPageVo);
        }
        return Result.ok(page);
    }

    @GetMapping("/{bookId}")
    @ApiOperation("根据id查询图书")
    public Result findBookById(@PathVariable Long bookId) {
        return bookService.findBookById(bookId);
    }
}
