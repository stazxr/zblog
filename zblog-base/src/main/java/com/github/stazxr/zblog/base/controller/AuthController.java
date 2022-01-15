package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.core.model.Result;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证管理
 *
 * @author SunTao
 * @since 2022-01-15
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：认证管理")
@RequestMapping("/api/auth")
public class AuthController {
    /**
     * 登录失败返回 401 以及提示信息.
     *
     * @return the rest
     */
    @PostMapping("/login_failure")
    public Result loginFailure() {
        return Result.failure("登录失败了，老哥");
    }

    /**
     * 登录失败返回 401 以及提示信息.
     *
     * @return the rest
     */
    @PostMapping("login_success")
    public Result loginSuccess() {
        return Result.success("登录成功");
    }
}
