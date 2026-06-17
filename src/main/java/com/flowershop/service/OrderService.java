package com.flowershop.service;

import com.flowershop.entity.Order;
import com.flowershop.entity.OrderItem;
import com.flowershop.vo.CartItemVO;

import java.util.List;
import java.util.Map;

/**
 * 订单服务接口
 */
public interface OrderService {

    /** 从购物车创建订单（事务：插入订单+明细+扣库存+清购物车） */
    Order createOrder(Integer customerId, String address,
                      String paymentMethod, String shippingAddress);

    /** 订单详情（含明细） */
    Map<String, Object> getOrderDetail(Integer orderId);

    /** 顾客的订单列表 */
    List<Order> getCustomerOrders(Integer customerId, int page, int size);

    /** 店铺的订单列表 */
    List<Order> getStoreOrders(Integer storeId, int page, int size);

    /** 支付 */
    void pay(Integer orderId);

    /** 发货 */
    void ship(Integer orderId);

    /** 完成 */
    void complete(Integer orderId);
}
