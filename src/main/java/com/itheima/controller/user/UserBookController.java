package com.itheima.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.domain.Book;
import com.itheima.service.IBookService;
import com.itheima.utils.Result;
import com.itheima.vo.UserPageVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/book")
public class UserBookController {

    @Resource
    private IBookService bookService;

    /**
     * 借阅图书
     *
     * @param bookId
     * @return
     */
    @PostMapping("/{bookId}")
    public Result borrow(@PathVariable Integer bookId) {
        return bookService.borrow(bookId);
    }

    /**
     * 归还图书
     *
     * @param bookId
     * @return
     */
    @PutMapping("/{bookId}")
    public Result restore(@PathVariable Integer bookId) {
        return bookService.restore(bookId);
    }

    /**
     * 预订图书
     *
     * @param bookId
     * @return
     */
    @GetMapping("/{bookId}")
    public Result booking(@PathVariable Integer bookId) {
        return bookService.booking(bookId);
    }

    /**
     * 普通用户分页模糊查询
     *
     * @param currentPage
     * @param pageSize
     * @param book
     * @return
     */
    @GetMapping("{currentPage}/{pageSize}")
    public Result findBook(@PathVariable int currentPage, @PathVariable int pageSize, Book book) {

        IPage<UserPageVo> page = bookService.getUserPage(currentPage, pageSize, book);
        //如果当前页码值大于总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = bookService.getUserPage((int) page.getPages(), pageSize, book);
        }
        return Result.ok(page);
    }
}
