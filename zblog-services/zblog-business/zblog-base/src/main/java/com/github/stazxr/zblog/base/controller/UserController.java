package com.github.stazxr.zblog.base.controller;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.base.domain.dto.*;
import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.base.service.UserService;
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

/**
 * 用户管理
 *
 * @author SunTao
 * @since 2020-12-10
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Api(value = "UserController", tags = { "用户管理" })
public class UserController {
    private final UserService userService;

    /**
     * 分页查询用户列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<UserVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询用户列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询用户列表", code = "USERQ001")
    public PageInfo<UserVo> pageList(UserQueryDto queryDto) {
        return userService.queryUserListByPage(queryDto);
    }

    /**
         * 分页查询用户列表（公共）
     *
     * @param queryDto 查询参数
     * @return PageInfo<UserVo>
     */
    @GetMapping(value = "/pageListOfPublic")
    @ApiOperation(value = "分页查询用户列表（公共）")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询用户列表（公共）", code = "USERQ002", level = RouterLevel.PUBLIC)
    public PageInfo<UserVo> pageListOfPublic(UserQueryDto queryDto) {
        return userService.queryUserListByPage(queryDto);
    }

    /**
     * 查询用户详情
     *
     * @param userId 用户id
     * @return UserVo
     */
    @GetMapping(value = "/queryUserDetail")
    @ApiOperation(value = "查询用户详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询用户详情", code = "USERQ003")
    public UserVo queryUserDetail(@RequestParam Long userId) {
        return userService.queryUserDetail(userId);
    }

    /**
     * 新增用户
     *
     * @param userDto 用户
     */
    @Log
    @PostMapping(value = "/addUser")
    @ApiOperation(value = "新增用户")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增用户", code = "USERA001")
    public void addUser(@RequestBody @Validated(Create.class) UserDto userDto) {
        userService.addUser(userDto);
    }

    /**
     * 编辑用户
     *
     * @param userDto 用户
     */
    @Log
    @PostMapping(value = "/editUser")
    @ApiOperation(value = "编辑用户")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "编辑用户", code = "USERU001")
    public void editUser(@RequestBody @Validated(Update.class) UserDto userDto) {
        userService.editUser(userDto);
    }

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    @Log
    @PostMapping(value = "/deleteUser")
    @ApiOperation(value = "删除用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除用户", code = "USERD001")
    public void deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
    }
}
