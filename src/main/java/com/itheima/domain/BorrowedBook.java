package com.itheima.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime borrowDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime returnDate;

}
