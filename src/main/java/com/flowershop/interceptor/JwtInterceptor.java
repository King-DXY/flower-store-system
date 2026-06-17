package com.flowershop.interceptor;

import com.flowershop.common.BusinessException;
import com.flowershop.common.ResultCode;
import com.flowershop.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 登录拦截器
 * <p>
 * 每个需要登录的请求到达 Controller 之前，先经过这里校验 Token。
 * 校验通过 → 把 userId 和 role 放入 request 属性，Controller 可以直接取用。
 * 校验失败 → 直接抛异常返回 401。
 */
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // OPTIONS 预检请求（浏览器 CORS）直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 从请求头取 Authorization: Bearer <token>
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "请先登录");
        }

        String token = authHeader.substring(7);  // 去掉 "Bearer " 前缀

        // 检查 Token 是否过期
        if (JwtUtil.isExpired(token)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "登录已过期，请重新登录");
        }

        try {
            // 解析 Token，把用户信息存入 request
            Integer userId = JwtUtil.getUserId(token);
            String role = JwtUtil.getRole(token);

            request.setAttribute("userId", userId);
            request.setAttribute("role", role);
            return true;

        } catch (Exception e) {
            log.warn("Token 解析失败: {}", e.getMessage());
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Token 无效");
        }
    }
}
