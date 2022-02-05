package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.entity.Role;

import java.util.List;

/**
 * 角色服务层
 *
 * @author SunTao
 * @since 2020-11-16
 */
public interface RoleService extends IService<Role> {
    /**
     * 查询用户角色列表（包含被禁用的角色）
     *
     * @param userId 用户序列
     * @return Roles
     */
    List<Role> queryRolesByUserId(Long userId);

    /**
     * 查询资源角色列表（包含被禁用的角色）
     *
     * @param permissionId 权限序列
     * @return Roles
     */
    List<Role> queryRolesByPermissionId(Long permissionId);
}
