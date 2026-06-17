package com.flowershop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 鲜花商城 Spring Boot 启动类
 */
@SpringBootApplication
public class FlowerStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowerStoreApplication.class, args);
        System.out.println("========================================");
        System.out.println("  鲜花商城 API 启动成功！");
        System.out.println("  Knife4j 接口文档: http://localhost:18080/doc.html");
        System.out.println("========================================");
    }
}
