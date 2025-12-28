package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.base.domain.dto.UserUpdatePassDto;
import com.github.stazxr.zblog.base.service.UserCenterService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户中心管理
 *
 * @author SunTao
 * @since 2025-12-24
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/center")
@Api(value = "UserCenterController", tags = { "用户中心管理" })
public class UserCenterController {
    private final UserCenterService userCenterService;

    /**
     * 强制修改密码
     *
     * @param passDto 用户密码信息
     */
    @Log
    @PostMapping("forceUpdatePass")
    @ApiOperation("强制修改密码")
    @ApiVersion(group = { BaseConst.ApiVersion.V_5_0_0 })
    @Router(name = "强制修改密码", code = "USECU001", level = RouterLevel.OPEN)
    public void forceUpdatePass(@RequestBody @Validated UserUpdatePassDto passDto) {
        userCenterService.forceUpdatePass(passDto);
    }

    /**
     * 修改个人密码
     *
     * @param passDto 用户密码信息
     */
    @Log
    @PostMapping("updateUserPass")
    @ApiOperation("修改个人密码")
    @ApiVersion(group = { BaseConst.ApiVersion.V_5_0_0 })
    @Router(name = "修改个人密码", code = "USECU002", level = RouterLevel.PUBLIC)
    public void updateUserPass(@RequestBody UserUpdatePassDto passDto) {
        userCenterService.updateUserPass(passDto);
    }





















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
//    /**
//     * 查询用户日志列表
//     *
//     * @param queryDto 查询参数
//     * @return userLog
//     */
//    @GetMapping("/queryUserLog")
//    @ApiOperation(value = "查询用户日志列表")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "查询用户日志列表", code = "queryUserLog", level = RouterLevel.PUBLIC)
//    public Result queryUserLog(LogQueryDto queryDto) {
//        // return Result.success().data(logService.queryUserLogListByPage(queryDto));
//        return null;
//    }
}
