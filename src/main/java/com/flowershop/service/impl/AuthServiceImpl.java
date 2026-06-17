package com.flowershop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flowershop.common.BusinessException;
import com.flowershop.common.ResultCode;
import com.flowershop.dto.LoginDTO;
import com.flowershop.dto.RegisterDTO;
import com.flowershop.entity.Customer;
import com.flowershop.entity.FlowerStore;
import com.flowershop.mapper.CustomerMapper;
import com.flowershop.mapper.FlowerStoreMapper;
import com.flowershop.service.AuthService;
import com.flowershop.util.JwtUtil;
import com.flowershop.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 认证服务实现（Phase 2：BCrypt 密码加密）
 * <p>
 * 密码策略：
 * - 新注册：密码用 BCrypt 加密后存入 password 列
 * - 登录：优先验证 password 列（BCrypt）；如果为空则验证 cpwd/fpwd（明文），
 *   验证成功后自动升级为 BCrypt 加密存储
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private FlowerStoreMapper flowerStoreMapper;

    // ==================== 顾客注册 ====================

    @Override
    public LoginVO customerRegister(RegisterDTO dto) {
        // 第1步：检查手机号是否已注册
        Customer existing = customerMapper.selectOne(
                new LambdaQueryWrapper<Customer>()
                        .eq(Customer::getPhone, dto.getPhone())
        );
        if (existing != null) {
            throw new BusinessException("该手机号已被注册");
        }

        // 第2步：构建顾客实体
        Customer customer = new Customer();
        customer.setCustomerName(dto.getName());
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        // ⭐ 密码用 BCrypt 加密存到 password 列
        customer.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        customer.setRegisterTime(LocalDateTime.now());
        customer.setStatus(1);

        // 第3步：插入数据库
        customerMapper.insert(customer);

        // 第4步：生成 Token
        String token = JwtUtil.generateToken(customer.getCustomerId(), "CUSTOMER");
        return new LoginVO(token, customer.getCustomerId(),
                customer.getCustomerName(), "CUSTOMER");
    }

    // ==================== 花店注册 ====================

    @Override
    public LoginVO storeRegister(RegisterDTO dto) {
        FlowerStore existing = flowerStoreMapper.selectOne(
                new LambdaQueryWrapper<FlowerStore>()
                        .eq(FlowerStore::getStoreName, dto.getName())
        );
        if (existing != null) {
            throw new BusinessException("该店铺名已被注册");
        }

        FlowerStore store = new FlowerStore();
        store.setStoreName(dto.getName());
        store.setAddress(dto.getAddress());
        store.setContactPhone(dto.getPhone());
        store.setOwnerName(dto.getOwnerName());
        // ⭐ 密码用 BCrypt 加密
        store.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        store.setCreateTime(LocalDateTime.now());
        store.setStatus(1);

        flowerStoreMapper.insert(store);

        String token = JwtUtil.generateToken(store.getStoreId(), "STORE");
        return new LoginVO(token, store.getStoreId(),
                store.getStoreName(), "STORE");
    }

    // ==================== 顾客登录（自动升级老密码） ====================

    @Override
    public LoginVO customerLogin(LoginDTO dto) {
        Customer customer = customerMapper.selectOne(
                new LambdaQueryWrapper<Customer>()
                        .eq(Customer::getPhone, dto.getAccount())
        );
        if (customer == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "账号或密码错误");
        }

        // ⭐ 验证密码：优先 BCrypt，兼容明文
        if (!verifyPassword(dto.getPassword(), customer.getPassword(), customer.getCpwd())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "账号或密码错误");
        }

        // ⭐ 如果 password 列为空，自动升级为 BCrypt
        if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
            customer.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
            customerMapper.updateById(customer);
        }

        String token = JwtUtil.generateToken(customer.getCustomerId(), "CUSTOMER");
        return new LoginVO(token, customer.getCustomerId(),
                customer.getCustomerName(), "CUSTOMER");
    }

    // ==================== 花店登录（自动升级老密码） ====================

    @Override
    public LoginVO storeLogin(LoginDTO dto) {
        FlowerStore store = flowerStoreMapper.selectOne(
                new LambdaQueryWrapper<FlowerStore>()
                        .eq(FlowerStore::getStoreName, dto.getAccount())
        );
        if (store == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "账号或密码错误");
        }

        // ⭐ 验证密码：优先 BCrypt，兼容明文
        if (!verifyPassword(dto.getPassword(), store.getPassword(), store.getFpwd())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "账号或密码错误");
        }

        // ⭐ 如果 password 列为空，自动升级为 BCrypt
        if (store.getPassword() == null || store.getPassword().isEmpty()) {
            store.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
            flowerStoreMapper.updateById(store);
        }

        String token = JwtUtil.generateToken(store.getStoreId(), "STORE");
        return new LoginVO(token, store.getStoreId(),
                store.getStoreName(), "STORE");
    }

    // ==================== 密码验证工具方法 ====================

    /**
     * 验证密码：优先 BCrypt，兼容老明文密码
     *
     * @param input        用户输入的明文密码
     * @param bcryptHash   数据库 password 列（BCrypt 密文，新用户有值，老用户为空）
     * @param plaintextPwd 数据库 cpwd/fpwd 列（明文，老用户有值）
     * @return true=密码正确
     */
    private boolean verifyPassword(String input, String bcryptHash, String plaintextPwd) {
        // 新用户：用 BCrypt 验证
        if (bcryptHash != null && !bcryptHash.isEmpty()) {
            try {
                return BCrypt.checkpw(input, bcryptHash);
            } catch (Exception e) {
                return false;
            }
        }
        // 老用户：回退到明文比对（等下次登录就会自动升级）
        if (plaintextPwd != null && !plaintextPwd.isEmpty()) {
            return input.equals(plaintextPwd);
        }
        return false;
    }
}
