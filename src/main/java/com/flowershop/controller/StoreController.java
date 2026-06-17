package com.flowershop.controller;

import com.flowershop.common.Result;
import com.flowershop.entity.Flower;
import com.flowershop.entity.FlowerStore;
import com.flowershop.service.FlowerService;
import com.flowershop.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺控制器（公开接口，不需要登录）
 */
@RestController
@RequestMapping("/api/stores")
@Tag(name = "店铺模块", description = "店铺列表、详情、热门店铺")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private FlowerService flowerService;

    // ===== 店铺列表 =====
    @GetMapping
    @Operation(summary = "店铺列表（分页）")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<FlowerStore> stores = storeService.queryStores(page, size);
        long total = storeService.countStores();

        Map<String, Object> result = new HashMap<>();
        result.put("list", stores);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);

        return Result.success(result);
    }

    // ===== 热门店铺 Top N =====
    @GetMapping("/top")
    @Operation(summary = "热门店铺 Top N")
    public Result<List<FlowerStore>> top(@RequestParam(defaultValue = "10") int n) {
        return Result.success(storeService.getTopStores(n));
    }

    // ===== 店铺详情 =====
    @GetMapping("/{id}")
    @Operation(summary = "店铺详情")
    public Result<FlowerStore> detail(@PathVariable Integer id) {
        FlowerStore store = storeService.queryById(id);
        if (store == null) {
            return Result.error(400, "店铺不存在");
        }
        return Result.success(store);
    }

    // ===== 某店铺的鲜花 =====
    @GetMapping("/{id}/flowers")
    @Operation(summary = "某店铺的鲜花列表")
    public Result<List<Flower>> storeFlowers(@PathVariable Integer id) {
        return Result.success(flowerService.queryByStoreId(id));
    }
}
