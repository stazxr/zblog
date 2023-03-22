package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.RoleAuthDto;
import com.github.stazxr.zblog.base.domain.dto.query.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.dto.UserRoleDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "RoleController", tags = { "角色控制器" })
public class RoleController {
    private final RoleService roleService;

    /**
     * 查询角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "查询角色列表（公共）")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询角色列表（公共）", code = "queryRoleList", level = BaseConst.PermLevel.PUBLIC)
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
    @ApiOperation(value = "分页查询角色列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询角色列表", code = "queryRoleListByPage")
    public Result pageList(RoleQueryDto queryDto) {
        return Result.success().data(roleService.queryRoleListByPage(queryDto));
    }

    /**
     * 查询角色详情
     *
     * @param roleId 角色id
     * @return RoleVo
     */
    @GetMapping(value = "/queryRoleDetail")
    @ApiOperation(value = "查询角色详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询角色详情", code = "queryRoleDetail", level = BaseConst.PermLevel.PUBLIC)
    public Result queryRoleDetail(@RequestParam Long roleId) {
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
    @ApiOperation(value = "新增角色")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增角色", code = "addRole")
    public Result addRole(@RequestBody Role role) {
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
    @ApiOperation(value = "编辑角色")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "编辑角色", code = "editRole")
    public Result editRole(@RequestBody Role role) {
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
    @ApiOperation(value = "删除角色")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除角色", code = "deleteRole")
    public Result deleteRole(@RequestParam Long roleId) {
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
    @ApiOperation(value = "角色授权")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "角色授权", code = "authRole")
    public Result authRole(@RequestBody RoleAuthDto authDto) {
        roleService.authRole(authDto);
        return Result.success();
    }

    /**
     * 查询角色对应的权限列表
     *
     * @param roleId 角色序列
     * @return permIdList
     */
    @GetMapping(value = "/queryPermIdsByRoleId")
    @ApiOperation(value = "查询角色对应的权限列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询角色对应的权限列表", code = "queryPermIdsByRoleId", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermIdsByRoleId(@RequestParam Long roleId) {
        return Result.success().data(roleService.queryPermIdsByRoleId(roleId));
    }

    /**
     * 查询角色对应的用户列表
     *
     * @param queryDto 查询参数
     * @return userList
     */
    @GetMapping(value = "/pageUsersByRoleId")
    @ApiOperation(value = "查询角色对应的用户列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
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
    @ApiOperation(value = "批量新增角色用户")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "批量新增角色用户", code = "batchAddUserRole")
    public Result batchAddUserRole(@RequestBody UserRoleDto userRoleDto) {
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
    @ApiOperation(value = "批量删除角色用户")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "批量删除角色用户", code = "batchDeleteUserRole")
    public Result batchDeleteUserRole(@RequestBody UserRoleDto userRoleDto) {
        roleService.batchDeleteUserRole(userRoleDto);
        return Result.success();
    }
}
