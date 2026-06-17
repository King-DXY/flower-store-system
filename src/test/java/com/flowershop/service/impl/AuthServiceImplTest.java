package com.flowershop.service.impl;

import com.flowershop.common.BusinessException;
import com.flowershop.dto.LoginDTO;
import com.flowershop.dto.RegisterDTO;
import com.flowershop.entity.Customer;
import com.flowershop.entity.FlowerStore;
import com.flowershop.mapper.CustomerMapper;
import com.flowershop.mapper.FlowerStoreMapper;
import com.flowershop.vo.LoginVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 认证服务单元测试
 * <p>
 * 用 Mockito 模拟 Mapper（不连真实数据库），测试 AuthService 的业务逻辑。
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock  // 假的 CustomerMapper，不会真连数据库
    private CustomerMapper customerMapper;

    @Mock
    private FlowerStoreMapper flowerStoreMapper;

    @InjectMocks  // 把假的 Mapper 注入到 AuthServiceImpl
    private AuthServiceImpl authService;

    private RegisterDTO registerDTO;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        registerDTO = new RegisterDTO();
        registerDTO.setName("张三");
        registerDTO.setPhone("13800138000");
        registerDTO.setEmail("test@qq.com");
        registerDTO.setPassword("123456");

        loginDTO = new LoginDTO();
        loginDTO.setAccount("13800138000");
        loginDTO.setPassword("123456");
    }

    // ========== 顾客注册测试 ==========

    @Test
    void testCustomerRegister_Success() {
        // 模拟：手机号不存在
        when(customerMapper.selectOne(any())).thenReturn(null);
        // 模拟：插入成功
        when(customerMapper.insert(any(Customer.class))).thenReturn(1);

        LoginVO result = authService.customerRegister(registerDTO);

        assertNotNull(result);
        assertEquals("CUSTOMER", result.getRole());
        assertEquals("张三", result.getName());
        assertNotNull(result.getToken());
        verify(customerMapper).insert(any(Customer.class));
    }

    @Test
    void testCustomerRegister_DuplicatePhone() {
        // 模拟：手机号已存在
        Customer existing = new Customer();
        existing.setPhone("13800138000");
        when(customerMapper.selectOne(any())).thenReturn(existing);

        // 应该抛异常
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            authService.customerRegister(registerDTO);
        });
        assertEquals("该手机号已被注册", ex.getMessage());
        // 确认没有执行插入
        verify(customerMapper, never()).insert(any());
    }

    // ========== 顾客登录测试 ==========

    @Test
    void testCustomerLogin_Success_BCrypt() {
        // 模拟：已有 BCrypt 加密密码的用户
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerName("张三");
        customer.setPhone("13800138000");
        customer.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt())); // BCrypt
        when(customerMapper.selectOne(any())).thenReturn(customer);

        LoginVO result = authService.customerLogin(loginDTO);

        assertNotNull(result);
        assertEquals("CUSTOMER", result.getRole());
        assertEquals("张三", result.getName());
    }

    @Test
    void testCustomerLogin_Success_Plaintext() {
        // 模拟：老用户，password 为空，cpwd 明文
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerName("老用户");
        customer.setPhone("13800138000");
        customer.setPassword(null);       // password 为空
        customer.setCpwd("123456");       // 明文密码

        when(customerMapper.selectOne(any())).thenReturn(customer);
        when(customerMapper.updateById(any())).thenReturn(1);  // 自动升级

        LoginVO result = authService.customerLogin(loginDTO);

        assertNotNull(result);
        assertEquals("老用户", result.getName());
        // 验证升级了密码（updateById 被调用）
        verify(customerMapper).updateById(any(Customer.class));
    }

    @Test
    void testCustomerLogin_WrongPassword() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setPhone("13800138000");
        customer.setPassword(BCrypt.hashpw("999999", BCrypt.gensalt())); // 密码是 999999
        when(customerMapper.selectOne(any())).thenReturn(customer);

        // 用户输入的是 123456，不匹配
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            authService.customerLogin(loginDTO);
        });
        assertEquals("账号或密码错误", ex.getMessage());
    }

    @Test
    void testCustomerLogin_UserNotFound() {
        when(customerMapper.selectOne(any())).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            authService.customerLogin(loginDTO);
        });
        assertEquals("账号或密码错误", ex.getMessage());
    }

    // ========== 花店注册测试 ==========

    @Test
    void testStoreRegister_Success() {
        registerDTO.setName("测试花店");
        registerDTO.setAddress("北京市");

        when(flowerStoreMapper.selectOne(any())).thenReturn(null);
        when(flowerStoreMapper.insert(any(FlowerStore.class))).thenReturn(1);

        LoginVO result = authService.storeRegister(registerDTO);

        assertNotNull(result);
        assertEquals("STORE", result.getRole());
        verify(flowerStoreMapper).insert(any(FlowerStore.class));
    }

    @Test
    void testStoreRegister_DuplicateName() {
        registerDTO.setName("已存在的花店");
        FlowerStore existing = new FlowerStore();
        existing.setStoreName("已存在的花店");
        when(flowerStoreMapper.selectOne(any())).thenReturn(existing);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            authService.storeRegister(registerDTO);
        });
        assertEquals("该店铺名已被注册", ex.getMessage());
    }
}
