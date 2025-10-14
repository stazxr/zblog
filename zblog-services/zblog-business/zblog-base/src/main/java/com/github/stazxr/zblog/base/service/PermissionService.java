package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.router.Resource;
import com.github.stazxr.zblog.base.domain.dto.PermissionDto;
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
     * @return PermissionVoList
     */
    List<PermissionVo> queryPermTree(PermissionQueryDto queryDto);

    /**
     * 查询权限详情
     *
     * @param permId 权限ID
     * @return PermissionVo
     */
    PermissionVo queryPermDetail(Long permId);

    /**
     * 分页查询权限接口列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<InterfaceVo>
     */
    PageInfo<InterfaceVo> pagePermInterfaces(PermissionQueryDto queryDto);

    /**
     * 分页查询权限角色列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<RoleVo>
     */
    PageInfo<RoleVo> pagePermRoles(PermissionQueryDto queryDto);

    /**
     * 分页查询权限日志列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<LogVo>
     */
    PageInfo<LogVo> pagePermLogs(PermissionQueryDto queryDto);

    /**
     * 查询权限编码列表
     *
     * @param searchKey 查询条件
     * @return List<PermCodeVo>
     */
    List<PermCodeVo> queryPermCodes(String searchKey);

    /**
     * 根据权限编码查询资源信息
     *
     * @param permCode 权限编码
     * @return Resource
     */
    Resource queryResourceByPermCode(String permCode);


    /**
     * 新增权限
     *
     * @param permissionDto 权限信息
     */
    void addPermission(PermissionDto permissionDto);

    /**
     * 编辑权限
     *
     * @param permissionDto 权限信息
     */
    void editPermission(PermissionDto permissionDto);

    /**
     * 删除权限
     *
     * @param permId 权限序列
     */
    void deletePermission(Long permId);

    /**
     * 批量删除角色权限
     *
     * @param rolePermDto 角色 - 权限对应信息
     */
    void batchDeleteRolePerm(RolePermDto rolePermDto);
}
