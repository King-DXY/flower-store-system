package com.flowershop.controller;

import com.flowershop.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 * <p>
 * 上传的图片保存在项目根目录下的 uploads/ 文件夹，
 * 访问地址：http://localhost:18080/uploads/文件名
 */
@RestController
@RequestMapping("/api/upload")
@Tag(name = "文件上传", description = "鲜花图片、店铺图片上传")
public class UploadController {

    // 上传目录（默认项目根目录下的 uploads）
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    /** 获取上传目录的绝对路径 */
    private Path getUploadPath() {
        Path path = Paths.get(uploadDir);
        if (!path.isAbsolute()) {
            // 相对路径 → 基于项目根目录
            path = Paths.get(System.getProperty("user.dir"), uploadDir);
        }
        return path;
    }

    /** 允许的图片类型 */
    private static final String[] ALLOWED_TYPES = {"image/jpeg", "image/png", "image/gif", "image/webp"};

    @PostMapping("/image")
    @Operation(summary = "上传图片（鲜花/店铺）")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {

        // 第1步：校验文件不为空
        if (file.isEmpty()) {
            return Result.error(400, "请选择文件");
        }

        // 第2步：校验文件类型
        String contentType = file.getContentType();
        boolean allowed = false;
        for (String type : ALLOWED_TYPES) {
            if (type.equals(contentType)) { allowed = true; break; }
        }
        if (!allowed) {
            return Result.error(400, "仅支持 JPG / PNG / GIF / WebP 格式");
        }

        // 第3步：校验大小（最大 5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            return Result.error(400, "文件大小不能超过 5MB");
        }

        try {
            // 第4步：生成唯一文件名：日期/原始文件名_UUID
            String originalName = file.getOriginalFilename();
            String suffix = originalName != null && originalName.contains(".")
                    ? originalName.substring(originalName.lastIndexOf(".")) : ".jpg";
            String newFileName = UUID.randomUUID().toString().substring(0, 8) + suffix;

            // 按日期分目录：uploads/2026-06-16/xxx.jpg
            String dateDir = LocalDate.now().toString();
            Path uploadPath = getUploadPath().resolve(dateDir);
            Files.createDirectories(uploadPath);

            // 第5步：保存文件
            File destFile = uploadPath.resolve(newFileName).toFile();
            file.transferTo(destFile);

            // 第6步：返回可访问的 URL
            String fileUrl = "/uploads/" + dateDir + "/" + newFileName;

            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("fileName", newFileName);
            result.put("originalName", originalName);

            return Result.success("上传成功", result);

        } catch (IOException e) {
            return Result.error(500, "文件保存失败：" + e.getMessage());
        }
    }
}
