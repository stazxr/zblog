package com.github.stazxr.zblog.base.service;

import com.github.stazxr.zblog.base.domain.dto.UserUpdatePassDto;

/**
 * 用户中心管理业务层
 *
 * @author SunTao
 * @since 2025-12-26
 */
public interface UserCenterService {
    /**
     * 强制修改密码
     *
     * @param passDto 用户密码信息
     */
    void forceUpdatePass(UserUpdatePassDto passDto);

    /**
     * 修改个人密码
     *
     * @param passDto 用户密码信息
     */
    void updateUserPass(UserUpdatePassDto passDto);


//    /**
//     * 根据用户名查询用户信息
//     *
//     * @param username 用户名
//     * @return User
//     */
//    User queryUserByUsername(String username);
//
//    /**
//     * 修改个人头像
//     *
//     * @param updateDto 用户信息
//     * @return boolean
//     */
//    boolean updateUserHeadImg(UserDto updateDto);
//
//    /**
//     * 修改个人基础信息
//     *
//     * @param updateDto 用户信息
//     * @return boolean
//     */
//    boolean updateUserBaseInfo(UserDto updateDto);
//
//    /**
//     * 修改个人邮箱
//     *
//     * @param emailDto 用户邮箱信息
//     * @return boolean
//     */
//    boolean updateUserEmail(UserUpdateEmailDto emailDto);
//
//    /**
//     * 修改用户的登录信息
//     *
//     * @param request 请求信息
//     * @param userId 用户编号
//     */
//    void updateUserLoginInfo(HttpServletRequest request, Long userId);
//
//    /**
//     * 记录用户令牌信息
//     *
//     * @param tokenStorage token
//     * @param flag 1: 登录；2：续签
//     */
//    void storageUserToken(UserTokenStorage tokenStorage, int flag);
//
//    /**
//     * 查询用户持久化的令牌信息
//     *
//     * @param userId 用户序列
//     * @return UserTokenStorage
//     */
//    UserTokenStorage queryUserStorageToken(Long userId);
//
//    /**
//     * 清除用户持久化的令牌信息
//     *
//     * @param userId 用户序列
//     */
//    void clearUserStorageToken(Long userId);
//
//    /**
//     * 用户注册
//     *
//     * @param registerDto 注册信息
//     */
//    void userRegister(UserRegisterDto registerDto);
//
//    /**
//     * 通过邮箱修改密码
//     *
//     * @param forgetPwdDto 密码信息
//     */
//    void updateUserPwdByEmail(ForgetPwdDto forgetPwdDto);
}
