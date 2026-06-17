package com.flowershop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j (Swagger 增强) 配置
 * <p>
 * 启动后访问 http://localhost:8080/doc.html 查看 API 文档，
 * 可以在页面上直接调试所有接口。
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("鲜花商城 API 文档")
                        .version("1.0.0")
                        .description("鲜花商城系统后端接口 —— 支持顾客浏览/购物车/下单 + 花店管理库存/销售")
                        .contact(new Contact()
                                .name("开发者")
                                .email("dev@flowershop.com")));
    }
}
