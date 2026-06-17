package com.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flowershop.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单 Mapper
 */
public interface OrderMapper extends BaseMapper<Order> {

    /** 根据顾客 ID 查询订单（按时间倒序） */
    @Select("SELECT * FROM orders WHERE customer_id = #{customerId} ORDER BY order_time DESC")
    List<Order> selectByCustomerId(@Param("customerId") Integer customerId);

    /** 根据店铺 ID 查询订单（按时间倒序） */
    @Select("SELECT * FROM orders WHERE store_id = #{storeId} ORDER BY order_time DESC")
    List<Order> selectByStoreId(@Param("storeId") Integer storeId);
}
