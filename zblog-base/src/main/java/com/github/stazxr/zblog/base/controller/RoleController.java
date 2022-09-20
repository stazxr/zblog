package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.RoleAuthDto;
import com.github.stazxr.zblog.base.domain.dto.query.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.dto.UserRoleDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理
 *
 * @author SunTao
 * @since 2020-11-19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    /**
     * 查询角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    @GetMapping(value = "/list")
    @Router(name = "查询角色列表", code = "queryRoleList", level = BaseConst.PermLevel.PUBLIC)
    public Result queryRoleList(RoleQueryDto queryDto) {
        return Result.success().data(roleService.queryRoleList(queryDto));
    }

    /**
     * 分页查询角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    @GetMapping(value = "/pageList")
    @Router(name = "分页查询角色列表", code = "queryRoleListByPage")
    public Result pageList(RoleQueryDto queryDto) {
        return Result.success().data(roleService.queryRoleListByPage(queryDto));
    }

    /**
     * 查询角色详情
     *
     * @param roleId 角色序列
     * @return RoleVo
     */
    @GetMapping(value = "/queryRoleDetail")
    @Router(name = "查询角色详情", code = "queryRoleDetail", level = BaseConst.PermLevel.PUBLIC)
    public Result queryRoleDetail(Long roleId) {
        return Result.success().data(roleService.queryRoleDetail(roleId));
    }

    /**
     * 新增角色
     *
     * @param role 角色
     * @return Result
     */
    @Log
    @PostMapping(value = "/addRole")
    @Router(name = "新增角色", code = "addRole")
    public Result addRole(@RequestBody Role role) {
        if (role.getId() != null) {
            log.warn("A new role cannot already have an ID: {}", role.getId());
            return Result.failure("参数错误");
        }

        roleService.addRole(role);
        return Result.success();
    }

    /**
     * 编辑角色
     *
     * @param role 角色
     * @return Result
     */
    @Log
    @PostMapping(value = "/editRole")
    @Router(name = "编辑角色", code = "editRole")
    public Result editRole(@RequestBody Role role) {
        if (role.getId() == null) {
            return Result.failure("参数错误，缺失ID");
        }

        roleService.editRole(role);
        return Result.success();
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteRole")
    @Router(name = "删除角色", code = "deleteRole")
    public Result deleteRole(@RequestPostSingleParam Long roleId) {
        if (roleId == null) {
            return Result.failure("参数错误，缺失ID");
        }

        roleService.deleteRole(roleId);
        return Result.success();
    }

    /**
     * 角色授权
     *
     * @param authDto 授权信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/authRole")
    @Router(name = "角色授权", code = "authRole")
    public Result authRole(@RequestBody RoleAuthDto authDto) {
        if (authDto.getRoleId() == null) {
            return Result.failure("参数错误，缺失roleID");
        }

        roleService.authRole(authDto);
        return Result.success();
    }

    /**
     * 查询角色对应的权限序号列表
     *
     * @param roleId 角色序列
     * @return permIdList
     */
    @GetMapping(value = "/queryPermIdsByRoleId")
    @Router(name = "查询角色对应的权限详情", code = "queryPermIdsByRoleId", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermIdsByRoleId(Long roleId) {
        return Result.success().data(roleService.queryPermIdsByRoleId(roleId));
    }

    /**
     * 查询角色对应的用户列表
     *
     * @param queryDto 查询参数
     * @return userList
     */
    @GetMapping(value = "/pageUsersByRoleId")
    @Router(name = "查询角色对应的用户列表", code = "pageUsersByRoleId")
    public Result pageUsersByRoleId(UserQueryDto queryDto) {
        return Result.success().data(roleService.pageUsersByRoleId(queryDto));
    }

    /**
     * 批量新增角色用户
     *
     * @param userRoleDto 角色 - 用户对应信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/batchAddUserRole")
    @Router(name = "批量新增角色用户", code = "batchAddUserRole")
    public Result batchAddUserRole(@RequestBody UserRoleDto userRoleDto) {
        if (userRoleDto.getRoleId() == null) {
            return Result.failure("参数错误，缺失roleID");
        }

        roleService.batchAddUserRole(userRoleDto);
        return Result.success();
    }

    /**
     * 批量删除角色用户
     *
     * @param userRoleDto 角色 - 用户对应信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/batchDeleteUserRole")
    @Router(name = "批量删除角色用户", code = "batchDeleteUserRole")
    public Result batchDeleteUserRole(@RequestBody UserRoleDto userRoleDto) {
        if (userRoleDto.getRoleId() == null) {
            return Result.failure("参数错误，缺失roleID");
        }

        roleService.batchDeleteUserRole(userRoleDto);
        return Result.success();
    }
}
