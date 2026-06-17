package com.flowershop.vo;

/**
 * 登录成功后返回的数据
 */
public class LoginVO {

    private String token;
    private Integer userId;
    private String name;
    private String role;

    public LoginVO() {}

    public LoginVO(String token, Integer userId, String name, String role) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    // ========== getter / setter ==========

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
