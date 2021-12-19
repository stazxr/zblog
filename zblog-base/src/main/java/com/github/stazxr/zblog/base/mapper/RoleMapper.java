package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

/**
 * 角色数据持久层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 清除角色对应的权限信息
     *
     * @param roleId 角色ID
     */
    @Delete("DELETE FROM sys_role_permission_tbl WHERE `ROLE_ID`=#{roleId}")
    void clearRolePermByRoleId(Long roleId);

    /**
     * 角色授权
     *
     * @param roleId       角色Id
     * @param permissionId 权限Id
     */
    @Insert("INSERT INTO sys_role_permission_tbl(`ROLE_ID`, `PERMISSION_ID`) VALUES(#{roleId}, #{permissionId})")
    void saveRolePerm(Long roleId, Long permissionId);
}
