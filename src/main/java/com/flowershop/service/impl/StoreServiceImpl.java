package com.flowershop.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowershop.entity.FlowerStore;
import com.flowershop.mapper.FlowerStoreMapper;
import com.flowershop.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 店铺服务实现
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private FlowerStoreMapper storeMapper;

    @Override
    public List<FlowerStore> queryStores(int page, int size) {
        Page<FlowerStore> pageObj = new Page<>(page, size);
        Page<FlowerStore> result = storeMapper.selectPage(pageObj, null);
        return result.getRecords();
    }

    @Override
    public long countStores() {
        return storeMapper.selectCount(null);
    }

    // @Cacheable(value = "topStores", key = "#n")  // TODO: 序列化兼容
    @Override
    public List<FlowerStore> getTopStores(int n) {
        Page<FlowerStore> pageObj = new Page<>(1, n);
        Page<FlowerStore> result = storeMapper.selectPage(pageObj, null);
        return result.getRecords();
    }

    @Override
    public FlowerStore queryById(Integer storeId) {
        return storeMapper.selectById(storeId);
    }

    @Override
    public List<FlowerStore> queryByAddress(String address) {
        return storeMapper.selectByAddress(address);
    }
}
