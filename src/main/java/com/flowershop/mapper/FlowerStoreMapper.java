package com.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flowershop.entity.FlowerStore;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 花店 Mapper
 */
public interface FlowerStoreMapper extends BaseMapper<FlowerStore> {

    /** 根据地址模糊搜索 */
    @Select("SELECT * FROM flowerstore WHERE address LIKE CONCAT('%', #{address}, '%')")
    List<FlowerStore> selectByAddress(@Param("address") String address);
}
