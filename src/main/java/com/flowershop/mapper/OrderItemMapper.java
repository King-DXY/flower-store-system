package com.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flowershop.entity.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单明细 Mapper
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /** 根据订单 ID 查询所有明细 */
    @Select("SELECT * FROM order_item WHERE order_id = #{orderId}")
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);
}
