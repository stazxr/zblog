package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.bas.router.Resource;
import com.github.stazxr.zblog.base.domain.dto.PermissionDto;
import com.github.stazxr.zblog.base.domain.dto.query.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.vo.*;

import java.util.List;

/**
 * 权限管理业务层
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
}
