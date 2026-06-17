package com.flowershop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowershop.entity.Flower;
import com.flowershop.mapper.FlowerMapper;
import com.flowershop.service.FlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 鲜花服务实现
 */
@Service
public class FlowerServiceImpl implements FlowerService {

    @Autowired
    private FlowerMapper flowerMapper;

    @Override
    public List<Flower> queryFlowers(int page, int size, String keyword, String type) {
        // 构造分页对象：第几页、每页几条
        Page<Flower> pageObj = new Page<>(page, size);

        // 构造查询条件
        LambdaQueryWrapper<Flower> wrapper = new LambdaQueryWrapper<>();

        // 关键词搜索：同时搜名称和类型
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w
                    .like(Flower::getFlowerName, keyword)
                    .or()
                    .like(Flower::getFlowerType, keyword));
        }

        // 按类型精确搜索
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Flower::getFlowerType, type);
        }

        wrapper.orderByDesc(Flower::getCreateTime);  // 按创建时间倒序

        // MyBatis-Plus 分页查询
        Page<Flower> result = flowerMapper.selectPage(pageObj, wrapper);
        return result.getRecords();
    }

    @Override
    public long countFlowers(String keyword, String type) {
        LambdaQueryWrapper<Flower> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w
                    .like(Flower::getFlowerName, keyword)
                    .or()
                    .like(Flower::getFlowerType, keyword));
        }
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Flower::getFlowerType, type);
        }
        return flowerMapper.selectCount(wrapper);
    }

    @Cacheable(value = "flower", key = "#flowerId")
    @Override
    public Flower queryById(Integer flowerId) {
        return flowerMapper.selectById(flowerId);
    }

    @Override
    public List<Flower> queryByStoreId(Integer storeId) {
        return flowerMapper.selectByStoreId(storeId);
    }

    @Override
    public List<Flower> queryByName(String name) {
        return flowerMapper.selectByName(name);
    }

    @Override
    public List<Flower> queryByType(String type) {
        return flowerMapper.selectByType(type);
    }
}
