package com.github.stazxr.zblog.base.service;

import com.github.stazxr.zblog.base.domain.bo.LoginUser;

/**
 * 认证管理业务层
 *
 * @author SunTao
 * @since 2022-07-24
 */
public interface AuthService {
    LoginUser currentUserDetail();
}
