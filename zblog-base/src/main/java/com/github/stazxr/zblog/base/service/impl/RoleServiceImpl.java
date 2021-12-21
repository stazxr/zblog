//package com.github.stazxr.zblog.base.service.impl;
//
//import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.github.stazxr.zblog.base.domain.entity.Role;
//import com.github.stazxr.zblog.base.mapper.RoleMapper;
//import com.github.stazxr.zblog.base.service.RoleService;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * 角色业务实现层
// *
// * @author SunTao
// * @since 2020-11-16
// */
//@Service
//@Transactional(rollbackFor = Exception.class)
//public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
//    @Resource
//    private RoleMapper roleMapper;
//
//    /**
//     * 根据角色编码查询角色
//     *
//     * @param roleCode 角色编码
//     * @return Role
//     */
//    @Override
//    public Role selectRoleByCode(String roleCode) {
//        return new LambdaQueryChainWrapper<>(roleMapper).eq(Role::getRoleCode, roleCode).one();
//    }
//
//    /**
//     * 根据用户编号获取用户持有的所有的角色
//     *
//     * @param userId 用户名
//     * @return roles 角色列表
//     */
//    @Override
//    public List<Role> selectRolesByUserId(Long userId) {
//        String inSql = "SELECT `ROLE_ID` FROM sys_user_role_tbl WHERE `USER_ID`=" + userId;
//        return new LambdaQueryChainWrapper<>(roleMapper).inSql(Role::getId, inSql).list();
//    }
//
//    /**
//     * 根据权限ID获取所有的角色-权限关联信息
//     *
//     * @param permissionId 权限ID
//     * @return RoleResourceModels
//     */
//    @Override
//    public List<Role> getRolePermission(Long permissionId) {
//        String inSql = "SELECT `ROLE_ID` FROM sys_role_permission_tbl WHERE `PERMISSION_ID`=" + permissionId;
//        return new LambdaQueryChainWrapper<>(roleMapper).inSql(Role::getId, inSql).list();
//    }
//
//    /**
//     * 新增角色
//     *
//     * @param role          角色信息
//     * @param permissionIds 权限列表
//     * @return boolean
//     */
//    @Override
//    public boolean saveRole(Role role, Long[] permissionIds) {
//        if (role.insert()) {
//            assignRole(role.getId(), permissionIds);
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 修改角色
//     *
//     * @param role          角色信息
//     * @param permissionIds 权限列表
//     * @return boolean
//     */
//    @Override
//    public boolean updateRole(Role role, Long[] permissionIds) {
//        if (role.updateById()) {
//            assignRole(role.getId(), permissionIds);
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 角色授权
//     *
//     * @param roleId        角色Id
//     * @param permissionIds 权限Id列表
//     */
//    private void assignRole(Long roleId, Long[] permissionIds) {
//        roleMapper.clearRolePermByRoleId(roleId);
//        if (permissionIds != null) {
//            for (Long permissionId : permissionIds) {
//                roleMapper.saveRolePerm(roleId, permissionId);
//            }
//        }
//    }
//}
