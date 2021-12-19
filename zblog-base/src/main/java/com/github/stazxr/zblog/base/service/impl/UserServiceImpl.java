package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.entity.User;
import com.github.stazxr.zblog.base.mapper.UserMapper;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.core.base.Const;
import com.github.stazxr.zblog.util.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户-业务实现层
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return User
     */
    @Override
    public User selectUserByUsername(String username) {
        return new LambdaQueryChainWrapper<>(userMapper).eq(User::getUsername, username).one();
    }

    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱
     * @return User
     */
    @Override
    public User selectUserByEmail(String email) {
        return new LambdaQueryChainWrapper<>(userMapper).eq(User::getEmail, email).one();
    }

    /**
     * 根据昵称查找用户
     *
     * @param nickname 昵称
     * @return User
     */
    @Override
    public User selectUserByNickname(String nickname) {
        return new LambdaQueryChainWrapper<>(userMapper).eq(User::getNickname, nickname).one();
    }

    /**
     * 设置用户的登录时间
     *
     * @param username  用户名
     * @param loginTime 登录时间
     */
    @Override
    public void setUserLoginTime(String username, String loginTime) {
        userMapper.setUserLoginTime(username, loginTime);
    }

    /**
     * 新增用户
     *
     * @param user    用户信息
     * @param roleIds 角色列表
     * @return boolean
     */
    @Override
    public boolean saveUser(User user, Long[] roleIds) {
        if (user.insert()) {
            String username = user.getUsername();
            if (!Const.USER_ADMIN.equals(username) && !Const.USER_SYSTEM.equals(username)) {
                assignUser(user.getId(), roleIds);
            }
            return true;
        }
        return false;
    }

    /**
     * 编辑用户
     *
     * @param user    用户信息
     * @param roleIds 角色列表
     * @return boolean
     */
    @Override
    public boolean updateUser(User user, Long[] roleIds) {
        if (user.updateById()) {
            String username = user.getUsername();
            if (!Const.USER_ADMIN.equals(username) && !Const.USER_SYSTEM.equals(username)) {
                assignUser(user.getId(), roleIds);
            }
            return true;
        }
        return false;
    }

    /**
     * 修改密码
     *
     * @param userId   用户ID
     * @param password 新密码
     * @return boolean
     */
    @Override
    public boolean updateUserPwd(Long userId, String password) {
        String changTime = DateUtils.formatNow();
        return userMapper.updateUserPwd(userId, password, changTime);
    }

    /**
     * 修改用户密码状态
     *
     * @param username 用户名
     * @param status   状态
     */
    @Override
    public void updateUserPwdStatus(String username, boolean status) {
        userMapper.updateUserPwdStatus(username, status);
    }

    /**
     * 用户授权
     *
     * @param userId  用户Id
     * @param roleIds 角色Id列表
     */
    private void assignUser(Long userId, Long[] roleIds) {
        userMapper.clearUserRoleByUserId(userId);
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                userMapper.saveUserRole(userId, roleId);
            }
        }
    }
}
