package com.planb;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@MapperScan("com.planb.dao")
public class BookHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookHubApplication.class, args);
        log.info("BookHub项目启动成功.......");
    }

}
