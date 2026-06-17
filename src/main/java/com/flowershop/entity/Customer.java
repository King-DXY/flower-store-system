package com.flowershop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 顾客实体 — 对应 customer 表
 */
@TableName("customer")
public class Customer {

    @TableId(type = IdType.AUTO)
    private Integer customerId;
    private String customerName;
    private String phone;
    private String email;
    private LocalDateTime registerTime;
    private LocalDateTime lastLoginTime;
    private Integer status;
    private String cpwd;            // 老明文密码（逐步废弃）
    private String password;        // BCrypt 加密密码

    // ========== getter / setter ==========

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getRegisterTime() { return registerTime; }
    public void setRegisterTime(LocalDateTime registerTime) { this.registerTime = registerTime; }

    public LocalDateTime getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(LocalDateTime lastLoginTime) { this.lastLoginTime = lastLoginTime; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getCpwd() { return cpwd; }
    public void setCpwd(String cpwd) { this.cpwd = cpwd; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
