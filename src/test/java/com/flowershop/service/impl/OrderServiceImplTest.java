package com.flowershop.service.impl;

import com.flowershop.common.BusinessException;
import com.flowershop.entity.Cart;
import com.flowershop.entity.Flower;
import com.flowershop.entity.Order;
import com.flowershop.mapper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 订单服务单元测试
 * <p>
 * 重点测试下单的事务逻辑：库存不足回滚、空购物车、正常下单。
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderMapper orderMapper;
    @Mock private OrderItemMapper orderItemMapper;
    @Mock private CartMapper cartMapper;
    @Mock private FlowerMapper flowerMapper;
    @Mock private FlowerStoreMapper storeMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private List<Cart> cartItems;
    private Flower flower;

    @BeforeEach
    void setUp() {
        // 准备一朵花
        flower = new Flower();
        flower.setFlowerId(1);
        flower.setFlowerName("红玫瑰");
        flower.setUnitPrice(new BigDecimal("15.99"));
        flower.setStock(100);
        flower.setStoreId(1);

        // 准备购物车：1条记录，3朵红玫瑰
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCustomerId(7);
        cart.setFlowerId(1);
        cart.setStoreId(1);
        cart.setQuantity(3);
        cartItems = List.of(cart);
    }

    // ========== 正常下单 ==========

    @Test
    void testCreateOrder_Success() {
        when(cartMapper.selectByCustomerId(7)).thenReturn(cartItems);
        when(flowerMapper.selectById(1)).thenReturn(flower);
        when(orderMapper.insert(any(Order.class))).thenReturn(1);
        when(orderItemMapper.insert(any())).thenReturn(1);
        when(flowerMapper.updateById(any(Flower.class))).thenReturn(1);
        when(cartMapper.deleteByCustomerId(7)).thenReturn(1);

        Order order = orderService.createOrder(7, "北京", "微信", "北京朝阳");

        assertNotNull(order);
        assertEquals(7, order.getCustomerId());
        assertEquals(1, order.getStoreId());
        assertEquals(0, order.getOrderStatus());  // 待支付
        assertEquals(new BigDecimal("47.97"), order.getTotalAmount()); // 15.99 × 3

        // 验证扣了库存
        verify(flowerMapper).updateById(any(Flower.class));
        // 验证清空了购物车
        verify(cartMapper).deleteByCustomerId(7);
    }

    // ========== 购物车为空 ==========

    @Test
    void testCreateOrder_EmptyCart() {
        when(cartMapper.selectByCustomerId(7)).thenReturn(Collections.emptyList());

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(7, "北京", "微信", "北京");
        });
        assertEquals("购物车为空，无法下单", ex.getMessage());

        // 验证没有插入订单
        verify(orderMapper, never()).insert(any());
    }

    // ========== 库存不足 ==========

    @Test
    void testCreateOrder_InsufficientStock() {
        // 设置库存只有2朵，但要买3朵
        flower.setStock(2);
        when(cartMapper.selectByCustomerId(7)).thenReturn(cartItems);
        when(flowerMapper.selectById(1)).thenReturn(flower);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(7, "北京", "微信", "北京");
        });
        assertEquals("鲜花【红玫瑰】库存不足", ex.getMessage());

        // 验证没有插入订单
        verify(orderMapper, never()).insert(any());
    }

    // ========== 支付和状态流转 ==========

    @Test
    void testPay_Success() {
        Order order = new Order();
        order.setOrderId(1);
        order.setOrderStatus(0);  // 待支付

        when(orderMapper.selectById(1)).thenReturn(order);
        when(orderMapper.updateById(any())).thenReturn(1);

        orderService.pay(1);

        assertEquals(1, order.getOrderStatus());  // 变成已支付
        assertNotNull(order.getPaymentTime());
    }

    @Test
    void testPay_WrongStatus() {
        Order order = new Order();
        order.setOrderId(1);
        order.setOrderStatus(1);  // 已经是已支付，不能再支付

        when(orderMapper.selectById(1)).thenReturn(order);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            orderService.pay(1);
        });
        assertEquals("订单状态不正确", ex.getMessage());
    }
}
