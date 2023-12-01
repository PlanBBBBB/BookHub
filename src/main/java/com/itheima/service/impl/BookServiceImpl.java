package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.constant.CrudOperation;
import com.itheima.constant.BookConstants;
import com.itheima.dao.AdminActionMapper;
import com.itheima.dao.BookMapper;
import com.itheima.dao.BorrowBookMapper;
import com.itheima.dao.PreOderBookMapper;
import com.itheima.entity.*;
import com.itheima.security.LoginUser;
import com.itheima.service.IBookService;
import com.itheima.utils.Result;
import com.itheima.dto.UserPageDto;
import com.itheima.vo.AddBookVo;
import com.itheima.vo.UserGetPageVo;
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
    public boolean addBook(AddBookVo addBookVo) {
        Book book = new Book();
        book.setISBN(addBookVo.getISBN());
        book.setAuthor(addBookVo.getAuthor());
        book.setTitle(addBookVo.getTitle());
        book.setPublicationDate(addBookVo.getPublicationDate());
        book.setStock(addBookVo.getStock());
        book.setReservationCount(0);
        book.setStatus("1");
        book.setImage(addBookVo.getImage());
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
    public IPage<Book> getAdminPage(UserGetPageVo userGetPageVo) {
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(userGetPageVo.getTitle()), Book::getTitle, "%" + userGetPageVo.getTitle() + "%")
                .like(Strings.isNotEmpty(userGetPageVo.getAuthor()), Book::getAuthor, "%" + userGetPageVo.getAuthor() + "%")
                .like(Strings.isNotEmpty(userGetPageVo.getISBN()), Book::getISBN, "%" + userGetPageVo.getISBN() + "%");
        IPage<Book> page = new Page<>(userGetPageVo.getCurrentPage(), userGetPageVo.getPageSize());
        bookMapper.selectPage(page, lqw);
        return page;
    }


    @Override
    public Result borrow(Integer bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book == null || book.getIsDeleted().equals("0")) {
            return Result.fail(BookConstants.BOOK_NOT_EXIST);
        }
        if (book.getStatus().equals("0")) {
            return Result.fail(BookConstants.INSUFFICIENT_STOCK);
        }

        // 已经借阅
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowedBook::getBookId, bookId).eq(BorrowedBook::getUserId, getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectOne(queryWrapper);
        if (borrowedBook != null && borrowedBook.getBorrowDate() != null && borrowedBook.getReturnDate() == null) {
            return Result.fail(BookConstants.ALREADY_BORROWED);
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
        LambdaQueryWrapper<PreOrderBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PreOrderBook::getBookId, bookId).eq(PreOrderBook::getUserId, getUserId());
        PreOrderBook preOrderBook = preOderBookMapper.selectOne(wrapper);
        if (preOrderBook != null) {
            preOderBookMapper.deleteById(preOrderBook.getRecordId());
        }

        return Result.ok(BookConstants.BORROW_SUCCESS);
    }

    @Override
    public Result restore(Integer bookId) {
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowedBook::getBookId, bookId).eq(BorrowedBook::getUserId, getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectOne(queryWrapper);
        if (borrowedBook == null || borrowedBook.getReturnDate() != null) {
            return Result.fail(BookConstants.NOT_YET_BORROWED);
        }
        borrowedBook.setReturnDate(LocalDateTime.now());
        borrowBookMapper.updateById(borrowedBook);
        Book book = bookMapper.selectById(bookId);
        Integer stock = book.getStock();
        stock++;
        book.setStock(stock);
        book.setStatus("1");
        bookMapper.updateById(book);
        return Result.ok(BookConstants.RETURN_SUCCESS);
    }

    @Override
    public Result booking(Integer bookId) {
        // 图书被删除或不存在
        Book book = bookMapper.selectById(bookId);
        if (book == null || book.getIsDeleted().equals("0")) {
            return Result.fail(BookConstants.BOOK_NOT_EXIST);
        }
        // 有库存，无需预约
        if (book.getStatus().equals("1")) {
            return Result.fail(BookConstants.SUFFICIENT_STOCK_NO_RESERVATION);
        }

        // 已借阅，未归还
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowedBook::getBookId, bookId).eq(BorrowedBook::getUserId, getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectOne(queryWrapper);
        if (borrowedBook != null && borrowedBook.getReturnDate() == null) {
            return Result.fail(BookConstants.ALREADY_BORROWED_RETURN_FIRST);
        }

        // 已经预订
        LambdaQueryWrapper<PreOrderBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PreOrderBook::getBookId, bookId).eq(PreOrderBook::getUserId, getUserId());
        PreOrderBook preOrderBook = preOderBookMapper.selectOne(wrapper);
        if (preOrderBook != null) {
            return Result.fail(BookConstants.ALREADY_RESERVED);
        }

        // 可以预订
        // 1.预约人数加一
        Integer reservationCount = book.getReservationCount();
        reservationCount++;
        book.setReservationCount(reservationCount);
        bookMapper.updateById(book);

        //添加预订记录
        preOrderBook = new PreOrderBook();
        preOrderBook.setBookingDate(LocalDateTime.now());
        preOrderBook.setBookId(bookId);
        preOrderBook.setUserId(getUserId());
        preOderBookMapper.insert(preOrderBook);

        return Result.ok(BookConstants.RESERVATION_SUCCESS);
    }

    @Override
    public IPage<UserPageDto> getUserPage(UserGetPageVo userGetPageVo) {

        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(userGetPageVo.getTitle()), Book::getTitle, "%" + userGetPageVo.getTitle() + "%")
                .like(Strings.isNotEmpty(userGetPageVo.getAuthor()), Book::getAuthor, "%" + userGetPageVo.getAuthor() + "%")
                .like(Strings.isNotEmpty(userGetPageVo.getISBN()), Book::getISBN, "%" + userGetPageVo.getISBN() + "%")
                .eq(Book::getStatus, "1")//有库存
                .eq(Book::getIsDeleted, "1");//未被删除
        IPage<Book> page = new Page<>(userGetPageVo.getCurrentPage(), userGetPageVo.getPageSize());
        bookMapper.selectPage(page, lqw);
        List<Book> records = page.getRecords();
        List<UserPageDto> list = records.stream().map(book1 -> {
            UserPageDto userPageVo = new UserPageDto();
            userPageVo.setTitle(book1.getTitle());
            userPageVo.setAuthor(book1.getAuthor());
            userPageVo.setISBN(book1.getISBN());
            userPageVo.setPublicationDate(book1.getPublicationDate());
            return userPageVo;
        }).collect(Collectors.toList());

        Page<UserPageDto> userPageVoPage = new Page<>();
        userPageVoPage.setPages(page.getPages());
        userPageVoPage.setCurrent(page.getCurrent());
        userPageVoPage.setSize(page.getSize());
        userPageVoPage.setTotal(page.getTotal());
        userPageVoPage.setRecords(list);
        return userPageVoPage;
    }


}
