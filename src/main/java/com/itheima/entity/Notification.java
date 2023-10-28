package com.itheima.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知和提醒表
 */
@Data
@TableName("Notifications")
@ApiModel("通知和提醒表")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Integer notificationId;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("通知类型")
    private String notificationType;

    @ApiModelProperty("通知内容")
    private String notificationContent;

    @ApiModelProperty("发送日期")
    private LocalDateTime sentDate;
}