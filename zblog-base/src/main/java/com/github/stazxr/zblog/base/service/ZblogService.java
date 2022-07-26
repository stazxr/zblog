package com.github.stazxr.zblog.base.service;

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
}
