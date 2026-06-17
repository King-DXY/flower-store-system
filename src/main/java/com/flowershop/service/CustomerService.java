package com.flowershop.service;

import com.flowershop.entity.Customer;
import com.flowershop.entity.Order;

import java.util.List;

/**
 * 顾客服务接口
 */
public interface CustomerService {

    /** 根据 ID 查顾客信息 */
    Customer queryById(Integer customerId);

    /** 更新顾客信息（姓名、手机、邮箱） */
    boolean updateInfo(Integer customerId, String name, String phone, String email);

    /** 查我的订单列表 */
    List<Order> queryMyOrders(Integer customerId, int page, int size);
}
