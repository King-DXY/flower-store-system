package com.flowershop.service;

import com.flowershop.entity.FlowerStore;

import java.util.List;

/**
 * 店铺服务接口
 */
public interface StoreService {

    /** 店铺列表（分页） */
    List<FlowerStore> queryStores(int page, int size);

    /** 查总数 */
    long countStores();

    /** 热门店铺 Top N */
    List<FlowerStore> getTopStores(int n);

    /** 店铺详情 */
    FlowerStore queryById(Integer storeId);

    /** 根据地址模糊搜索 */
    List<FlowerStore> queryByAddress(String address);
}
