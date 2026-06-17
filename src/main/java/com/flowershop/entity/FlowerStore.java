package com.flowershop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 花店实体 — 对应 flowerstore 表
 */
@TableName("flowerstore")
public class FlowerStore {

    @TableId(type = IdType.AUTO)
    private Integer storeId;
    private String storeName;
    private String address;
    private String phone;
    private String ownerName;
    private LocalDateTime createTime;
    private Integer status;
    private String contactPhone;
    private String fpwd;            // 老明文密码（逐步废弃）
    private String password;        // BCrypt 加密密码

    // ========== getter / setter ==========

    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getFpwd() { return fpwd; }
    public void setFpwd(String fpwd) { this.fpwd = fpwd; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
