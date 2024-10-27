package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.UserPassLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户密码更新日志持久化接口
 *
 * @author SunTao
 * @since 2022-08-03
 */
public interface UserPassLogMapper {
    /**
     * 查询用户历史密码修改记录
     *
     * @param userId 用户序列
     * @param count 账号
     * @return oldPassList
     */
    List<String> selectUserOldPass(@Param("userId") Long userId, @Param("count") int count);

    /**
     * 新增用户密码修改记录
     *
     * @param passLog 实体信息
     * @return if success return 1
     */
    int insertUserPassLog(UserPassLog passLog);
}
