package com.itheima.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.domain.Book;
import com.itheima.service.IBookService;
import com.itheima.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private IBookService bookService;

    /**
     * 借阅图书
     *
     * @param bookId
     * @return
     */
    @PostMapping("/{bookId}")
    public R borrow(@PathVariable Integer bookId) {
        boolean isBorrow = bookService.borrow(bookId);
        return new R(isBorrow);
    }

    /**
     * 归还图书
     *
     * @param bookId
     * @return
     */
    @PutMapping("/{bookId}")
    public R restore(@PathVariable Integer bookId) {
        boolean isRestore = bookService.restore(bookId);
        return new R(isRestore);
    }

    /**
     * 预订图书
     *
     * @param bookId
     * @return
     */
    @GetMapping("/{bookId}")
    public R booking(@PathVariable Integer bookId) {
        //TODO 预订图书
        return new R(bookService.booking(bookId));
    }
}
