package com.flowershop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flowershop.common.BusinessException;
import com.flowershop.entity.*;
import com.flowershop.mapper.*;
import com.flowershop.service.StoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 店铺管理服务实现
 */
@Service
public class StoreManageServiceImpl implements StoreManageService {

    @Autowired
    private FlowerMapper flowerMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private FlowerStoreMapper storeMapper;

    @Override
    public List<Flower> getMyStock(Integer storeId) {
        return flowerMapper.selectByStoreId(storeId);
    }

    @Override
    public void stockIn(Integer storeId, Integer flowerId, Integer quantity) {
        Flower flower = flowerMapper.selectById(flowerId);
        if (flower == null) {
            throw new BusinessException("鲜花不存在");
        }
        flower.setStock(flower.getStock() + quantity);
        flowerMapper.updateById(flower);
    }

    @Override
    public void stockOut(Integer storeId, Integer flowerId, Integer quantity) {
        Flower flower = flowerMapper.selectById(flowerId);
        if (flower == null) {
            throw new BusinessException("鲜花不存在");
        }
        if (flower.getStock() < quantity) {
            throw new BusinessException("库存不足，当前库存：" + flower.getStock());
        }
        flower.setStock(flower.getStock() - quantity);
        flowerMapper.updateById(flower);
    }

    @Override
    public Flower cultivate(Integer storeId, String flowerName, String flowerType,
                             BigDecimal unitPrice, BigDecimal costPrice,
                             String flowerMeaning, Integer quantity) {
        Flower flower = new Flower();
        flower.setFlowerName(flowerName);
        flower.setFlowerType(flowerType);
        flower.setUnitPrice(unitPrice);
        flower.setCostPrice(costPrice);
        flower.setFlowerMeaning(flowerMeaning);
        flower.setStock(quantity);
        flower.setStoreId(storeId);
        flower.setCreateTime(LocalDateTime.now());
        flower.setStatus(1);
        flowerMapper.insert(flower);
        return flower;
    }

    @Override
    public List<Order> getMyOrders(Integer storeId, int page, int size) {
        return orderMapper.selectByStoreId(storeId);
    }

    @Override
    public Map<String, Object> getSalesStats(Integer storeId) {
        List<Order> orders = orderMapper.selectByStoreId(storeId);

        BigDecimal totalRevenue = BigDecimal.ZERO;    // 总销售额
        BigDecimal totalProfit = BigDecimal.ZERO;     // 总利润
        int validOrderCount = 0;
        List<Map<String, Object>> salesList = new ArrayList<>();

        for (Order order : orders) {
            Flower flower = flowerMapper.selectById(order.getFlowerId());
            if (flower != null) {
                // 计算利润 = 售价 - 成本价
                BigDecimal unitProfit = flower.getUnitPrice().subtract(flower.getCostPrice() != null
                        ? flower.getCostPrice() : BigDecimal.ZERO);
                BigDecimal orderProfit = unitProfit.multiply(
                        BigDecimal.valueOf(order.getQuantity() != null ? order.getQuantity() : 1));

                Map<String, Object> sale = new HashMap<>();
                sale.put("orderId", order.getOrderId());
                sale.put("flowerName", flower.getFlowerName());
                sale.put("quantity", order.getQuantity());
                sale.put("revenue", order.getTotalAmount());
                sale.put("profit", orderProfit);
                sale.put("unitPrice", flower.getUnitPrice());
                sale.put("costPrice", flower.getCostPrice());
                sale.put("paymentMethod", order.getPaymentMethod());
                sale.put("orderTime", order.getOrderTime());
                salesList.add(sale);

                totalRevenue = totalRevenue.add(order.getTotalAmount());
                totalProfit = totalProfit.add(orderProfit);
                validOrderCount++;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalOrders", orders.size());
        result.put("validOrders", validOrderCount);
        result.put("totalRevenue", totalRevenue);
        result.put("totalProfit", totalProfit);
        result.put("avgProfitPerOrder", validOrderCount > 0
                ? totalProfit.divide(BigDecimal.valueOf(validOrderCount), 2, java.math.RoundingMode.HALF_UP)
                : BigDecimal.ZERO);
        result.put("salesList", salesList);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellToCustomer(Integer storeId, Integer flowerId, Integer quantity,
                                Integer customerId, String paymentMethod) {
        // 查鲜花
        Flower flower = flowerMapper.selectById(flowerId);
        if (flower == null) {
            throw new BusinessException("鲜花不存在");
        }
        if (flower.getStock() < quantity) {
            throw new BusinessException("库存不足");
        }

        BigDecimal totalAmount = flower.getUnitPrice().multiply(BigDecimal.valueOf(quantity));

        // 扣库存
        flower.setStock(flower.getStock() - quantity);
        flowerMapper.updateById(flower);

        // 创建订单
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setStoreId(storeId);
        order.setOrderTime(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(1);  // 直接已支付
        order.setPaymentTime(LocalDateTime.now());
        order.setFlowerId(flowerId);
        order.setQuantity(quantity);
        order.setPaymentMethod(paymentMethod);
        orderMapper.insert(order);
    }
}
