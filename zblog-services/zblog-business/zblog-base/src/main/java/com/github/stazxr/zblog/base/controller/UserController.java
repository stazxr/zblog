package com.github.stazxr.zblog.base.controller;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.msg.Result;
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












//    /**
//     * 修改个人头像
//     *
//     * @param updateDto 用户信息
//     * @return Result
//     */
//    @Log
//    @PostMapping("updateUserHeadImg")
//    @ApiOperation("修改个人头像")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "修改个人头像", code = "updateUserHeadImg", level = RouterLevel.PUBLIC)
//    public Result updateUserHeadImg(@RequestBody UserDto updateDto) {
//        return userService.updateUserHeadImg(updateDto) ? Result.success() : Result.failure();
//    }
//
//    /**
//     * 修改个人基础信息
//     *
//     * @param updateDto 用户信息
//     * @return Result
//     */
//    @Log
//    @PostMapping("updateUserBaseInfo")
//    @ApiOperation("修改个人基础信息")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "修改个人基础信息", code = "updateUserBaseInfo", level = RouterLevel.PUBLIC)
//    public Result updateUserBaseInfo(@RequestBody UserDto updateDto) {
//        return userService.updateUserBaseInfo(updateDto) ? Result.success() : Result.failure();
//    }
//
//    /**
//     * 修改个人密码
//     *
//     * @param passDto 用户密码信息
//     * @return Result
//     */
//    @Log
//    @PostMapping("updateUserPass")
//    @ApiOperation("修改个人密码")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "修改个人密码", code = "updateUserPass", level = RouterLevel.PUBLIC)
//    public Result updateUserPass(@RequestBody UserUpdatePassDto passDto) {
//        return userService.updateUserPass(passDto) ? Result.success() : Result.failure();
//    }
//
//    /**
//     * 强制修改密码
//     *
//     * @param passDto 用户密码信息
//     * @return Result
//     */
//    @PostMapping("forceUpdatePass")
//    @ApiOperation("强制修改密码")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "强制修改密码", code = "forceUpdateUserPass", level = RouterLevel.OPEN)
//    public Result forceUpdatePass(@RequestBody UserUpdatePassDto passDto) {
//        userService.forceUpdatePass(passDto);
//        return Result.success();
//    }
//
//    /**
//     * 修改个人邮箱
//     *
//     * @param emailDto 用户邮箱信息
//     * @return Result
//     */
//    @Log
//    @PostMapping("updateUserEmail")
//    @ApiOperation("修改个人邮箱")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "修改个人邮箱", code = "updateUserEmail", level = RouterLevel.PUBLIC)
//    public Result updateUserEmail(@RequestBody UserUpdateEmailDto emailDto) {
//        return userService.updateUserEmail(emailDto) ? Result.success() : Result.failure();
//    }
//
//
//    /**
//     * 用户注册
//     *
//     * @param registerDto 注册信息
//     * @return Result
//     */
//    @PostMapping(value = "/register")
//    @ApiOperation("用户注册")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "用户注册", code = "userRegister", level = RouterLevel.OPEN)
//    public Result userRegister(@RequestBody UserRegisterDto registerDto) {
//        userService.userRegister(registerDto);
//        return Result.success();
//    }
//
//    /**
//     * 通过邮箱修改密码
//     *
//     * @param forgetPwdDto 密码信息
//     * @return Result
//     */
//    @PostMapping(value = "/updateUserPwdByEmail")
//    @ApiOperation("通过邮箱修改密码")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "通过邮箱修改密码", code = "updateUserPwdByEmail", level = RouterLevel.OPEN)
//    public Result updateUserPwdByEmail(@RequestBody ForgetPwdDto forgetPwdDto) {
//        userService.updateUserPwdByEmail(forgetPwdDto);
//        return Result.success();
//    }
}
