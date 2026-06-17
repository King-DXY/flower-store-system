package com.flowershop.controller;

import com.flowershop.common.Result;
import com.flowershop.dto.LoginDTO;
import com.flowershop.dto.RegisterDTO;
import com.flowershop.service.AuthService;
import com.flowershop.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器（注册 + 登录）
 * <p>
 * 接收前端发来的 HTTP 请求，调用 Service 处理，返回统一格式 Result<T>。
 */
@RestController  // = @Controller + @ResponseBody（返回 JSON，不返回页面）
@RequestMapping("/api/auth")  // 所有接口地址都以 /api/auth 开头
@Tag(name = "认证模块", description = "顾客和花店的注册、登录接口")
public class AuthController {

    @Autowired
    private AuthService authService;  // Spring 自动注入，不需要手动 new

    // ==========================================================
    //  顾客注册
    //  请求：POST http://localhost:8082/api/auth/customer/register
    //  请求体示例：{"name":"张三","phone":"13800138000","email":"t@qq.com","password":"123456"}
    // ==========================================================
    @PostMapping("/customer/register")
    @Operation(summary = "顾客注册")
    public Result<LoginVO> customerRegister(@Valid @RequestBody RegisterDTO dto) {
        // @Valid = 自动校验字段（@NotBlank、@Pattern等），不通过会返回400错误
        LoginVO result = authService.customerRegister(dto);
        return Result.success("注册成功", result);
    }

    // ==========================================================
    //  花店注册
    //  请求：POST http://localhost:8082/api/auth/store/register
    //  请求体示例：{"name":"太阳花店","phone":"13800138001","address":"北京市朝阳区","password":"123456"}
    // ==========================================================
    @PostMapping("/store/register")
    @Operation(summary = "花店注册")
    public Result<LoginVO> storeRegister(@Valid @RequestBody RegisterDTO dto) {
        LoginVO result = authService.storeRegister(dto);
        return Result.success("注册成功", result);
    }

    // ==========================================================
    //  顾客登录（手机号+密码）
    //  请求：POST http://localhost:8082/api/auth/customer/login
    //  请求体示例：{"account":"13800138000","password":"123456"}
    // ==========================================================
    @PostMapping("/customer/login")
    @Operation(summary = "顾客登录")
    public Result<LoginVO> customerLogin(@Valid @RequestBody LoginDTO dto) {
        LoginVO result = authService.customerLogin(dto);
        return Result.success("登录成功", result);
    }

    // ==========================================================
    //  花店登录（店铺名+密码）
    //  请求：POST http://localhost:8082/api/auth/store/login
    //  请求体示例：{"account":"太阳花店","password":"123456"}
    // ==========================================================
    @PostMapping("/store/login")
    @Operation(summary = "花店登录")
    public Result<LoginVO> storeLogin(@Valid @RequestBody LoginDTO dto) {
        LoginVO result = authService.storeLogin(dto);
        return Result.success("登录成功", result);
    }
}
