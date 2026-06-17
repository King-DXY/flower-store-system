package com.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flowershop.entity.Customer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 顾客 Mapper
 */
public interface CustomerMapper extends BaseMapper<Customer> {

    /** 根据手机号查询 */
    @Select("SELECT * FROM customer WHERE phone = #{phone}")
    Customer selectByPhone(@Param("phone") String phone);
}
