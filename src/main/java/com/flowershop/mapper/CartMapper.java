package com.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flowershop.entity.Cart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 购物车 Mapper
 */
public interface CartMapper extends BaseMapper<Cart> {

    /** 根据顾客 ID 查询购物车 */
    @Select("SELECT * FROM cart WHERE customer_id = #{customerId} ORDER BY create_time DESC")
    List<Cart> selectByCustomerId(@Param("customerId") Integer customerId);

    /** 查找某顾客购物车中的某鲜花 */
    @Select("SELECT * FROM cart WHERE customer_id = #{customerId} AND flower_id = #{flowerId}")
    Cart selectByCustomerAndFlower(@Param("customerId") Integer customerId,
                                    @Param("flowerId") Integer flowerId);

    /** 清空某顾客的购物车 */
    @Delete("DELETE FROM cart WHERE customer_id = #{customerId}")
    int deleteByCustomerId(@Param("customerId") Integer customerId);
}
