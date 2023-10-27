package com.itheima.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知和提醒表
 */
@Data
@TableName("Notifications")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Integer notificationId;
    private Integer userId;
    private String notificationType;
    private String notificationContent;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime sentDate;
}