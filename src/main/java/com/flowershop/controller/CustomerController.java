package com.flowershop.controller;

import com.flowershop.common.Result;
import com.flowershop.entity.Customer;
import com.flowershop.entity.Order;
import com.flowershop.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 顾客控制器（需要登录）
 */
@RestController
@RequestMapping("/api/customer")
@Tag(name = "顾客模块", description = "个人信息、我的订单")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // ===== 查看个人信息 =====
    @GetMapping("/info")
    @Operation(summary = "查看个人信息")
    public Result<Customer> info(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        Customer customer = customerService.queryById(userId);
        if (customer != null) {
            customer.setCpwd(null);  // 不要返回密码
        }
        return Result.success(customer);
    }

    // ===== 修改个人信息 =====
    @PutMapping("/info")
    @Operation(summary = "修改个人信息")
    public Result<String> updateInfo(HttpServletRequest request,
                                      @RequestParam String name,
                                      @RequestParam String phone,
                                      @RequestParam(required = false) String email) {
        Integer userId = (Integer) request.getAttribute("userId");
        boolean ok = customerService.updateInfo(userId, name, phone, email);
        return ok ? Result.success("修改成功") : Result.error(400, "修改失败");
    }

    // ===== 我的订单 =====
    @GetMapping("/orders")
    @Operation(summary = "我的订单列表")
    public Result<Map<String, Object>> myOrders(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Integer userId = (Integer) request.getAttribute("userId");
        List<Order> orders = customerService.queryMyOrders(userId, page, size);

        Map<String, Object> result = new HashMap<>();
        result.put("list", orders);
        result.put("page", page);
        result.put("size", size);
        return Result.success(result);
    }
}
