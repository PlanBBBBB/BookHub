package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.constant.CrudOperation;
import com.itheima.dao.AdminActionMapper;
import com.itheima.dao.BookMapper;
import com.itheima.dao.BorrowBookMapper;
import com.itheima.dao.PreOderBookMapper;
import com.itheima.domain.*;
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

    @Resource
    private PreOderBookMapper preOderBookMapper;

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

        // 添加借书记录
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowedBook::getBookId, bookId).eq(BorrowedBook::getUserId, getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectOne(queryWrapper);
        borrowedBook.setBorrowDate(LocalDateTime.now());
        borrowedBook.setBookId(bookId);
        borrowedBook.setUserId(getUserId());
        borrowBookMapper.updateById(borrowedBook);

        // 更新图书库存，状态
        Integer stock = book.getStock();
        stock--;
        if (stock <= 0) book.setStatus("0");
        book.setStock(stock);
        bookMapper.updateById(book);

        // 删除预约记录，如果有的话
        LambdaQueryWrapper<PreOderBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PreOderBook::getBookId, bookId).eq(PreOderBook::getUserId, getUserId());
        PreOderBook preOderBook = preOderBookMapper.selectOne(wrapper);
        if (preOderBook != null) {
            preOderBookMapper.deleteById(preOderBook.getRecordId());
        }

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
        // 有库存，无需预约
        Book book = bookMapper.selectById(bookId);
        if (book.getStatus().equals("1")) {
            return false;
        }

        // 已借阅，未归还
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowedBook::getBookId, bookId).eq(BorrowedBook::getUserId, getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectOne(queryWrapper);
        if (borrowedBook != null && borrowedBook.getReturnDate() == null) {
            return false;
        }

        // 已经预订
        LambdaQueryWrapper<PreOderBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PreOderBook::getBookId, bookId).eq(PreOderBook::getUserId, getUserId());
        PreOderBook preOderBook = preOderBookMapper.selectOne(wrapper);
        if (preOderBook != null) {
            return false;
        }

        // 可以预订
        // 1.预约人数加一
        Integer reservationCount = book.getReservationCount();
        reservationCount++;
        book.setReservationCount(reservationCount);
        bookMapper.updateById(book);
        //添加预订记录
        preOderBook = new PreOderBook();
        preOderBook.setBookingDate(LocalDateTime.now());
        preOderBook.setBookId(bookId);
        preOderBook.setUserId(getUserId());
        preOderBookMapper.insert(preOderBook);

        return true;
    }

}
