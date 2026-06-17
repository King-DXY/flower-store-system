package com.flowershop.service;

import com.flowershop.dto.LoginDTO;
import com.flowershop.dto.RegisterDTO;
import com.flowershop.vo.LoginVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /** 顾客注册 */
    LoginVO customerRegister(RegisterDTO dto);

    /** 花店注册 */
    LoginVO storeRegister(RegisterDTO dto);

    /** 顾客登录（用手机号 + 密码） */
    LoginVO customerLogin(LoginDTO dto);

    /** 花店登录（用店铺名 + 密码） */
    LoginVO storeLogin(LoginDTO dto);
}
