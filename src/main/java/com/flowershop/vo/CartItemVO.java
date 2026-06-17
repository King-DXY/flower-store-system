package com.flowershop.vo;

import java.math.BigDecimal;

/**
 * 购物车展示用的对象（关联了鲜花信息）
 */
public class CartItemVO {

    private Long cartId;            // 购物车记录 ID
    private Integer flowerId;       // 鲜花 ID
    private String flowerName;      // 鲜花名称
    private BigDecimal unitPrice;   // 单价
    private Integer quantity;       // 数量
    private BigDecimal subtotal;    // 小计
    private Integer stock;          // 当前库存

    public CartItemVO() {}

    public CartItemVO(Long cartId, Integer flowerId, String flowerName,
                      BigDecimal unitPrice, Integer quantity, Integer stock) {
        this.cartId = cartId;
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.stock = stock;
    }

    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }

    public Integer getFlowerId() { return flowerId; }
    public void setFlowerId(Integer flowerId) { this.flowerId = flowerId; }

    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
