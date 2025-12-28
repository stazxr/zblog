package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.security.service.SecurityRoleService;
import com.github.stazxr.zblog.base.domain.dto.RoleAuthDto;
import com.github.stazxr.zblog.base.domain.dto.RoleDto;
import com.github.stazxr.zblog.base.domain.dto.query.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.dto.UserRoleDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;

import java.util.List;
import java.util.Set;

/**
 * 角色管理业务层
 *
 * @author SunTao
 * @since 2020-11-16
 */
public interface RoleService extends SecurityRoleService, IService<Role> {
    /**
     * 分页查询角色列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<RoleVo>
     */
    PageInfo<RoleVo> queryRoleListByPage(RoleQueryDto queryDto);

    /**
     * 查询角色列表
     *
     * @param queryDto 查询参数
     * @return List<RoleVo>
     */
    List<RoleVo> queryRoleList(RoleQueryDto queryDto);

    /**
     * 查询角色详情
     *
     * @param roleId 角色序列
     * @return RoleVo
     */
    RoleVo queryRoleDetail(Long roleId);

    /**
     * 新增角色
     *
     * @param roleDto 角色
     */
    void addRole(RoleDto roleDto);

    /**
     * 编辑角色
     *
     * @param roleDto 角色
     */
    void editRole(RoleDto roleDto);

    /**
     * 角色授权
     *
     * @param authDto 授权信息
     */
    void authRole(RoleAuthDto authDto);

    /**
     * 查询角色对应的权限id列表
     *
     * @param roleId 角色id
     * @return permIds
     */
    Set<Long> queryPermIdsByRoleId(Long roleId);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     */
    void deleteRole(Long roleId);

    /**
     * 批量新增用户角色
     *
     * @param userRoleDto 角色 - 用户对应信息
     */
    void batchAddUserRole(UserRoleDto userRoleDto);

    /**
     * 批量删除用户角色
     *
     * @param userRoleDto 角色 - 用户对应信息
     */
    void batchDeleteUserRole(UserRoleDto userRoleDto);
}
