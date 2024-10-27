package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.domain.dto.RolePermDto;
import com.github.stazxr.zblog.base.domain.entity.RolePermissionRelation;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 角色权限数据持久层
 *
 * @author SunTao
 * @since 2022-01-14
 */
public interface RolePermMapper extends BaseMapper<RolePermissionRelation> {
    /**
     * 根据权限序列删除中间数据
     *
     * @param permId 权限ID
     */
    void deleteByPermId(@Param("permId") Long permId);

    /**
     * 根据角色序列删除中间数据
     *
     * @param roleId 角色ID
     */
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色序列和权限序列删除中间数据
     *
     * @param permId 权限ID
     * @param roleId 角色ID
     */
    void deleteByRoleIdAndPermId(@Param("permId") Long permId, @Param("roleId") Long roleId);

    /**
     * 查询角色对应的权限序号列表
     *
     * @param roleId 角色序列
     * @return permIdList
     */
    Set<Long> selectPermIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量删除角色权限
     *
     * @param rolePermDto 角色 - 权限对应信息
     */
    void batchDeleteRolePerm(RolePermDto rolePermDto);
}
