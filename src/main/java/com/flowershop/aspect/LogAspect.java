package com.flowershop.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

/**
 * AOP 日志切面
 * <p>
 * 自动记录所有 Controller 方法的：
 * - 请求地址 + 参数
 * - 执行耗时
 * - 异常信息
 * <p>
 * 无需在每个方法里手写 log.info，全靠切面自动完成。
 */
@Aspect    // 声明这是一个切面
@Component // 让 Spring 管理
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 环绕通知：拦截 com.flowershop.controller 包下所有方法
     * ProceedingJoinPoint = 被拦截的方法
     */
    @Around("execution(* com.flowershop.controller..*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = "UNKNOWN";
        String url = "UNKNOWN";
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            method = request.getMethod();
            url = request.getRequestURI();
        }

        // 方法名
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        // 参数（只记录简单参数，不记录文件上传等大对象）
        String args = Arrays.toString(joinPoint.getArgs());
        if (args.length() > 200) {
            args = args.substring(0, 200) + "...";
        }

        log.info(">>> [{}] {} | {}.{} | args={}", method, url, className, methodName, args);

        try {
            Object result = joinPoint.proceed();  // 执行实际方法
            long cost = System.currentTimeMillis() - start;
            log.info("<<< [{}] {} | 耗时 {}ms | 成功", method, url, cost);
            return result;
        } catch (Throwable e) {
            long cost = System.currentTimeMillis() - start;
            log.error("<<< [{}] {} | 耗时 {}ms | 异常: {}", method, url, cost, e.getMessage());
            throw e;  // 继续抛出，让 GlobalExceptionHandler 处理
        }
    }
}
