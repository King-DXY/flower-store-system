package com.flowershop.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类 — 负责生成和解析 Token
 */
public class JwtUtil {

    private static final String DEFAULT_SECRET = "flower-store-system-secret-key-2024-min-32-chars!!";
    private static final long DEFAULT_EXPIRATION = 86400000L; // 24小时

    private static String secret = DEFAULT_SECRET;
    private static long expiration = DEFAULT_EXPIRATION;

    /** 由 JwtConfig 在启动时调用，注入 yml 配置 */
    public static void init(String secret, long expiration) {
        if (secret != null && secret.length() >= 32) {
            JwtUtil.secret = secret;
        }
        if (expiration > 0) {
            JwtUtil.expiration = expiration;
        }
    }

    // ==================== 生成 Token ====================

    /**
     * @param userId 用户 ID（customer_id 或 store_id）
     * @param role   角色：CUSTOMER 或 STORE
     */
    public static String generateToken(Integer userId, String role) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    // ==================== 解析 Token ====================

    public static Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean isExpired(String token) {
        try {
            return parseToken(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public static Integer getUserId(String token) {
        return parseToken(token).get("userId", Integer.class);
    }

    public static String getRole(String token) {
        return parseToken(token).get("role", String.class);
    }
}
