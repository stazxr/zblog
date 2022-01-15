package com.github.stazxr.zblog.base.service;

import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.core.base.BaseService;

/**
 * 用户服务层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface UserService extends BaseService<User> {
    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return User
     */
    User queryUserByUsername(String username);
}
