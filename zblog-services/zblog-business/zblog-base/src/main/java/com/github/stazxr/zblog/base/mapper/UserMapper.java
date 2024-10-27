package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
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
     * 查询用户列表
     *
     * @param queryDto 查询参数
     * @return userList
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
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return User
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户信息
     *
     * @param email 邮箱
     * @return User
     */
    User selectByEmail(@Param("email") String email);

    /**
     * 根据昵称查询用户信息
     *
     * @param nickname 昵称
     * @return User
     */
    User selectByNickname(@Param("nickname") String nickname);

    /**
     * 根据用户名或邮箱查询登录用户信息
     *
     * @param username 用户名或邮箱
     * @param isEmail  是否是邮箱查询
     * @return User
     */
    User selectLoginUserByUsernameOrEmail(@Param("username") String username, @Param("isEmail") boolean isEmail);
}
