package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.rest.IgnoreResult;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注销管理
 *
 * @author SunTao
 * @since 2026-05-22
 */
@RestController
@RequiredArgsConstructor
@Api(value = "LogoutController", tags = { "注销管理" })
public class LogoutController {
    /**
     * 系统登出
     */
    @Log
    @IgnoreResult
    @PostMapping("/api/logout")
    @ApiOperation(value = "用户登出")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "用户登出", code = "LOGO0001", level = RouterLevel.PUBLIC)
    public void logout() {
    }
}
