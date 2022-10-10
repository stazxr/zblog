package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.query.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.dto.RolePermDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.log.annotation.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 权限管理
 *
 * @author SunTao
 * @since 2020-11-19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/perms")
public class PermissionController {
    private final PermissionService permissionService;

    /**
     * 查询权限列表（树）
     *
     * @param queryDto 查询参数
     * @return PermissionVoList
     */
    @GetMapping(value = "/treeList")
    @Router(name = "查询权限列表（树）", code = "queryPermTreeList")
    public Result queryPermTreeList(PermissionQueryDto queryDto) {
        return Result.success().data(permissionService.queryPermTreeList(queryDto));
    }

    /**
     * 查询权限详情
     *
     * @param permId 权限ID
     * @return PermissionVo
     */
    @Log
    @GetMapping(value = "/queryPermDetail")
    @Router(name = "查询权限详情", code = "queryPermDetail")
    public Result queryPermDetail(Long permId) {
        return Result.success().data(permissionService.queryPermDetail(permId));
    }

    /**
     * 查询权限可访问的接口列表
     *
     * @param queryDto 查询参数
     * @return interfaceList
     */
    @GetMapping(value = "/queryPermInterfaces")
    @Router(name = "查询权限可访问的接口列表", code = "queryPermInterfaces", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermInterfaces(PermissionQueryDto queryDto) {
        return Result.success().data(permissionService.queryPermInterfaces(queryDto));
    }

    /**
     * 查询可以访问权限的角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    @GetMapping(value = "/queryPermRoles")
    @Router(name = "查询可以访问权限的角色列表", code = "queryPermRoles", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermRole(PermissionQueryDto queryDto) {
        return Result.success().data(permissionService.queryPermRoles(queryDto));
    }

    /**
     * 查询权限的操作日志列表
     *
     * @param queryDto 查询参数
     * @return logList
     */
    @GetMapping("/queryPermLogs")
    @Router(name = "查询权限的操作日志列表", code = "queryPermLogs", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermLogs(PermissionQueryDto queryDto) {
        return Result.success().data(permissionService.queryPermLogs(queryDto));
    }

    /**
     * 查询权限树
     *
     * @param queryDto 查询参数
     * @return PermissionVoList
     */
    @GetMapping(value = "/tree")
    @Router(name = "查询权限树", code = "queryPermTree", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermTree(PermissionQueryDto queryDto) {
        return Result.success().data(permissionService.queryPermTreeList(queryDto));
    }

    /**
     * 查询权限编码列表
     *
     * @param searchKey 查询条件
     * @return PermCodeVo
     */
    @GetMapping(value = "/queryPermCodes")
    @Router(name = "查询权限编码列表", code = "queryPermCodes", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermCodes(String searchKey) {
        return Result.success().data(permissionService.queryPermCodes(searchKey));
    }

    /**
     * 查询权限信息
     *
     * @param permId 权限ID
     * @return PermissionVo
     */
    @GetMapping(value = "/queryPermInfo")
    @Router(name = "查询权限信息", code = "queryPermInfo", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermInfo(Long permId) {
        return Result.success().data(permissionService.queryPermDetail(permId));
    }

    /**
     * 新增权限
     *
     * @param permission 权限
     * @return Result
     */
    @Log
    @PostMapping(value = "/addPerm")
    @Router(name = "新增权限", code = "addPerm")
    public Result addPerm(@RequestBody Permission permission) {
        permissionService.addPermission(permission);
        return Result.success();
    }

    /**
     * 编辑权限
     *
     * @param permission 权限
     * @return Result
     */
    @Log
    @PostMapping(value = "/editPerm")
    @Router(name = "编辑权限", code = "editPerm")
    public Result editPerm(@RequestBody Permission permission) {
        permissionService.editPermission(permission);
        return Result.success();
    }

    /**
     * 删除权限
     *
     * @param permId 权限ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deletePerm")
    @Router(name = "删除权限", code = "deletePerm")
    public Result deletePerm(@RequestPostSingleParam Long permId) {
        permissionService.deletePermission(permId);
        return Result.success();
    }

    /**
     * 查询用户菜单列表
     *
     * @return menuTree
     */
    @GetMapping(value = "/queryUserMenus")
    @Router(name = "查询用户菜单列表", code = "queryUserMenus", level = BaseConst.PermLevel.PUBLIC)
    public Result buildUserMenus() {
        return Result.success().data(permissionService.queryUserMenus(SecurityUtils.getLoginId()));
    }

    /**
     * 批量删除角色权限
     *
     * @param rolePermDto 角色 - 权限对应信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/batchDeleteRolePerm")
    @Router(name = "批量删除角色权限", code = "batchDeleteRolePerm")
    public Result batchDeleteRolePerm(@RequestBody RolePermDto rolePermDto) {
        permissionService.batchDeleteRolePerm(rolePermDto);
        return Result.success();
    }
}
