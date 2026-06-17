package com.flowershop.service.impl;

import com.flowershop.common.BusinessException;
import com.flowershop.entity.*;
import com.flowershop.mapper.*;
import com.flowershop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 订单服务实现
 * <p>
 * 创建订单是整个系统最核心的业务逻辑，使用 @Transactional 保证：
 * 如果中间任何一步失败（库存不足、插入失败），前面的操作全部回滚。
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private FlowerMapper flowerMapper;

    @Autowired
    private FlowerStoreMapper storeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)  // 事务注解：任何异常都回滚
    public Order createOrder(Integer customerId, String address,
                              String paymentMethod, String shippingAddress) {
        // 第1步：从 Redis 查购物车 → {flowerId: quantity}
        String cartKey = "cart:" + customerId;
        Map<Object, Object> cartEntries = redisTemplate.opsForHash().entries(cartKey);
        if (cartEntries.isEmpty()) {
            throw new BusinessException("购物车为空，无法下单");
        }

        // 第2步：取第一条记录的店铺 ID
        Integer firstFlowerId = Integer.parseInt(cartEntries.keySet().iterator().next().toString());
        Flower firstFlower = flowerMapper.selectById(firstFlowerId);
        Integer storeId = firstFlower != null ? firstFlower.getStoreId() : 1;

        // 第3步：校验每种花是否存在、库存是否充足，同时计算总额
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<Integer, Integer> flowerQuantityMap = new HashMap<>();  // flowerId → quantity
        for (Map.Entry<Object, Object> entry : cartEntries.entrySet()) {
            Integer flowerId = Integer.parseInt(entry.getKey().toString());
            Integer quantity = Integer.parseInt(entry.getValue().toString());
            Flower flower = flowerMapper.selectById(flowerId);
            if (flower == null) {
                throw new BusinessException("鲜花【" + flowerId + "】已下架");
            }
            if (flower.getStock() < quantity) {
                throw new BusinessException("鲜花【" + flower.getFlowerName() + "】库存不足");
            }
            totalAmount = totalAmount.add(
                    flower.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
            flowerQuantityMap.put(flowerId, quantity);
        }

        // 第4步：创建订单主记录
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setStoreId(storeId);
        order.setOrderTime(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(0);          // 待支付
        order.setAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setShippingAddress(shippingAddress);
        orderMapper.insert(order);

        // 第5步：创建订单明细 + 扣库存
        for (Map.Entry<Integer, Integer> entry : flowerQuantityMap.entrySet()) {
            Integer flowerId = entry.getKey();
            Integer quantity = entry.getValue();
            Flower flower = flowerMapper.selectById(flowerId);

            OrderItem item = new OrderItem();
            item.setOrderId(order.getOrderId());
            item.setFlowerId(flowerId);
            item.setFlowerName(flower.getFlowerName());
            item.setUnitPrice(flower.getUnitPrice());
            item.setQuantity(quantity);
            item.setSubtotal(flower.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
            orderItemMapper.insert(item);

            // 扣减库存
            flower.setStock(flower.getStock() - quantity);
            flowerMapper.updateById(flower);
        }

        // 第6步：清空 Redis 购物车
        redisTemplate.delete(cartKey);

        return order;
    }

    @Override
    public Map<String, Object> getOrderDetail(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        List<OrderItem> items = orderItemMapper.selectByOrderId(Long.valueOf(orderId));
        FlowerStore store = storeMapper.selectById(order.getStoreId());

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("items", items);
        result.put("storeName", store != null ? store.getStoreName() : "未知");
        return result;
    }

    @Override
    public List<Order> getCustomerOrders(Integer customerId, int page, int size) {
        return orderMapper.selectByCustomerId(customerId);
    }

    @Override
    public List<Order> getStoreOrders(Integer storeId, int page, int size) {
        return orderMapper.selectByStoreId(storeId);
    }

    @Override
    public void pay(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (order.getOrderStatus() != 0) throw new BusinessException("订单状态不正确");
        order.setOrderStatus(1);  // 已支付
        order.setPaymentTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    public void ship(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (order.getOrderStatus() != 1) throw new BusinessException("订单状态不正确");
        order.setOrderStatus(2);  // 已发货
        orderMapper.updateById(order);
    }

    @Override
    public void complete(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (order.getOrderStatus() != 2) throw new BusinessException("订单状态不正确");
        order.setOrderStatus(3);  // 已完成
        orderMapper.updateById(order);
    }
}
