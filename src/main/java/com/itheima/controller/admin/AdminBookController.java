package com.itheima.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.domain.Book;
import com.itheima.service.IBookService;
import com.itheima.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/book")
@Slf4j
public class AdminBookController {

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
    public Result save(@RequestBody Book book) {
        boolean flag = bookService.saveBook(book);
        if (flag) return Result.ok("添加成功^_^");
        else return Result.fail("添加失败-_-!");
    }


    /**
     * 修改图书
     *
     * @param book
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result update(@RequestBody Book book) {
        boolean flag = bookService.modify(book);
        if (flag) return Result.ok("修改成功^_^");
        else return Result.fail("修改失败-_-!");
    }


    /**
     * 删除图书
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result delete(@PathVariable Integer id) {
        boolean flag = bookService.delete(id);
        if (flag) return Result.ok("删除成功^_^");
        else return Result.fail("删除失败-_-!");
    }


    /**
     * 管理员分页模糊查询
     *
     * @param currentPage
     * @param pageSize
     * @param book
     * @return
     */
    @GetMapping("{currentPage}/{pageSize}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result findBook(@PathVariable int currentPage, @PathVariable int pageSize, Book book) {

        IPage<Book> page = bookService.getAdminPage(currentPage, pageSize, book);
        //如果当前页码值大于总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = bookService.getAdminPage((int) page.getPages(), pageSize, book);
        }
        return Result.ok(page);
    }
}
