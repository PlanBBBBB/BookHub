package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.constant.EmailTitle;
import com.itheima.constant.NotificationMessages;
import com.itheima.constant.TransactionStatus;
import com.itheima.dao.*;
import com.itheima.domain.*;
import com.itheima.service.INotifyService;
import com.itheima.utils.MailDemoSum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotifyServiceImpl extends ServiceImpl<NotifyMapper, Notification> implements INotifyService {

    @Resource
    private NotifyMapper notifyMapper;
    @Resource
    private PreOderBookMapper preOderBookMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private BorrowBookMapper borrowBookMapper;

    @Override
    public Boolean bookingNotice() {
        List<PreOderBook> preOderBooks = preOderBookMapper.selectList(null);
        for (PreOderBook preOderBook : preOderBooks) {
            Integer bookId = preOderBook.getBookId();
            Book book = bookMapper.selectById(bookId);
            if (book.getStatus().equals("1")) {
                String email = userMapper.selectById(preOderBook.getUserId()).getEmail();
                String emailTitle = EmailTitle.BOOKING_TITLE;
                sendEmail(email, NotificationMessages.BOOKING_MESSAGE, emailTitle, bookId);
                Notification notification = new Notification();
                notification.setNotificationType(TransactionStatus.BOOKING);
                notification.setSentDate(LocalDateTime.now());
                notification.setUserId(preOderBook.getUserId());
                notification.setNotificationContent(NotificationMessages.BOOKING_MESSAGE);
                notifyMapper.insert(notification);
            }
        }
        return true;
    }


    @Override
    public Boolean returnNotice() {
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(BorrowedBook::getReturnDate)
                .ge(BorrowedBook::getBorrowDate, LocalDateTime.now().minusDays(30))
                .lt(BorrowedBook::getBorrowDate, LocalDateTime.now().minusDays(20));
        List<BorrowedBook> borrowedBooks = borrowBookMapper.selectList(queryWrapper);
        return sendNotification(borrowedBooks, NotificationMessages.RETURN_SOON_MESSAGE, TransactionStatus.RETURN);
    }


    @Override
    public Boolean overdueNotice() {
        LambdaQueryWrapper<BorrowedBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(BorrowedBook::getReturnDate)
                .ge(BorrowedBook::getBorrowDate, LocalDateTime.now().minusDays(30));
        List<BorrowedBook> borrowedBooks = borrowBookMapper.selectList(queryWrapper);
        return sendNotification(borrowedBooks, NotificationMessages.OVERDUE_MESSAGE, TransactionStatus.OVERDUE);
    }

    private Boolean sendNotification(List<BorrowedBook> borrowedBooks, String message, TransactionStatus transactionStatus) {
        for (BorrowedBook borrowedBook : borrowedBooks) {
            Integer userId = borrowedBook.getUserId();
            User user = userMapper.selectById(userId);
            String emailTitle = "";
            if (transactionStatus.equals(TransactionStatus.RETURN)) {
                emailTitle = EmailTitle.RETURN_TITLE;
            } else if (transactionStatus.equals(TransactionStatus.OVERDUE)) {
                emailTitle = EmailTitle.OVERDUE_TITLE;
            }
            sendEmail(user.getEmail(), message, emailTitle, borrowedBook.getBookId());

            Notification notification = new Notification();
            notification.setNotificationType(transactionStatus);
            notification.setSentDate(LocalDateTime.now());
            notification.setUserId(userId);
            notification.setNotificationContent(message);
            notifyMapper.insert(notification);
        }
        return true;
    }

    private void sendEmail(String email, String returnSoonMessage, String emailTitle, Integer bookId) {
        Book book = bookMapper.selectById(bookId);
        String title = book.getTitle();
        String message = returnSoonMessage + NotificationMessages.BOOK_TITLE_MESSAGE + title;
        try {
            MailDemoSum.sendEmail(email, emailTitle, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
