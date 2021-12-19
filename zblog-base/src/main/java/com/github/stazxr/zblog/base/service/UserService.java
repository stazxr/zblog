package com.github.stazxr.zblog.base.service;

import com.github.stazxr.zblog.base.entity.User;
import com.github.stazxr.zblog.core.base.service.BaseService;

/**
 * 用户服务层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface UserService extends BaseService<User> {
    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return User
     */
    User selectUserByUsername(String username);

    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱
     * @return User
     */
    User selectUserByEmail(String email);

    /**
     * 根据昵称查找用户
     *
     * @param nickname 昵称
     * @return User
     */
    User selectUserByNickname(String nickname);

    /**
     * 设置用户的登录时间
     *
     * @param username      用户名
     * @param loginTime     登录时间
     */
    void setUserLoginTime(String username, String loginTime);

    /**
     * 新增用户
     *
     * @param user    用户信息
     * @param roleIds 角色列表
     * @return boolean
     */
    boolean saveUser(User user, Long[] roleIds);

    /**
     * 编辑用户
     *
     * @param user    用户信息
     * @param roleIds 角色列表
     * @return boolean
     */
    boolean updateUser(User user, Long[] roleIds);

    /**
     * 修改密码
     *
     * @param userId   用户ID
     * @param password 新密码
     * @return boolean
     */
    boolean updateUserPwd(Long userId, String password);

    /**
     * 修改用户密码状态
     *
     * @param username 用户名
     * @param status 状态
     */
    void updateUserPwdStatus(String username, boolean status);
}
