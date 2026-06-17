package com.flowershop.config;

import com.flowershop.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置：拦截器 + 跨域
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    // 映射 /uploads/** → 上传文件的实际磁盘路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = uploadDir;
        if (!new java.io.File(uploadPath).isAbsolute()) {
            uploadPath = System.getProperty("user.dir") + "/" + uploadPath;
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/**")              // 所有 /api/ 开头的请求都要拦截
                .excludePathPatterns(                     // 但以下路径放行（不需要登录）
                        "/api/auth/**",                   // 注册、登录
                        "/api/flowers/**",                // ⭐ 鲜花浏览（公开）
                        "/api/stores/**",                 // ⭐ 店铺浏览（公开）
                        "/api/upload/**",                 // ⭐ 文件上传（公开）
                        "/uploads/**",                    // ⭐ 上传图片（公开访问）
                        "/actuator/**",                   // ⭐ 健康监控（公开）
                        "/doc.html",                      // Knife4j 文档页
                        "/swagger-ui/**",                 // Swagger UI 资源
                        "/v3/api-docs/**",                // OpenAPI JSON
                        "/webjars/**",                    // Knife4j 静态资源
                        "/favicon.ico"                    // 网站图标
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                        // 所有路径允许跨域
                .allowedOriginPatterns("*")               // 允许任意来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
