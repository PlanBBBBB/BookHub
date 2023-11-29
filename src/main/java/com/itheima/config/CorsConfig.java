//package com.itheima.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@Slf4j
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        log.info("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//        // 设置允许跨域的路径
//        registry.addMapping("/**")
//                // 设置允许跨域请求的域名
//                .allowedOrigins("*")
//                // 是否允许携带 cookie
//                .allowCredentials(true)
//                // 设置允许的请求方式
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
//                // 设置允许的 header 属性
//                .allowedHeaders("*")
//                // 跨域允许时间
//                .maxAge(3600);
//    }
//}