package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据持久层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 查询邮箱个数
     *
     * @param userId 排除的用户序列
     * @param email 邮箱
     * @return count
     */
    int selectEmailCountNotSelf(@Param("userId") Long userId, @Param("email") String email);
}
