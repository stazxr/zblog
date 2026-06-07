package com.github.stazxr.zblog.base.service;

import com.github.stazxr.zblog.base.domain.bo.LoginUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证管理业务层
 *
 * @author SunTao
 * @since 2022-07-24
 */
public interface AuthService {
    /**
     * 获取当前登录用户信息
     *
     * @return LoginUser
     */
    LoginUser currentUserDetail();

    /**
     * 刷新令牌信息
     *
     * @param request  请求
     * @param response 响应
     * @return boolean 续签是否成功
     */
    boolean refreshToken(HttpServletRequest request, HttpServletResponse response);
}
