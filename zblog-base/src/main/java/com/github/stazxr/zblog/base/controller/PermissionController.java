//package com.github.stazxr.zblog.base.controller;
//
//import com.github.stazxr.zblog.base.model.entity.Permission;
//import com.github.stazxr.zblog.base.model.enums.PermissionType;
//import com.github.stazxr.zblog.base.service.PermissionService;
//import com.github.stazxr.zblog.core.annotation.Log;
//import com.github.stazxr.zblog.core.annotation.Route;
//import com.github.stazxr.zblog.core.enums.LogLevel;
//import com.github.stazxr.zblog.core.enums.LogType;
//import com.github.stazxr.zblog.core.model.Result;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * 权限管理
// *
// * @author SunTao
// * @since 2020-11-19
// */
//@Slf4j
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/perms")
//public class PermissionController {
//    private final PermissionService permissionService;
//
//    private final SecurityManager securityManager;
//
//    /**
//     * 查询权限列表
//     *
//     * @param menuFlag 是否只获取菜单
//     * @return 系统权限列表
//     */
//    @GetMapping("/list")
//    @Route(name = "查询权限列表", permCode = "queryPermission")
//    public Result listPermission(Boolean menuFlag) {
//        List<Permission> permissionList;
//        if (menuFlag != null && menuFlag) {
//            permissionList = permissionService.lambdaQuery()
//                    .select(Permission::getId, Permission::getParentId, Permission::getName, Permission::getType)
//                    .eq(Permission::getActive, true)
//                    .in(Permission::getType, PermissionType.DIR, PermissionType.MENU)
//                    .orderByAsc(Permission::getOrder)
//                    .list();
//        } else {
//            permissionList = permissionService.lambdaQuery().orderByAsc(Permission::getOrder).list();
//        }
//
//        return Result.success().data(permissionList);
//    }
//
//    /**
//     * 添加权限
//     *
//     * @param permission 权限信息
//     * @return Result result
//     */
//    @PostMapping("/add")
//    @Route(name = "添加权限", permCode = "addPermission")
//    @Log(name = "添加权限", type = LogType.ADD, level = LogLevel.WARN)
//    public Result addPermission(Permission permission) {
//        // 判断权限名称是否存在
//        if (permissionService.selectPermissionByName(permission.getName()) != null) {
//            return Result.failure("权限名称已存在");
//        }
//
//        if (PermissionType.DIR.equals(permission.getType())) {
//            // 目录的权限标识和Url为空
//            permission.setPerm(null);
//            permission.setUrl(null);
//        } else {
//            // 非目录判断权限标识和权限链接是否已经存在
//            if (permissionService.selectPermissionByPerm(permission.getPerm()) != null) {
//                return Result.failure("权限标识已存在");
//            }
//
//            if (permissionService.selectPermissionByUrl(permission.getUrl()) != null) {
//                return Result.failure("权限链接已存在");
//            }
//        }
//
//        permission.setActive(true);
//        if (permissionService.save(permission)) {
//            return Result.success();
//        }
//
//        return Result.failure();
//    }
//
//    /**
//     * 编辑权限
//     *
//     * @param permission 权限信息
//     * @return Result result
//     */
//    @PostMapping("/edit")
//    @ResponseBody
//    @Log(name = "编辑权限", type = LogType.UPDATE, level = LogLevel.WARN)
//    public Result editPermission(Permission permission) {
//        // 判断权限名称是否存在
//        Permission dbPerm = permissionService.selectPermissionByName(permission.getName());
//        if (dbPerm != null && !dbPerm.getId().equals(permission.getId())) {
//            return Result.failure("权限名称已存在");
//        }
//
//        Permission dbPermission = permissionService.getById(permission.getId());
//        if (PermissionType.DIR.equals(dbPermission.getType())) {
//            // 目录的权限标识和Url为空
//            permission.setPerm(null);
//            permission.setUrl(null);
//        } else {
//            // 非目录判断权限标识和权限Url是否已经存在
//            dbPerm = permissionService.selectPermissionByPerm(permission.getPerm());
//            if (dbPerm != null && !dbPerm.getId().equals(permission.getId())) {
//                return Result.failure("权限标识已存在");
//            }
//
//            dbPerm = permissionService.selectPermissionByUrl(permission.getUrl());
//            if (dbPerm != null && !dbPerm.getId().equals(permission.getId())) {
//                return Result.failure("权限Url已存在");
//            }
//        }
//
//        permission.setType(dbPermission.getType());
//        if (permissionService.updateById(permission)) {
//            return Result.success();
//        }
//
//        return Result.failure();
//    }
//
//    /**
//     * 权限下线
//     *
//     * @param permissionId 权限ID
//     * @return Result
//     */
//    @PostMapping("/offline")
//    @ResponseBody
//    @Route(desc = "权限下线")
//    @Log(name = "权限下线", type = LogType.UPDATE, level = LogLevel.WARN)
//    public Result offlinePermission(@RequestParam Long permissionId) {
//        boolean flag = permissionService.lambdaUpdate().eq(Permission::getId, permissionId).set(Permission::getActive, false).update();
//        if (flag) {
//            return Result.success("下线成功");
//        }
//        return Result.failure();
//    }
//
//    /**
//     * 权限上线
//     *
//     * @param permissionId 权限ID
//     * @return Result
//     */
//    @PostMapping("/online")
//    @ResponseBody
//    @RouteInfo(desc = "权限上线")
//    @OperateLog(name = "权限上线", module = "权限管理",
//        type = OperateType.UPDATE, level = OperateLevel.WARN
//    )
//    public Result onlinePermission(@RequestParam Long permissionId) {
//        boolean flag = permissionService.lambdaUpdate().eq(Permission::getId, permissionId).set(Permission::getActive, true).update();
//        if (flag) {
//            return Result.success("上线成功");
//        }
//        return Result.failure();
//    }
//
//    /**
//     * 删除权限
//     *
//     * @param permissionId 权限Id
//     * @return Result
//     */
//    @DeleteMapping("/delete")
//    @Route(name = "删除权限", permCode = "")
//    @Log(name = "删除权限", module = "权限管理",
//        type = OperateType.DELETE, level = OperateLevel.RISK
//    )
//    public Result deletePermission(@RequestParam Long permissionId) {
//        Integer subCount = permissionService.lambdaQuery().eq(Permission::getParentId, permissionId).count();
//        if (subCount > 0) {
//            return Result.failure("无法直接删除上级资源，请先删除下级资源");
//        }
//
//        if (permissionService.removeById(permissionId)) {
//            return Result.success();
//        }
//        return Result.failure();
//    }
//}
