package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.query.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.dto.RolePermDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.vo.*;
import com.github.stazxr.zblog.log.domain.vo.LogVo;

import java.util.List;
import java.util.Set;

/**
 * 权限服务层
 *
 * @author SunTao
 * @since 2020-11-16
 */
public interface PermissionService extends IService<Permission> {
    /**
     * 查询权限列表（树）
     *
     * @param queryDto 查询参数
     * @return permsList
     */
    List<PermissionVo> queryPermTreeList(PermissionQueryDto queryDto);

    /**
     * 查询权限详情
     *
     * @param permId 权限ID
     * @return PermissionVo
     */
    PermissionVo queryPermDetail(Long permId);

    /**
     * 查询权限对应的接口列表
     *
     * @param queryDto 查询参数
     * @return interfaceList
     */
    PageInfo<InterfaceVo> queryPermInterfaces(PermissionQueryDto queryDto);

    /**
     * 查询权限对应的角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    PageInfo<RoleVo> queryPermRoles(PermissionQueryDto queryDto);

    /**
     * 查询权限对应的日志列表
     *
     * @param queryDto 查询参数
     * @return logList
     */
    PageInfo<LogVo> queryPermLogs(PermissionQueryDto queryDto);

    /**
     * 查询权限编码列表
     *
     * @param searchKey 查询条件
     * @return PermCodeVo
     */
    List<PermCodeVo> queryPermCodes(String searchKey);

    /**
     * 新增权限
     *
     * @param permission 权限
     */
    void addPermission(Permission permission);

    /**
     * 编辑权限
     *
     * @param permission 权限
     */
    void editPermission(Permission permission);

    /**
     * 删除权限
     *
     * @param permId 权限序列
     */
    void deletePermission(Long permId);

    /**
     * 查询用户菜单列表
     *
     * @param userId 用户序列
     * @return menuTree
     */
    List<MenuVo> queryUserMenus(Long userId);

    /**
     * 批量删除角色权限
     *
     * @param rolePermDto 角色 - 权限对应信息
     */
    void batchDeleteRolePerm(RolePermDto rolePermDto);

    /**
     * 查询用户的权限列表（权限编码）
     *
     * @param userId 用户序列
     * @return permCodes
     */
    Set<String> queryUserPerms(Long userId);
}
