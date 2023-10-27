package com.itheima.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("PreOderBook")
public class PreOderBook {
    @TableId(type = IdType.AUTO)
    private Integer recordId;
    private Integer userId;
    private Integer bookId;
    private LocalDateTime bookingDate;
}
