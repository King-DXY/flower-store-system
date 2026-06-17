package com.flowershop.dto;

import jakarta.validation.constraints.*;

/**
 * 注册请求体
 */
public class RegisterDTO {

    @NotBlank(message = "名称不能为空")
    @Size(max = 50, message = "名称最长50字")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Size(max = 200, message = "地址最长200字")
    private String address;

    @Size(max = 30, message = "店主姓名最长30字")
    private String ownerName;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    private String password;

    // ========== getter / setter ==========

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
