package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.base.domain.bo.LoginUser;
import com.github.stazxr.zblog.base.service.AuthService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.IgnoreLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证管理
 *
 * @author SunTao
 * @since 2022-01-15
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Api(value = "AuthController", tags = { "认证控制器" })
public class AuthController {
    private final AuthService authService;

    /**
     * 获取当前登录用户信息
     *
     * @return login username
     */
    @IgnoreLog
    @GetMapping("/loginId")
    @ApiOperation(value = "获取当前登录用户信息")
    @ApiVersion(BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "获取当前登录用户信息", code = "AUTH0001", level = RouterLevel.PUBLIC)
    public LoginUser currentUserDetail() {
        return authService.currentUserDetail();
    }
}
