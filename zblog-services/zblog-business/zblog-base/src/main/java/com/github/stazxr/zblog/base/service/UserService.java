package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import com.github.stazxr.zblog.base.domain.dto.UserDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.vo.UserVo;

/**
 * 用户管理业务层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface UserService extends SecurityUserService, IService<User> {
    /**
     * 分页查询用户列表
     *
     * @param queryDto 查询参数
     * @return IPage<UserVo>
     */
    IPage<UserVo> queryUserListByPage(UserQueryDto queryDto);

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
     * @param userDto 用户信息
     */
    void addUser(UserDto userDto);

    /**
     * 编辑用户
     *
     * @param userDto 用户
     */
    void editUser(UserDto userDto);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    void deleteUser(Long userId);
}
