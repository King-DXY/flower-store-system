package com.flowershop.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置
 * <p>
 * 注册分页插件，扫描 Mapper 接口。
 */
@Configuration
@MapperScan("com.flowershop.mapper")  // 自动扫描 Mapper 接口，不需要每个都加 @Mapper
public class MyBatisPlusConfig {

    /**
     * MyBatis-Plus 拦截器链
     * 目前只加了分页插件，后续可以加乐观锁、防全表更新等
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页插件：自动识别数据库类型，拼接分页 SQL
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 单页最大 100 条，防止恶意拉取全量数据
        paginationInterceptor.setMaxLimit(100L);
        interceptor.addInnerInterceptor(paginationInterceptor);

        return interceptor;
    }
}
