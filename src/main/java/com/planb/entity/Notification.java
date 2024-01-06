package com.planb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    /**
     * 通知ID
     */
    @TableId(type = IdType.AUTO)
    private Integer notificationId;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Integer userId;

    /**
     * 通知类型
     */
    @ApiModelProperty("通知类型")
    private String notificationType;

    /**
     * 通知内容
     */
    @ApiModelProperty("通知内容")
    private String notificationContent;

    /**
     * 发送日期
     */
    @ApiModelProperty("发送日期")
    private LocalDateTime sentDate;
}