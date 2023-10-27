package com.itheima.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("PreOrderBook")
public class PreOderBook {
    @TableId(type = IdType.AUTO)
    private Integer recordId;
    private Integer userId;
    private Integer bookId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime bookingDate;
}
