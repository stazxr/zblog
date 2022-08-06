package com.github.stazxr.zblog.base.service;

import com.github.stazxr.zblog.base.domain.dto.UserUpdateDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateEmailDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdatePassDto;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.core.base.BaseService;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 修改个人头像
     *
     * @param updateDto 用户信息
     * @return boolean
     */
    boolean updateUserHeadImg(UserUpdateDto updateDto);

    /**
     * 修改个人基础信息
     *
     * @param updateDto 用户信息
     * @return boolean
     */
    boolean updateUserBaseInfo(UserUpdateDto updateDto);

    /**
     * 修改个人密码
     *
     * @param passDto 用户密码信息
     * @return boolean
     */
    boolean updateUserPass(UserUpdatePassDto passDto);

    /**
     * 修改个人邮箱
     *
     * @param emailDto 用户邮箱信息
     * @return boolean
     */
    boolean updateUserEmail(UserUpdateEmailDto emailDto);

    /**
     * 修改用户的登录信息
     *
     * @param request 请求信息
     * @param userId 用户编号
     */
    void updateUserLoginInfo(HttpServletRequest request, Long userId);
}
