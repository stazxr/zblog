package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.ratelimit.annotation.RateLimit;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.base.domain.bo.LoginUser;
import com.github.stazxr.zblog.base.service.AuthService;
import com.github.stazxr.zblog.core.base.BaseConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
     * @return LoginUser
     */
    @GetMapping("/loginId")
    @ApiOperation(value = "获取当前登录用户信息")
    @ApiVersion(BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "获取当前登录用户信息", code = "AUTH0001", level = RouterLevel.PUBLIC)
    public LoginUser currentUserDetail() {
        return authService.currentUserDetail();
    }

    /**
     * 续签
     *
     * @param request  请求
     * @param response 响应
     * @return boolean 续签是否成功
     */
    @PostMapping("/refresh")
    @ApiOperation("续签")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    // @RateLimit(time = 5, enableIp = true, enableUser = true)
    @Router(name = "续签", code = "AUTH0002", level = RouterLevel.OPEN)
    public boolean refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return authService.refreshToken(request, response);
    }
}
