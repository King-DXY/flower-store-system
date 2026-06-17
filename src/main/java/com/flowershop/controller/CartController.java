package com.flowershop.controller;

import com.flowershop.common.Result;
import com.flowershop.service.CartService;
import com.flowershop.service.impl.CartServiceImpl;
import com.flowershop.vo.CartItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器（需要顾客登录）—— Redis 版
 */
@RestController
@RequestMapping("/api/cart")
@Tag(name = "购物车模块", description = "购物车增删改查（Redis）")
public class CartController {

    @Autowired
    private CartServiceImpl cartService;  // 直接注入实现类，用 Redis 特有方法

    // ===== 查看购物车 =====
    @GetMapping
    @Operation(summary = "查看我的购物车")
    public Result<List<CartItemVO>> list(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        return Result.success(cartService.getCart(userId));
    }

    // ===== 添加商品 =====
    @PostMapping("/items")
    @Operation(summary = "添加商品到购物车")
    public Result<String> add(HttpServletRequest request,
                               @RequestParam Integer flowerId,
                               @RequestParam Integer storeId,
                               @RequestParam(defaultValue = "1") Integer quantity) {
        Integer userId = (Integer) request.getAttribute("userId");
        cartService.addItem(userId, flowerId, storeId, quantity);
        return Result.success("已添加到购物车");
    }

    // ===== 修改数量（Redis 版用 flowerId） =====
    @PutMapping("/items/{flowerId}")
    @Operation(summary = "修改购物车商品数量")
    public Result<String> update(HttpServletRequest request,
                                  @PathVariable Integer flowerId,
                                  @RequestParam Integer quantity) {
        Integer userId = (Integer) request.getAttribute("userId");
        cartService.updateQuantity(userId, flowerId, quantity);
        return Result.success("已更新");
    }

    // ===== 删除某商品（Redis 版用 flowerId） =====
    @DeleteMapping("/items/{flowerId}")
    @Operation(summary = "从购物车删除商品")
    public Result<String> delete(HttpServletRequest request,
                                  @PathVariable Integer flowerId) {
        Integer userId = (Integer) request.getAttribute("userId");
        cartService.removeItem(userId, flowerId);
        return Result.success("已删除");
    }

    // ===== 清空购物车 =====
    @DeleteMapping
    @Operation(summary = "清空购物车")
    public Result<String> clear(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        cartService.clearCart(userId);
        return Result.success("购物车已清空");
    }
}
