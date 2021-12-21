//package com.github.stazxr.zblog.base.service.impl;
//
//import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.github.stazxr.zblog.base.domain.entity.Permission;
//import com.github.stazxr.zblog.base.domain.enums.PermissionType;
//import com.github.stazxr.zblog.base.mapper.PermissionMapper;
//import com.github.stazxr.zblog.base.service.PermissionService;
//import com.github.stazxr.zblog.base.util.Constants;
//import com.github.stazxr.zblog.core.base.BaseConst;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * 权限业务实现层
// *
// * @author SunTao
// * @since 2020-11-16
// */
//@Service
//@Transactional(rollbackFor = Exception.class)
//public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
//    @Resource
//    private PermissionMapper permissionMapper;
//
//    /**
//     * 根据请求链接获取权限
//     *
//     * @param url 请求链接
//     * @return Permission
//     */
//    @Override
//    public Permission selectPermissionByUrl(String url) {
//        return new LambdaQueryChainWrapper<>(permissionMapper).eq(Permission::getUrl, url).one();
//    }
//
//    /**
//     * 根据权限标识查找权限信息
//     *
//     * @param perm 权限标识
//     * @return Permission
//     */
//    @Override
//    public Permission selectPermissionByPerm(String perm) {
//        return new LambdaQueryChainWrapper<>(permissionMapper).eq(Permission::getPerm, perm).one();
//    }
//
//    /**
//     * 根据权限名称查找权限信息
//     *
//     * @param name 权限名称
//     * @return Permission
//     */
//    @Override
//    public Permission selectPermissionByName(String name) {
//        return new LambdaQueryChainWrapper<>(permissionMapper).eq(Permission::getName, name).one();
//    }
//
//    /**
//     * 获取用户对应的菜单列表
//     *
//     * @param username 用户名
//     * @return 菜单列表
//     */
//    @Override
//    public List<Permission> selectUserMenus(String username) {
//        List<Permission> permissions;
//        if (BaseConst.USER_ADMIN.equals(username)) {
//            permissions = new LambdaQueryChainWrapper<>(permissionMapper)
//                    .in(Permission::getType, Arrays.asList(PermissionType.DIR, PermissionType.MENU))
//                    .orderByAsc(Permission::getOrder)
//                    .list();
//        } else {
//            permissions = permissionMapper.selectUserMenus(username);
//        }
//        return buildPermissionTree(permissions);
//    }
//
//    /**
//     * 查询用户对应的权限列表
//     *
//     * @param username 用户名
//     * @return perms，用户拥有的权限信息
//     */
//    @Override
//    public Set<String> selectUserPerms(String username) {
//        if (BaseConst.USER_ADMIN.equals(username)) {
//            Set<String> permList = new HashSet<>();
//            new LambdaQueryChainWrapper<>(permissionMapper)
//                    .select(Permission::getPerm)
//                    .isNotNull(Permission::getPerm)
//                    .list()
//                    .forEach(dd -> permList.add(dd.getPerm()));
//            return permList;
//        } else {
//            return permissionMapper.selectUserPerms(username);
//        }
//    }
//
//    private static List<Permission> buildPermissionTree(List<Permission> permissionList) {
//        Map<Long, List<Permission>> parentIdToPermissionListMap = permissionList.stream().collect(Collectors.groupingBy(Permission::getParentId));
//        List<Permission> rootLevelPermissionList = parentIdToPermissionListMap.getOrDefault(Constants.TOP_MENU_ID, Collections.emptyList());
//        fetchChildren(rootLevelPermissionList, parentIdToPermissionListMap);
//        return rootLevelPermissionList;
//    }
//
//    private static void fetchChildren(List<Permission> permissionList, Map<Long, List<Permission>> parentIdToPermissionListMap) {
//        if (permissionList == null || permissionList.isEmpty()) {
//            return;
//        }
//        for (Permission permission : permissionList) {
//            List<Permission> childrenList = parentIdToPermissionListMap.get(permission.getId());
//            fetchChildren(childrenList, parentIdToPermissionListMap);
//            permission.setChildrenList(childrenList);
//        }
//    }
//}
