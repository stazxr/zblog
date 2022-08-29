package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.vo.PermissionVo;
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

import java.util.List;

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
     * 查询权限列表
     *
     * @param queryDto 查询参数
     * @return permsList
     */
    @GetMapping(value = "/list")
    @Router(name = "查询权限列表", code = "permPage")
    public Result queryPermList(PermissionQueryDto queryDto) {
        List<PermissionVo> permissions = permissionService.queryPermList(queryDto);
        return Result.success().data(permissions);
    }

    /**
     * 构建菜单树
     *
     * @return menuTree
     */
    @GetMapping(value = "/buildMenus")
    @Router(name = "构建菜单树", code = "buildMenus", level = BaseConst.PermLevel.PUBLIC)
    public Result buildMenus() {
        return Result.success().data(permissionService.buildMenus(SecurityUtils.getLoginId()));
    }

    /**
     * 查找所有注册的权限编码
     *
     * @return permCodes
     */
    @GetMapping(value = "/queryPermCodes")
    @Router(name = "构建菜单树", code = "queryPermCodes", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermCodes() {
        return Result.success().data(permissionService.queryPermCodes());
    }

    /**
     * 查询权限详情
     *
     * @param permId 权限ID
     * @return PermissionVo
     */
    @GetMapping(value = "/queryPermDetail")
    @Router(name = "查询权限详情", code = "queryPermDetail")
    public Result queryPermDetail(Long permId) {
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
        if (permission.getId() != null) {
            log.warn("A new permission cannot already have an ID: {}", permission.getId());
            return Result.failure("参数错误");
        }

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
        if (permission.getId() == null) {
            return Result.failure("参数错误，缺失ID");
        }

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
        if (permId == null) {
            return Result.failure("参数错误，缺失ID");
        }

        permissionService.deletePermission(permId);
        return Result.success();
    }
}
