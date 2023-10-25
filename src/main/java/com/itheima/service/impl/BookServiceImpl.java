package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.CrudOperation;
import com.itheima.dao.AdminActionMapper;
import com.itheima.dao.BookMapper;
import com.itheima.dao.BorrowBookMapper;
import com.itheima.domain.AdminAction;
import com.itheima.domain.Book;
import com.itheima.domain.BorrowedBook;
import com.itheima.domain.LoginUser;
import com.itheima.service.IBookService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private AdminActionMapper adminActionMapper;

    @Resource
    private BorrowBookMapper borrowBookMapper;

    @Override
    public boolean saveBook(Book book) {
        adminAction(book.getBookId(), CrudOperation.INSERT);
        book.setReservationCount(0);
        return bookMapper.insert(book) > 0;
    }

    @Override
    public boolean modify(Book book) {
        adminAction(book.getBookId(), CrudOperation.UPDATE);
        return bookMapper.updateById(book) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        adminAction(id, CrudOperation.DELETE);
        return bookMapper.deleteById(id) > 0;
    }


    private void adminAction(Integer bookId, Enum type) {
        Integer userId = getUserId();
        AdminAction adminAction = new AdminAction();
        adminAction.setActionDate(LocalDateTime.now());
        adminAction.setActionType(type);
        adminAction.setBookId(bookId);
        adminAction.setAdminUserId(userId);
        adminActionMapper.insert(adminAction);
    }

    private Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getUser().getUserId();
    }

    @Override
    public IPage<Book> getPage(int currentPage, int pageSize, Book book) {
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(book.getTitle()), Book::getTitle, "%" + book.getTitle() + "%")
                .like(Strings.isNotEmpty(book.getAuthor()), Book::getAuthor, "%" + book.getAuthor() + "%")
                .like(Strings.isNotEmpty(book.getISBN()), Book::getISBN, "%" + book.getISBN() + "%")
                .eq(Strings.isNotEmpty(book.getStatus()), Book::getStatus, book.getStatus());
        IPage<Book> page = new Page<>(currentPage, pageSize);
        bookMapper.selectPage(page, lqw);
        return page;
    }


    @Override
    public boolean borrow(Integer bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book.getStatus().equals("0")) {
            return false;
        }
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowedBook::getBookId, bookId).eq(BorrowedBook::getUserId, getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectOne(queryWrapper);
        borrowedBook.setBorrowDate(LocalDateTime.now());
        borrowedBook.setBookId(bookId);
        borrowedBook.setUserId(getUserId());
        borrowBookMapper.updateById(borrowedBook);
        Integer stock = book.getStock();
        stock--;
        if (stock <= 0) book.setStatus("0");
        book.setStock(stock);
        bookMapper.updateById(book);
        return true;
    }

    @Override
    public boolean restore(Integer bookId) {
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowedBook::getBookId, bookId).eq(BorrowedBook::getUserId, getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectOne(queryWrapper);
        borrowedBook.setReturnDate(LocalDateTime.now());
        borrowBookMapper.updateById(borrowedBook);
        Book book = bookMapper.selectById(bookId);
        Integer stock = book.getStock();
        stock++;
        book.setStock(stock);
        book.setStatus("1");
        bookMapper.updateById(book);
        return true;
    }

    @Override
    public Boolean booking(Integer bookId) {
        return null;

    }

}
