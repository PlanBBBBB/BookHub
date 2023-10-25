package com.itheima.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书管理员操作记录表
 */
@Data
@TableName("adminactions")
public class AdminAction {
    @TableId(type = IdType.AUTO)
    private Integer recordId;
    private Enum actionType;
    private Integer adminUserId;
    private Integer bookId;
    private LocalDateTime actionDate;

}