package com.planb.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知和提醒表
 */
@Data
@Table("Notifications")
@ApiModel("通知和提醒表")
public class Notification {
    @Id(keyType = KeyType.Auto)
    @ApiModelProperty("通知id")
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