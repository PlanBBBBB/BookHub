package com.planb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.planb.constant.EmailTitle;
import com.planb.constant.NotificationMessages;
import com.planb.constant.TransactionStatus;
import com.planb.dao.*;
import com.planb.entity.*;
import com.planb.service.INotifyService;
import com.planb.utils.MailDemoSum;
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
        List<PreOrderBook> preOrderBooks = preOderBookMapper.selectList(null);
        for (PreOrderBook preOrderBook : preOrderBooks) {
            Integer bookId = preOrderBook.getBookId();
            Book book = bookMapper.selectById(bookId);
            if (book.getStatus().equals("1")) {
                String email = userMapper.selectById(preOrderBook.getUserId()).getEmail();
                String emailTitle = EmailTitle.BOOKING_TITLE;
                sendEmail(email, NotificationMessages.BOOKING_MESSAGE, emailTitle, bookId);
                Notification notification = new Notification();
                notification.setNotificationType(String.valueOf(TransactionStatus.BOOKING));
                notification.setSentDate(LocalDateTime.now());
                notification.setUserId(preOrderBook.getUserId());
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
                .lt(BorrowedBook::getBorrowDate, LocalDateTime.now().minusDays(30));
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
            notification.setNotificationType(String.valueOf(transactionStatus));
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
