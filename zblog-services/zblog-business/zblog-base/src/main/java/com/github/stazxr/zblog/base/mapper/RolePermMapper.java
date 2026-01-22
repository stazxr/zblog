package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.RolePermissionRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 角色权限数据层
 *
 * @author SunTao
 * @since 2022-01-14
 */
public interface RolePermMapper {
    /**
     * 批量插入角色权限关联信息
     *
     * @param rolePerms 角色权限关联信息
     */
    void insertBatch(@Param("list") List<RolePermissionRelation> rolePerms);

    /**
     * 根据权限序列删除中间数据（软删除）
     *
     * @param permId 权限ID
     */
    void deleteByPermIdSoft(@Param("permId") Long permId);

    /**
     * 根据权限序列删除中间数据（硬删除）
     *
     * @param permId 权限ID
     */
    void deleteByPermIdHard(@Param("permId") Long permId);

    /**
     * 根据角色序列删除中间数据（软删除）
     *
     * @param roleId 角色ID
     */
    void deleteByRoleIdSoft(@Param("roleId") Long roleId);

    /**
     * 根据角色序列删除中间数据（硬删除）
     *
     * @param roleId 角色ID
     */
    void deleteByRoleIdHard(@Param("roleId") Long roleId);

    /**
     * 查询角色对应的权限序号列表
     *
     * @param roleId 角色ID
     * @return Set<Long> permIdList
     */
    Set<Long> selectPermIdsByRoleId(@Param("roleId") Long roleId);
}
