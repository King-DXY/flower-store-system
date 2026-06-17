package com.flowershop.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowershop.entity.Customer;
import com.flowershop.entity.Order;
import com.flowershop.mapper.CustomerMapper;
import com.flowershop.mapper.OrderMapper;
import com.flowershop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 顾客服务实现
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Customer queryById(Integer customerId) {
        return customerMapper.selectById(customerId);
    }

    @Override
    public boolean updateInfo(Integer customerId, String name, String phone, String email) {
        Customer customer = customerMapper.selectById(customerId);
        if (customer == null) {
            return false;
        }
        customer.setCustomerName(name);
        customer.setPhone(phone);
        customer.setEmail(email);
        return customerMapper.updateById(customer) > 0;
    }

    @Override
    public List<Order> queryMyOrders(Integer customerId, int page, int size) {
        Page<Order> pageObj = new Page<>(page, size);
        Page<Order> result = orderMapper.selectPage(pageObj,
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order>()
                        .eq(Order::getCustomerId, customerId)
                        .orderByDesc(Order::getOrderTime));
        return result.getRecords();
    }
}
