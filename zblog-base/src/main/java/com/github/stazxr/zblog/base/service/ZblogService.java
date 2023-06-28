package com.github.stazxr.zblog.base.service;

import com.github.stazxr.zblog.base.domain.bo.LoginUser;
import com.github.stazxr.zblog.base.domain.vo.UserVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 部分公共接口
 *
 * @author SunTao
 * @since 2022-07-24
 */
public interface ZblogService {
    /**
     * 清除记住我信息
     *
     * @param username 用户名
     */
    void removeRememberMe(String username);

    /**
     * 检查用户的登录状态
     *
     * @param request 请求信息
     * @return LoginUser
     */
    LoginUser checkUserLoginStatus(HttpServletRequest request);
}
