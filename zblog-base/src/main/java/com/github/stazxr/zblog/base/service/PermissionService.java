//package com.github.stazxr.zblog.base.service;
//
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.github.stazxr.zblog.base.domain.entity.Permission;
//
//import java.util.List;
//import java.util.Set;
//
///**
// * 权限服务层
// *
// * @author SunTao
// * @since 2020-11-16
// */
//public interface PermissionService extends IService<Permission> {
//    /**
//     * 根据请求链接获取权限
//     *
//     * @param url 请求链接
//     * @return Permission
//     */
//    Permission selectPermissionByUrl(String url);
//
//    /**
//     * 根据权限标识查找权限信息
//     *
//     * @param perm 权限标识
//     * @return Permission
//     */
//    Permission selectPermissionByPerm(String perm);
//
//    /**
//     * 根据权限名称查找权限信息
//     *
//     * @param name 权限名称
//     * @return Permission
//     */
//    Permission selectPermissionByName(String name);
//
//    /**
//     * 获取用户对应的菜单列表
//     *
//     * @param username 用户名
//     * @return 菜单列表
//     */
//    List<Permission> selectUserMenus(String username);
//
//    /**
//     * 查询用户对应的权限列表
//     *
//     * @param username 用户名
//     * @return perms，用户拥有的权限信息
//     */
//    Set<String> selectUserPerms(String username);
//}
