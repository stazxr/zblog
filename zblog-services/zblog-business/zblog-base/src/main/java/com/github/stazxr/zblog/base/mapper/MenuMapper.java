package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单存储
 *
 * @author SunTao
 * @since 2025-07-06
 */
public interface MenuMapper {
    /**
     * 查询所有的菜单
     *
     * @return 所有的菜单列表
     */
    List<Permission> selectAllMenu();

    /**
     * 查询用户对应的菜单
     *
     * @param userId 用户序列
     * @return 启用的菜单列表
     */
    List<Permission> selectMenuByUserId(@Param("userId") Long userId);

}
