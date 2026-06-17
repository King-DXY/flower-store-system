package com.flowershop.controller;

import com.flowershop.common.Result;
import com.flowershop.entity.Flower;
import com.flowershop.entity.Order;
import com.flowershop.service.StoreManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 店铺管理控制器（需要花店登录）
 */
@RestController
@RequestMapping("/api/store/manage")
@Tag(name = "店铺管理", description = "库存管理、培育新品种、销售统计、直接销售")
public class StoreManageController {

    @Autowired
    private StoreManageService storeManageService;

    // ===== 查库存 =====
    @GetMapping("/stock")
    @Operation(summary = "查看我的库存")
    public Result<List<Flower>> stock(HttpServletRequest request) {
        Integer storeId = (Integer) request.getAttribute("userId");
        return Result.success(storeManageService.getMyStock(storeId));
    }

    // ===== 入库 =====
    @PostMapping("/stock/in")
    @Operation(summary = "入库（增加库存）")
    public Result<String> stockIn(HttpServletRequest request,
                                   @RequestParam Integer flowerId,
                                   @RequestParam Integer quantity) {
        Integer storeId = (Integer) request.getAttribute("userId");
        storeManageService.stockIn(storeId, flowerId, quantity);
        return Result.success("入库成功");
    }

    // ===== 出库 =====
    @PostMapping("/stock/out")
    @Operation(summary = "出库（减少库存）")
    public Result<String> stockOut(HttpServletRequest request,
                                    @RequestParam Integer flowerId,
                                    @RequestParam Integer quantity) {
        Integer storeId = (Integer) request.getAttribute("userId");
        storeManageService.stockOut(storeId, flowerId, quantity);
        return Result.success("出库成功");
    }

    // ===== 培育新品种 =====
    @PostMapping("/cultivate")
    @Operation(summary = "培育新品种鲜花")
    public Result<Flower> cultivate(HttpServletRequest request,
                                     @RequestParam String flowerName,
                                     @RequestParam String flowerType,
                                     @RequestParam BigDecimal unitPrice,
                                     @RequestParam BigDecimal costPrice,
                                     @RequestParam(required = false) String flowerMeaning,
                                     @RequestParam(defaultValue = "1") Integer quantity) {
        Integer storeId = (Integer) request.getAttribute("userId");
        Flower flower = storeManageService.cultivate(storeId, flowerName, flowerType,
                unitPrice, costPrice, flowerMeaning, quantity);
        return Result.success("培育成功", flower);
    }

    // ===== 查看订单 =====
    @GetMapping("/orders")
    @Operation(summary = "我的店铺订单")
    public Result<List<Order>> orders(HttpServletRequest request,
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        Integer storeId = (Integer) request.getAttribute("userId");
        return Result.success(storeManageService.getMyOrders(storeId, page, size));
    }

    // ===== 销售统计 =====
    @GetMapping("/sales")
    @Operation(summary = "销售统计")
    public Result<Map<String, Object>> sales(HttpServletRequest request) {
        Integer storeId = (Integer) request.getAttribute("userId");
        return Result.success(storeManageService.getSalesStats(storeId));
    }

    // ===== 直接销售给顾客 =====
    @PostMapping("/sell")
    @Operation(summary = "直接销售给顾客")
    public Result<String> sell(HttpServletRequest request,
                                @RequestParam Integer flowerId,
                                @RequestParam Integer quantity,
                                @RequestParam Integer customerId,
                                @RequestParam String paymentMethod) {
        Integer storeId = (Integer) request.getAttribute("userId");
        storeManageService.sellToCustomer(storeId, flowerId, quantity, customerId, paymentMethod);
        return Result.success("销售成功");
    }
}
