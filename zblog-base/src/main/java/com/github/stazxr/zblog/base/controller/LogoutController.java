package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.component.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.cache.util.GlobalCacheHelper;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.IgnoreResult;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.util.net.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.github.stazxr.zblog.core.base.BaseConst.PermLevel.OPEN;
import static com.github.stazxr.zblog.core.base.BaseConst.PermLevel.PUBLIC;

/**
 * 登出管理
 *
 * @author SunTao
 * @since 2022-07-24
 */
@RestController
@RequiredArgsConstructor
@Api(value = "LogoutController", tags = { "注销控制器" })
public class LogoutController {
    private final UserService userService;

    private final JwtTokenStorage jwtTokenStorage;

    /**
     * 系统登出，这里只做路由注册，逻辑在处理器中
     */
    @IgnoreResult
    @PostMapping("/api/logout")
    @ApiOperation(value = "用户注销")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "用户注销", code = "logout", level = PUBLIC)
    public void logout() {
    }

    /**
     * 自定义用户注销
     */
    @Log
    @PostMapping("/api/logout/custom")
    @ApiOperation(value = "自定义用户注销")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_2_0 })
    @Router(name = "自定义用户注销", code = "customLogout", level = OPEN)
    public Result customLogout(@RequestParam Long userId, HttpServletRequest request) {
        // 注销 token
        jwtTokenStorage.expire(userId);
        userService.clearUserStorageToken(userId);
        GlobalCacheHelper.remove(Constants.CacheKey.preTkn.cacheKey().concat(":").concat(String.valueOf(userId)));

        // 清除统一登陆的用户信息
        String ip = IpUtils.getIp(request);
        GlobalCacheHelper.remove(Constants.CacheKey.ssoTkn.cacheKey().concat(":").concat(ip));
        return Result.success();
    }
}
