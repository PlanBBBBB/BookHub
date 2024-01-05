package com.planb.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.planb.constant.CrudOperation;
import com.planb.constant.BookConstants;
import com.planb.dao.AdminActionMapper;
import com.planb.dao.BookMapper;
import com.planb.dao.BorrowBookMapper;
import com.planb.dao.PreOrderBookMapper;
import com.planb.entity.*;
import com.planb.security.LoginUser;
import com.planb.service.IBookService;
import com.planb.utils.Result;
import com.planb.dto.UserPageDto;
import com.planb.vo.AddBookVo;
import com.planb.vo.UserGetPageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookServiceImpl implements IBookService {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private AdminActionMapper adminActionMapper;

    @Resource
    private BorrowBookMapper borrowBookMapper;

    @Resource
    private PreOrderBookMapper preOderBookMapper;

    @Override
    public boolean addBook(AddBookVo addBookVo) {
        Book book = new Book();
        book.setISBN(addBookVo.getISBN());
        book.setAuthor(addBookVo.getAuthor());
        book.setTitle(addBookVo.getTitle());
        book.setPublicationDate(addBookVo.getPublicationDate());
        book.setStock(addBookVo.getStock());
        book.setImage(addBookVo.getImage());
        int i = bookMapper.addBook(book);
        adminAction(book.getBookId(), CrudOperation.INSERT);
        return i > 0;
    }

    @Override
    public boolean modify(Book book) {
        adminAction(book.getBookId(), CrudOperation.UPDATE);
        return bookMapper.update(book) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        Book book = bookMapper.getById(id);
        if (book.getIsDeleted().equals("1")) return false;
        book.setIsDeleted("1");
        adminAction(id, CrudOperation.DELETE);
        bookMapper.update(book);
        return true;
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
        PageHelper.startPage(userGetPageVo.getCurrentPage(), userGetPageVo.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("title", userGetPageVo.getTitle());
        paramMap.put("author", userGetPageVo.getAuthor());
        paramMap.put("ISBN", userGetPageVo.getISBN());
        paramMap.put("isDeleted", null);

        List<Book> books = bookMapper.selectBooksByConditions(paramMap);
        PageInfo<Book> pageInfo = new PageInfo<>(books);

        IPage<Book> page = new Page<>();
        page.setPages(page.getPages());
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageSize());
        page.setTotal(pageInfo.getTotal());
        page.setRecords(books);
        return page;
    }


    @Override
    public Result borrow(Integer bookId) {
        Book book = bookMapper.getById(bookId);
        if (book == null || book.getIsDeleted().equals("1")) {
            return Result.fail(BookConstants.BOOK_NOT_EXIST);
        }
        if (book.getStatus().equals("0")) {
            return Result.fail(BookConstants.INSUFFICIENT_STOCK);
        }

        // 已经借阅
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bookId", bookId);
        paramMap.put("userId", getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectBorrowedBook(paramMap);
        if (borrowedBook != null && borrowedBook.getBorrowDate() != null && borrowedBook.getReturnDate() == null) {
            return Result.fail(BookConstants.ALREADY_BORROWED);
        }

        // 曾经借阅
        if (borrowedBook != null && borrowedBook.getBorrowDate() != null && borrowedBook.getReturnDate() != null) {
            Map<String, Object> paramMap2 = new HashMap<>();
            paramMap.put("returnDate", null);
            paramMap.put("recordId", borrowedBook.getRecordId());
            paramMap.put("borrowDate", LocalDateTime.now());
            borrowBookMapper.updateBorrowedBook(paramMap2);
        }

        // 未曾借阅，覆盖以前的借阅记录
        if (borrowedBook == null) {
            borrowedBook = new BorrowedBook();
            borrowedBook.setBorrowDate(LocalDateTime.now());
            borrowedBook.setBookId(bookId);
            borrowedBook.setUserId(getUserId());
            borrowBookMapper.insertBorrowedBook(borrowedBook);
        }
        // 更新图书库存，状态
        Integer stock = book.getStock();
        stock--;
        if (stock <= 0) book.setStatus("0");
        book.setStock(stock);
        bookMapper.update(book);

        // 删除预约记录，如果有的话
        Map<String, Object> paramMap3 = new HashMap<>();
        paramMap3.put("bookId", bookId);
        paramMap3.put("userId", getUserId());
        PreOrderBook preOrderBook = preOderBookMapper.selectPreOrderBook(paramMap3);
        if (preOrderBook != null) {
            preOderBookMapper.deleteById(preOrderBook.getRecordId());
        }

        return Result.ok(BookConstants.BORROW_SUCCESS);
    }

    @Override
    public Result restore(Integer bookId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bookId", bookId);
        paramMap.put("userId", getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectBorrowedBook(paramMap);
        if (borrowedBook == null || borrowedBook.getReturnDate() != null) {
            return Result.fail(BookConstants.NOT_YET_BORROWED);
        }
        HashMap<String, Object> paramMap2 = new HashMap<>();
        paramMap2.put("recordId", borrowedBook.getRecordId());
        paramMap2.put("returnDate", LocalDateTime.now());
        borrowBookMapper.updateById(paramMap2);
        Book book = bookMapper.getById(bookId);
        Integer stock = book.getStock();
        stock++;
        book.setStock(stock);
        book.setStatus("1");
        bookMapper.update(book);
        return Result.ok(BookConstants.RETURN_SUCCESS);
    }

    @Override
    public Result booking(Integer bookId) {
        // 图书被删除或不存在
        Book book = bookMapper.getById(bookId);
        if (book == null || book.getIsDeleted().equals("1")) {
            return Result.fail(BookConstants.BOOK_NOT_EXIST);
        }
        // 有库存，无需预约
        if (book.getStatus().equals("1")) {
            return Result.fail(BookConstants.SUFFICIENT_STOCK_NO_RESERVATION);
        }

        // 已借阅，未归还
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bookId", bookId);
        paramMap.put("userId", getUserId());
        BorrowedBook borrowedBook = borrowBookMapper.selectBorrowedBook(paramMap);
        if (borrowedBook != null && borrowedBook.getReturnDate() == null) {
            return Result.fail(BookConstants.ALREADY_BORROWED_RETURN_FIRST);
        }

        // 已经预订
        Map<String, Object> paramMap2 = new HashMap<>();
        paramMap2.put("bookId", bookId);
        paramMap2.put("userId", getUserId());
        PreOrderBook preOrderBook = preOderBookMapper.selectPreOrderBook(paramMap2);
        if (preOrderBook != null) {
            return Result.fail(BookConstants.ALREADY_RESERVED);
        }

        // 可以预订
        // 1.预约人数加一
        Integer reservationCount = book.getReservationCount();
        reservationCount++;
        book.setReservationCount(reservationCount);
        bookMapper.update(book);

        //添加预订记录
        preOrderBook = new PreOrderBook();
        preOrderBook.setBookingDate(LocalDateTime.now());
        preOrderBook.setBookId(bookId);
        preOrderBook.setUserId(getUserId());
        preOderBookMapper.insertPreOderBook(preOrderBook);

        return Result.ok(BookConstants.RESERVATION_SUCCESS);
    }

    @Override
    public IPage<UserPageDto> getUserPage(UserGetPageVo userGetPageVo) {
        PageHelper.startPage(userGetPageVo.getCurrentPage(), userGetPageVo.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("title", userGetPageVo.getTitle());
        paramMap.put("author", userGetPageVo.getAuthor());
        paramMap.put("ISBN", userGetPageVo.getISBN());
        paramMap.put("isDeleted", 0);

        List<Book> books = bookMapper.selectBooksByConditions(paramMap);
        PageInfo<Book> pageInfo = new PageInfo<>(books);

        List<UserPageDto> list = books.stream().map(book1 -> {
            UserPageDto userPageDto = new UserPageDto();
            userPageDto.setBookId(book1.getBookId());
            userPageDto.setTitle(book1.getTitle());
            userPageDto.setAuthor(book1.getAuthor());
            userPageDto.setISBN(book1.getISBN());
            userPageDto.setPublicationDate(book1.getPublicationDate());
            userPageDto.setStatus(book1.getStatus());
            userPageDto.setImage(book1.getImage());
            Map<String, Object> paramMap2 = new HashMap<>();
            paramMap2.put("bookId", book1.getBookId());
            paramMap2.put("userId", getUserId());
            BorrowedBook borrowedBook = borrowBookMapper.selectBorrowedBook(paramMap2);
            if (borrowedBook == null)
                userPageDto.setIsBorrowed("0");
            else if (borrowedBook.getReturnDate() != null)
                userPageDto.setIsBorrowed("0");
            else
                userPageDto.setIsBorrowed("1");
            return userPageDto;
        }).collect(Collectors.toList());

        Page<UserPageDto> userPageVoPage = new Page<>();
        userPageVoPage.setPages(pageInfo.getPages());
        userPageVoPage.setCurrent(pageInfo.getPageNum());
        userPageVoPage.setSize(pageInfo.getSize());
        userPageVoPage.setTotal(pageInfo.getTotal());
        userPageVoPage.setRecords(list);
        return userPageVoPage;
    }

    @Override
    public Result findBookById(Integer bookId) {
        Book book = bookMapper.getById(bookId);
        if (book == null)
            return Result.fail(BookConstants.BOOK_NOT_EXIST);
        return Result.ok(book);
    }


}
