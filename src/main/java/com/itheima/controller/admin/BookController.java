package com.itheima.controller.admin;

import com.itheima.domain.Book;
import com.itheima.service.IBookService;
import com.itheima.utils.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/book")
public class BookController {

    @Resource
    private IBookService bookService;

    /**
     * 新增图书
     *
     * @param book
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public R save(@RequestBody Book book) {
        boolean flag = bookService.saveBook(book);
        return new R(flag, flag ? "添加成功^_^" : "添加失败-_-!");
    }


    /**
     * 修改图书
     *
     * @param book
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public R update(@RequestBody Book book) {
        boolean flag = bookService.modify(book);
        return new R(flag, flag ? "修改成功^_^" : "修改失败-_-!");
    }


    /**
     * 删除图书
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public R delete(@PathVariable Integer id) {
        return new R(bookService.delete(id));
    }

}
