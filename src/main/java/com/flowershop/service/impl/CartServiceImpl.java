package com.flowershop.service.impl;

import com.flowershop.common.BusinessException;
import com.flowershop.entity.Cart;
import com.flowershop.entity.Flower;
import com.flowershop.mapper.FlowerMapper;
import com.flowershop.service.CartService;
import com.flowershop.vo.CartItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 购物车服务 — Redis 版
 * <p>
 * 购物车数据存 Redis，结构：
 *   cart:{customerId} → Hash
 *     field = flowerId
 *     value = quantity
 * <p>
 * 优势：读写极快，天然支持过期，适合高并发
 */
@Service
public class CartServiceImpl implements CartService {

    private static final String CART_PREFIX = "cart:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private FlowerMapper flowerMapper;

    /** 获取某顾客的购物车 Hash key */
    private String cartKey(Integer customerId) {
        return CART_PREFIX + customerId;
    }

    @Override
    public List<CartItemVO> getCart(Integer customerId) {
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String key = cartKey(customerId);

        Map<String, Integer> entries = hashOps.entries(key);
        List<CartItemVO> result = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : entries.entrySet()) {
            Integer flowerId = Integer.parseInt(entry.getKey());
            Integer quantity = entry.getValue();

            Flower flower = flowerMapper.selectById(flowerId);
            if (flower != null) {
                result.add(new CartItemVO(
                        null,  // Redis 版没有 cartId
                        flower.getFlowerId(),
                        flower.getFlowerName(),
                        flower.getUnitPrice(),
                        quantity,
                        flower.getStock()
                ));
            }
        }
        return result;
    }

    @Override
    public void addItem(Integer customerId, Integer flowerId, Integer storeId, Integer quantity) {
        Flower flower = flowerMapper.selectById(flowerId);
        if (flower == null) {
            throw new BusinessException("鲜花不存在");
        }
        if (flower.getStock() < quantity) {
            throw new BusinessException("库存不足");
        }

        String key = cartKey(customerId);
        // 获取已有数量，累加
        Object existing = redisTemplate.opsForHash().get(key, String.valueOf(flowerId));
        int newQty = quantity;
        if (existing != null) {
            newQty += (Integer) existing;
        }
        redisTemplate.opsForHash().put(key, String.valueOf(flowerId), newQty);
    }

    @Override
    public void updateQuantity(Long cartId, Integer quantity) {
        // Redis 版不用 cartId，用 flowerId 定位
        // 这个方法在 Redis 版中通过 addItem 覆盖实现
    }

    /** Redis 版：根据 flowerId 更新数量 */
    public void updateQuantity(Integer customerId, Integer flowerId, Integer quantity) {
        String key = cartKey(customerId);
        if (quantity <= 0) {
            redisTemplate.opsForHash().delete(key, String.valueOf(flowerId));
        } else {
            redisTemplate.opsForHash().put(key, String.valueOf(flowerId), quantity);
        }
    }

    @Override
    public void removeItem(Long cartId) {
        // Redis 版不用 cartId
    }

    /** Redis 版：根据 flowerId 删除 */
    public void removeItem(Integer customerId, Integer flowerId) {
        String key = cartKey(customerId);
        redisTemplate.opsForHash().delete(key, String.valueOf(flowerId));
    }

    @Override
    public void clearCart(Integer customerId) {
        redisTemplate.delete(cartKey(customerId));
    }
}
