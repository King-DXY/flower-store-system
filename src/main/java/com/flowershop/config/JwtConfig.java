package com.flowershop.config;

import com.flowershop.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

/**
 * JWT 配置 — 把 application.yml 中的配置注入到 JwtUtil
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @PostConstruct
    public void init() {
        JwtUtil.init(secret, expiration);
    }
}
