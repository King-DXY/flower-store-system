package com.flowershop.service;

import com.flowershop.entity.Flower;

import java.util.List;

/**
 * 鲜花服务接口
 */
public interface FlowerService {

    /** 分页查询鲜花（支持按名称/类型搜索） */
    List<Flower> queryFlowers(int page, int size, String keyword, String type);

    /** 查总数（配合分页用） */
    long countFlowers(String keyword, String type);

    /** 根据 ID 查鲜花详情 */
    Flower queryById(Integer flowerId);

    /** 根据店铺 ID 查鲜花列表 */
    List<Flower> queryByStoreId(Integer storeId);

    /** 根据名称模糊搜索 */
    List<Flower> queryByName(String name);

    /** 根据类型搜索 */
    List<Flower> queryByType(String type);
}
