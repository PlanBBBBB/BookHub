package com.itheima.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 借阅记录表
 */
@Data
@TableName("borrowedbook")
public class BorrowedBook {
    @TableId(type = IdType.AUTO)
    private Integer recordId;
    private Integer userId;
    private Integer bookId;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;

}
