package com.flowershop.controller;

import com.flowershop.common.Result;
import com.flowershop.entity.Order;
import com.flowershop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单控制器（需要登录）
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "订单模块", description = "下单、订单列表、订单详情、支付/发货/完成")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // ===== 创建订单（从购物车） =====
    @PostMapping
    @Operation(summary = "从购物车下单")
    public Result<Order> create(HttpServletRequest request,
                                 @RequestParam String address,
                                 @RequestParam String paymentMethod,
                                 @RequestParam(required = false) String shippingAddress) {
        Integer userId = (Integer) request.getAttribute("userId");
        Order order = orderService.createOrder(userId, address, paymentMethod, shippingAddress);
        return Result.success("下单成功", order);
    }

    // ===== 订单详情 =====
    @GetMapping("/{id}")
    @Operation(summary = "订单详情（含明细）")
    public Result<Map<String, Object>> detail(@PathVariable Integer id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    // ===== 我的订单（顾客端） =====
    @GetMapping("/my")
    @Operation(summary = "我的订单列表")
    public Result<List<Order>> myOrders(HttpServletRequest request,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Integer userId = (Integer) request.getAttribute("userId");
        return Result.success(orderService.getCustomerOrders(userId, page, size));
    }

    // ===== 支付 =====
    @PutMapping("/{id}/pay")
    @Operation(summary = "支付订单")
    public Result<String> pay(@PathVariable Integer id) {
        orderService.pay(id);
        return Result.success("支付成功");
    }

    // ===== 发货 =====
    @PutMapping("/{id}/ship")
    @Operation(summary = "发货")
    public Result<String> ship(@PathVariable Integer id) {
        orderService.ship(id);
        return Result.success("已发货");
    }

    // ===== 完成 =====
    @PutMapping("/{id}/complete")
    @Operation(summary = "完成订单")
    public Result<String> complete(@PathVariable Integer id) {
        orderService.complete(id);
        return Result.success("订单已完成");
    }
}
