package com.github.stazxr.zblog.base.controller;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateEmailDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateHeadImgDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdatePassDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateSelfDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserLogQueryDto;
import com.github.stazxr.zblog.base.service.UserCenterService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("强制修改个人密码")
    @ApiVersion(group = { BaseConst.ApiVersion.V_5_0_0 })
    @Router(name = "强制修改个人密码", code = "USECU001", level = RouterLevel.OPEN)
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

    /**
     * 修改个人头像
     *
     * @param headImgDto 用户头像信息
     */
    @Log
    @PostMapping("updateUserHeadImg")
    @ApiOperation("修改个人头像")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "修改个人头像", code = "USECU003", level = RouterLevel.PUBLIC)
    public void updateUserHeadImg(@RequestBody @Validated UserUpdateHeadImgDto headImgDto) {
        userCenterService.updateUserHeadImg(headImgDto);
    }

    /**
     * 修改个人邮箱
     *
     * @param emailDto 用户邮箱信息
     */
    @Log
    @PostMapping("updateUserEmail")
    @ApiOperation("修改个人邮箱")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "修改个人邮箱", code = "USECU004", level = RouterLevel.PUBLIC)
    public void updateUserEmail(@RequestBody @Validated UserUpdateEmailDto emailDto) {
        userCenterService.updateUserEmail(emailDto);
    }

    /**
     * 修改个人信息
     *
     * @param selfDto 用户个人信息
     */
    @Log
    @PostMapping("updateUserSelfInfo")
    @ApiOperation("修改个人信息")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "修改个人信息", code = "USECU005", level = RouterLevel.PUBLIC)
    public void updateUserSelfInfo(@RequestBody @Validated UserUpdateSelfDto selfDto) {
        userCenterService.updateUserSelfInfo(selfDto);
    }

    /**
     * 分页查询用户操作日志列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<LogVo>
     */
    @GetMapping("/pageUserLogList")
    @ApiOperation(value = "分页查询用户操作日志列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询用户操作日志列表", code = "pageUserLogList", level = RouterLevel.PUBLIC)
    public PageInfo<LogVo> pageUserLogList(UserLogQueryDto queryDto) {
        return userCenterService.queryUserLogListByPage(queryDto);
    }
}
