package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.domain.entity.UserPassLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户密码记录数据层
 *
 * @author SunTao
 * @since 2022-08-03
 */
public interface UserPassLogMapper extends BaseMapper<UserPassLog> {
    /**
     * 查询用户历史密码修改记录
     *
     * @param userId 用户序列
     * @param count 账号
     * @return oldPassList
     */
    List<String> selectUserHistoryPass(@Param("userId") Long userId, @Param("count") int count);
}
