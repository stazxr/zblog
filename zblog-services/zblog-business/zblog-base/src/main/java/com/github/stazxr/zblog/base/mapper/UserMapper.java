package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据持久层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据 {@code userId} 查询用户信息。
     *
     * @param userId 用户 ID
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
     * 更新用户登录时间
     *
     * @param userId     用户id
     * @param loginTime  登录时间
     * @param errorCount 登录失败次数
     */
    void updateUserLoginTime(@Param("userId") Long userId, @Param("loginTime") String loginTime, @Param("errorCount") Integer errorCount);











    /**
     * 查询用户列表
     *
     * @param queryDto 查询参数
     * @return List<UserVo>
     */
    List<UserVo> selectUserList(UserQueryDto queryDto);

    /**
     * 查询用户详情
     *
     * @param userId 用户序列
     * @return UserVo
     */
    UserVo selectUserDetail(@Param("userId") Long userId);

    /**
     * 查询用户登录失败次数
     *
     * @param userId 用户序列
     * @return 登录失败次数
     */
    int selectUserLoginErrorCount(@Param("userId") Long userId);

    /**
     * 修改用户状态
     *
     * @param userId 用户序列
     * @param status 用户状态
     */
    void updateUserStatus(@Param("userId") Long userId, @Param("status") Integer status);


//
//    /**
//     * 根据邮箱查询用户信息
//     *
//     * @param email 邮箱
//     * @return User
//     */
//    User selectByEmail(@Param("email") String email);
//
//    /**
//     * 根据昵称查询用户信息
//     *
//     * @param nickname 昵称
//     * @return User
//     */
//    User selectByNickname(@Param("nickname") String nickname);
//
//    /**
//     * 根据用户名或邮箱查询登录用户信息
//     *
//     * @param username 用户名或邮箱
//     * @param isEmail  是否是邮箱查询
//     * @return User
//     */
//    User selectLoginUserByUsernameOrEmail(@Param("username") String username, @Param("isEmail") boolean isEmail);
}
