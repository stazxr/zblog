package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.core.annotation.Router;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
     * 获取当前登录用户的用户名
     *
     * @return login username
     */
    @GetMapping("/loginId")
    @ApiOperation("获取当前登录用户的用户名")
    @Router(name = "获取当前登录用户的用户名", code = "loginId")
    public String currentUserName(@AuthenticationPrincipal User user) {
        return user.getUsername();
    }
}
