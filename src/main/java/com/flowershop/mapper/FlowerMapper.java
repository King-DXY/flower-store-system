package com.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flowershop.entity.Flower;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 鲜花 Mapper
 */
public interface FlowerMapper extends BaseMapper<Flower> {

    /** 根据名称模糊搜索 */
    @Select("SELECT * FROM flower WHERE flower_name LIKE CONCAT('%', #{name}, '%')")
    List<Flower> selectByName(@Param("name") String name);

    /** 根据类型查询 */
    @Select("SELECT * FROM flower WHERE flower_type = #{type}")
    List<Flower> selectByType(@Param("type") String type);

    /** 根据店铺 ID 查询 */
    @Select("SELECT * FROM flower WHERE store_id = #{storeId}")
    List<Flower> selectByStoreId(@Param("storeId") Integer storeId);

    /** 查询库存大于指定值的鲜花 */
    @Select("SELECT * FROM flower WHERE stock >= #{minStock}")
    List<Flower> selectByMinStock(@Param("minStock") int minStock);

    /** 根据店铺 ID 和鲜花 ID 查询 */
    @Select("SELECT * FROM flower WHERE store_id = #{storeId} AND flower_id = #{flowerId}")
    Flower selectByStoreIdAndFlowerId(@Param("storeId") Integer storeId,
                                       @Param("flowerId") Integer flowerId);
}
