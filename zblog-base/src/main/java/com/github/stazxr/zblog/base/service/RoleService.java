//package com.github.stazxr.zblog.base.service;
//
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.github.stazxr.zblog.base.domain.entity.Role;
//
//import java.util.List;
//
///**
// * 角色服务层
// *
// * @author SunTao
// * @since 2020-11-16
// */
//public interface RoleService extends IService<Role> {
//    /**
//     * 根据角色编码查询角色信息
//     *
//     * @param roleCode 角色编码
//     * @return Role
//     */
//    Role selectRoleByCode(String roleCode);
//
//    /**
//     * 根据用户编号获取用户持有的所有的角色
//     *
//     * @param userId 用户名
//     * @return roles 角色列表
//     */
//    List<Role> selectRolesByUserId(Long userId);
//
//    /**
//     * 根据权限ID获取所有的角色-权限关联信息
//     *
//     * @param permissionId 权限ID
//     * @return RoleResourceModels
//     */
//    List<Role> getRolePermission(Long permissionId);
//
//    /**
//     * 新增角色
//     *
//     * @param role 角色信息
//     * @param permissionIds 权限列表
//     * @return boolean
//     */
//    boolean saveRole(Role role, Long[] permissionIds);
//
//    /**
//     * 修改角色
//     *
//     * @param role 角色信息
//     * @param permissionIds 权限列表
//     * @return boolean
//     */
//    boolean updateRole(Role role, Long[] permissionIds);
//}
