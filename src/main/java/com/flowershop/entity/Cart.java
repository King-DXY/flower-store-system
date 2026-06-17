package com.flowershop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 购物车实体 — 对应 cart 表
 */
@TableName("cart")
public class Cart {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer customerId;
    private Integer flowerId;
    private Integer storeId;
    private Integer quantity;
    private LocalDateTime createTime;

    // ========== getter / setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getFlowerId() { return flowerId; }
    public void setFlowerId(Integer flowerId) { this.flowerId = flowerId; }

    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
