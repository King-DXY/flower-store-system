package com.flowershop.service;

import com.flowershop.entity.Flower;
import com.flowershop.entity.Order;

import java.util.List;
import java.util.Map;

/**
 * 店铺管理服务接口
 */
public interface StoreManageService {

    /** 查我的库存 */
    List<Flower> getMyStock(Integer storeId);

    /** 入库（已有鲜花增加库存） */
    void stockIn(Integer storeId, Integer flowerId, Integer quantity);

    /** 出库（减少库存） */
    void stockOut(Integer storeId, Integer flowerId, Integer quantity);

    /** 培育新品种 */
    Flower cultivate(Integer storeId, String flowerName, String flowerType,
                     java.math.BigDecimal unitPrice, java.math.BigDecimal costPrice,
                     String flowerMeaning, Integer quantity);

    /** 查看我的店铺订单 */
    List<Order> getMyOrders(Integer storeId, int page, int size);

    /** 销售统计 */
    Map<String, Object> getSalesStats(Integer storeId);

    /** 直接销售给顾客 */
    void sellToCustomer(Integer storeId, Integer flowerId, Integer quantity,
                        Integer customerId, String paymentMethod);
}
