package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.msg.Result;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.base.domain.dto.PermissionDto;
import com.github.stazxr.zblog.base.domain.dto.query.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.dto.RolePermDto;
import com.github.stazxr.zblog.base.domain.vo.PermissionVo;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@Api(value = "PermissionController", tags = { "权限管理" })
public class PermissionController {
    private final PermissionService permissionService;

    /**
     * 查询权限树列表
     *
     * @param queryDto 查询参数
     * @return List<PermissionVo>
     */
    @GetMapping(value = "/queryPermTree")
    @ApiOperation(value = "查询权限树列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询权限树列表", code = "PERMQ001")
    public List<PermissionVo> queryPermTree(PermissionQueryDto queryDto) {
        return permissionService.queryPermTree(queryDto);
    }

    /**
     * 查询权限树列表（公共）
     *
     * @param queryDto 查询参数
     * @return List<PermissionVo>
     */
    @GetMapping(value = "/queryPublicPermTree")
    @ApiOperation(value = "查询权限树列表（公共）")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询权限树列表（公共）", code = "PERMQ008", level = RouterLevel.PUBLIC)
    public List<PermissionVo> queryPublicPermTree(PermissionQueryDto queryDto) {
        return permissionService.queryPermTree(queryDto);
    }

    /**
     * 查询权限详情
     *
     * @param permId 权限id
     * @return PermissionVo
     */
    @GetMapping(value = "/queryPermDetail")
    @ApiOperation("查询权限详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "permId", value = "权限id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询权限详情", code = "PERMQ002")
    public Result queryPermDetail(@RequestParam Long permId) {
        return Result.s(permissionService.queryPermDetail(permId));
    }

    /**
     * 分页查询权限接口列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<InterfaceVo>
     */
    @GetMapping(value = "/pagePermInterfaces")
    @ApiOperation(value = "分页查询权限接口列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询权限接口列表", code = "PERMQ003")
    public Result pagePermInterfaces(PermissionQueryDto queryDto) {
        return Result.success().data(permissionService.pagePermInterfaces(queryDto));
    }

    /**
     * 分页查询权限角色列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<RoleVo>
     */
    @GetMapping(value = "/pagePermRoles")
    @ApiOperation(value = "分页查询权限角色列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询权限角色列表", code = "PERMQ004")
    public Result pagePermRoles(PermissionQueryDto queryDto) {
        return Result.s(permissionService.pagePermRoles(queryDto));
    }

    /**
     * 分页查询权限日志列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<LogVo>
     */
    @GetMapping("/pagePermLogs")
    @ApiOperation(value = "分页查询权限日志列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询权限日志列表", code = "PERMQ005")
    public Result pagePermLogs(PermissionQueryDto queryDto) {
        return Result.s(permissionService.pagePermLogs(queryDto));
    }

    /**
     * 查询权限编码列表
     *
     * @param searchKey 查询条件
     * @return List<PermCodeVo>
     */
    @GetMapping(value = "/queryPermCodes")
    @ApiOperation(value = "查询权限编码列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "searchKey", value = "权限名称或权限编码", dataTypeClass = String.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询权限编码列表", code = "PERMQ006", level = RouterLevel.PUBLIC)
    public Result queryPermCodes(@RequestParam(required = false) String searchKey) {
        return Result.s(permissionService.queryPermCodes(searchKey));
    }

    /**
     * 根据权限编码查询资源信息
     *
     * @param permCode 权限编码
     * @return {@link com.github.stazxr.zblog.bas.router.Resource}
     */
    @GetMapping(value = "/queryResourceByPermCode")
    @ApiOperation(value = "根据权限编码查询资源信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "permCode", value = "权限编码", dataTypeClass = String.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_5_0_0 })
    @Router(name = "根据权限编码查询资源信息", code = "PERMQ007", level = RouterLevel.PUBLIC)
    public Result queryResourceByPermCode(@RequestParam String permCode) {
        return Result.s(permissionService.queryResourceByPermCode(permCode));
    }

    /**
     * 新增权限
     *
     * @param permissionDto 权限
     */
    @Log
    @PostMapping(value = "/addPerm")
    @ApiOperation(value = "新增权限")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增权限", code = "PERMA001")
    public void addPerm(@RequestBody @Validated(Create.class) PermissionDto permissionDto) {
        permissionService.addPermission(permissionDto);
    }

    /**
     * 编辑权限
     *
     * @param permissionDto 权限信息
     */
    @Log
    @PostMapping(value = "/editPerm")
    @ApiOperation(value = "编辑权限")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "编辑权限", code = "PERMU001")
    public void editPerm(@RequestBody @Validated(Update.class) PermissionDto permissionDto) {
        permissionService.editPermission(permissionDto);
    }

    /**
     * 删除权限
     *
     * @param permId 权限ID
     */
    @Log
    @PostMapping(value = "/deletePerm")
    @ApiOperation(value = "删除权限")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "permId", value = "权限id", dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除权限", code = "PERMD001")
    public void deletePerm(@RequestParam Long permId) {
        permissionService.deletePermission(permId);
    }

    /**
     * 批量删除角色权限
     *
     * @param rolePermDto 角色 - 权限对应信息
     */
    @Log
    @PostMapping(value = "/batchDeleteRolePerm")
    @ApiOperation(value = "批量删除角色权限")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "批量删除角色权限", code = "batchDeleteRolePerm")
    public void batchDeleteRolePerm(@RequestBody RolePermDto rolePermDto) {
        permissionService.batchDeleteRolePerm(rolePermDto);
    }
}
