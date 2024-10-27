package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.UserTokenStorage;
import org.apache.ibatis.annotations.Param;

/**
 * 用户令牌持久化接口
 *
 * @author SunTao
 * @since 2022-08-18
 */
public interface UserTokenStorageMapper {
    /**
     * 新增用户令牌信息
     *
     * @param tokenStorage token
     * @return row
     */
    int insertUserTokenStorage(UserTokenStorage tokenStorage);

    /**
     * 更新用户令牌信息
     *
     * @param tokenStorage token
     * @return row
     */
    int updateUserTokenStorage(UserTokenStorage tokenStorage);

    /**
     * 删除用户令牌信息
     *
     * @param userId userId
     */
    void deleteUserTokenStorage(@Param("userId") Long userId);

    /**
     * 查询用户持久化的令牌信息
     *
     * @param userId 用户序列
     * @return UserTokenStorage
     */
    UserTokenStorage selectUserTokenStorageByUserId(@Param("userId") Long userId);
}
