package com.github.stazxr.zblog.base.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 部分公共查询
 *
 * @author SunTao
 * @since 2022-07-24
 */
public interface ZblogMapper {
    /**
     * 清除记住我信息
     *
     * @param username 用户名
     */
    void removeRememberMe(@Param("username") String username);
}
