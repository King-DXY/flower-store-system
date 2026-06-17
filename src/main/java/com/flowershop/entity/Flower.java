package com.flowershop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 鲜花实体 — 对应 flower 表
 */
@TableName("flower")
public class Flower {

    @TableId(type = IdType.AUTO)
    private Integer flowerId;
    private String flowerName;
    private String flowerType;
    private BigDecimal unitPrice;
    private Integer stock;
    private BigDecimal purchasePrice;
    private LocalDateTime createTime;
    private Integer status;
    private BigDecimal costPrice;
    private Integer storeId;
    private String flowerMeaning;
    @TableField("DiscountedPrice")
    private Double discountedPrice;

    // ========== getter / setter ==========

    public Integer getFlowerId() { return flowerId; }
    public void setFlowerId(Integer flowerId) { this.flowerId = flowerId; }

    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }

    public String getFlowerType() { return flowerType; }
    public void setFlowerType(String flowerType) { this.flowerType = flowerType; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }

    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }

    public String getFlowerMeaning() { return flowerMeaning; }
    public void setFlowerMeaning(String flowerMeaning) { this.flowerMeaning = flowerMeaning; }

    public Double getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(Double discountedPrice) { this.discountedPrice = discountedPrice; }
}
