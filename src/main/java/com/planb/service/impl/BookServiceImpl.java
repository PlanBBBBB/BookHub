package com.planb.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.planb.constant.CrudOperation;
import com.planb.constant.BookConstants;
import com.planb.dao.AdminActionMapper;
import com.planb.dao.BookMapper;
import com.planb.dao.BorrowBookMapper;
import com.planb.dao.PreOderBookMapper;
import com.planb.dto.AddBookDto;
import com.planb.dto.UserGetPageDto;
import com.planb.entity.*;
import com.planb.security.LoginUser;
import com.planb.service.IBookService;
import com.planb.vo.Result;
import com.planb.vo.UserPageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.planb.entity.table.BookTableDef.BOOK;
import static com.planb.entity.table.BorrowedBookTableDef.BORROWED_BOOK;
import static com.planb.entity.table.PreOrderBookTableDef.PRE_ORDER_BOOK;

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
    private PreOderBookMapper preOderBookMapper;

    @Override
    public boolean addBook(AddBookDto addBookDto) {
        Book book = new Book();
        book.setISBN(addBookDto.getISBN());
        book.setAuthor(addBookDto.getAuthor());
        book.setTitle(addBookDto.getTitle());
        book.setPublicationDate(addBookDto.getPublicationDate());
        book.setStock(addBookDto.getStock());
        book.setReservationCount(0);
        book.setStatus("1");
        book.setImage(addBookDto.getImage());
        book.setIsDeleted("0");
        int i = bookMapper.insert(book);
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
        Book book = bookMapper.selectOneById(id);
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
    public Page<Book> getAdminPage(UserGetPageDto userGetPageDto) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(BOOK.TITLE.like(userGetPageDto.getTitle(),Strings.isNotEmpty(userGetPageDto.getTitle())))
                .where(BOOK.AUTHOR.like(userGetPageDto.getAuthor(),Strings.isNotEmpty(userGetPageDto.getAuthor())))
                .where(BOOK.I_S_B_N.like(userGetPageDto.getISBN(),Strings.isNotEmpty(userGetPageDto.getISBN())));
        Page<Book> page = bookMapper.paginate(userGetPageDto.getCurrentPage(), userGetPageDto.getPageSize(), queryWrapper);
        return page;
    }


    @Override
    public Result borrow(Integer bookId) {
        Book book = bookMapper.selectOneById(bookId);
        if (book == null || book.getIsDeleted().equals("1")) {
            return Result.fail(BookConstants.BOOK_NOT_EXIST);
        }
        if (book.getStatus().equals("0")) {
            return Result.fail(BookConstants.INSUFFICIENT_STOCK);
        }

        // 已经借阅
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(BORROWED_BOOK.BOOK_ID.eq(bookId)).where(BORROWED_BOOK.USER_ID.eq(getUserId()));
        BorrowedBook borrowedBook = borrowBookMapper.selectOneByQuery(queryWrapper);
        if (borrowedBook != null && borrowedBook.getBorrowDate() != null && borrowedBook.getReturnDate() == null) {
            return Result.fail(BookConstants.ALREADY_BORROWED);
        }

        // 曾经借阅
        if (borrowedBook != null && borrowedBook.getBorrowDate() != null && borrowedBook.getReturnDate() != null) {

            UpdateChain.of(BorrowedBook.class)
                    .set(BORROWED_BOOK.RETURN_DATE,null)
                    .set(BORROWED_BOOK.BORROW_DATE,LocalDateTime.now())
                    .where(BORROWED_BOOK.RECORD_ID.eq(borrowedBook.getRecordId()))
                    .update();
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
        bookMapper.update(book);

        // 删除预约记录，如果有的话
        PreOrderBook preOrderBook = QueryChain.of(preOderBookMapper)
                .select(PRE_ORDER_BOOK.ALL_COLUMNS)
                .from(PRE_ORDER_BOOK)
                .where(PRE_ORDER_BOOK.BOOK_ID.eq(bookId))
                .where(PRE_ORDER_BOOK.USER_ID.eq(getUserId()))
                .one();

        if (preOrderBook != null) {
            preOderBookMapper.deleteById(preOrderBook.getRecordId());
        }

        return Result.ok(BookConstants.BORROW_SUCCESS);
    }

    @Override
    public Result restore(Integer bookId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(BORROWED_BOOK.BOOK_ID.eq(bookId)).where(BORROWED_BOOK.USER_ID.eq(getUserId()));
        BorrowedBook borrowedBook = borrowBookMapper.selectOneByQuery(queryWrapper);
        if (borrowedBook == null || borrowedBook.getReturnDate() != null) {
            return Result.fail(BookConstants.NOT_YET_BORROWED);
        }
        borrowedBook.setReturnDate(LocalDateTime.now());
        borrowBookMapper.update(borrowedBook);
        Book book = bookMapper.selectOneById(bookId);
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
        Book book = bookMapper.selectOneById(bookId);
        if (book == null || book.getIsDeleted().equals("1")) {
            return Result.fail(BookConstants.BOOK_NOT_EXIST);
        }
        // 有库存，无需预约
        if (book.getStatus().equals("1")) {
            return Result.fail(BookConstants.SUFFICIENT_STOCK_NO_RESERVATION);
        }

        // 已借阅，未归还
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(BORROWED_BOOK.BOOK_ID.eq(bookId)).where(BORROWED_BOOK.USER_ID.eq(getUserId()));
        BorrowedBook borrowedBook = borrowBookMapper.selectOneByQuery(queryWrapper);
        if (borrowedBook != null && borrowedBook.getReturnDate() == null) {
            return Result.fail(BookConstants.ALREADY_BORROWED_RETURN_FIRST);
        }

        // 已经预订
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.where(PRE_ORDER_BOOK.BOOK_ID.eq(bookId)).where(PRE_ORDER_BOOK.USER_ID.eq(getUserId()));
        PreOrderBook preOrderBook = preOderBookMapper.selectOneByQuery(wrapper);
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
        preOderBookMapper.insert(preOrderBook);

        return Result.ok(BookConstants.RESERVATION_SUCCESS);
    }

    @Override
    public Page<UserPageVo> getUserPage(UserGetPageDto userGetPageDto) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(BOOK.TITLE.like(userGetPageDto.getTitle(),Strings.isNotEmpty(userGetPageDto.getTitle())))
                .where(BOOK.AUTHOR.like(userGetPageDto.getAuthor(),Strings.isNotEmpty(userGetPageDto.getAuthor())))
                .where(BOOK.I_S_B_N.like(userGetPageDto.getISBN(),Strings.isNotEmpty(userGetPageDto.getISBN())))
                .where(BOOK.IS_DELETED.eq("0"));//未被删除
        Page<Book> page = bookMapper.paginate(userGetPageDto.getCurrentPage(), userGetPageDto.getPageSize(), queryWrapper);

        List<Book> records = page.getRecords();
        List<UserPageVo> list = records.stream().map(book1 -> {
            UserPageVo userPageVo = new UserPageVo();
            userPageVo.setBookId(book1.getBookId());
            userPageVo.setTitle(book1.getTitle());
            userPageVo.setAuthor(book1.getAuthor());
            userPageVo.setISBN(book1.getISBN());
            userPageVo.setPublicationDate(book1.getPublicationDate());
            userPageVo.setStatus(book1.getStatus());
            userPageVo.setImage(book1.getImage());
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.where(BORROWED_BOOK.USER_ID.eq(getUserId()))
                    .where(BORROWED_BOOK.BOOK_ID.eq(book1.getBookId()));
            BorrowedBook borrowedBook = borrowBookMapper.selectOneByQuery(queryWrapper1);
            if (borrowedBook == null)
                userPageVo.setIsBorrowed("0");
            else if (borrowedBook.getReturnDate() != null)
                userPageVo.setIsBorrowed("0");
            else
                userPageVo.setIsBorrowed("1");
            return userPageVo;
        }).collect(Collectors.toList());

        Page<UserPageVo> userPageVoPage = new Page<>();
        userPageVoPage.setPageNumber(page.getPageNumber());
        userPageVoPage.setPageSize(page.getPageSize());
        userPageVoPage.setTotalPage(page.getTotalPage());
        userPageVoPage.setTotalRow(page.getTotalRow());
        userPageVoPage.setRecords(list);
        return userPageVoPage;
    }

    @Override
    public Result findBookById(Long bookId) {
        Book book = bookMapper.selectOneById(bookId);
        if (book == null)
            return Result.fail(BookConstants.BOOK_NOT_EXIST);
        return Result.ok(book);
    }


}
