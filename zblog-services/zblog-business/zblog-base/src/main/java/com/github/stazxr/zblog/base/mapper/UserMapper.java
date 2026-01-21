package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateProfileDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserLogQueryDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户管理数据层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据 {@code userId} 查询用户信息。
     *
     * @param userId 用户序列
     * @return 用户的 {@link User} 实例
     */
    User selectUserById(@Param("userId") Long userId);

    /**
     * 根据 {@code username} 查询用户信息
     *
     * @param username 用户名
     * @return User
     */
    User selectUserByUsername(@Param("username") String username);

    /**
     * 根据 {@code userId} 查询用户角色信息。
     *
     * @param userId 用户 ID
     * @return 用户角色列表
     */
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 查询所有的权限编码信息。
     *
     * @return 权限编码列表
     */
    List<String> selectAllMd5PermCodes();

    /**
     * 根据 {@code userId} 查询用户权限信息。
     *
     * @param userId 用户 ID
     * @return 用户权限列表
     */
    List<String> selectMd5PermCodesByUserId(@Param("userId") Long userId);

    /**
     * 更新用户登录信息-成功
     *
     * @param userId     用户id
     * @param loginTime  登录时间
     */
    void updateLoginInfoWhenSuccess(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime);

    /**
     * 更新用户登录信息-失败
     *
     * @param userId           用户id
     * @param maxFailCount     登录失败阙值
     * @param lockedExpireTime 锁定到期时间
     */
    void updateLoginInfoWhenFailed(@Param("userId") Long userId, @Param("maxFailCount") int maxFailCount, @Param("lockedExpireTime") LocalDateTime lockedExpireTime);

    /**
     * 查询用户列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<UserVo>
     */
    IPage<UserVo> selectUserList(@Param("page") Page<UserVo> page, @Param("query") UserQueryDto queryDto);

    /**
     * 查询用户详情
     *
     * @param userId 用户序列
     * @return UserVo
     */
    UserVo selectUserDetail(@Param("userId") Long userId);

    /**
     * 修改用户状态
     *
     * @param userId 用户序列
     * @param status 用户状态
     */
    void updateUserStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 用户密码修改
     *
     * @param userId              用户序列
     * @param password            密码
     * @param changePasswordTime  密码修改时间
     * @return 修改行数
     */
    int updateUserPassword(@Param("userId") Long userId, @Param("password") String password,
        @Param("changePasswordTime") LocalDateTime changePasswordTime, @Param("passwordExpireTime") LocalDateTime passwordExpireTime);

    /**
     * 用户头像修改
     *
     * @param userId  用户序列
     * @param headImg 用户头像
     * @return 修改行数
     */
    int updateUserHeadImg(@Param("userId") Long userId, @Param("headImg") String headImg);

    /**
     * 用户邮箱修改
     *
     * @param userId  用户序列
     * @param email  用户邮箱
     * @return 修改行数
     */
    int updateUserEmail(@Param("userId") Long userId, @Param("email") String email);

    /**
     * 用户信息修改
     *
     * @param selfDto    用户个人信息
     * @param updateUser 修改用户
     * @param updateTime 修改时间
     * @return 修改行数
     */
    int updateUserSelf(@Param("u") UserUpdateProfileDto selfDto, @Param("updateUser") Long updateUser, @Param("updateTime") String updateTime);

    /**
     * 查询用户操作日志列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<LogVo>
     */
    IPage<LogVo> selectUserLogList(@Param("page") Page<LogVo> page, @Param("query") UserLogQueryDto queryDto);

    /**
     * 判断是否存在，忽略逻辑删除
     *
     * @param queryWrapper 查询条件
     * @return boolean
     */
    @Select("SELECT IF(EXISTS (SELECT 1 FROM user ${ew.customSqlSegment}), 1, 0)")
    boolean existsIgnoreDeleted(@Param(Constants.WRAPPER) LambdaQueryWrapper<User> queryWrapper);
}
