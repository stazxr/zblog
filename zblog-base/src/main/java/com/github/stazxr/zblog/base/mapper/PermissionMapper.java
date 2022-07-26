package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限数据持久层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 查询所有的菜单
     *
     * @return 所有的菜单列表
     */
    List<Permission> selectMenu();

    /**
     * 查询用户对应的菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Permission> selectMenuByUserId(@Param("userId") Long userId);
}
