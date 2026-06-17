package com.flowershop.service;

import com.flowershop.vo.CartItemVO;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {

    /** 获取我的购物车列表 */
    List<CartItemVO> getCart(Integer customerId);

    /** 添加商品到购物车（已存在则累加数量） */
    void addItem(Integer customerId, Integer flowerId, Integer storeId, Integer quantity);

    /** 更新购物车某商品的数量 */
    void updateQuantity(Long cartId, Integer quantity);

    /** 从购物车删除某商品 */
    void removeItem(Long cartId);

    /** 清空购物车 */
    void clearCart(Integer customerId);
}
