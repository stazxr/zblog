package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.*;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class UserController {
    private final UserService userService;

    /**
     * 修改个人头像
     *
     * @param updateDto 用户信息
     * @return Result
     */
    @Log
    @PostMapping("updateUserHeadImg")
    @Router(name = "修改个人头像", code = "updateUserHeadImg", level = BaseConst.PermLevel.PUBLIC)
    public Result updateUserHeadImg(@RequestBody UserUpdateDto updateDto) {
        return userService.updateUserHeadImg(updateDto) ? Result.success() : Result.failure();
    }

    /**
     * 修改个人基础信息
     *
     * @param updateDto 用户信息
     * @return Result
     */
    @Log
    @PostMapping("updateUserBaseInfo")
    @Router(name = "修改个人基础信息", code = "updateUserBaseInfo", level = BaseConst.PermLevel.PUBLIC)
    public Result updateUserBaseInfo(@RequestBody UserUpdateDto updateDto) {
        return userService.updateUserBaseInfo(updateDto) ? Result.success() : Result.failure();
    }

    /**
     * 修改个人密码
     *
     * @param passDto 用户密码信息
     * @return Result
     */
    @Log
    @PostMapping("updateUserPass")
    @Router(name = "修改个人密码", code = "updateUserPass", level = BaseConst.PermLevel.PUBLIC)
    public Result updateUserPass(@RequestBody UserUpdatePassDto passDto) {
        return userService.updateUserPass(passDto) ? Result.success() : Result.failure();
    }

    /**
     * 修改个人邮箱
     *
     * @param emailDto 用户邮箱信息
     * @return Result
     */
    @Log
    @PostMapping("updateUserEmail")
    @Router(name = "修改个人邮箱", code = "updateUserEmail", level = BaseConst.PermLevel.PUBLIC)
    public Result updateUserEmail(@RequestBody UserUpdateEmailDto emailDto) {
        return userService.updateUserEmail(emailDto) ? Result.success() : Result.failure();
    }

    /**
     * 查询用户列表（公共接口）
     *
     * @param queryDto 查询参数
     * @return userList
     */
    @GetMapping(value = "/pageListOfCommon")
    @Router(name = "查询用户列表（公共接口）", code = "pageUserListOfCommon", level = BaseConst.PermLevel.PUBLIC)
    public Result pageUserListOfCommon(UserQueryDto queryDto) {
        return Result.success().data(userService.queryUserListByPage(queryDto));
    }
}
