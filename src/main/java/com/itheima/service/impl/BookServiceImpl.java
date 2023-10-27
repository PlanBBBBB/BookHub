package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import com.itheima.utils.Result;
import com.itheima.vo.UserPageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        book.setReservationCount(0);
        book.setPublicationDate(LocalDateTime.now());
        book.setIsDeleted("1");
        int i = bookMapper.insert(book);
        adminAction(book.getBookId(), CrudOperation.INSERT);
        return i > 0;
    }

    @Override
    public boolean modify(Book book) {
        adminAction(book.getBookId(), CrudOperation.UPDATE);
        return bookMapper.updateById(book) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        adminAction(id, CrudOperation.DELETE);
        Book book = getById(id);
        book.setIsDeleted("0");
        return bookMapper.updateById(book) > 0;
    }


    private void adminAction(Integer bookId, Enum type) {
        Integer userId = getUserId();
        AdminAction adminAction = new AdminAction();
        adminAction.setActionDate(LocalDateTime.now());
        adminAction.setActionType(String.valueOf(type));
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
    public IPage<Book> getAdminPage(int currentPage, int pageSize, Book book) {
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(book.getTitle()), Book::getTitle, "%" + book.getTitle() + "%")
                .like(Strings.isNotEmpty(book.getAuthor()), Book::getAuthor, "%" + book.getAuthor() + "%")
                .like(Strings.isNotEmpty(book.getISBN()), Book::getISBN, "%" + book.getISBN() + "%");
        IPage<Book> page = new Page<>(currentPage, pageSize);
        bookMapper.selectPage(page, lqw);
        return page;
    }


    @Override
    public Result borrow(Integer bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book == null || book.getIsDeleted().equals("0")) {
            return Result.fail("图书不存在");
        }
        if (book.getStatus().equals("0")) {
            return Result.fail("图书库存不足，无法借阅");
        }

        // 已经借阅
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowedBook::getBookId, bookId).eq(BorrowedBook::getUserId, getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectOne(queryWrapper);
        if (borrowedBook != null && borrowedBook.getBorrowDate() != null && borrowedBook.getReturnDate() == null) {
            return Result.fail("您已借阅过该图书，请先归还");
        }

        // 曾经借阅
        if (borrowedBook != null && borrowedBook.getBorrowDate() != null && borrowedBook.getReturnDate() != null) {

            UpdateWrapper<BorrowedBook> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("return_date", null); // 设置returnDate为null
            updateWrapper.eq("record_id", borrowedBook.getRecordId()); // 指定更新条件
            updateWrapper.set("borrow_date", LocalDateTime.now());
            borrowBookMapper.update(null, updateWrapper);
        }

        // 未曾借阅，覆盖以前的借阅记录
        if (borrowedBook == null) {
            borrowedBook = new BorrowedBook();
            borrowedBook.setBorrowDate(LocalDateTime.now());
            borrowedBook.setBookId(bookId);
            borrowedBook.setUserId(getUserId());
            borrowBookMapper.insert(borrowedBook);
        }
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

        return Result.ok("借阅成功");
    }

    @Override
    public Result restore(Integer bookId) {
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowedBook::getBookId, bookId).eq(BorrowedBook::getUserId, getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectOne(queryWrapper);
        if (borrowedBook == null || borrowedBook.getReturnDate() != null) {
            return Result.fail("还未借阅该书");
        }
        borrowedBook.setReturnDate(LocalDateTime.now());
        borrowBookMapper.updateById(borrowedBook);
        Book book = bookMapper.selectById(bookId);
        Integer stock = book.getStock();
        stock++;
        book.setStock(stock);
        book.setStatus("1");
        bookMapper.updateById(book);
        return Result.ok("归还成功");
    }

    @Override
    public Result booking(Integer bookId) {
        // 图书被删除或不存在
        Book book = bookMapper.selectById(bookId);
        if (book == null || book.getIsDeleted().equals("0")) {
            return Result.fail("图书不存在");
        }
        // 有库存，无需预约
        if (book.getStatus().equals("1")) {
            return Result.fail("图书有库存，无需预约");
        }

        // 已借阅，未归还
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowedBook::getBookId, bookId).eq(BorrowedBook::getUserId, getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectOne(queryWrapper);
        if (borrowedBook != null && borrowedBook.getReturnDate() == null) {
            return Result.fail("已借阅，请先归还图书");
        }

        // 已经预订
        LambdaQueryWrapper<PreOderBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PreOderBook::getBookId, bookId).eq(PreOderBook::getUserId, getUserId());
        PreOderBook preOderBook = preOderBookMapper.selectOne(wrapper);
        if (preOderBook != null) {
            return Result.fail("已预订，请勿重复预订");
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

        return Result.ok("预订成功");
    }

    @Override
    public IPage<UserPageVo> getUserPage(int currentPage, int pageSize, Book book) {
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(book.getTitle()), Book::getTitle, "%" + book.getTitle() + "%")
                .like(Strings.isNotEmpty(book.getAuthor()), Book::getAuthor, "%" + book.getAuthor() + "%")
                .like(Strings.isNotEmpty(book.getISBN()), Book::getISBN, "%" + book.getISBN() + "%")
                .eq(Strings.isNotEmpty(book.getStatus()), Book::getStatus, book.getStatus())//有库存
                .eq(Book::getIsDeleted, "1");//未被删除
        IPage<Book> page = new Page<>(currentPage, pageSize);
        bookMapper.selectPage(page, lqw);
        List<Book> records = page.getRecords();
        List<UserPageVo> list = records.stream().map(book1 -> {
            UserPageVo userPageVo = new UserPageVo();
            userPageVo.setTitle(book1.getTitle());
            userPageVo.setAuthor(book1.getAuthor());
            userPageVo.setISBN(book1.getISBN());
            userPageVo.setPublicationDate(book1.getPublicationDate());
            return userPageVo;
        }).collect(Collectors.toList());

        Page<UserPageVo> userPageVoPage = new Page<>();
        userPageVoPage.setPages(page.getPages());
        userPageVoPage.setCurrent(page.getCurrent());
        userPageVoPage.setSize(page.getSize());
        userPageVoPage.setTotal(page.getTotal());
        userPageVoPage.setRecords(list);
        return userPageVoPage;
    }

}
