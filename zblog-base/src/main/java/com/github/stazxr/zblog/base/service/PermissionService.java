package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.dto.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.vo.MenuVo;
import com.github.stazxr.zblog.base.domain.vo.PermCodeVo;
import com.github.stazxr.zblog.base.domain.vo.PermissionVo;

import java.util.List;

/**
 * 权限服务层
 *
 * @author SunTao
 * @since 2020-11-16
 */
public interface PermissionService extends IService<Permission> {
    /**
     * 查询权限列表
     *
     * @param queryDto 查询参数
     * @return permsList
     */
    List<PermissionVo> queryPermList(PermissionQueryDto queryDto);

    /**
     * 构造前端菜单模型
     *
     * @param userId 用户序列
     * @return menuTree
     */
    List<MenuVo> buildMenus(Long userId);

    /**
     * 查找所有注册的权限编码
     *
     * @return permCodes
     */
    List<PermCodeVo> queryPermCodes();

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
     * @param permId 权限ID
     */
    void deletePermission(Long permId);

    /**
     * 查询权限详情
     *
     * @param permId 权限ID
     * @return PermissionVo
     */
    PermissionVo queryPermDetail(Long permId);
}
