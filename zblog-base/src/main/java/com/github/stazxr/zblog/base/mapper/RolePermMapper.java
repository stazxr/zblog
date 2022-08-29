package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.domain.entity.RolePermissionRelation;
import org.apache.ibatis.annotations.Param;

/**
 * 角色权限数据持久层
 *
 * @author SunTao
 * @since 2022-01-14
 */
public interface RolePermMapper extends BaseMapper<RolePermissionRelation> {
    /**
     * 根据权限ID删除中间数据
     *
     * @param permId 权限ID
     */
    void deleteByPermId(@Param("permId") Long permId);

    /**
     * 根据角色ID和权限ID删除中间数据
     *
     * @param permId 权限ID
     * @param roleId 角色ID
     */
    void deleteByRoleIdAndPermId(@Param("permId") Long permId, @Param("roleId") Long roleId);
}
