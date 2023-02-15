package com.github.stazxr.zblog.base.service;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.*;
import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.entity.UserTokenStorage;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
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
    boolean updateUserHeadImg(UserDto updateDto);

    /**
     * 修改个人基础信息
     *
     * @param updateDto 用户信息
     * @return boolean
     */
    boolean updateUserBaseInfo(UserDto updateDto);

    /**
     * 修改个人密码
     *
     * @param passDto 用户密码信息
     * @return boolean
     */
    boolean updateUserPass(UserUpdatePassDto passDto);

    /**
     * 强制修改密码
     *
     * @param passDto 用户密码信息
     */
    void forceUpdatePass(UserUpdatePassDto passDto);

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

    /**
     * 记录用户令牌信息
     *
     * @param tokenStorage token
     * @param flag 1: 登录；2：续签
     */
    void storageUserToken(UserTokenStorage tokenStorage, int flag);

    /**
     * 查询用户持久化的令牌信息
     *
     * @param userId 用户序列
     * @return UserTokenStorage
     */
    UserTokenStorage queryUserStorageToken(Long userId);

    /**
     * 查询用户列表
     *
     * @param queryDto 查询参数
     * @return userList
     */
    PageInfo<UserVo> queryUserListByPage(UserQueryDto queryDto);

    /**
     * 查询用户详情
     *
     * @param userId 用户序列
     * @return UserVo
     */
    UserVo queryUserDetail(Long userId);

    /**
     * 新增用户
     *
     * @param user 用户信息
     */
    void addUser(UserDto user);

    /**
     * 编辑用户
     *
     * @param user 用户信息
     */
    void editUser(UserDto user);

    /**
     * 删除用户
     *
     * @param userId 用户序列
     */
    void deleteUser(Long userId);

    /**
     * 更新用户状态
     *
     * @param user 用户信息
     */
    void updateUserStatus(UserDto user);

    /**
     * 用户注册
     *
     * @param registerDto 注册信息
     */
    void userRegister(UserRegisterDto registerDto);

    /**
     * 通过邮箱修改密码
     *
     * @param forgetPwdDto 密码信息
     */
    void updateUserPwdByEmail(ForgetPwdDto forgetPwdDto);
}
