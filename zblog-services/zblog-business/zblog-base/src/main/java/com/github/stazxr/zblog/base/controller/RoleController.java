package com.github.stazxr.zblog.base.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.base.domain.dto.RoleAuthDto;
import com.github.stazxr.zblog.base.domain.dto.RoleDto;
import com.github.stazxr.zblog.base.domain.dto.query.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.dto.UserRoleDto;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;
import com.github.stazxr.zblog.base.service.RoleService;
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
import java.util.Set;

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
@Api(value = "RoleController", tags = { "角色管理" })
public class RoleController {
    private final RoleService roleService;

    /**
     * 分页查询角色列表
     *
     * @param queryDto 查询参数
     * @return Page<RoleVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询角色列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询角色列表", code = "ROLEQ001")
    public IPage<RoleVo> pageList(RoleQueryDto queryDto) {
        return roleService.queryRoleListByPage(queryDto);
    }

    /**
     * 查询角色列表
     *
     * @param queryDto 查询参数
     * @return List<RoleVo>
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "查询角色列表（公共）")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询角色列表（公共）", code = "ROLEQ002", level = RouterLevel.PUBLIC)
    public List<RoleVo> queryRoleList(RoleQueryDto queryDto) {
        return roleService.queryRoleList(queryDto);
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
    @Router(name = "查询角色详情", code = "ROLEQ003")
    public RoleVo queryRoleDetail(@RequestParam Long roleId) {
        return roleService.queryRoleDetail(roleId);
    }

    /**
     * 新增角色
     *
     * @param roleDto 角色
     */
    @Log
    @PostMapping(value = "/addRole")
    @ApiOperation(value = "新增角色")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增角色", code = "ROLEA001")
    public void addRole(@RequestBody @Validated(Create.class) RoleDto roleDto) {
        roleService.addRole(roleDto);
    }

    /**
     * 编辑角色
     *
     * @param roleDto 角色
     */
    @Log
    @PostMapping(value = "/editRole")
    @ApiOperation(value = "编辑角色")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "编辑角色", code = "ROLEU001")
    public void editRole(@RequestBody @Validated(Update.class) RoleDto roleDto) {
        roleService.editRole(roleDto);
    }

    /**
     * 角色授权
     *
     * @param authDto 授权信息
     */
    @Log
    @PostMapping(value = "/authRole")
    @ApiOperation(value = "角色授权")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "角色授权", code = "ROLEU002")
    public void authRole(@RequestBody @Validated RoleAuthDto authDto) {
        roleService.authRole(authDto);
    }

    /**
     * 查询角色对应的权限id列表
     *
     * @param roleId 角色id
     * @return permIds
     */
    @GetMapping(value = "/queryPermIdsByRoleId")
    @ApiOperation(value = "查询角色对应的权限id列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询角色对应的权限id列表", code = "ROLEQ004", level = RouterLevel.PUBLIC, remark = "适配角色授权")
    public Set<Long> queryPermIdsByRoleId(@RequestParam Long roleId) {
        return roleService.queryPermIdsByRoleId(roleId);
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     */
    @Log
    @PostMapping(value = "/deleteRole")
    @ApiOperation(value = "删除角色")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除角色", code = "ROLED001")
    public void deleteRole(@RequestParam Long roleId) {
        roleService.deleteRole(roleId);
    }

    /**
     * 批量新增角色用户
     *
     * @param userRoleDto 角色 - 用户对应信息
     */
    @Log
    @PostMapping(value = "/batchAddUserRole")
    @ApiOperation(value = "批量新增角色用户")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "批量新增角色用户", code = "ROLEA002")
    public void batchAddUserRole(@RequestBody UserRoleDto userRoleDto) {
        roleService.batchAddUserRole(userRoleDto);
    }

    /**
     * 批量删除角色用户
     *
     * @param userRoleDto 角色 - 用户对应信息
     */
    @Log
    @PostMapping(value = "/batchDeleteUserRole")
    @ApiOperation(value = "批量删除角色用户")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "批量删除角色用户", code = "ROLED002")
    public void batchDeleteUserRole(@RequestBody UserRoleDto userRoleDto) {
        roleService.batchDeleteUserRole(userRoleDto);
    }
}
