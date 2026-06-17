package com.flowershop.controller;

import com.flowershop.common.Result;
import com.flowershop.entity.Flower;
import com.flowershop.service.FlowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 鲜花控制器（公开接口，不需要登录）
 */
@RestController
@RequestMapping("/api/flowers")
@Tag(name = "鲜花模块", description = "鲜花浏览、搜索、详情")
public class FlowerController {

    @Autowired
    private FlowerService flowerService;

    // ===== 鲜花列表（分页+搜索）=====
    @GetMapping
    @Operation(summary = "鲜花列表（分页+搜索）")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type) {

        List<Flower> flowers = flowerService.queryFlowers(page, size, keyword, type);
        long total = flowerService.countFlowers(keyword, type);

        Map<String, Object> result = new HashMap<>();
        result.put("list", flowers);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);

        return Result.success(result);
    }

    // ===== 鲜花详情 =====
    @GetMapping("/{id}")
    @Operation(summary = "鲜花详情")
    public Result<Flower> detail(@PathVariable Integer id) {
        Flower flower = flowerService.queryById(id);
        if (flower == null) {
            return Result.error(400, "鲜花不存在");
        }
        return Result.success(flower);
    }

    // ===== 某店铺的鲜花列表 =====
    @GetMapping("/store/{storeId}")
    @Operation(summary = "某店铺的鲜花")
    public Result<List<Flower>> byStore(@PathVariable Integer storeId) {
        return Result.success(flowerService.queryByStoreId(storeId));
    }

    // ===== 按名称模糊搜索 =====
    @GetMapping("/search")
    @Operation(summary = "按名称搜索鲜花")
    public Result<List<Flower>> search(@RequestParam String name) {
        return Result.success(flowerService.queryByName(name));
    }
}
